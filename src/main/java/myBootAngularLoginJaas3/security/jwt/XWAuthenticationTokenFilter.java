package myBootAngularLoginJaas3.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import myBootAngularLoginJaas3.service.UserDetailsServiceImpl;


@Component
public class XWAuthenticationTokenFilter extends OncePerRequestFilter {

    
    
    UserDetailsServiceImpl userDetailsService;
  //  @Resource
   // private XWTokenUtil xwTokenUtil;
    
    @Autowired
	JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(XWAuthenticationTokenFilter.class);
   
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	logger.info("[doFilterInternal]: STARTED");
        String authHeader = request.getHeader("Authorization");
       logger.info("[doFilterInternal]: Authorization_Header ==> "+authHeader);
        String tokenHead = "Bearer ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
        	logger.info("[doFilterInternal]: 1");
            String authToken = authHeader.substring(tokenHead.length());
            logger.info("[doFilterInternal]: 2");
            String username = jwtUtils.getUserNameFromJwtToken(authToken);
            logger.info("[doFilterInternal]: USERNAME "+username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
              
                
                 
                    userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtUtils.validateJwtToken(authToken, userDetails)) {
                   
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

}