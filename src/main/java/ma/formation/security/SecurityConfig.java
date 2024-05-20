package ma.formation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

import static org.hibernate.criterion.Restrictions.and;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user/index", true)
                .permitAll();

        http.rememberMe()
                .key("uniqueAndSecretKey") // Clé secrète pour signer les tokens Remember Me
                .rememberMeParameter("remember-me") // Paramètre du formulaire pour le Remember Me
                .tokenValiditySeconds(86400); // Durée de validité du token Remember Me en secondes (ici, 24 heures)

        http.logout().permitAll();
        http.authorizeRequests()
                .antMatchers("/home","/fonts/**","/webfonts/**","/").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/resources/**","/static/**","/plugins/**","/about_us","/contact_us","/reset_password", "/webjars/**", "/login","/css/**", "/js/**","/frontendold/**", "/frontend/**","/backend/**").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");
    }



        /*http
                .authorizeRequests()
                .antMatchers("/resources/**", "/webjars/**", "/login").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/user/**").hasAnyAuthority("USER")
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user/index")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .headers()
                .cacheControl().disable();
        http.exceptionHandling().accessDeniedPage("/403");
    }*/





}
