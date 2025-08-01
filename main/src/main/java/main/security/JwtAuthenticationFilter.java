package main.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException{
        String jwt = extractJwtFromRequest(httpServletRequest);

        if (jwt != null && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractUsername(jwt);
            List<GrantedAuthority> authorities = jwtUtil.extractAuthorities(jwt);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = httpServletRequest.getHeader("Authorization");
//        String jwt = null;
//        String username = null;
//
//        if(authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//            username = jwtUtil.extractUsername(jwt);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
////            UserDetails userDetails = this.userService.loadUserByEmail(username);
//            UserDetails userDetails = this.userService.loadUserByUsername(username);
//
//            List<GrantedAuthority> authorities = jwtUtil.extractAuthorities(jwt);
//
//
//
//            if (jwtUtil.validateToken(jwt, userDetails)) {
//
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, authorities);
//
//
//
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
}
