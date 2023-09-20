package com.viettel.vdt2023.controller;

import java.util.PrimitiveIterator;

public class JobControllerBuilder {
    private String pathSourcode;
    private String pathDepend;
    private String imageName;

    private String cridential_jenkins_minikube;

    private String minikube_url;
    private String port;

    public JobControllerBuilder(String src,String depend, String imageName, String cridential_jenkins_minikube,
                                String minikube_url,String port){
        this.pathSourcode=src;
        this.pathDepend=depend;
        this.imageName=imageName;
        this.cridential_jenkins_minikube=cridential_jenkins_minikube;
        this.minikube_url=minikube_url;
        this.port=port;
    }

    public String createJenkinsJob(){
        String jenkin_pipeline_script="<?xml version='1.1' encoding='UTF-8'?>\n" +
                "<flow-definition plugin=\"workflow-job@1326.ve643e00e9220\">\n" +
                "    <actions>\n" +
                "        <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction\n" +
                "                plugin=\"pipeline-model-definition@2.2144.v077a_d1928a_40\"/>\n" +
                "        <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction\n" +
                "                plugin=\"pipeline-model-definition@2.2144.v077a_d1928a_40\">\n" +
                "            <jobProperties/>\n" +
                "            <triggers/>\n" +
                "            <parameters/>\n" +
                "            <options/>\n" +
                "        </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>\n" +
                "    </actions>\n" +
                "    <description>Chọn xây dựng khi có thay đổi được đẩy lên từ GitLab</description>\n" +
                "    <keepDependencies>false</keepDependencies>\n" +
                "    <properties>\n" +
                "        <io.fabric8.jenkins.openshiftsync.BuildConfigProjectProperty plugin=\"openshift-sync@1.1.0.795.v95fa_27a_a_e287\">\n" +
                "            <uid></uid>\n" +
                "            <namespace></namespace>\n" +
                "            <name></name>\n" +
                "            <resourceVersion></resourceVersion>\n" +
                "        </io.fabric8.jenkins.openshiftsync.BuildConfigProjectProperty>\n" +
                "        <com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.7.15\">\n" +
                "            <gitLabConnection>my gitlab connect</gitLabConnection>\n" +
                "            <jobCredentialId></jobCredentialId>\n" +
                "            <useAlternativeCredential>false</useAlternativeCredential>\n" +
                "        </com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n" +
                "        <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "            <triggers>\n" +
                "                <com.dabsquared.gitlabjenkins.GitLabPushTrigger plugin=\"gitlab-plugin@1.7.15\">\n" +
                "                    <spec></spec>\n" +
                "                    <triggerOnPush>true</triggerOnPush>\n" +
                "                    <triggerToBranchDeleteRequest>false</triggerToBranchDeleteRequest>\n" +
                "                    <triggerOnMergeRequest>true</triggerOnMergeRequest>\n" +
                "                    <triggerOnlyIfNewCommitsPushed>false</triggerOnlyIfNewCommitsPushed>\n" +
                "                    <triggerOnPipelineEvent>false</triggerOnPipelineEvent>\n" +
                "                    <triggerOnAcceptedMergeRequest>false</triggerOnAcceptedMergeRequest>\n" +
                "                    <triggerOnClosedMergeRequest>false</triggerOnClosedMergeRequest>\n" +
                "                    <triggerOnApprovedMergeRequest>true</triggerOnApprovedMergeRequest>\n" +
                "                    <triggerOpenMergeRequestOnPush>never</triggerOpenMergeRequestOnPush>\n" +
                "                    <triggerOnNoteRequest>true</triggerOnNoteRequest>\n" +
                "                    <noteRegex>Jenkins please retry a build</noteRegex>\n" +
                "                    <ciSkip>true</ciSkip>\n" +
                "                    <skipWorkInProgressMergeRequest>true</skipWorkInProgressMergeRequest>\n" +
                "                    <labelsThatForcesBuildIfAdded></labelsThatForcesBuildIfAdded>\n" +
                "                    <setBuildDescription>true</setBuildDescription>\n" +
                "                    <branchFilterType>All</branchFilterType>\n" +
                "                    <includeBranchesSpec></includeBranchesSpec>\n" +
                "                    <excludeBranchesSpec></excludeBranchesSpec>\n" +
                "                    <sourceBranchRegex></sourceBranchRegex>\n" +
                "                    <targetBranchRegex></targetBranchRegex>\n" +
                "                    <pendingBuildName></pendingBuildName>\n" +
                "                    <cancelPendingBuildsOnUpdate>false</cancelPendingBuildsOnUpdate>\n" +
                "                </com.dabsquared.gitlabjenkins.GitLabPushTrigger>\n" +
                "            </triggers>\n" +
                "        </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "    </properties>\n" +
                "    <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@3774.v4a_d648d409ce\">\n" +
                "        <script>\n" +
                "pipeline {\n" +
                "    agent any\n" +
                "    environment {\n" +
                "        registry = \"vugiangcoder/" + imageName + "\"\n" +
                "        registryCredential = \"dockerhub_id\"\n" +
                "        dockerImage = \"\"\n" +
                "    }\n" +
                "    stages {\n" +
                "        stage(\"Clone kho Git\") {\n" +
                "            steps {\n" +
                "                dir(\"sourcecode\"){\n" +
                "                git(\n" +
                "                    url: \"" + pathSourcode + "\",\n" +
                "                    branch: \"main\",\n" +
                "                    changelog: true,\n" +
                "                    poll: true\n" +
                "                )\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        stage(\"Build d image\") {\n" +
                "            steps {\n" +
                "                dir(\"sourcecode\"){\n" +
                "                script {\n" +
                "                    dockerImage = docker.build registry + \":latest\"\n" +
                "                }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        stage(\"Đẩy ảnh lên Docker Hub\") {\n" +
                "            steps {\n" +
                "                   dir(\"sourcecode\"){\n" +
                "                script {\n" +
                "                    docker.withRegistry(\"\", registryCredential) {\n" +
                "                        dockerImage.push()\n" +
                "                    }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "        }\n" +
                "        stage(\"Clone kho Git phụ thuộc\") {\n" +
                "            steps {\n" +
                "                dir(\"depend\"){\n" +
                "                git(\n" +
                "                    url: \"" + pathDepend + "\",\n" +
                "                    branch: \"main\",\n" +
                "                    changelog: true,\n" +
                "                    poll: true\n" +
                "                )\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        stage(\"Triển khai tệp yaml lên Kubernetes\") {\n" +
                "            steps {\n" +
                "                dir(\"depend\") {\n" +
                "                    withKubeConfig([\n" +
                "                        credentialsId: \"" + cridential_jenkins_minikube + "\",\n" +
                "                        serverUrl: \"" + minikube_url + ":" + port + "\"\n" +
                "                    ]) {\n" +
                "                        bat \"kubectl apply -f deployment.yaml\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n" + "        </script>\n" +
                "        <sandbox>true</sandbox>\n" +
                "    </definition>\n" +
                "    <triggers/>\n" +
                "    <disabled>false</disabled>\n" +
                "</flow-definition>";
        return jenkin_pipeline_script;

    }
}
