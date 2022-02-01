package com.ec.busgeomap.web.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ec.busgeomap.web.app.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

    String[] resources = new String[]{"/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**","/lib/**"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("HTTP " + http);
		http
	        .authorizeRequests()
	        .antMatchers(resources).permitAll()  
	        .antMatchers("/app_busgeomap/home").permitAll()
	        .antMatchers("/app_busgeomap/role").access("hasRole('ADMIN')")
	        .antMatchers("/app_busgeomap/codeqr").hasAnyRole("ADMIN","USER")
	        .antMatchers("/app_busgeomap/assigne_bus*").access("hasAnyRole('ADMIN','USER')")
	        .antMatchers("/app_busgeomap/users").access("hasRole('ADMIN')")
	            .anyRequest().authenticated()
	            .and()
	        .formLogin()
	            .loginPage("/app_busgeomap/login")
	            .permitAll()
	            .defaultSuccessUrl("/app_busgeomap/report")
	            .failureUrl("/app_busgeomap/login?error=true")
	            .usernameParameter("username")
	            .passwordParameter("password")
	            .and()
	        .logout()
	            .permitAll()
	            .logoutSuccessUrl("/app_busgeomap/login?logout");
	}
	
	@Autowired
	UserDetailServiceImpl userDetailService;
    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}
    
}
