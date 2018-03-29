/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 *
 * 描述: 数据库初始化
 * @author wangjing.dc@qq.com
 */
//@Component
public class DataInitializer implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
	
	@Autowired
	private  DataSource dataSource;
	
	private ApplicationContext applicationContext;
	
	@Autowired
	public DataInitializer(ApplicationContext ctx) {
		this.applicationContext = ctx;
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		System.err.println("执行数据初始化操作......");
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作 22222222 <<<<<<<<<<<<<");
		try {
			this.runSql();
		}catch(Exception e) {
			log.error(null, e);
		}
		
		System.out.println("执行数据初始化操作，完成......");
	}
	
	/**
	 * 执行初始化数据库脚本
	 */
	private void runSql() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.setContinueOnError(false);
		
		List<Resource> resources = this.getScripts();
		if(resources.size() < 1) {
			log.info("------------没有可执行的数据库脚本------------");
			return;
		}
		
		for (Resource resource : resources) {
			populator.addScript(resource);
		}
		DataSource dataSource = this.dataSource;
		DatabasePopulatorUtils.execute(populator, dataSource);
	}
	
	private List<Resource> getScripts() {

		List<String> fallbackResources = new ArrayList<String>();
//		fallbackResources.add("classpath*:" + fallback + "-" + platform + ".sql");
		fallbackResources.add("classpath*:db_schema.sql");
		
		return this.getResources(fallbackResources);
	}
	
	private List<Resource> getResources(List<String> locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (String location : locations) {
			for (Resource resource : doGetResources(location)) {
				if (resource.exists()) {
					resources.add(resource);
				}
			}
		}
		return resources;
	}

	private Resource[] doGetResources(String location) {
		try {
			SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(
					this.applicationContext, Collections.singletonList(location));
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		catch (Exception ex) {
			throw new IllegalStateException("Unable to load resources from " + location,
					ex);
		}
	}

}
