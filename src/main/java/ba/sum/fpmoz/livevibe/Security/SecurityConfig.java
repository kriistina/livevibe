package ba.sum.fpmoz.livevibe.Security;

import ba.sum.fpmoz.livevibe.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService appUserService;

    // Constructor injection
    public SecurityConfig(UserService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // New way to disable CSRF in Spring Security 6.1+
                .formLogin(httpForm -> {
                    httpForm.loginPage("/login").permitAll();
                    httpForm.defaultSuccessUrl("/", true);  // Default redirect after login
                    httpForm.successHandler((request, response, authentication) -> {
                        String redirectUrl = authentication.getAuthorities().stream()
                                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))  // Check if user is an ADMIN
                                ? "/admin"  // Redirect to admin page
                                : "/";  // Redirect to home page for regular users
                        response.sendRedirect(redirectUrl);
                    });
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(
                            "/login",
                            "/register",
                            "/auth/register",
                            "/auth/register-admin",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/webjars/**"
                    ).permitAll();

                    registry.requestMatchers("/admin/**").hasAuthority("ADMIN");  // Ensure that only admins can access the admin page
                    registry.anyRequest().authenticated();  // Authenticate other requests
                })

                .build();
    }
}
