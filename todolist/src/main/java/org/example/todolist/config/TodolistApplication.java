package org.example.todolist.config;

import org.glassfish.jersey.server.ResourceConfig;

public class TodolistApplication extends ResourceConfig {

	public TodolistApplication(){
		register(new TodolistApplicationBinder());
		packages(true, "org.example.todolist");
	}
	
}
