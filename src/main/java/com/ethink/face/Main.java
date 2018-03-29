/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sqlite.SQLiteDataSource;


/**
 *
 * 描述: 系统启动， 基础信息配置
 * @author wangjing.dc@qq.com
 */
@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
@MapperScan({"com.ethink.face.db"})
public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	/**
	 * 添加跨域支持
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry)
			 */
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins("*");
			}
		};
	}
	
	/**
	 * 配置数据源
	 * @return
	 */
	@Bean
	@ConfigurationProperties("app.datasource")
	public DataSource dataSource() {
		SQLiteDataSource ds = new SQLiteDataSource();
		return ds;
	}
	
	public static void main(String[] args) {
		//creatBasePath();
		SpringApplication app = new SpringApplication(Main.class);
		ConfigurableApplicationContext context = app.run(args);
		System.out.println("---------------Application startup success-----------------");
	}

	
}
