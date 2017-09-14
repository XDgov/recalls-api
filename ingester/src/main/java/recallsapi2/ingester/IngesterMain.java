package recallsapi2.ingester;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import recallsapi2.dto.JobControl;
import recallsapi2.ingester.dao.JobControlDAO;

/**
 *
 * @author ferru001
 */
public class IngesterMain {

    private static final Logger logger = Logger.getLogger(IngesterMain.class);

    public static void main(String[] args) {

        try {
            //construct Quartz scheduler
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();

            //grab all the active job controls we need to run ingestions on
            JobControlDAO jobControlDAO = new JobControlDAO("recalls-api.properties");
            List<JobControl> jobControlList = jobControlDAO.getJobControls(true);

            //loop through the jobs and create a job for each in Quartz
            for (int x = 0; x < jobControlList.size(); x++) {
                try {
                    Class<? extends Job> jobClazz = (Class<? extends Job>) Class.forName("recallsapi2.ingester." + jobControlList.get(x).getiIngesterDefinitionName() + "Ingester");
                    JobDetail jobDetail = JobBuilder.newJob(jobClazz).
                            withIdentity(jobControlList.get(x).getiIngesterDefinitionName() + "-job", "jobsGroup").
                            build();

                    //create the trigger to cause job to run every 60 minutes
                    //TODO - craft definition of trigger from Job Control definition in DB.
                    Trigger trigger = newTrigger().withIdentity(jobControlList.get(x).getiIngesterDefinitionName() + "-trigger", "triggersGroup").
                            withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
                    try {
                        //schedule job
                        sched.scheduleJob(jobDetail, trigger);
                    } catch (SchedulerException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                } catch (ClassNotFoundException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        } catch (IOException | SQLException | SchedulerException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
