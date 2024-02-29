package com.kwm0304.cli.service;

import com.kwm0304.cli.template.security.*;
import org.springframework.stereotype.Service;

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
    private GeneratorService generatorService;
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
                           UserRepositoryOptionalTemplate userRepositoryOptionalTemplate,
                           GeneratorService generatorService) {
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
        this.generatorService = generatorService;
    }

    public String makeSecurityFiles(String userClass, boolean useLombok, String parentDirString, String modelDirString, String userIdType) {
        String authResponseContent = authResponseTemplate.genAuthResponse(modelDirString, useLombok);
        generatorService.
        authResponseTemplate.genAuthResponse(modelDirString, useLombok);
        authControllerTemplate.genAuthController(userClass, parentDirString, useLombok, modelDirString);
        authService.genAuthService(parentDirString, modelDirString, userClass, useLombok);
        corsConfigTemplate.genCorsConfig(parentDirString);
        jwtAuthFilterTemplate.genAuthFilter(parentDirString,useLombok);
        jwtServiceTemplate.genJwtService(parentDirString, modelDirString, userClass, useLombok);
        logoutHandlerTemplate.genLogoutHandler(parentDirString, modelDirString, useLombok);
        roleTemplate.genRoleEnum(modelDirString);
        securityConfigTemplate.genSecurityConfig(parentDirString, useLombok);
        tokenRepositoryTemplate.genTokenRepository(modelDirString, parentDirString);
        tokenTemplate.genToken(userClass, modelDirString, useLombok);
        userDetailsServiceImplTemplate.genUserDetailsService(parentDirString, userClass, useLombok);
        userFieldsTemplate.genMethods(userClass, useLombok);
        userRepositoryOptionalTemplate.genMethod(userClass, parentDirString, modelDirString, userIdType);
    }

}
