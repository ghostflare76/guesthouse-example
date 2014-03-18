package com.cowboy76.batch.config;

import javax.annotation.PostConstruct;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement

@PropertySource("classpath:environment/database_loc.properties")
public class DataSourceConfiguration  {
	
	@Value("classpath:org/springframework/batch/core/schema-drop-mysql.sql")
	private Resource dropScript;

	@Value("classpath:org/springframework/batch/core/schema-mysql.sql")
	private Resource createScript;
	
	@Value("classpath:schema-report.sql")
	private Resource createTempScript;

	@Autowired
	private Environment environment;


	@PostConstruct
	protected void initialize() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(dropScript);
		populator.addScript(createScript);
		populator.addScript(createTempScript);
		populator.setContinueOnError(true);
		DatabasePopulatorUtils.execute(populator, dataSource());
	}
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DataSource ds = new DataSource();	
		ds.setDriverClassName(environment.getRequiredProperty("default.ds.jdbc.driverClassName"));
		ds.setUrl(environment.getRequiredProperty("default.ds.jdbc.url"));		
		ds.setUsername(environment.getRequiredProperty("default.ds.jdbc.username"));
		ds.setPassword(environment.getRequiredProperty("default.ds.jdbc.password"));
		ds.setMaxActive(environment.getRequiredProperty("default.ds.jdbc.maxActive", int.class));
		ds.setMinIdle(environment.getRequiredProperty("default.ds.jdbc.minIdle", int.class));
		ds.setMaxIdle(environment.getRequiredProperty("default.ds.jdbc.maxIdle", int.class));
		ds.setMaxWait(environment.getRequiredProperty("default.ds.jdbc.maxWait", int.class));
		ds.setInitialSize(environment.getRequiredProperty("default.ds.jdbc.initialSize", int.class));
		ds.setValidationQuery(environment.getRequiredProperty("default.ds.jdbc.validationQuery"));
		ds.setValidationInterval(environment.getRequiredProperty("default.ds.jdbc.validationInterval", long.class));
		ds.setTestOnBorrow(environment.getRequiredProperty("default.ds.jdbc.testOnBorrow", boolean.class));
		ds.setTestWhileIdle(environment.getRequiredProperty("default.ds.jdbc.testWhileIdle", boolean.class));
		ds.setTimeBetweenEvictionRunsMillis(environment.getRequiredProperty("default.ds.jdbc.timeBetweenEvictionRunsMillis", int.class));
		ds.setRemoveAbandoned(environment.getRequiredProperty("default.ds.jdbc.removeAbandoned", boolean.class));
		ds.setRemoveAbandonedTimeout(environment.getRequiredProperty("default.ds.jdbc.removeAbandonedTimeout", int.class));
		ds.setLogAbandoned(environment.getRequiredProperty("default.ds.jdbc.logAbandoned", boolean.class));
		ds.setAbandonWhenPercentageFull(environment.getRequiredProperty("default.ds.jdbc.abandonWhenPercentageFull", int.class));
		ds.setJdbcInterceptors(environment.getRequiredProperty("default.ds.jdbc.jdbcInterceptors"));
		ds.setConnectionProperties(environment.getRequiredProperty("default.ds.jdbc.connectionProperties"));
	
		
		return ds;
	}
	
	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
		//return new WebSphereUowTransactionManager();
	}

	/*@Override
	public RoutingDataSource dataSource() {
		RoutingDataSource routingDateSource = new RoutingDataSource();
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put(DataSourceType.DEFAULT, defaultDataSource());
		routingDateSource.setDefaultTargetDataSource(defaultDataSource());
		routingDateSource.setTargetDataSources(targetDataSources);
		return routingDateSource;
	}*/



}
