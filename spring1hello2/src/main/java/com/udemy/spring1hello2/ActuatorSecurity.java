package com.udemy.spring1hello2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ActuatorSecurity {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.mvcMatchers("/", "/welcome", "/hello").permitAll() // APIは認証不要
				.mvcMatchers("/actuator/**").hasRole("ADMIN") // ActuatorはADMINロールユーザのみ
				.anyRequest().denyAll() // 上記以外はアクセスさせない
				)
			.formLogin();
		
		return http.build();
	}
	
	// APIアクセス・actuatorのhealthは認証不要とする
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/actuator/health");
	}
	
	// actuatorの認証ユーザ
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("admin")
				.password("{noop}admin")
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

}
