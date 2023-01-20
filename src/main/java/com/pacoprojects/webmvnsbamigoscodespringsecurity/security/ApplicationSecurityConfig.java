package com.pacoprojects.webmvnsbamigoscodespringsecurity.security;

import com.pacoprojects.webmvnsbamigoscodespringsecurity.auth.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;

    private final ApplicationUserService applicationUserService;

    public ApplicationSecurityConfig(PasswordConfig passwordConfig, ApplicationUserService applicationUserService) {
        this.passwordConfig = passwordConfig;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

//                TODO: I Will teach this in detail in the next section
                /* Configurando o CSRF para que o Token nao seja acessado por um script JS*/
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(
//                        ApplicationUserRole.ADMIN.name(),
//                        ApplicationUserRole.ADMIN_TRAINEE.name())
                .anyRequest().authenticated()

                /* Basic Authentication*/
//                .and().httpBasic();

                /* Form Authentication*/
                .and().formLogin()

                /* -LOGIN- Indicando end-point para sobrescrever default Spring Security LOGIN PAGE*/
                .loginPage("/login").permitAll()
                /* Possivel customizar "name" do INPUT da Pagina HTML onde ira passar o USERNAME */
                .usernameParameter("username")
                /* Possivel customizar "name" do INPUT da Pagina HTML onde ira passar o PASSWORD */
                .passwordParameter("password")
                /* Definindo URL que sera enviado apos LOGIN SUCCESS, true = Forçar REDIRECT */
                .defaultSuccessUrl("/courses", true)
                /* Gera um cookie no HEADER da Response com um Token valido por 2 semanas | Pagina deve ter componente checkbox com name = remember-me|*/
                .and().rememberMe()
                /* Possivel customizar "name" do CHECKBOX da Pagina HTML onde ira passar o REMEMBER-ME=true ou false */
                .rememberMeParameter("remember-me")
                /* TOKEN_EXPIRATION_TIME*/
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                /* SENHA UTILIZADA PARA CRIPTOGRAFAR  */
                .key("SenhaExtremamenteSecreta")

                /* -LOGOUT- Indicando end-point que pode ser sobrescrito ou nao para deslogar*/
                .and().logout().logoutUrl("/logout")
                /* Se CSRF estiver desabilitado e quiser utilizar logout como GET deve implementar esse metodo */
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                /* Define que usuario esta deslogado */
                .clearAuthentication(true)
                /* Define que Sessao de Usuario esta invalida */
                .invalidateHttpSession(true)
                /* Deleta os cookie criados abaixo*/
                .deleteCookies("JSESSIONID", "remember-me")
                /* Redireciona para URL ao deslogar*/
                .logoutSuccessUrl("/login");

    }

/*    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("augusto")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.STUDENT.name()) // Spring Security vai ler roles como: ROLE_STUDENT
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails user2 = User.builder()
                .username("gustavo")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.ADMIN.name()) // Spring Security vai ler roles como: ROLE_ADMIN
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails user3 = User.builder()
                .username("tom")
                .password(passwordConfig.passwordEncoder().encode("123"))
//                .roles(ApplicationUserRole.ADMIN_TRAINEE.name()) // Spring Security vai ler roles como: ROLE_ADMIN_TRAINEE
                .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }*/

    /* METODO DESNECESSARIO posso usar somente o configure(AuthenticationManagerBuilder) exemplo de SOLUCAO comentado abaixo*/
    @Bean
    public DaoAuthenticationProvider daoAuthenticatorProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(applicationUserService);
        auth.setPasswordEncoder(passwordConfig.passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticatorProvider());
//        auth.userDetailsService(applicationUserService).passwordEncoder(passwordConfig.passwordEncoder()); // SOLUCAO
    }
}
