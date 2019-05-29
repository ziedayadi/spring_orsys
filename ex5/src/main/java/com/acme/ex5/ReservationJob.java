package com.acme.ex5;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.lang.reflect.Constructor;

@EnableBatchProcessing
@SpringBootApplication
public class ReservationJob {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    public Step fakeStep(){
        return stepBuilderFactory.get("step1")
                .<ReservationRow, ReservationRow>chunk(10)
                .reader(()->{
                    try{Thread.sleep(1000);}catch(Exception e){}
                    return  new ReservationRow();
                })
                //.processor((ReservationRow x) -> x)
                .writer(items -> System.out.println(items))
                .build();
    }

    // pour que close ne soit pas appelé à la fermeture de l'applicationContext
    // car la fin du job a déjà conduit à appeler la méthode close.
    // la méthode close ne vient de ItemReader mais de ItemStreamReader
    // (qu'implémente JdbcCursorItemReader)
    @Bean(destroyMethod = "")
    @StepScope // => inscription d'un proxy
    // pour que ce soit un proxy héritage :
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS )
    // sinon : proxy par association. Il faut alors choisir ItemStreamReader comme type de retour
    // afin que le bean (ici : le reader retournée par la méthode) soit vu comme tel et non comme un simple
    // ItemReader (qui n'aurait pas de méthode open).
    public ItemReader reader(){
        String q = "select b.id as bookId, b.title as bookTitle, m.username from Reservation r join Member m on r.member_id = m.id join Book b on r.book_id = b.id";
        DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex5", "postgres", null);
        return new JdbcCursorItemReaderBuilder()
                .name("my-reader")
                .dataSource(ds)
                .sql(q)
                .beanRowMapper(ReservationRow.class)
                .build();
    }

    @Bean
    public DataSource ds(){
        return new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex5", "postgres", null);
    }

    @Bean
    public ItemWriter writer(){
        FileSystemResource destination = new FileSystemResource("c:\\formation_spring\\files\\reservations.csv");
        String[] columns = new String[]{"bookId", "username","bookTitle"};
        return new FlatFileItemWriterBuilder()
                .name("my-writer")
                .resource(destination)
                .delimited().delimiter(";").names(columns)
                .build();
    }



    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<ReservationRow, ReservationRow>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Step step2(){
        return  stepBuilderFactory.get("step2")
                .tasklet((contrib, context) -> {
                    System.out.println("send mail");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job myJob(JobBuilderFactory jobBuilderFactory){
        return jobBuilderFactory.get("myjob")
                .start(step1())
                .next(step2())
                .build();
    }

    public static void main(String[] args) throws NoSuchMethodException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
        Class<?> clazz = ReservationRow.class;
        try{
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            System.out.println(ctor);
            Object bean = ctor.newInstance();
        }
        catch (Exception e){
            System.out.println("0 arg constructor does not exist");
        }*/

        try(var ctx = SpringApplication.run(ReservationJob.class, args)){
        //    var ctx = new AnnotationConfigApplicationContext(ReservationJob.class);
            JobLauncher launcher = ctx.getBean(JobLauncher.class);
            Job job = ctx.getBean(Job.class);
            JobExecution execution = launcher.run(job, new JobParametersBuilder().addString("a", "a").toJobParameters());
            System.out.println(execution.getStatus());

        }
    }
}
