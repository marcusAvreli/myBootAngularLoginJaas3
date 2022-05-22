package myBootAngularLoginJaas3.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * User专用的UsernamePasswordAuthenticationToken
 * AuthenticationManager会遍历使用Provider的supports()方法,判断AuthenticationToken是不是自己想要的
 * @author xuwang
 * Created on 2019/06/01 15:58
 */
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAuthenticationToken(Object principal, Object credentials){
        super(principal,credentials);
    }
}