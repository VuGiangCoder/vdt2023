package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.ProjectApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.gitlab.api.models.Project;
import com.viettel.vdt2023.gitlab.api.models.Visibility;
import com.viettel.vdt2023.jenkins.api.JenkinsServer;
import com.viettel.vdt2023.jenkins.api.model.Job;
import com.viettel.vdt2023.security.jwt.JwtUtils;
import com.viettel.vdt2023.service.*;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.io.BufferedReader;

import static java.lang.Thread.sleep;

@RestController
@RequiredArgsConstructor
public class CentralizestorageController {

    Dotenv dotenv = Dotenv.load();
    String gitlab_url = dotenv.get("gitlab_url");
    String gitlab_user = dotenv.get("gitlab_user");
    String gitlab_pass = dotenv.get("gitlab_pass");
    String gitlab_token = dotenv.get("gitlab_token");
    String jenkins_token = dotenv.get("jenkins_token");
    String jenkins_url = dotenv.get("jenkins_url");
    String jenkins_user = dotenv.get("jenkins_user");
    String jenkins_pass = dotenv.get("jenkins_pass");
    String cridential_jenkins_minikube = dotenv.get("cridential_jenkins_minikube");
    String minikube_url = dotenv.get("minikube_url");
    String port = dotenv.get("port");
    String hello = dotenv.get("hello");

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

    private final JwtUtils jwtUtils;


