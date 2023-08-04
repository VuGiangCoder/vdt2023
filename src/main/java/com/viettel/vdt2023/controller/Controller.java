package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.gitlab.api.models.Visibility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    public String sayOke() {
        return "oke";
    }

    @PostMapping("/api/create-system")
    public String createSystem(@RequestBody Group group) throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        groupApi.addGroup(group.getName_of_system(), group.getName_of_system(), group.getDescription(), group.getVisibility(), group.getLfsEnabled(),
                group.getRequestAccessEnabled(), group.getParentId());
        return "oke";
    }

}
