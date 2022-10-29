package com.jForce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jForce.service.UserSecurityService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserSecurityService uss;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		auth.inMemoryAuthentication().withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
		auth.userDetailsService(uss).passwordEncoder(encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http = http.authorizeRequests()
				.antMatchers("/voting").permitAll()
				.antMatchers("/voting/home").permitAll()
				.antMatchers("/voting/register").permitAll()
				.antMatchers("/voting/login").permitAll()
				.antMatchers("/voting/candidate/list").permitAll()
				.antMatchers("/voting/candidate/add").hasAnyRole("ADMIN")
				.antMatchers("/voting/candidate/update").hasAnyRole("ADMIN")
				.antMatchers("/voting/candidate/delete").hasAnyRole("ADMIN")
				.antMatchers("/voting/addvote").hasAnyRole("USER")
				.anyRequest().authenticated().and().httpBasic().and()
				.formLogin()
				.loginPage("/voting/login").defaultSuccessUrl("/voting/candidate/list").usernameParameter("email").passwordParameter("password")
				.loginProcessingUrl("/voting/login").permitAll()
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/voting/logout")).invalidateHttpSession(true)
				.logoutSuccessUrl("/voting/login").permitAll()
				.and().csrf().disable();

	}
}