    @PostMapping("/api/create-system")
    public String createSystem(@RequestBody CreateSystemRequestEntity createSystemRequestEntity,
                               @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlab_url, gitlab_user, gitlab_pass);
        GroupApi groupApi = new GroupApi(gitLabApi);
        Group group = groupApi.addGroup(createSystemRequestEntity.getName(), createSystemRequestEntity.getName(),
                createSystemRequestEntity.getDecription(), Visibility.PRIVATE, true, true,
                161L);
        SystemEntity systemEntity = new SystemEntity(null, createSystemRequestEntity.getName(), group.getId());
        try {
            systemService.saveSystem(systemEntity);
            String username = jwtUtils.getUsernameFromJWT(token);
            UserEntity userEntity = userService.loadUserByUsername(username);
            UserSystemEntity userSystemEntity = new UserSystemEntity(null, userEntity, systemEntity, true);
            userSystemService.saveUserSystem(userSystemEntity);
            groupApi.addMember(group, userEntity.getGitlabId(), 30);
            return "Success to create system";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to create system";
        }

    }

    @PostMapping("/api/add-member-system")
    public String addMemberToSystem(@RequestBody AddMemberSystemRequestEntity addMemberSystemRequest,
                                    @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        String name = jwtUtils.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlab_url, gitlab_user, gitlab_pass);
        GroupApi groupApi = new GroupApi(gitLabApi);
        SystemEntity systemEn = systemService.loadSystemByName(addMemberSystemRequest.getGroupname());
        UserSystemEntity userSystemEn = userSystemService.loadUserSystem(userEn, systemEn);
        if (userSystemEn == null) {
            return "Not Authenticate";
        }
        Group group = groupApi.getGroup(systemEn.getGitlabId());
        List<String> usernames = addMemberSystemRequest.getUsername();
        for (String username : usernames) {
            UserEntity userEntity = userService.loadUserByUsername(username);
            try {
                groupApi.addMember(group, userEntity.getGitlabId(), 30);
                UserSystemEntity userSystemEntity = new UserSystemEntity(null, userEntity, systemEn, false);
                userSystemService.saveUserSystem(userSystemEntity);
            } catch (Exception e) {
                String message = "Fail to add member to system";
                return message;
            }
        }
        return "Add member successfully";
    }

    @PostMapping("/api/create-service")
    public String createService(@RequestBody CreateServiceRequestEntity createServiceRequestEntity,
                                @RequestHeader("Authorization") String authToken) throws GitLabApiException, URISyntaxException, IOException {
        String token = authToken.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return "Not Authentication";
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlab_url, gitlab_user, gitlab_pass);
        GroupApi groupApi = new GroupApi(gitLabApi);
        ProjectApi projectApi = new ProjectApi(gitLabApi);
        SystemEntity systemEntity = systemService.loadSystemByName(createServiceRequestEntity.getParentGroupName());
        String name = jwtUtils.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        UserSystemEntity userSystemEntity = userSystemService.loadUserSystem(userEn, systemEntity);
        if (userSystemEntity == null) {
            return "Not Authentication";
        }
        Group childGroup = groupApi.addGroup(createServiceRequestEntity.getName(), createServiceRequestEntity.getName(),
                createServiceRequestEntity.getDescription(), Visibility.PRIVATE, true, true,
                systemEntity.getGitlabId());
        String username = jwtUtils.getUsernameFromJWT(token);
        UserEntity userEntity = userService.loadUserByUsername(username);
        try {
            groupApi.addMember(childGroup, userEntity.getGitlabId(), 30);
            ServiceEntity serviceEntity = new ServiceEntity(null, createServiceRequestEntity.getName(), systemEntity,
                    childGroup.getId());
            serviceService.saveService(serviceEntity);
            UserServiceEntity userServiceEntity = new UserServiceEntity(null, userEntity, serviceEntity, true);
            userServiceService.saveUserService(userServiceEntity);
            Project sourceCodeRepo = projectApi.createProject(childGroup.getId(),
                    createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() +
                            "sourcecode");
            String url = "http://" + gitlab_user + ":" + gitlab_token + "@" + "192.168.56.1" + "/viettel-vdt2023/";
            String pathSourceCode = url + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() +
                    createServiceRequestEntity.getName() + "sourcecode ";
            String imageName = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName();
            Project dependencyRepo = projectApi.createProject(childGroup.getId(),
                    createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName() +
                            "dependency");
            String pathDepend = url + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName()
                    + createServiceRequestEntity.getName() + "dependency";
            String nameDepend = createServiceRequestEntity.getParentGroupName() + createServiceRequestEntity.getName();
            JobControllerBuilder jobControllerBuilder = new JobControllerBuilder(pathSourceCode, pathDepend, imageName,
                    cridential_jenkins_minikube, minikube_url, port);
            String jenkins_script_pipeline_sourceCode = jobControllerBuilder.createJenkinsJob();
            JenkinsServer jenkins = new JenkinsServer(new URI(jenkins_url), jenkins_user, jenkins_pass);
            jenkins.createJob(imageName, jenkins_script_pipeline_sourceCode, true);
            projectApi.addHook(dependencyRepo, "http://" + jenkins_user + ":" + jenkins_token +
                    "@192.168.56.1:1234/project/" + imageName, true, true, true);
            String link = "Link source code: " + gitlab_url + "/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() +
                    createServiceRequestEntity.getName() + "sourcecode\n"
                    + "Link dependency: " + gitlab_url + "/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "/" + createServiceRequestEntity.getParentGroupName() +
                    createServiceRequestEntity.getName() + "dependency";
            return link;
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/api/add-member-service")
    public String addMemberToService(@RequestBody AddMemberServiceRequestEntity addMemberServiceRequest,
                                     @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        String name = jwtUtils.getUsernameFromJWT(token);
        UserEntity userEn = userService.loadUserByUsername(name);
        System.out.println(userEn);
        GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlab_url, gitlab_user, gitlab_pass);
        GroupApi groupApi = new GroupApi(gitLabApi);
        ServiceEntity serviceEn = serviceService.loadServiceByName(addMemberServiceRequest.getGroupname());
        UserServiceEntity userServiceEn = userServiceService.loadUserService(userEn, serviceEn);
        if (userServiceEn == null) {
            return "Not Authorization";
        }
        Group group = groupApi.getGroup(serviceEn.getGitlabId());
        List<String> usernames = addMemberServiceRequest.getUsername();
        for (String username : usernames) {
            UserEntity userEntity = userService.loadUserByUsername(username);
            System.out.println(userEntity);
            try {
                groupApi.addMember(group, userEntity.getGitlabId(), 30);
                UserServiceEntity userServiceEntity = new UserServiceEntity(null, userEntity, serviceEn, false);
                userServiceService.saveUserService(userServiceEntity);
            } catch (Exception e) {
                return e.toString();
            }
        }
        return "Success to add member to service";
    }

    @GetMapping("/get-console")
    public String getConsole(@RequestParam(name = "imagename") String imagename) {
        String jenkinsURL = jenkins_url + "/";
        String jobName = imagename;
        String buildNumber = "lastBuild";

        String username = jenkins_user;
        String password = jenkins_pass; // Điều này là một API token hoặc mật khẩu của bạn

        try {
            String authString = username + ":" + password;
            String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());

            URL url = new URL(jenkinsURL + "job/" + jobName + "/" + buildNumber + "/consoleText");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", authHeaderValue);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                String console="";
                while ((line = reader.readLine()) != null) {
                    console+=line+"\n";
                }
                reader.close();
                return console;
            } else {
                return "Failed to fetch console log. Response code: " + connection.getResponseCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
