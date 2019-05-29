package com.acme.ex3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories
public class ApplicationConfig {

	@Bean(name="messageSource")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("applicationMessages");
		return messageSource;
	}

	@EnableWebSecurity @EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex3", "postgres", null);
			String usersByUsernameQuery = "select username, password, true from Member where username=?";
			String authoritiesByUsernameQuery = "select username, authority from authorities where username=?";
			PasswordEncoder encoder = new BCryptPasswordEncoder();

			auth.jdbcAuthentication()
					.dataSource(ds)
					.usersByUsernameQuery(usersByUsernameQuery)
					.authoritiesByUsernameQuery(authoritiesByUsernameQuery)
					.passwordEncoder(encoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.formLogin()
					.and()
					.authorizeRequests()
						.anyRequest().permitAll();
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}
}