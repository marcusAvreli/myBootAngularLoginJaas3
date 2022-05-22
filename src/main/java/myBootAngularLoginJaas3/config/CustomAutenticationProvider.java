package myBootAngularLoginJaas3.config;


import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import com.sun.security.auth.UserPrincipal;


import myBootAngularLoginJaas3.service.UserDetailsServiceImpl;
//@Component
public class CustomAutenticationProvider extends DaoAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationProvider delegate;
    @Autowired
	UserDetailsServiceImpl userDetailsService;
    public CustomAutenticationProvider(AuthenticationProvider delegate) {
        this.delegate = delegate;
       setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Authentication a = delegate.authenticate(authentication);

        if(a.isAuthenticated()){
            a = super.authenticate(a);
        }else{
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        return a;
    }

    private List<GrantedAuthority> loadRolesFromDatabaseHere(String name) {
        GrantedAuthority grantedAuthority =new JaasGrantedAuthority(name, new UserPrincipal(name));
        return Arrays.asList(grantedAuthority);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
                    throws AuthenticationException {


        if(!authentication.isAuthenticated())
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));


    }
}