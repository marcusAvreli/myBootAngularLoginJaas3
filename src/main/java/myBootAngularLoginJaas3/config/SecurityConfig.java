
package myBootAngularLoginJaas3.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import myBootAngularLoginJaas3.service.EntryPointUnauthorizedHandler;
import myBootAngularLoginJaas3.service.RestAccessDeniedHandler;
import myBootAngularLoginJaas3.security.jwt.XWAuthenticationTokenFilter;

import myBootAngularLoginJaas3.service.UserDetailsServiceImpl;






//@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	 /*private static final JaasAuthenticationCallbackHandler[] CALLBACK_HANDLERS 
     = new JaasAuthenticationCallbackHandler[] { new JaasAuthenticationNameCallbackHandler(), 
                                                 new JaasAuthenticationPasswordCallbackHandler(),
                                                 JaasHttpCallbackHandlerFilter.CALLBACK_HANDLER
                                               };*/
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
    @Resource
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    @Resource
    private RestAccessDeniedHandler restAccessDeniedHandler;


	@Autowired
	UserDetailsServiceImpl userDetailsService;
	   @Bean
			public XWAuthenticationTokenFilter xwAuthenticationTokenFilter() {
				return new XWAuthenticationTokenFilter();
			}

	/**
	 * Defining these beens in an embedded configuration to ensure they are all
	 * constructed before being used by the logout filter.
	 */
@Primary
	@Bean
	public AuthenticationProvider customAuthenticationProvider() {

		//CustomAutenticationProvider provider = new CustomAutenticationProvider(new CustomJaasAuthenticationProvider());
		CustomJaasAuthenticationProvider provider = new CustomJaasAuthenticationProvider();
		Configuration jaasConfig = createJaasConfig();
		provider.setConfiguration(jaasConfig);
		//provider.setUserDetailsService(userDetailsService());
		// provider.setAuthorityGranters(authorityGranters);
		// DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		// provider.setPasswordEncoder(passwordEncoder);
		// provider.setUserDetailsService(hibernateUserDetailsService);
		return provider;
	}

	// CGLib-based proxies by setting proxyTargetClass=true on @EnableAsync and/or
	// @EnableCaching.

	/*@Bean

	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {

		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}*/
	

	/*
	 * @Bean public ApplicationContextProvider applicationContextProvider() {
	 * 
	 * return new ApplicationContextProvider(); }
	 */
@Bean
	public UserDetailsServiceImpl userDetailsService() {

		return new UserDetailsServiceImpl();
	}


	/*
	 * @Override protected UserDetailsService userDetailsService() { return new
	 * UserDetailsServiceImpl(); }
	 */
/*	@Bean
	public UserRepository userRepository() {

		return new UserRepositoryImpl();
	}*/

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(customAuthenticationProvider());
	//	auth.userDetailsService(userDetailsService());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
/*
   http.
	                csrf().disable().
	                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                .authorizeRequests()
	                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                .antMatchers( "/api/auth/**").permitAll()
					.antMatchers("/api/test/**").permitAll()
					.antMatchers("/api/rest/**").permitAll()
					.anyRequest().authenticated()
	                .and()
	                .headers().cacheControl();
	        http.addFilterBefore(xwAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	        http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).accessDeniedHandler(restAccessDeniedHandler);
 */
		logger.info("***SECURITY_CONFIGURATION_STARTED***");
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
	       
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers( "/api/auth/**").permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers("/api/rest/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.headers().cacheControl();
		 //http.addFilterBefore(jaasFilter(), BasicAuthenticationFilter.class);
		//http.addFilterBefore(authTokenFilter(), BasicAuthenticationFilter.class);
		http.addFilterBefore(xwAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		logger.info("***SECURITY_CONFIGURATION_FINISHED***");
		

	}

	/*@Bean
	public JaasApiIntegrationFilter jaasFilter() {

		JaasApiIntegrationFilter filter = new JaasApiIntegrationFilter();
		filter.setCreateEmptySubject(true);
		return filter;
	}*/
	


/*	@Bean(name = "mvcHandlerMappingIntrospector")
	public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
		return new HandlerMappingIntrospector();
	}*/
	
	/* public AppConfigurationEntry[] getAppConfigurationEntry() {
	      Map<String, String> options = new HashMap<>();
	      options.put("debug", System.getProperty("sun.security.krb5.debug", "true"));
	      options.put("keyTab", keytab);
	      options.put("principal", principal);
	      options.put("useKeyTab", "true");
	      options.put("storeKey", "true");
	      options.put("doNotPrompt", "true");
	      options.put("refreshKrb5Config", "true");
	      options.put("isInitiator", Boolean.toString(isInitiator));

	      // Do not store/use ticket in/from cache. SecurityContext does not renew it by doing something like "kinit -R"
	      // Instead a new ticket is requested during the renewal.
	      // Credentials are explicitly saved into credential cache ${SDC_DATA}/sdc-krb5.ticketCache for services like
	      // Kafka to pick up.
	      return new AppConfigurationEntry[]{
	          new AppConfigurationEntry("myBootAngularLoginJaas3.service.SampleLoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
	                                    options)};
	    }*/
	private  Configuration createJaasConfig() {

        // Create entry options.
		Map<String, String> options = new HashMap<>();
		options.put("debug", "true");
        

        // Create entries.
        AppConfigurationEntry[] entries = {
                new AppConfigurationEntry(
                        "myBootAngularLoginJaas3.service.SampleLoginModule",
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                        options)
        };

        // Create configuration.
        return new Configuration() {
            
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
              
                return entries;
            }
        };

    }
	  
}
