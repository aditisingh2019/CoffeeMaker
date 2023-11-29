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

/**
 * This is the configuration this helps us make sure only certain permission
 * level roels can access certain cites. Once the User is authencated this class
 * helps us map them to the right pages as well
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * This UserDetailsService class helps us load users and see if they can be
     * authenticated by what is in the backend via username and password
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This is a creation of a passwordenocder that other methods uses
     *
     * @return BCryptPasswordEncoder the type of password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines which URL paths should be secured and which should not
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/", "/index", "/registry.html", "/api/v1/users/**", "/privacy.html", "/logo.png", "/coffee_beans.png" )
                .permitAll().antMatchers( "/manager/**" ).hasAuthority( "MANAGER" ).antMatchers( "/customer/**" )
                .hasAuthority( "CUSTOMER" ).antMatchers( "/staff/**" ).hasAuthority( "STAFF" ).and().formLogin()
                .loginPage( "/login" ).successHandler( successHandler() ).permitAll().and().logout()
                .logoutSuccessUrl( "/" ).permitAll().and().csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
    }

    /**
     * This is a creation of a passwordenocder that other methods uses
     *
     * @return BCryptPasswordEncoder the type of password encoder
     */

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * This configures the auth and checks if the user can be logged in or not
     *
     * @param auth
     *            the AuthenticationManagerBuilder object that we are trying to
     *            see will return the authenticated user or not
     * @throws Exception
     *             Exception is thrown if the auth object runs into a problem
     */
    @Autowired
    public void configureAuth ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userDetailsService ).passwordEncoder( bCryptPasswordEncoder() );
    }

    /**
     * This is the success handler ojbect after the user is authenticated it
     * remaps the user to the right site.
     *
     * @return the AuthenticationSuccessHandler which has a redirection link to
     *         the right html page
     */
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
