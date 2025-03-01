package com.http200ok.finbuddy.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoTransferScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("autoTransferJob")
    private final Job autoTransferJob;

    @Qualifier("retryFailedAutoTransferJob")
    private final Job retryFailedAutoTransferJob;

    public AutoTransferScheduler(JobLauncher jobLauncher,
                                 @Qualifier("autoTransferJob") Job autoTransferJob,
                                 @Qualifier("retryFailedAutoTransferJob") Job retryFailedAutoTransferJob) {
        this.jobLauncher = jobLauncher;
        this.autoTransferJob = autoTransferJob;
        this.retryFailedAutoTransferJob = retryFailedAutoTransferJob;
    }

    @Scheduled(cron = "0 28 13 * * ?")
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

    @Scheduled(cron = "0 22 * * * ?")
    public void runRetryFailedAutoTransferJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(retryFailedAutoTransferJob, params);
            System.out.println("실패한 자동이체 Job 실행됨");
        } catch (Exception e) {
            System.out.println("실패한 자동이체 Job 실행 중 오류" + e.getMessage());
        }
    }
}
