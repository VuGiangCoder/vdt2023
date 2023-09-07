package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.jenkins.api.JenkinsServer;
import com.viettel.vdt2023.jenkins.api.JenkinsTriggerHelper;
import com.viettel.vdt2023.jenkins.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


import static java.lang.Thread.sleep;

@RestController
public class CiCdController {

    @PostMapping("/create-job")
    public void createJob() throws URISyntaxException, IOException, InterruptedException {
        JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:1234/"), "minhgiang89", "giang2010gc@");
        String filePathPro = "D:\\vdt2023\\src\\main\\resources\\configPro.xml";
        String filePath = "D:\\vdt2023\\src\\main\\resources\\config.xml";
        byte[] bytePro = Files.readAllBytes(Paths.get(filePathPro));
        String jobXmlPro = new String(bytePro);
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String jobXml = new String(bytes);
        jenkins.createJob("hello-world14", jobXmlPro, true);
        Job job = jenkins.getJob("hello-world14");
        job.build(true);
        //jenkins.updateJob("hello-world14",jobXml,true);
    }

    @PostMapping("/delete")
    public void deleteGroup() throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://192.168.56.1:80", "root",
                "12345678");
        GroupApi groupApi = new GroupApi(gitLabApi);
        List<Group> groups = groupApi.getGroups();
        for (Group gr : groups) {
            groupApi.deleteGroup(gr);
        }
    }

    @PostMapping("/deploy")
    public void deploy() throws FileNotFoundException {
        String path = "src/main/resources/deployment.yaml";
        InputStream inputStream = new FileInputStream(path);
        try (KubernetesClient k8s = new KubernetesClientBuilder().build()) {
            k8s.load(inputStream)
                    .inNamespace("default")
                    .createOrReplace();
        }
    }

    @PostMapping("/get-job-status")
    public void build() throws IOException, URISyntaxException, InterruptedException {
        JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:1234"), "minhgiang89", "giang2010gc@");
        JenkinsTriggerHelper jenkinsTriggerHelper = new JenkinsTriggerHelper(jenkins, 10000L);
        jenkinsTriggerHelper.triggerJobAndWaitUntilFinished("hello-world13", true);
    }

    @GetMapping("/status")
    public void check(@RequestParam(name = "jobname") String jobName) throws UnsupportedEncodingException {
        String jenkinsUrl = "https://localhost:1234/job/";

        String jobNameURL = java.net.URLEncoder.encode(jobName, "UTF-8");
        try {
            URL url = new URL(jenkinsUrl + jobNameURL + "/lastBuild/api/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.out.println("URL Error: " + conn.getResponseCode());
                System.out.println("      (job name [" + jobName + "] probably wrong)");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            String buildStatusJsonString = builder.toString();
            JSONObject buildStatusJson = new JSONObject(buildStatusJsonString.isEmpty());
            System.out.println(buildStatusJsonString);
            if (buildStatusJson.has("result")) {
                System.out.println("[" + jobName + "] build status: " + buildStatusJson.getString("result"));
                if (!"SUCCESS".equals(buildStatusJson.getString("result"))) {
                    System.out.println("success");
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to fetch or parse JSON");
        }
    }

}
