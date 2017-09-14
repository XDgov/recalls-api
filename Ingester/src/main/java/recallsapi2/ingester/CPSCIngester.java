package recallsapi2.ingester;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolbox.bonecp.BoneCP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import recallsapi2.generic.JobControl;
import recallsapi2.ingester.dao.CPSCIngesterDAO;
import recallsapi2.ingester.dto.cpsc.CPSCRecallsItem;

/**
 *
 * @author ferru001
 */
public class CPSCIngester implements IIngester {

    private static final Logger logger = Logger.getLogger(CPSCIngester.class);
    private static final String DATE_FMR = "yyyy-MM-dd";
    private static final String TIME_FMR = "yyyyMMddHHmmssSS";
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FMR);
    private static final DateFormat TIME_FORMATTER = new SimpleDateFormat(TIME_FMR);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("********* KICKED OFF CPSC LOADER ***********");
        try {
            SchedulerContext schedulerContext = null;
            schedulerContext = jec.getScheduler().getContext();
            Map<String, JobControl> jobControlMap = (Map<String, JobControl>) schedulerContext.get("jobControlMap");
            BoneCP connectionPool = (BoneCP) schedulerContext.get("connectionPool");
            Date lastIngestDateTime = DATE_FORMATTER.parse("1800-01-01");
            logger.info("lastIngestDateTime: " + lastIngestDateTime);
            //  BufferedWriter writer = null;
            try {
                //create JsonParser object
                URL cpscUrl = new URL(String.format("https://www.saferproducts.gov/RestWebServices/Recall?format=json&RecallDateStart=%s", DATE_FORMATTER.format(lastIngestDateTime)));
                logger.info("CPSC URL: " + cpscUrl);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cpscUrl.openStream()));

//                String line = null;
//                writer = Files.newBufferedWriter(new File("C:\\work\\recalls-api\\tmp\\cpsc-output-" + TIME_FORMATTER.format(new Date()) + ".json").toPath(), StandardCharsets.UTF_8);
//                //write this JSON to disk
//                while ((line = bufferedReader.readLine()) != null) {
//                    writer.write(line);
//                    writer.newLine();
//                }
//                writer.flush();
//                bufferedReader.close();
                //re-read to parse
                //bufferedReader = new BufferedReader(new InputStreamReader(cpscUrl.openStream()));
                ObjectMapper mapper = new ObjectMapper();

                List<CPSCRecallsItem> cpscRecallItems = mapper.readValue(bufferedReader, new TypeReference<List<CPSCRecallsItem>>() {
                });

                logger.info("Inserting " + cpscRecallItems.size() + " CPSC recall items into the DB");

                CPSCIngesterDAO cpscIngesterDAO = new CPSCIngesterDAO(connectionPool);
                cpscIngesterDAO.loadCPSCRecallsRecords(cpscRecallItems, jobControlMap.get("CPSC"));
                logger.info("Loading done!");
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
                throw new JobExecutionException(ex.getMessage(), ex);
            }

        } catch (ParseException | SchedulerException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}
