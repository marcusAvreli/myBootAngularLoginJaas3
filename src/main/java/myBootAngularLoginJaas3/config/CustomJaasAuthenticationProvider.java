
package myBootAngularLoginJaas3.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.URIParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;


import myBootAngularLoginJaas3.service.RoleGranterFromMap;
import myBootAngularLoginJaas3.service.UsernameAuthenticationToken;


@Component
public class CustomJaasAuthenticationProvider extends DefaultJaasAuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(CustomJaasAuthenticationProvider.class);

	// public enum LogFields { PERM, ENTITY, RESULT, USER, GROUPS, IP_ADDRESS };
	public enum LogFields {
		RESULT, USER, GROUPS
	};


	//@org.springframework.beans.factory.annotation.Value("${security.log.auth:false}")
	//private boolean logAuthentication;


	//@org.springframework.beans.factory.annotation.Value("${security.log.auth.level:DEBUG}")
//	private String logLevelValue;


//	@org.springframework.beans.factory.annotation.Value("${security.log.auth.login.format:Authentication attempt: {RESULT}, user: {USER}}")
//	private String loginFormat;


//	@org.springframework.beans.factory.annotation.Value("${security.log.auth.logout.format:Logout attempt: {RESULT}, user: {USER}}")
//	private String logoutFormat;

	@org.springframework.beans.factory.annotation.Value("${jaasConfig}")
	private String configFilePath;

	private String loginMessage;


	private String loginContextName;

