package com.kwm0304.cli.service;

import com.kwm0304.cli.model.FileContent;
import com.kwm0304.cli.template.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityService {

    private final AuthControllerTemplate authControllerTemplate;
    private final AuthResponseTemplate authResponseTemplate;
    private final AuthService authService;
    private final CorsConfigTemplate corsConfigTemplate;
    private final JwtAuthFilterTemplate jwtAuthFilterTemplate;
    private final JwtServiceTemplate jwtServiceTemplate;
    private final LogoutHandlerTemplate logoutHandlerTemplate;
    private final RoleTemplate roleTemplate;
    private final SecurityConfigTemplate securityConfigTemplate;
    private final TokenRepositoryTemplate tokenRepositoryTemplate;
    private final TokenTemplate tokenTemplate;
    private final UserDetailsServiceImplTemplate userDetailsServiceImplTemplate;
    private final UserFieldsTemplate userFieldsTemplate;
    private final UserRepositoryOptionalTemplate userRepositoryOptionalTemplate;
    public SecurityService(AuthControllerTemplate authControllerTemplate,
                           AuthResponseTemplate authResponseTemplate,
                           AuthService authService,
                           CorsConfigTemplate corsConfigTemplate,
                           JwtAuthFilterTemplate jwtAuthFilterTemplate,
                           JwtServiceTemplate jwtServiceTemplate,
                           LogoutHandlerTemplate logoutHandlerTemplate,
                           RoleTemplate roleTemplate,
                           SecurityConfigTemplate securityConfigTemplate,
                           TokenRepositoryTemplate tokenRepositoryTemplate,
                           TokenTemplate tokenTemplate,
                           UserDetailsServiceImplTemplate userDetailsServiceImplTemplate,
                           UserFieldsTemplate userFieldsTemplate,
                           UserRepositoryOptionalTemplate userRepositoryOptionalTemplate) {
        this.authControllerTemplate = authControllerTemplate;
        this.authResponseTemplate = authResponseTemplate;
        this.authService = authService;
        this.corsConfigTemplate = corsConfigTemplate;
        this.jwtAuthFilterTemplate = jwtAuthFilterTemplate;
        this.jwtServiceTemplate = jwtServiceTemplate;
        this.logoutHandlerTemplate = logoutHandlerTemplate;
        this.roleTemplate = roleTemplate;
        this.securityConfigTemplate = securityConfigTemplate;
        this.tokenRepositoryTemplate = tokenRepositoryTemplate;
        this.tokenTemplate = tokenTemplate;
        this.userDetailsServiceImplTemplate = userDetailsServiceImplTemplate;
        this.userFieldsTemplate = userFieldsTemplate;
        this.userRepositoryOptionalTemplate = userRepositoryOptionalTemplate;
    }

    public List<FileContent> makeConfigFiles(String parentDirString, String modelDirString, boolean useLombok) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("CustomLogoutHandler.java", logoutHandlerTemplate.genLogoutHandler(parentDirString, modelDirString, useLombok)));
        files.add(new FileContent("SecurityConfig.java", securityConfigTemplate.genSecurityConfig(parentDirString, useLombok)));
        files.add(new FileContent("CorsConfig.java", corsConfigTemplate.genCorsConfig(parentDirString)));
        return files;
    }

    public List<FileContent> makeFilterFiles(String parentDirString, boolean useLombok) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("JwtAuthFilter.java", jwtAuthFilterTemplate.genAuthFilter(parentDirString, useLombok)));
        return files;
    }

    public List<FileContent> makeServiceFiles(String parentDirString, String modelDirString, String userClass, boolean useLombok) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("AuthService.java", authService.genAuthService(parentDirString, modelDirString, userClass, useLombok)));
        files.add(new FileContent("JwtService.java", jwtServiceTemplate.genJwtService(parentDirString,modelDirString, userClass, useLombok)));
        files.add(new FileContent("UserDetailsServiceImpl.java", userDetailsServiceImplTemplate.genUserDetailsService(parentDirString, userClass, useLombok)));
        return files;
    }

    public List<FileContent> makeControllerFiles(String parentDirString, String userClass, boolean useLombok, String modelDirString) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("AuthController.java", authControllerTemplate.genAuthController(parentDirString, userClass, useLombok, modelDirString)));
        return files;
    }

    public List<FileContent> makeModelFiles(String modelDirString, boolean useLombok, String userClass) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("AuthResponse.java", authResponseTemplate.genAuthResponse(modelDirString, useLombok)));
        files.add(new FileContent("Role.java", roleTemplate.genRoleEnum(modelDirString)));
        files.add(new FileContent("Token.java", tokenTemplate.genToken(userClass, modelDirString, useLombok)));
        return files;
    }

    public List<FileContent> makeRepositoryFiles(String modelDirString, String parentDirString, String userClass, String idField) {
        List<FileContent> files = new ArrayList<>();
        files.add(new FileContent("TokenRepository.java", tokenRepositoryTemplate.genTokenRepository(modelDirString, parentDirString, userClass, idField)));
        return files;
    }

}
