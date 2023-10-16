package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Parse parse;
    private final Store store;
    private final Scheduler scheduler;
    private final int time;

    public Grabber(Parse parse, Store store, Scheduler scheduler, int time) {
        this.parse = parse;
        this.store = store;
        this.scheduler = scheduler;
        this.time = time;
    }

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    static Properties cfg = new Properties();

    @Override
    public void init() throws SchedulerException {

    }

    @Override
    public void init(HabrCareerParse habrCareerParse, PsqlStore store, Scheduler scheduler, Grabber grabber) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        data.put("graber", grabber);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(time)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    @Override
    public void cfg() {
        try (InputStream in = Grabber.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class GrabJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            Grabber grabber = (Grabber) map.get("graber");
            /* TO DO impl logic */
            try {
        /*        HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();
                HabrCareerParse habrCareerParse = new HabrCareerParse(habr);  */
                List<Post> posts = parse.list(PAGE_LINK);

                posts.forEach(store::save);
                System.out.println(store.findById(1000130906));
                System.out.println(store.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/UseParallelGC.txt.UseParallelGC.txt 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            /*    out.write(post.toString().getBytes());  */
                            out.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
 /*
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        var parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        var store = new PsqlStore(cfg);
        var time = Integer.parseInt(cfg.getProperty("time"));
        new Grabber(parse, store, scheduler, time).init();  */

        var parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        var store = new PsqlStore(cfg);
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Grabber grab = new Grabber(parse, store, scheduler, 100);

        /*     scheduler.start();  */

        grab.cfg();
        scheduler = grab.scheduler();
        store = grab.store();
        grab.init(new HabrCareerParse(new HabrCareerDateTimeParser()), store, scheduler, grab);
        grab.web(store);

        scheduler.start();
    }

    private PsqlStore store() {
        return (PsqlStore) this.store;
    }

    private Scheduler scheduler() {
        return this.scheduler;
    }
}