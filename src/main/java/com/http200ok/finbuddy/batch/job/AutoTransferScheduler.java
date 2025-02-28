package com.http200ok.finbuddy.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransferScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("autoTransferJob")
    private final Job autoTransferJob;



    @Scheduled(cron = "0 40 15 * * ?")
    public void runAutoTransferJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(autoTransferJob, params);
            System.out.println("Batch 자동이체 Job 실행됨");
        } catch (Exception e) {
            System.out.println("Batch 자동이체 Job 실행 중 오류" + e.getMessage());
        }
    }
}
