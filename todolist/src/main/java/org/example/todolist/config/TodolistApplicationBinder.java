package org.example.todolist.config;

import org.example.todolist.auth.AuthService;
import org.example.todolist.auth.SimpleAuthService;
import org.example.todolist.dao.MongoDBTodoTaskDAO;
import org.example.todolist.dao.MongoDBUserDAO;
import org.example.todolist.dao.TodoTaskDAO;
import org.example.todolist.dao.UserDAO;
import org.example.todolist.service.NotificationService;
import org.example.todolist.service.SearchBoxSearchService;
import org.example.todolist.service.SearchService;
import org.example.todolist.service.TodolistService;
import org.example.todolist.service.TodolistServiceImpl;
import org.example.todolist.service.TwilioSMSNotificationService;
import org.example.todolist.service.scheduling.FakeSearchBoxIndexingJobScheduler;
import org.example.todolist.service.scheduling.FakeTwilioSMSNotificationScheduler;
import org.example.todolist.service.scheduling.NotificationScheduler;
import org.example.todolist.service.scheduling.SearchBoxIndexingJobScheduler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class TodolistApplicationBinder extends AbstractBinder {

	@Override
	protected void configure() {
		// Gets runtime config
		bind(TodolistApplicationConfig.class).to(ApplicationConfig.class);
		
		// Provides toy authorization
		bind(SimpleAuthService.class).to(AuthService.class);
		
		// Business logic
		bind(TodolistServiceImpl.class).to(TodolistService.class);
		bind(SearchBoxSearchService.class).to(SearchService.class);
		bind(TwilioSMSNotificationService.class).to(NotificationService.class);
		
		// If anything goes wrong, these guys will manage it
		bind(FakeTwilioSMSNotificationScheduler.class).to(NotificationScheduler.class);
		bind(FakeSearchBoxIndexingJobScheduler.class).to(SearchBoxIndexingJobScheduler.class);
		
		// Entities are stored in Mongo collections
		bind(MongoDBTodoTaskDAO.class).to(TodoTaskDAO.class);
		bind(MongoDBUserDAO.class).to(UserDAO.class);
	}

}