//	private RoleGranterFromMap roleGranter=new RoleGranterFromMap();

	private JaasAuthenticationCallbackHandler[] callbackHandlers;

	public CustomJaasAuthenticationProvider() {

		super.setAuthorityGranters(new AuthorityGranter[] { new RoleGranterFromMap() });
		super.setConfiguration(createJaasConfig());
		
		logger.info("**[CustomJaasAuthenticationProvider_CONSTRUCTOR] FINISHED**");
	}
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

	@PostConstruct
	protected void initLogging() {

	}

	/*protected  Configuration getConfiguration() {
			logger.info("**[GET_CONFIGURATION_STARTED]**");
		  Configuration login_conf = null;
		  logger.info("[GET_CONFIGURATION_STARTED]:1");
		  //find login file configuration from Storm configuration
		  String loginConfigurationFile =System.getProperty("login.config.url.1");
		  //String loginConfigurationFile = "./src/main/resources/jaas.config";
		  logger.info("SystemProperties"+System.getProperties());
		  logger.info("[GET_CONFIGURATION_STARTED]:configFilePath ==> "+configFilePath);
		  if ((loginConfigurationFile != null) && (loginConfigurationFile.length() > 0)) {
			  logger.info("[GET_CONFIGURATION_STARTED]:3");
		    File config_file = new File(loginConfigurationFile);
		    logger.info("[GET_CONFIGURATION_STARTED]:4");
		    logger.info("[GET_CONFIGURATION_STARTED]: file_exists ==> "+config_file.exists());
		    logger.info("[GET_CONFIGURATION_STARTED]: file_path ==> "+config_file.getPath());
		    logger.info("[GET_CONFIGURATION_STARTED]: file_canRead ==> "+config_file.canRead());
		    if (!config_file.exists()) {
		    	throw new RuntimeException("File " + loginConfigurationFile +
	                    " does not exist.");
		    }
		    if (!config_file.canRead()) {
		      throw new RuntimeException("File " + loginConfigurationFile +
		                    " cannot be read.");
		    }
		    try {
		      URI config_uri = config_file.toURI();
		      login_conf = Configuration.getInstance("JavaLoginConfig", new URIParameter(config_uri));
		    } catch (Exception ex) {
		      throw new RuntimeException(ex);
		    }
		  }
		  return login_conf;
		}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.jaas.
	 * AbstractJaasAuthenticationProvider#setCallbackHandlers(org.springframework.
	 * security.authentication.jaas.JaasAuthenticationCallbackHandler[])
	 */
	@Override
	public void setCallbackHandlers(JaasAuthenticationCallbackHandler[] callbackHandlers) {

		logger.info("[setCallbackHandlers]: STARTED");
		logger.info("[setCallbackHandlers]: callbackHandlers ==>"+callbackHandlers);
		super.setCallbackHandlers(callbackHandlers);
		
		logger.info("[setCallbackHandlers]: 1");
		this.callbackHandlers = callbackHandlers;
		logger.info("[setCallbackHandlers]: FINISHED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.jaas.
	 * AbstractJaasAuthenticationProvider#setLoginContextName(java.lang.String)
	 */
	@Override
	public void setLoginContextName(String loginContextName) {

		logger.info("[setLoginContextName] STARTED");
		super.setLoginContextName(loginContextName);
		logger.info("[setLoginContextName] 1");
		this.loginContextName = loginContextName;
		logger.info("[setLoginContextName] loginContextName ==> " + loginContextName);
		logger.info("[setLoginContextName] FINISHED");
	}

	protected String getContextName() {

		return loginContextName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.jaas.
	 * AbstractJaasAuthenticationProvider#handleLogout(org.springframework.security.
	 * core.session.SessionDestroyedEvent)
	 */
	@Override
	protected void handleLogout(SessionDestroyedEvent event) {

		List<SecurityContext> contexts = event.getSecurityContexts();

		if (contexts.isEmpty()) {
			log.debug("The destroyed session has no SecurityContexts");
			return;
		}

		for (SecurityContext context : contexts) {
			Authentication auth = context.getAuthentication();

			if (auth != null) {
				try {
					getLoginContext(auth).ifPresent(loginContext -> {
						JaasAuthenticationToken jaasToken = (JaasAuthenticationToken) auth;

						try {
							loginContext.logout();

						} catch (LoginException e) {

							log.debug("Error logging out of LoginContext", e);
						}
					});
				} catch (LoginException e) {
					log.warn("Error obtaining LoginContext", e);
				}
			}
		}
	}

	protected LoginContext createLoginContext(Subject subject, CallbackHandler handler) throws LoginException {

		logger.info("[createLoginContext] STARTED");
		return new LoginContext(getContextName(), subject, handler, getConfiguration());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.jaas.
	 * AbstractJaasAuthenticationProvider#publishFailureEvent(org.springframework.
	 * security.authentication.UsernamePasswordAuthenticationToken,
	 * org.springframework.security.core.AuthenticationException)
	 */

	/*
	 * private Function<LogFields, Object>
	 * deriveSuccessTokenValues(JaasAuthenticationToken token) {
	 * 
	 * return (field) -> { switch (field) { case RESULT: return "success"; case
	 * USER: return token.getName(); case GROUPS: return
	 * token.getAuthorities().stream().map(JaasGrantedAuthority.class::cast)
	 * .map(JaasGrantedAuthority::getPrincipal).filter(Group.class::isInstance).map(
	 * Principal::getName) .collect(Collectors.joining(", ")); // case IP_ADDRESS:
	 * // return ""; default: return ""; } }; }
	 */

	/*
	 * private Function<LogFields, Object> deriveFailedTokenValues(Exception
	 * exception, UsernamePasswordAuthenticationToken token) {
	 * 
	 * return (field) -> { switch (field) { case RESULT: return "failed - " +
	 * exception.getMessage(); case USER: return token.getName(); case GROUPS:
	 * return token.getAuthorities().stream().filter(JaasGrantedAuthority.class::
	 * isInstance) .map(JaasGrantedAuthority.class::cast).map(JaasGrantedAuthority::
	 * getPrincipal)
	 * .filter(Group.class::isInstance).map(Principal::getName).collect(Collectors.
	 * joining(", ")); // case IP_ADDRESS: // return ""; default: return ""; } }; }
	 */

	private Optional<LoginContext> getLoginContext(Authentication auth) throws LoginException {

		logger.info("[getLoginContext] STARTED");
		LoginContext loginContext;
		logger.info("[getLoginContext] 1");
		if (auth instanceof JaasAuthenticationToken) {
			logger.info("[getLoginContext] 2");
			JaasAuthenticationToken token = (JaasAuthenticationToken) auth;
			logger.info("[getLoginContext] 3");
			loginContext = token.getLoginContext();
			logger.info("[getLoginContext] 4");
			if (loginContext == null) {
				logger.info("[getLoginContext] 5");
				loginContext = createLoginContext(createSubject(auth), new InternalCallbackHandler(auth));
				logger.info("[getLoginContext] 6");
				logger.info("Created LoginContext for auth: {}", auth);
			} else {
				logger.info("[getLoginContext] 7");
				logger.info("Using LoginContext from token: {}", token);
			}
		} else {
			logger.info("[getLoginContext] 8");
			loginContext = createLoginContext(createSubject(auth), new InternalCallbackHandler(auth));
			logger.debug("Created LoginContext for auth: {}", auth);
		}
		logger.info("[getLoginContext] loginContext ==> " + loginContext);
		logger.info("[getLoginContext] FINISHED");
		return Optional.ofNullable(loginContext);
	}

	private Subject createSubject(Authentication auth) {
		logger.info("[createSubject] STARTED");
		// LoggingUtil.log(log, this.logLevel, LogFields.class, this.logoutFormat,
		// null);
		return null;
	}

	/**
	 * Wrapper class for JAASAuthenticationCallbackHandlers
	 */
	private class InternalCallbackHandler implements CallbackHandler {

		private final Authentication authentication;

		public InternalCallbackHandler(Authentication authentication) {

			logger.info("[InternalCallbackHandler]:1");
			this.authentication = authentication;
		}

		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

			/*
			 * for (JaasAuthenticationCallbackHandler handler :
			 * DefaultKyloJaasAuthenticationProvider.this.callbackHandlers) { for (Callback
			 * callback : callbacks) { handler.handle(callback, this.authentication); } }
			 */
		}
	}
	 /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> aClass) {
    	logger.info("[supports] STARTED");
    	logger.info("[supports] Canonical Name==> "+aClass.getCanonicalName());
    	logger.info("[supports] Name==> "+aClass.getName());
    	logger.info("[supports] Simple Name==> "+aClass.getSimpleName());
    	logger.info("[supports] Type Name==> "+aClass.getTypeName());
    	boolean supports = UserAuthenticationToken.class.isAssignableFrom(aClass);
    	logger.info("[supports] ==> "+supports);
    	logger.info("[supports] FINISHED");
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
    	logger.info("[authenticate] STARTED");
        return super.authenticate(((UserAuthenticationToken) auth));
    }

}
