package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.gitlab.api.GitLabApi;
import com.viettel.vdt2023.gitlab.api.GitLabApiException;
import com.viettel.vdt2023.gitlab.api.GroupApi;
import com.viettel.vdt2023.gitlab.api.ProjectApi;
import com.viettel.vdt2023.gitlab.api.models.Group;
import com.viettel.vdt2023.gitlab.api.models.Project;
import com.viettel.vdt2023.gitlab.api.models.Visibility;
import com.viettel.vdt2023.jwt.JwtTokenProvider;
import com.viettel.vdt2023.service.*;
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
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
        GroupApi groupApi = new GroupApi(gitLabApi);
        Group group = groupApi.addGroup(createSystemRequestEntity.getName(), createSystemRequestEntity.getName(),
                createSystemRequestEntity.getDecription(), Visibility.PRIVATE, true, true,
                70977408L);
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
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
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
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
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
            groupApi.addMember(childGroup, userEntity.getGitlabId(), 30);
            UserServiceEntity userServiceEntity = new UserServiceEntity(null, userEntity, serviceEntity, true);
            userServiceService.saveUserService(userServiceEntity);
            Project sorceCodeRepo = projectApi.createProject(childGroup.getId(), "sourcecode");
            Project dependencyRepo = projectApi.createProject(childGroup.getId(), "dependency");
            String link = "Link source code: " + "https://gitlab.com/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "sourcecode \n"
                    + "Link source code: " + "https://gitlab.com/viettel-vdt2023/" + createServiceRequestEntity.getParentGroupName()
                    + "/" + createServiceRequestEntity.getName() + "dependenvy";
            return link;
        } catch (Exception e) {

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
        GitLabApi gitLabApi = GitLabApi.oauth2Login("https://gitlab.com", "giang2010gc@gmail.com",
                "Gag08092001@");
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
        UserEntity userEntity = new UserEntity(null, "19020807@vnu.edu.vn", "abcdef", RoleEntity.ADMIN, 15083731L);
        userService.saveUser(userEntity);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }

    @GetMapping("/test1")
    public String generateJWT1() {
        UserEntity userEntity = new UserEntity(null, "giang2010gc@gmail.com", "abcdef", RoleEntity.ADMIN, 10416103L);
        userService.saveUser(userEntity);
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
