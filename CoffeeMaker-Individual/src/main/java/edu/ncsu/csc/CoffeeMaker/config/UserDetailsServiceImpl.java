package edu.ncsu.csc.CoffeeMaker.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername ( final String username ) throws UsernameNotFoundException {
        System.out.println( "User's username" + username );
        final User user = userRepository.findByUsername( username );

        if ( user == null ) {
            throw new UsernameNotFoundException( "User not found with username: " + username );
        }
        final Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add( new SimpleGrantedAuthority( user.getRole() ) );
        System.out.println( "User's authorities" + authorities.toString() );

        return new org.springframework.security.core.userdetails.User( user.getUsername(), user.getPassword(),
                authorities );
    }

}
