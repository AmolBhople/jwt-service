package ab.techstack.spring.jwtservice.filter;

import ab.techstack.spring.jwtservice.controller.JWTWelcomeController;
import ab.techstack.spring.jwtservice.service.CustomUserDetailsService;
import ab.techstack.spring.jwtservice.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String username = null;

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        logger.info("authorizationHeader : "+authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);

            logger.info("token : "+token);
            logger.info("username : "+username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            logger.info("userDetails : "+userDetails);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
