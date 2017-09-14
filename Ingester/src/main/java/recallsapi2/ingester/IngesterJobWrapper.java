package recallsapi2.ingester;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import recallsapi2.generic.JobControl;

/**
 *
 * @author ferru001
 */
public class IngesterJobWrapper implements Job {

    private static final Logger logger = Logger.getLogger(IngesterJobWrapper.class);
    private final JobControl jobControl;

    public IngesterJobWrapper(JobControl jobControl) {
        this.jobControl = jobControl;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            Class<?> clazz = Class.forName("recallsapi2.ingester." + jobControl.getiIngesterDefinitionName() + "Ingester");
            IIngester ingesterObj = (IIngester) clazz.newInstance();
            ingesterObj.execute(jec);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
            throw new JobExecutionException(ex.getMessage());
        }
    }

}
