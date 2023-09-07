package com.viettel.vdt2023.jenkins.api;


import com.viettel.vdt2023.jenkins.api.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
public class JenkinsTriggerHelper {

    private final JenkinsServer server;
    private final Long retryInterval;
    private static final Long DEFAULT_RETRY_INTERVAL = 200L;

    public JenkinsTriggerHelper(JenkinsServer server) {
        this.server = server;
        this.retryInterval = DEFAULT_RETRY_INTERVAL;
    }

    public JenkinsTriggerHelper(JenkinsServer server, Long retryInterval) {
        this.server = server;
        this.retryInterval = retryInterval;
    }

    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName) throws IOException, InterruptedException {
        return triggerJobAndWaitUntilFinished(jobName, false);
    }


    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params)
            throws IOException, InterruptedException {
        return triggerJobAndWaitUntilFinished(jobName, params, false);
    }

    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params,
            boolean crumbFlag) throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueReference queueRef = job.build(params, crumbFlag);

        return triggerJobAndWaitUntilFinished(jobName, queueRef);
    }


    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params,
                                                           Map<String, File> fileParams,
                                                           boolean crumbFlag) throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueReference queueRef = job.build(params, fileParams, crumbFlag);

        return triggerJobAndWaitUntilFinished(jobName, queueRef);
    }


    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params,
                                                           Map<String, File> fileParams) throws IOException, InterruptedException {
        return triggerJobAndWaitUntilFinished(jobName, params, fileParams, false);
    }

    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, boolean crumbFlag)
            throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueReference queueRef = job.build(crumbFlag);
        return triggerJobAndWaitUntilFinished(jobName, queueRef);
    }

    private BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, QueueReference queueRef)
            throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueItem queueItem = this.server.getQueueItem(queueRef);

        while (!queueItem.isCancelled() && job.isInQueue()) {
            Thread.sleep(retryInterval);
            job = this.server.getJob(jobName);
            queueItem = this.server.getQueueItem(queueRef);
        }

        Build build = server.getBuild(queueItem);
        if (queueItem.isCancelled()) {
            return build.details();
        }

        boolean isBuilding = build.details().isBuilding();
        while (isBuilding) {
            Thread.sleep(retryInterval);
            isBuilding = build.details().isBuilding();
        }

        return build.details();
    }
}
