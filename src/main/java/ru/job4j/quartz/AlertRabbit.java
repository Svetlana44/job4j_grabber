package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    static Properties configTimeScheduler = new Properties();
    static Connection connection;

    public static void initProperties() {

        try (InputStream in = AlertRabbit1.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            configTimeScheduler.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initConnection() {
        try {
            initProperties();
            connection = DriverManager.getConnection(
                    configTimeScheduler.getProperty("url"),
                    configTimeScheduler.getProperty("username"),
                    configTimeScheduler.getProperty("password"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                        + "id serial primary key,"
                        + "created_date timestamp"
                        + "); ");
        ) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void InsertTable(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + tableName + " (created_date) values(?); ");
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().withNano(0)));
        preparedStatement.executeUpdate();
    }

    public static void main(String[] args) throws SQLException {
        try {
            initConnection();
            createTable("rabbit");

            List<Long> store = new ArrayList<>();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            System.out.println(store);
        } catch (Exception se) {
            se.printStackTrace();
        }
        connection.close();

    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
            try {
                createTable("rabbit");
                InsertTable("rabbit");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}