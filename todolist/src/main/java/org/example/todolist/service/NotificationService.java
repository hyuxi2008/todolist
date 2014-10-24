package org.example.todolist.service;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface NotificationService {

	void sendNotification(TodoTask task);
	
}
