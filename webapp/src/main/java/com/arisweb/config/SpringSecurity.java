package com.arisweb.config;

import com.arisweb.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.arisweb.security.ApplicationUserRole.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	private UserDetailsService userDetailsService;


	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) ->
						authorize.requestMatchers(antMatcher("/register/**")).permitAll()
								.requestMatchers(antMatcher("/error**")).permitAll()
								.requestMatchers(antMatcher("/")).permitAll()
								.requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
								.requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
								.requestMatchers(antMatcher("/v2/api-docs/**")).permitAll()
								.requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
								.requestMatchers(antMatcher("/api/**")).permitAll()
								.requestMatchers(antMatcher("/index")).permitAll()
								.requestMatchers(antMatcher("/media/*")).permitAll()
								.requestMatchers(antMatcher("/resources/**")).permitAll()
								.requestMatchers(antMatcher("/users**")).hasRole(ADMIN.name())
								.requestMatchers(antMatcher("/user/**")).hasRole(ADMIN.name())
								.requestMatchers(antMatcher("/listMachines")).hasRole(ADMIN.name())
								.requestMatchers(antMatcher("/dashboard/**")).hasRole(ADMIN.name())

				).formLogin(
						form -> form
								.loginPage("/login")
								.loginProcessingUrl("/login")
								.defaultSuccessUrl("/dashboard/index")
								.permitAll()
				).logout(
						logout -> logout
								.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
								.permitAll()
				);
		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}
}
