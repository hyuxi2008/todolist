package org.example.todolist.config;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ApplicationConfig {

	String getProperty(String name);
	
}
