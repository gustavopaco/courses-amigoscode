package com.pacoprojects.webmvnsbamigoscodespringsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;

    public ApplicationSecurityConfig(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                TODO: I Will teach this in detail in the next section
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(
                        ApplicationUserRole.ADMIN.name(),
                        ApplicationUserRole.ADMIN_TRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("augusto")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.STUDENT.name()) /* Spring Security vai ler roles como: ROLE_STUDENT */
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails user2 = User.builder()
                .username("gustavo")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.ADMIN.name()) /* Spring Security vai ler roles como: ROLE_ADMIN */
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails user3 = User.builder()
                .username("tom")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.ADMIN_TRAINEE.name()) /* Spring Security vai ler roles como: ROLE_ADMIN_TRAINEE */
                .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }
}
