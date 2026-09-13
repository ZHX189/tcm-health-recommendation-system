package com.tcm.security.filter;

import cn.hutool.core.util.StrUtil;
import com.tcm.common.constant.CommonConstant;
import com.tcm.security.context.UserContext;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if (StrUtil.isNotBlank(token) && jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserId(token);
                String username = jwtUtil.getUsername(token);
                Integer userType = jwtUtil.getUserType(token);

                // 设置用户上下文
                UserContext userContext = new UserContext(userId, username, userType);
                UserContextHolder.setContext(userContext);

                // 设置Spring Security认证
                String role = switch (userType) {
                    case 1 -> "ROLE_USER";
                    case 2 -> "ROLE_STAFF";
                    case 3 -> "ROLE_ADMIN";
                    default -> "ROLE_USER";
                };

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.debug("JWT认证失败: {}", e.getMessage());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(CommonConstant.TOKEN_PREFIX)) {
            return bearerToken.substring(CommonConstant.TOKEN_PREFIX.length());
        }
        return null;
    }
}
