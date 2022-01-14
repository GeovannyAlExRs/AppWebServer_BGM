package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Roles;
import com.ec.busgeomap.web.app.model.Users;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	ServiceUsers serviceUsers; 
	
	@Autowired
	ServiceRole serviceRole;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(" BUSCAR USUARIO " + username);
		
		Users users = new Users();
		Roles roles = new Roles();
		
		try {
			users = serviceUsers.readUsersDoc(username);
			System.err.println("USUARIO RECUPERADO PARA LOGIN : " + users);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			new UsernameNotFoundException("NO EXISTE EL USUARIO");
			
			e.printStackTrace();
		}

		ArrayList<Roles> roleList = new ArrayList<>();
		List list = new ArrayList<>();
		
		try {
			roleList = serviceRole.readAllRol();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Roles r : roleList) {
			System.err.println("ROL RECUPERADO PARA LOGIN : " + r);
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(r.getRol_description());
			System.err.println("ROL RECUPERADO PARA LOGIN GRANTED AUTHORITY: " + grantedAuthority.getAuthority() + " " + grantedAuthority.toString());
			list.add(grantedAuthority);
			System.err.println(" AGREGO A LA LISTA" + list.indexOf(0));
		}
	   
		System.err.println(" LISTA DE ROLES AGREGADOS" + list);
		
	    UserDetails userDetail = new User(users.getUse_name(), users.getUse_pass_crypt(), list);
		System.out.println("USER DETAIL => USERNAME: " + userDetail.getUsername() + ", PASSWORD: " + userDetail.getPassword() + " OBJ USER DETAIL: " + userDetail.getAuthorities());
		return userDetail;
	}

}
