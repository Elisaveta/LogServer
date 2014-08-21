package com.pravila.samples.logserver.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.base.Preconditions;
import com.pravila.samples.logserver.persistence.LogMessageService;
import com.pravila.samples.logserver.persistence.LogMessageServiceImpl;
/**
 * 
 * @author Elisaveta Manasieva
 * @version 1.0.0
 * application context configuration
 *
 */
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-mysql.properties" })
@EnableJpaRepositories({ "com.pravila.samples.logserver.persistence" })
public class PersistenceJPAConfig {

	@Autowired
	private Environment env;

	public PersistenceJPAConfig() {
		super();
	}

	@Bean
	public LogMessageService logMessageService() {
		return new LogMessageServiceImpl();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "com.pravila.samples.logserver.persistence" });
		em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	public EntityManager entityManger() throws IllegalStateException,
			PropertyVetoException {
		return entityManagerFactory().getObject().createEntityManager();
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Preconditions.checkNotNull(env
				.getProperty("jdbc.driverClassName")));
		dataSource.setUrl(Preconditions.checkNotNull(env
				.getProperty("jdbc.url")));
		dataSource.setUsername(Preconditions.checkNotNull(env
				.getProperty("jdbc.user")));
		dataSource.setPassword(Preconditions.checkNotNull(env
				.getProperty("jdbc.pass")));

		return dataSource;
	}

	@Bean
	public JpaTransactionManager transactionManager()
			throws IllegalStateException, PropertyVetoException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory()
				.getObject());
		return transactionManager;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() throws IllegalStateException,
			PropertyVetoException {
		JdbcTemplate template = new JdbcTemplate(dataSource());

		return template;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
				env.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect",
				env.getProperty("hibernate.dialect"));
		hibernateProperties.put("hibernate.show_sql",
				env.getRequiredProperty("hibernate.show_sql"));
		return hibernateProperties;
	}

}