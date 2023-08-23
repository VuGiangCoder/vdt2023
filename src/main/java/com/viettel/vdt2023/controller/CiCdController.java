package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.jenkins.api.JenkinsServer;
import com.viettel.vdt2023.jenkins.api.model.Job;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@RestController
public class CiCdController {

    @PostMapping("/create-job")
    public void createJob() throws URISyntaxException, IOException, InterruptedException {
        JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:1234"), "minhgiang89", "giang2010gc@");
        String filePathPro = "D:\\vdt2023\\src\\main\\resources\\configPro.xml";
        String filePath = "D:\\vdt2023\\src\\main\\resources\\config.xml";
        byte[] bytePro = Files.readAllBytes(Paths.get(filePathPro));
        String jobXmlPro = new String(bytePro);
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String jobXml = new String(bytes);
        jenkins.createJob("hello-world13", jobXmlPro, true);
//        Map<String, Job> jobs = jenkins.getJobs();
        Job job = jenkins.getJob("hello-world13");
        job.build(true);
        //jenkins.updateJob("hello-world13",jobXml,true);
    }

    @PostMapping("/delete")
    public void deleteGroup() throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        List<Group> groups = groupApi.getGroups();
        for (Group gr: groups) {
            groupApi.deleteGroup(gr);
        }
    }
}
