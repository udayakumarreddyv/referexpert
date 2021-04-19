package com.referexpert.referexpert.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.referexpert.referexpert.constant.Constants;

@Configuration
public class MysqlDBConfig {
    
    private final static Logger logger = LoggerFactory.getLogger(MysqlDBConfig.class);

	@Autowired
	Environment env;

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "mysql")
	public DataSource mysqlDataSource() {
	    logger.info("MysqlDBConfig :: Creating mysqlDataSource");
		DataSource dataSource = (DataSource) DataSourceBuilder.create()
				.driverClassName(env.getProperty(Constants.MYSQL_DATASOURCE_DRIVER))
				.url(env.getProperty(Constants.MYSQL_DATASOURCE_URL))
				.username(env.getProperty(Constants.MYSQL_DATASOURCE_USER))
				.password(env.getProperty(Constants.MYSQL_DATASOURCE_PASSWORD)).build();

		dataSource.setMaxActive(Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_MAX_ACTIVE)));
		dataSource.setInitialSize(Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_INITIAL_SIZE)));
		dataSource.setMaxIdle(Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_MAX_IDLE)));
		dataSource.setMinIdle(Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_MIN_IDLE)));
		dataSource.setValidationQuery(env.getProperty(Constants.MYSQL_DATASOURCE_VALIDATION_QUERY));
		dataSource
				.setTestOnConnect(Boolean.parseBoolean(env.getProperty(Constants.MYSQL_DATASOURCE_TEST_ON_CONNECT)));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty(Constants.MYSQL_DATASOURCE_TEST_ON_BORROW)));
		dataSource.setTestOnReturn(Boolean.parseBoolean(env.getProperty(Constants.MYSQL_DATASOURCE_TEST_ON_RETURN)));
		dataSource
				.setTestWhileIdle(Boolean.parseBoolean(env.getProperty(Constants.MYSQL_DATASOURCE_TEST_WHILE_IDLE)));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_TIME_BETWEEN_EVICTION_RUNS_MILLS)));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_MIN_EVICTABLE_IDLE_TIME_MILLS)));
		dataSource.setMaxWait(Integer.parseInt(env.getProperty(Constants.MYSQL_DATASOURCE_MAX_WAIT)));
		return dataSource;
	}

	@Bean
	public JdbcTemplate mysqlJdbcTemplate(DataSource mysqlDataSource) {
	    logger.info("MysqlDBConfig :: Creating mysqlJdbcTemplate");
		return new JdbcTemplate(mysqlDataSource);
	}

}
