package edu.ncsu.csc.CoffeeMaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines which URL paths should be secured and which should not
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/", "/index", "/registry.html", "/api/v1/users/**" ).permitAll()
                .antMatchers( "/manager/**" ).hasAuthority( "MANAGER" ).antMatchers( "/customer/**" )
                .hasAuthority( "CUSTOMER" ).antMatchers( "/staff/**" ).hasAuthority( "STAFF" ).and().formLogin()
                .loginPage( "/login" ).successHandler( successHandler() ).permitAll().and().logout()
                .logoutSuccessUrl( "/" ).permitAll().and().csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuth ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userDetailsService ).passwordEncoder( bCryptPasswordEncoder() );
    }

    @Bean
    public AuthenticationSuccessHandler successHandler () {
        final SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl( "/" ); // Set the default target URL after
                                            // successful login

        return ( request, response, authentication ) -> {
            // Custom logic to determine the target URL based on user roles
            if ( authentication.getAuthorities().stream()
                    .anyMatch( grantedAuthority -> grantedAuthority.getAuthority().equals( "CUSTOMER" ) ) ) {
                response.sendRedirect( "/customer/home" );
            }
            else if ( authentication.getAuthorities().stream()
                    .anyMatch( grantedAuthority -> grantedAuthority.getAuthority().equals( "MANAGER" ) ) ) {
                response.sendRedirect( "/manager/home" );
            }
            else if ( authentication.getAuthorities().stream()
                    .anyMatch( grantedAuthority -> grantedAuthority.getAuthority().equals( "STAFF" ) ) ) {
                response.sendRedirect( "/staff/home" );
            }
            else {
                handler.onAuthenticationSuccess( request, response, authentication );
            }
        };

    }
}
