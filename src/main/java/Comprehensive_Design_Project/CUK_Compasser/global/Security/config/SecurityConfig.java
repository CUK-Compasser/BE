package Comprehensive_Design_Project.CUK_Compasser.global.Security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( (request) ->
                        request.requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
