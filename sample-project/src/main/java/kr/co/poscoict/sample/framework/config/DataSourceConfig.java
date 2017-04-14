package kr.co.poscoict.sample.framework.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.poscoict.sample.framework.profile.Dev;
import kr.co.poscoict.sample.framework.profile.Local;
import kr.co.poscoict.sample.framework.profile.Prod;

/**
 * DataSource Configuration
 * @author libedi
 *
 */
@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {
	
	@Local
	@Dev
	static class DevDataSource {
		@Bean
		@ConfigurationProperties(prefix = "spring.datasource")
		public DataSource dataSource() {
			return DataSourceBuilder.create().build();
		}
	}
	
	@Prod
	static class ProdDataSource {
		@Bean
		@ConfigurationProperties(prefix = "spring.datasource.prod")
		public DataSource dataSource() {
			return DataSourceBuilder.create().build();
		}
	}
}