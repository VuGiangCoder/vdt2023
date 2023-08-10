package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.gitlab.api.models.Visibility;
import com.viettel.vdt2023.jwt.JwtTokenProvider;
import com.viettel.vdt2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    @Autowired
    private final UserService userService;
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    @PostMapping("/api/create-system")
    public void createSystem(@RequestBody CreateSystemRequestEntity createSystemRequestEntity,
                             @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        Group group = groupApi.addGroup(createSystemRequestEntity.getName(), createSystemRequestEntity.getName(),
                createSystemRequestEntity.getDecription(), Visibility.PRIVATE, true, true,
                70977408L);

        System.out.println(group.getPath());
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        UserEntity userEntity = userService.loadUserByUsername(username);
        System.out.println(userEntity.toString());
        groupApi.addMember(group, userEntity.getGitlabId(), 30);
    }
    @PostMapping("/api/add-member-system")
    public String addMemberToSystem(@RequestBody AddMemberSystemRequestEntity addMemberSystemRequest) throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        List<Group> groups = groupApi.getGroups();
        for (Group group : groups) {
            if (group.getWebUrl().equals("https://gitlab.com/groups/viettel-vdt2023/" + addMemberSystemRequest.getGroupname())) {
                List<String> usernames = addMemberSystemRequest.getUsername();
                for (String username : usernames) {
                    UserEntity userEntity = userService.loadUserByUsername(username);
                    try {
                        groupApi.addMember(group, userEntity.getGitlabId(), 30);
                    } catch (GitLabApiException gitLabApiException) {
                        return username + "Đã trong group";
                    }
                }
            }
        }
        return null;
    }
    @GetMapping("/systems")
    public List<Group> getListGroup() throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        return groupApi.getGroups();
    }
    @PostMapping("/api/create-service")
    public String createService(@RequestBody CreateServiceRequestEntity createServiceRequestEntity,
                              @RequestHeader("Authorization") String authToken) throws GitLabApiException {
        String token = authToken.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            return null;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        List<Group> groups = groupApi.getGroups();
        for (Group group : groups) {
            if (group.getWebUrl().equals("https://gitlab.com/groups/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName())) {
                Group childGroup = groupApi.addGroup(createServiceRequestEntity.getName(), createServiceRequestEntity.getName(),
                        createServiceRequestEntity.getDescription(), Visibility.PRIVATE, true, true,
                        group.getId());
                String username = jwtTokenProvider.getUsernameFromJWT(token);
                UserEntity userEntity = userService.loadUserByUsername(username);
                System.out.println(userEntity.toString());
                groupApi.addMember(childGroup, userEntity.getGitlabId(), 30);
            }
        }
        return "Thêm thành công";
    }


    @GetMapping("/test")
    public String generateJWT() {
        UserEntity userEntity = new UserEntity(1L, "19020807@vnu.edu.vn", "abcdef", RoleEntity.ADMIN, 15083731L);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }

    @GetMapping("/test1")
    public String generateJWT1() {
        UserEntity userEntity = new UserEntity(1L, "giang2010gc@gmail.com", "abcdef", RoleEntity.ADMIN, 10416103L);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }

    @GetMapping("/system")
    public Group getGroup(@RequestParam String path) throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        Group group = groupApi.getGroup(path);
        return group;
    }

    @GetMapping("/group")
    public Long getGroups(@RequestParam String groupname) throws GitLabApiException {
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        List<Group> groups = groupApi.getGroups();

        //List<Integer> groupIds = new ArrayList<>();
        for (Group group : groups) {
            if (group.getWebUrl().equals(groupname)) {
                //groupIds.add(Math.toIntExact(group.getId()));
                return group.getId();
            }
        }
        return null;
    }
}
