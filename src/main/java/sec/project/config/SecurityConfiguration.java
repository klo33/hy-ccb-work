package sec.project.config;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("default")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ServletContext servletContext;
//  servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.URL/* .COOKIE*/));
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // FLAW several

        http.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll().and()
                .authorizeRequests().antMatchers("/events/*/form").permitAll().and()
                .authorizeRequests().antMatchers("/redirect").permitAll().and()
                // FLAW allows access to /users submappings
                .authorizeRequests().antMatchers("/users").hasAuthority("ADMIN").and()
                .authorizeRequests().anyRequest().authenticated().and()
                .formLogin().loginPage("/login").permitAll().and().logout().permitAll();
        // FLAW CSRF-safety disabled
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
