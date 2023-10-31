package com.martini.sparrow.security.realm;

import com.alibaba.druid.util.StringUtils;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author martini at 2020/12/14 7:43
 */
@Component
@Slf4j
public class UserRealm extends AuthorizingRealm {

    private final static String BLANK_ACCOUNT = "### Account can't be empty";
    private final static String NO_SUCH_ACCOUNT = "### No such account";

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("授权: [{}]", principals.getPrimaryPrincipal().toString());
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String account = token.getPrincipal().toString();
        System.out.println("userMapper: " + userMapper);
        if (StringUtils.isEmpty(account)) throw new AccountException(BLANK_ACCOUNT);
        User user = account.contains("@") ? userMapper.findByEmail(account) : userMapper.findByPhone(account);
        if (user == null) throw new UnknownAccountException(NO_SUCH_ACCOUNT);
        return new SimpleAuthenticationInfo(account, user.getPassword(), this.getName());
    }
}
