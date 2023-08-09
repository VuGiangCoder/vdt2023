package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.CreateSystemRequestEntity;
import com.viettel.vdt2023.entity.RoleEntity;
import com.viettel.vdt2023.entity.UserDetail;
import com.viettel.vdt2023.entity.UserEntity;
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
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        UserEntity userEntity = userService.loadUserByUsername(username);
        System.out.println(userEntity.toString());
        groupApi.addMember(group, userEntity.getGitlabId(), 30);
    }
    @GetMapping("/test")
    public String generateJWT() {
        UserEntity userEntity = new UserEntity(1L, "19020807@vnu.edu.vn", "abcdef", RoleEntity.ADMIN, 15083731L);
        UserDetail userDetail = new UserDetail(userEntity);
        return jwtTokenProvider.generateToken(userDetail);
    }
}
