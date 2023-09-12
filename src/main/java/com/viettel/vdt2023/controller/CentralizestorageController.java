package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.ProjectApi;
import com.viettel.vdt2023.gitlab.api.models.Environment;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.gitlab.api.models.Project;
import com.viettel.vdt2023.gitlab.api.models.Visibility;
import com.viettel.vdt2023.jenkins.api.JenkinsServer;
import com.viettel.vdt2023.jenkins.api.model.Job;
import com.viettel.vdt2023.jwt.JwtTokenProvider;
import com.viettel.vdt2023.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.Thread.sleep;

@RestController
@RequiredArgsConstructor
public class CentralizestorageController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final SystemService systemService;

    @Autowired
    private final ServiceService serviceService;

    @Autowired
    private final UserSystemService userSystemService;

    @Autowired
    private final UserServiceService userServiceService;
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @PostMapping("/api/create-system")
    public void createSystem(@RequestBody CreateSystemRequestEntity createSystemRequestEntity,
                             @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        Group group = groupApi.addGroup(createSystemRequestEntity.getName(), createSystemRequestEntity.getName(),
                createSystemRequestEntity.getDecription(), Visibility.PRIVATE, true, true,
                161L);
        SystemEntity systemEntity = new SystemEntity(null, createSystemRequestEntity.getName(), group.getId());
        System.out.println(systemEntity);
        try {
            systemService.saveSystem(systemEntity);
            String username = jwtTokenProvider.getUsernameFromJWT(token);
            UserEntity userEntity = userService.loadUserByUsername(username);
            System.out.println(userEntity.toString());
            UserSystemEntity userSystemEntity = new UserSystemEntity(null, userEntity, systemEntity, true);
            userSystemService.saveUserSystem(userSystemEntity);
            groupApi.addMember(group, userEntity.getGitlabId(), 30);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @PostMapping("/api/add-member-system")
    public void addMemberToSystem(@RequestBody AddMemberSystemRequestEntity addMemberSystemRequest,
                                  @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        String name = jwtTokenProvider.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        System.out.println(userEn);
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        SystemEntity systemEn = systemService.loadSystemByName(addMemberSystemRequest.getGroupname());
        UserSystemEntity userSystemEn = userSystemService.loadUserSystem(userEn, systemEn);
        if (userSystemEn == null) {
            System.out.println("Denied");
        }
        Group group = groupApi.getGroup(systemEn.getGitlabId());
        System.out.println(group);
        List<String> usernames = addMemberSystemRequest.getUsername();
        for (String username : usernames) {
            UserEntity userEntity = userService.loadUserByUsername(username);
            System.out.println(userEntity);
            try {
                groupApi.addMember(group, userEntity.getGitlabId(), 30);
                UserSystemEntity userSystemEntity = new UserSystemEntity(null, userEntity, systemEn, false);
                userSystemService.saveUserSystem(userSystemEntity);
            } catch (Exception e) {
                String message = "them " + username + " khong thanh cong";
                System.out.println(message);
            }
        }
    }

    @PostMapping("/api/create-service")
    public String createService(@RequestBody CreateServiceRequestEntity createServiceRequestEntity,
                                @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return null;
        }
        String jenins_token = "11404cc9a1fd9088daf541b548fd6ac68b";
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        ProjectApi projectApi = new ProjectApi(gitLabApi);
        SystemEntity systemEntity = systemService.loadSystemByName(createServiceRequestEntity.getParentGroupName());
        String name = jwtTokenProvider.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        UserSystemEntity userSystemEntity = userSystemService.loadUserSystem(userEn, systemEntity);
        if (userSystemEntity == null) {
            return "Denied";
        }
        Group childGroup = groupApi.addGroup(createServiceRequestEntity.getName(), createServiceRequestEntity.getName(),
                createServiceRequestEntity.getDescription(), Visibility.PRIVATE, true, true,
                systemEntity.getGitlabId());
        try {
            ServiceEntity serviceEntity = new ServiceEntity(1L, createServiceRequestEntity.getName(), systemEntity, childGroup.getId());
            serviceService.saveService(serviceEntity);
            String username = jwtTokenProvider.getUsernameFromJWT(token);
            UserEntity userEntity = userService.loadUserByUsername(username);
            System.out.println(userEntity.toString());
            try {
                groupApi.addMember(childGroup, userEntity.getGitlabId(), 30);
            } catch (Exception e1) {
                System.out.println(e1);
            } finally {
                UserServiceEntity userServiceEntity = new UserServiceEntity(null, userEntity, serviceEntity, true);
                userServiceService.saveUserService(userServiceEntity);
                System.out.println(userServiceEntity);
                Project sourceCodeRepo = projectApi.createProject(childGroup.getId(), createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "sourcecode");
                String pathSourceCode = "http://root:L8uX4Y3zsEstqPXAfy9o@192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "sourcecode ";
                String imageName = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName();

                Project dependencyRepo = projectApi.createProject(childGroup.getId(), createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency");
                String pathDepend = "http://root:L8uX4Y3zsEstqPXAfy9o@192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency";
                String nameDepend = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName();
                String jenkins_script_pipeline_sourceCode = createJenkinsJob(pathSourceCode, pathDepend, imageName);
                JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:1234"), "minhgiang89", "1140fb4d62c3ef2cac6ffe07d17df34249");
                jenkins.createJob(imageName, jenkins_script_pipeline_sourceCode, true);
//                Job job = jenkins.getJob(imageName);
//                job.build(true);
                projectApi.addHook(dependencyRepo, "http://minhgiang89:" + jenins_token + "@192.168.56.1:1234/project/"
                        + imageName, true, true, true);
                String link = "Link source code: " + "http://192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "sourcecode\n"
                        + "Link dependency: " + "http://192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency";
                return link;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/api/add-member-service")
    public void addMemberToService(@RequestBody AddMemberServiceRequestEntity addMemberServiceRequest,
                                   @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        String name = jwtTokenProvider.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        System.out.println(userEn);
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        ServiceEntity serviceEn = serviceService.loadServiceByName(addMemberServiceRequest.getGroupname());
        UserServiceEntity userServiceEn = userServiceService.loadUserService(userEn, serviceEn);
        if (userServiceEn == null) {
            System.out.println("Denied");
        }
        Group group = groupApi.getGroup(serviceEn.getGitlabId());
        System.out.println(group);
        List<String> usernames = addMemberServiceRequest.getUsername();
        for (String username : usernames) {
            UserEntity userEntity = userService.loadUserByUsername(username);
            System.out.println(userEntity);
            try {
                groupApi.addMember(group, userEntity.getGitlabId(), 30);
                UserServiceEntity userServiceEntity = new UserServiceEntity(null, userEntity, serviceEn, false);
                userServiceService.saveUserService(userServiceEntity);
            } catch (Exception e) {
                System.out.println("Thêm member vào service không thành công");
            }
        }
    }

//    @GetMapping("/test")
//    public String generateJWT() {
//        UserEntity userEntity = new UserEntity(null, "vugiangcoder1", "abcdef", RoleEntity.ADMIN, 35L);
//        userService.saveUser(userEntity);
//        UserDetail userDetail = new UserDetail(userEntity);
//        return jwtTokenProvider.generateToken(userDetail);
//    }
//
//    @GetMapping("/test1")
//    public String generateJWT1() {
//        UserEntity userEntity = new UserEntity(null, "vugiangcoder2", "abcdef", RoleEntity.ADMIN, 36L);
//        userService.saveUser(userEntity);
//        UserDetail userDetail = new UserDetail(userEntity);
//        return jwtTokenProvider.generateToken(userDetail);
//    }

    @GetMapping()
    public String createJenkinsJob(String pathSourcode, String pathDepend, String imageName) {
        String jenkin_pipeline_script = "<?xml version='1.1' encoding='UTF-8'?>\n" +
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
                "properties([\n" +
                "    gitLabConnection('http://root:L8uX4Y3zsEstqPXAfy9o@192.168.56.1:80/viettel-vdt2023/tv360/login/tv360loginsourcecode'),\n" +
                "    pipelineTriggers([\n" +
                "        [\n" +
                "            $class: 'GitLabPushTrigger',\n" +
                "            triggerOnPush: true,\n" +
                "            triggerOnMergeRequest: true,\n" +
                "        ]\n" +
                "    ]),\n" +
                "    disableConcurrentBuilds(),\n" +
                "    overrideIndexTriggers(false)\n" +
                "])\n" +
                "\n" +
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
                "                script {\n" +
                "                    docker.withRegistry(\"\", registryCredential) {\n" +
                "                        dockerImage.push()\n" +
                "                    }\n" +
                "                }\n" +
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
                "                        credentialsId: \"config-jenkins-k8s\",\n" +
                "                        serverUrl: \"https://127.0.0.1:59653\"\n" +
                "                    ]) {\n" +
                "                        bat \"kubectl apply -f deployment.yaml\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +                "        </script>\n" +
                "        <sandbox>true</sandbox>\n" +
                "    </definition>\n" +
                "    <triggers/>\n" +
                "    <disabled>false</disabled>\n" +
                "</flow-definition>";
        return jenkin_pipeline_script;
    }

    public String createJenkinsJobPro() throws IOException {
        String filePathPro = "D:\\vdt2023\\src\\main\\resources\\configPro.xml";
        byte[] bytePro = Files.readAllBytes(Paths.get(filePathPro));
        String jobXmlPro = new String(bytePro);
        return jobXmlPro;
    }
}
