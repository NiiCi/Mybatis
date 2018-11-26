package com.nc.realm;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nc.entity.Admin;
import com.nc.entity.Pers;
import com.nc.entity.Roles;
import com.nc.service.AdminService;
import com.nc.service.NavService;





@Component
public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	NavService navServie;
	@Autowired
	AdminService adminService;
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		 Set<String> roles = new HashSet<>();
		 Set<String> stringPermissions = new HashSet<>();
		 //通过用户名查询角色,存入set集合
		 List<Roles> roleList = navServie.queryRolesByName(principal.getPrimaryPrincipal().toString());
		 for (Roles role : roleList) {
			roles.add(role.getRoleCode());
		 }
		 List<Pers> perList = navServie.queryAllPers(principal.getPrimaryPrincipal().toString());
		 for (Pers pers : perList) {
			 stringPermissions.add(pers.getPerCode());
		}
		
		 //通过用户名查询角色权限,存入set集合
	/*	 List<Pers> perList = adminService.queryPersByRoleid(principal.getPrimaryPrincipal().toString());
		for (Pers pers : perList) {
			stringPermissions.add(pers.getPerCode());
		}*/
	/*	 if (principal.getPrimaryPrincipal().toString().equals("贝贝")) {
			roles.add("admin");
			stringPermissions.add("*");
		}*/
		 SimpleAuthorizationInfo sz = new SimpleAuthorizationInfo();
		 sz.setRoles(roles);
		 sz.setStringPermissions(stringPermissions);
		return sz;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authen) throws AuthenticationException {
		//将authen的类型转换为其子类下的UsernamePasswordToken
		UsernamePasswordToken up = (UsernamePasswordToken) authen;
		String name = (String) up.getUsername();
		String truepwd = null;
		/*获取密码时,AuthenticationToken 获取的密码会乱码,
		用其子类下的UsernamePasswordToken子类获取密码*/
		String password = new String(up.getPassword());
		//通过用户名查询正确的密码
		Admin infoList = adminService.queryAdminByName(name);
		//将查询的密码赋值给truepwd
		
			truepwd = infoList.getAdmin_password();
			System.out.println("truepwd-----: " + truepwd.toString());
		
		System.out.println("用户名-----: "+name+" , "+"密码-----: "+password);
	/*	if (name.equals("贝贝")) {
			System.out.println("管理员登录");
		}else if (name.equals("王王")) {
			System.out.println("普通用户登录");
		}else{
			throw new UnknownAccountException("用户名不存在!!!");
		}*/
		
		//如果身份认证验证成功，返回一个AuthenticationInfo实现,失败则抛出异常
		/*SimpleAuthenticationInfo sa=new SimpleAuthenticationInfo(name, "asdasd", getName());*/
		SimpleAuthenticationInfo sa = new SimpleAuthenticationInfo(name, truepwd.toString(), ByteSource.Util.bytes(name), getName());
		return sa;
	}
}

