package com.martini.sparrow.security.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author martini at 2020/12/14 10:51
 */
public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = 8297118345003160956L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
