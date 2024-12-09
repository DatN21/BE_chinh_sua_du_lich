package com.dulich.toudulich.configuration;

import com.dulich.toudulich.Model.RoleModel;
import com.dulich.toudulich.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebConfig  {
    private final JwtTokenFilter jwtTokenFilter ;

    @Value("${api.prefix}")
    private String apiPrefix ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            // Public endpoints
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            ).permitAll()

                            // bookings endpoints
                            .requestMatchers(HttpMethod.PUT, String.format("%s/bookings/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/bookings/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/bookings/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/bookings/user/**", apiPrefix))
                            .hasAnyRole(RoleModel.USER, RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.GET, String.format("%s/bookings/**", apiPrefix))
                            .hasRole(RoleModel.ADMIN)


                            // tours endpoints
                            .requestMatchers(HttpMethod.PUT, String.format("%s/tours/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/tours/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/tours/**", apiPrefix)).hasRole( RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.GET, String.format("%s/tours/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/tours/search/**", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/users/full/**", apiPrefix)).hasRole( RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/users/admin/", apiPrefix)).hasAnyRole(RoleModel.USER, RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.POST, String.format("%s/users/details/**", apiPrefix)).hasAnyRole(RoleModel.USER, RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/users/**", apiPrefix)).hasAnyRole(RoleModel.USER, RoleModel.ADMIN)
                            // Any other request must be authenticated

                            .requestMatchers(HttpMethod.GET, String.format("%s/images/user/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/images/**", apiPrefix)).hasRole( RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.GET, String.format("%s/images/full/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/images/**", apiPrefix)).permitAll()


                            .requestMatchers(HttpMethod.GET, String.format("%s/admin/**", apiPrefix)).hasRole( RoleModel.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/admin/**", apiPrefix)).hasRole( RoleModel.ADMIN)



                            .anyRequest().authenticated();


                })
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration() ;
                configuration.setAllowedOrigins(List.of("http://localhost:4200"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
                source.registerCorsConfiguration("/**",configuration);
                httpSecurityCorsConfigurer.configurationSource(source) ;
            }
        }) ;
        return httpSecurity.build();

    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:/tmp/uploads/");  // Đường dẫn thư mục lưu trữ ảnh
//    }
}
