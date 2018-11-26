package com.nc.util;

import java.io.InputStream;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.nc.dao.AdminMapper;


@Component("mybatisUtil")
public class MybatisUtil {
	private static Logger logger = Logger.getLogger(MybatisUtil.class);
	public static SqlSession getSession(){
			try {
				String resource = "mybatis-config.xml";
				InputStream inputStream = Resources.getResourceAsStream(resource);
				//2. build SqlSessionFactory	
				SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
				//3. a SqlSession from SqlSessionFactory
				SqlSession session = sqlSessionFactory.openSession(true);
				return session;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public void testerTask(){
		logger.info("-------task Xml配置方式任务调度测试--------");
	}
	
	@Scheduled(/*cron = "0 0 3 * * ?",*/fixedDelay=2000)
	public void testerAnnotationTask(){
		logger.info("-------task注解方式任务调度测试--------");
	}
	
	public void testQuartz() {
		logger.info("-------quartz任务调度测试--------");
	}
}