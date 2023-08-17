package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.jenkins.api.JenkinsServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class CiCdController {

    @PostMapping("/create-job")
    public void createJob() throws URISyntaxException, IOException {
        JenkinsServer jenkins = new JenkinsServer(new URI("http://192.168.56.1:1234"), "minhgiang89", "giang2010gc@");
        System.out.println(jenkins);
        String filePath = "D:\\vdt2023\\src\\main\\resources\\config.xml";
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String jobXml = new String(bytes);
        jenkins.createJob("hello-world11", jobXml, true);
    }


}
