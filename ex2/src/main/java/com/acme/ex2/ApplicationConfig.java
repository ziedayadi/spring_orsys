package com.acme.ex2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;


//@Configuration
//@PropertySource("classpath:application.properties")
@EnableJpaRepositories//(entityManagerFactoryRef = "emf", transactionManagerRef = "txManager")
@SpringBootApplication
//@EnableAutoConfiguration
public class ApplicationConfig {

//	@Autowired
//	private Environment env;


    @Autowired
    EntityManagerFactory entityManagerFactory;


//	@Bean(destroyMethod = "close")
//	public DataSource ds() {
//		HikariDataSource ds = new HikariDataSource();
//		ds.setJdbcUrl(env.getProperty("datasource.url"));
//		ds.setDriverClassName(env.getProperty("datasource.driver-class-name"));
//		ds.setUsername(env.getProperty("datasource.username"));
//
//		return ds;
//	}

    @PostConstruct
    public void initChache() {

        System.out.println("Init the cache at the begining of the app ");
        EntityManager em = entityManagerFactory.createEntityManager();
        entityManagerFactory.getMetamodel().getEntities().stream().filter(e -> e.getJavaType().isAnnotationPresent(Cacheable.class))
                .map(e -> "select x from " + e.getName() + " x")
                .forEach(q -> em.createQuery(q).getResultList());

        em.close();


    }

//	@Bean
//	public FactoryBean<EntityManagerFactory> emf(){
//		var factoryBean = new LocalContainerEntityManagerFactoryBean();
//		// Nous reprenons ici la configuration autrefois écrite dans le fichier persistence.xml
//		factoryBean.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
//		factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
//		factoryBean.setPackagesToScan("com.acme.ex2.model");
//		factoryBean.setDataSource(ds());
//
//		Properties jpaProperties = new Properties();
    // TODO : décommenter les deux lignes ci dessous (après avoir fait en sorte que la méthode emf puisse accéder aux propriétés du fichier applicationProperties)
//		jpaProperties.put("hibernate.dialect", env.getProperty("jpa.properties.hibernate.dialect"));
//		jpaProperties.put("hibernate.cache.region.factory_class", env.getProperty("jpa.properties.hibernate.cache.region.factory_class"));
//
//		factoryBean.setJpaProperties(jpaProperties);
//
//		return factoryBean;
//	}

//	@Bean
//	public PlatformTransactionManager txManager(EntityManagerFactory emf){
//		return new JpaTransactionManager(emf);
//	}

    public static void main(String[] args) /*just to check if application context is properly configured*/ {
        SpringApplication.run(ApplicationConfig.class, args);
    }


    @EnableWebSecurity
    static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex2", "postgres", null);
            String usersByUsernameQuery = "select username, password, true from Member where username=?";
            String authoritiesByUsernameQuery = "select username, authority from authorities where username=?";
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            auth.jdbcAuthentication().dataSource(ds).
                    usersByUsernameQuery(usersByUsernameQuery).
                    authoritiesByUsernameQuery(authoritiesByUsernameQuery).
                    passwordEncoder(encoder);

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().authenticationEntryPoint((req, resp, ex) -> {
                resp.setStatus(401);
            })
                    .and().exceptionHandling().accessDeniedHandler((req, resp, ex) -> resp.setStatus(403))
                    .and().logout().logoutSuccessHandler((req, resp, auth) -> resp.setStatus(204))
                    .and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/reservations").hasAuthority("borrow-books")
                    .antMatchers("/", "/**/*.html", "/**.js", "/favicon.ico").permitAll()
                    .anyRequest().authenticated();

        }


        @RestController
        public static class AuthenticationController{

            @PostMapping("authentication")
            public Map<String, Object> onSucessfulAuthentication(Authentication auth) {
                return Map.of(
                        "username", auth.getName(),
                        "authorities", auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList())
                );
            }
        }

    }


}
