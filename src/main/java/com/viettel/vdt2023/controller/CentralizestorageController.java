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
                73L);
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
    public String addMemberToSystem(@RequestBody AddMemberSystemRequestEntity addMemberSystemRequest,
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
            return "Denied";
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
                return "them " + username + " khong thanh cong";
            }
        }
        return null;
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
                String imageNameSourceCode = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "sourcecode";
                String jenkins_script_pipeline_sourceCode = createJenkinsJob(pathSourceCode, imageNameSourceCode);
                JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:1234"), "minhgiang89", "giang2010gc@");
                jenkins.createJob(imageNameSourceCode, createJenkinsJobPro(), true);
                Job job = jenkins.getJob(imageNameSourceCode);
                job.build(true);
                sleep(3000);
                jenkins.updateJob(imageNameSourceCode, jenkins_script_pipeline_sourceCode, true);
                projectApi.addHook(sourceCodeRepo, "http://minhgiang89:" + jenins_token + "@192.168.56.1:1234/project/"
                        + imageNameSourceCode, true, true, true);
                Project dependencyRepo = projectApi.createProject(childGroup.getId(), createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency");
                String pathDepend = "http://root:L8uX4Y3zsEstqPXAfy9o@192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency";
                String imageNameDepend = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency";
                String jenkins_script_pipeline_depend = createJenkinsJob(pathDepend, imageNameDepend);
                jenkins.createJob(imageNameDepend, createJenkinsJobPro(), true);
                job.build(true);
                jenkins.updateJob(imageNameDepend, jenkins_script_pipeline_depend, true);
                projectApi.addHook(dependencyRepo, "http://minhgiang89:" + jenins_token + "@192.168.56.1:1234/project/"
                        + imageNameDepend, true, true, true);
                String link = "Link source code: " + "http://192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "sourcecode\n"
                        + "Link dependency: " + "http://192.168.56.1:80/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                        + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() + "dependency";
                return link;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    @PostMapping("/api/add-member-service")
    public String addMemberToService(@RequestBody AddMemberServiceRequestEntity addMemberServiceRequest,
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
            return "Denied";
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
                return "them " + username + " khong thanh cong";
            }
        }
        return null;
    }

    @GetMapping("/test")
    public String generateJWT() {
        UserEntity userEntity = new UserEntity(null, "vugiangcoder1", "abcdef", RoleEntity.ADMIN, 35L);
        userService.saveUser(userEntity);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }

    @GetMapping("/test1")
    public String generateJWT1() {
        UserEntity userEntity = new UserEntity(null, "vugiangcoder2", "abcdef", RoleEntity.ADMIN, 36L);
        userService.saveUser(userEntity);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }

    public String createJenkinsJob(String path, String imageName) {
        String jenkin_pipeline_script = "<?xml version='1.1' encoding='UTF-8'?>\n" +
                "<flow-definition plugin=\"workflow-job@2.40\">\n" +
                "    <actions>\n" +
                "        <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@1.11.2\" />\n" +
                "    </actions>\n" +
                "    <description></description>\n" +
                "    <keepDependencies>false</keepDependencies>\n" +
                "    <properties>\n" +
                "        <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "        </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "    </properties>\n" +
                "    <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.86\">\n" +
                "    <script>\n" +
                "       properties([\n" +
                "           gitLabConnection('" + path + "'),\n" +
                "           pipelineTriggers([\n" +
                "               [\n" +
                "                   $class               : 'GitLabPushTrigger',\n" +
                "                   triggerOnPush        : true,\n" +
                "                   triggerOnMergeRequest: true,\n" +
                "               ]\n" +
                "           ]),\n" +
                "           disableConcurrentBuilds(),\n" +
                "           overrideIndexTriggers(false)\n" +
                "      ])\n" +
                "      pipeline{\n" +
                "            agent any\n" +
                "            environment {\n" +
                "                registry = \"vugiangcoder/" + imageName + "\"\n" +
                "                registryCredential = \"dockerhub\"\n" +
                "                dockerImage = \"\"\n" +
                "            }\n" +
                "            stages{\n" +
                "                stage(\"Clone Git repository\"){\n" +
                "                    steps{\n" +
                "                        git(\n" +
                "                            url:\"" + path + "\",\n" +
                "                            branch: \"main\",\n" +
                "                            changelog: true,\n" +
                "                            poll: true\n" +
                "                        )\n" +
                "                    }\n" +
                "                }\n" +
                "                stage(\"Build image\"){\n" +
                "                    steps{\n" +
                "                        script{\n" +
                "                            dockerImage=docker.build registry + \":latest\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "                stage(\"Push image to Docker Hub\"){\n" +
                "                steps{\n" +
                "                    script{\n" +
                "                        docker.withRegistry( \"\", registryCredential){\n" +
                "                            dockerImage.push()\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
//                "        post {\n" +
//                "            always {\n" +
//                "                script {\n" +
//                "                    dockerImage.clean()\n" +
//                "                }\n" +
//                "            }\n" +
//                "        }\n" +
                "        }\n" +
                "    </script>\n" +
                "    <sandbox>true</sandbox>\n" +
                "    </definition>\n" +
                "    <triggers />\n" +
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
