package org.example.todolist.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.jvnet.hk2.annotations.Service;


@Service
public class TodolistApplicationConfig implements ApplicationConfig {

	private Properties props;
	
	@Context
	private ServletContext context;
	
	@PostConstruct
	public void loadConfig(){
		props = new Properties();
		try {
			props.load(context.getResourceAsStream("/WEB-INF/todolist.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getProperty(String name){
		return props.getProperty(name);
	}
	
}
