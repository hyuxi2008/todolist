package org.example.todolist.service;

import javax.inject.Inject;

import org.example.todolist.auth.AuthService;
import org.example.todolist.dao.TodoTaskDAO;
import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Service;

@Service
public class TodolistServiceImpl implements TodolistService {

	@Inject
	private AuthService authService;

	@Inject
	private TodoTaskDAO dao;

	@Inject
	private SearchService searchService;

	@Inject
	private NotificationService notificationService;

	@Override
	public String createTask(TodoTask task) {
		task.setUser(authService.currentUser().getUsername());
		String taskId = dao.create(task);
		task.setId(taskId);
		searchService.index(task);
		return taskId;
	}

	@Override
	public TodoTask getTask(String taskId) {
		TodoTask result = dao.findOne(taskId);
		if ((result != null)
				&& (!authService.currentUser().getUsername()
						.equals(result.getUser()))) {
			// Task exists but does not belong to requesting user
			// I could raise a security exception, but I return a 404
			// since I don't want to tell a malicious user the object exists
			result = null;
		}
		return result;
	}

	@Override
	public TodoTask deleteTask(String taskId) {
		TodoTask task = getTask(taskId);
		if (task != null) {
			// It exists and belongs to requesting user
			task = dao.delete(taskId);
			searchService.delete(taskId);
		}
		return task;
	}

	@Override
	public TodoTask updateTask(String taskId, TodoTask task) {
		TodoTask oldTask = null;
		if (taskId.equals(task.getId())) {
			// You can't request A and modify B
			oldTask = getTask(taskId);
		}
		if (oldTask != null) {
			// It exists and belongs to requesting user
			boolean wasDone = oldTask.isDone();
			boolean done = task.isDone();

			// Owner cannot change
			task.setUser(oldTask.getUser());
			dao.update(task);

			if ((!wasDone) && done) {
				notificationService.sendNotification(task);
			}

			searchService.update(task);

		}
		return task;
	}

	@Override
	public TodoTask mark(String taskId, boolean b) {
		TodoTask task = getTask(taskId);
		if (task != null) {
			// It exists and belongs to requesting user
			task.setDone(b);
			task = updateTask(taskId, task);
		}
		return task;
	}

}
