package dev.thinktank.mvno.mcp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UtilService {

    public static String getCurrentUser() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        return authentication.getTokenAttributes().get("preferred_username").toString();
    }
}
