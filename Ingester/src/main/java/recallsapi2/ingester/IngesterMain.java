package recallsapi2.ingester;

import com.jolbox.bonecp.BoneCP;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import recallsapi2.generic.JobControl;
import recallsapi2.ingester.dao.JobControlDAO;
import recallsapi2.recallsglobals.GlobalDataAccess;

/**
 *
 * @author ferru001
 */
public class IngesterMain {

    private static final Logger logger = Logger.getLogger(IngesterMain.class);

    private static Properties getQuartzProperties() {
        Properties quartzProps = new Properties();
        quartzProps.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        quartzProps.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        quartzProps.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        quartzProps.setProperty("org.quartz.threadPool.threadCount", "4");
        return quartzProps;
    }

    public static void main(String[] args) {

        try {

            //construct Quartz scheduler
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(getQuartzProperties());
            Scheduler sched = schedFact.getScheduler();
            sched.start();

            //create a database connection pool to use for the load
            BoneCP connectionPool = GlobalDataAccess.createConnectionPool("recalls-api.properties");
            //grab all the active job controls we need to run ingestions on
            JobControlDAO jobControlDAO = new JobControlDAO(connectionPool);

            List<JobControl> jobControlList = jobControlDAO.getJobControls(true);
            logger.info("Loaded " + jobControlList.size() + " jobs");
            //create a map out of the job controls list
            Map<String, JobControl> jobControlMap = new HashMap<>();
            for (JobControl jc : jobControlList) {
                jobControlMap.put(jc.getiIngesterDefinitionName(), jc);
            }

            sched.getContext().put("connectionPool", connectionPool);
            sched.getContext().put("jobControlMap", jobControlMap);


//            loop through the jobs and create a job for each in Quartz
            for (int x = 0; x < jobControlList.size(); x++) {
                try {
                    Class<? extends Job> jobClazz = (Class<? extends Job>) Class.forName("recallsapi2.ingester." + jobControlList.get(x).getiIngesterDefinitionName() + "Ingester");
                    JobDetail jobDetail = JobBuilder.newJob(jobClazz).
                            withIdentity(jobControlList.get(x).getiIngesterDefinitionName() + "-job", "jobsGroup").
                            build();

                    //create the trigger to cause job to run every 60 minutes
                    //TODO - craft definition of trigger from Job Control definition in DB.
                    Trigger trigger = newTrigger().withIdentity(jobControlList.get(x).getiIngesterDefinitionName() + "-trigger", "triggersGroup")
                            .startNow()
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1))
                            .build();
                    try {
                        logger.info("Scheduling job");
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
