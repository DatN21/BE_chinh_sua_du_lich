package com.dulich.toudulich.filter;

import com.dulich.toudulich.Model.UserModel;
import com.dulich.toudulich.component.JwtTokenUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix ;
    private final UserDetailsService userDetailsService ;
    private final JwtTokenUtil jwtTokenUtil ;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
            try {
                if (isBypassToken(request)){
                    filterChain.doFilter(request,response);
                    return;
                }
                final String authHeader = request.getHeader("Authorization") ;
                if (authHeader == null || !authHeader.startsWith("Bearer ")){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }

                final String token = authHeader.substring(7) ;
                final String phone =  jwtTokenUtil.extractPhone(token) ;
                if(phone!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserModel user = (UserModel) userDetailsService.loadUserByUsername(phone);
                    if (jwtTokenUtil.validateToken(token, user)){
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        ) ;
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

                filterChain.doFilter(request, response);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
    }

    private boolean isBypassToken(@Nonnull HttpServletRequest request){
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/tours", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );
        for (Pair<String,String> bypassToken : bypassTokens){
            if (request.getServletPath().contains(bypassToken.getLeft()) &&
                    request.getMethod().equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }



}
