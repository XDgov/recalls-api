
import java.util.Properties;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author ferru001
 */
public class QuartzTest {

    public static void main(String[] args) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("dummyJobName", "group1").build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1).repeatForever())
                .build();

        Scheduler scheduler = new StdSchedulerFactory(getQuartzProperties()).getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    private static Properties getQuartzProperties() {
        Properties quartzProps = new Properties();
        quartzProps.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        quartzProps.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        quartzProps.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        quartzProps.setProperty("org.quartz.threadPool.threadCount", "4");
        return quartzProps;
    }
}
