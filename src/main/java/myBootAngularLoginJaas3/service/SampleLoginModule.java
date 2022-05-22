package myBootAngularLoginJaas3.service;


import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleLoginModule implements LoginModule {

	private static final Logger logger = LoggerFactory.getLogger(SampleLoginModule.class);

    private Subject subject;
    private String password;
    private String username;
    private static Map<String, String> USER_PASSWORDS = new HashMap<String, String>();

    static {
        USER_PASSWORDS.put("test", "test");
    }

    public boolean abort() throws LoginException {
        return true;
    }

    public boolean commit() throws LoginException {
        return true;
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        logger.info("INITIALIZE STARTED");
        try {
        	logger.info("[INITIALIZE]:1");
            NameCallback nameCallback = new NameCallback("prompt");
            logger.info("[INITIALIZE]:2");
            PasswordCallback passwordCallback = new PasswordCallback("prompt",false);
            logger.info("[INITIALIZE]:3");
            callbackHandler.handle(new Callback[] { nameCallback,passwordCallback });
            logger.info("[INITIALIZE]:4");
            this.password = new String(passwordCallback.getPassword());
            logger.info("[INITIALIZE]: password ==> "+password);
            this.username = nameCallback.getName();
            logger.info("[INITIALIZE]: username ==> "+username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("INITIALIZE FINISHED");
    }

    public boolean login() throws LoginException {
    	logger.info("LOGIN STARTED");
       /* if (USER_PASSWORDS.get(username) == null
                || !USER_PASSWORDS.get(username).equals(password)) {
            throw new LoginException("username is not equal to password");
        }*/

        subject.getPrincipals().add(new CustomPrincipal(username));
        logger.info("LOGIN FINISHED");
        return true;
    }

    public boolean logout() throws LoginException {
    	logger.info("LOGOUT STARTED");
        return true;
    }

    private static class CustomPrincipal implements Principal, Serializable {
        private final String username;
        
        public CustomPrincipal(String username) {
        	logger.info("[CustomPrincipal] constructor username ==> "+username);
            this.username = username;
        }

        public String getName() {
        	logger.info("[CustomPrincipal] getusername ==> "+username);
            return username;
        }
    }

}