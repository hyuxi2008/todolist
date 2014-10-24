package org.example.todolist.service;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface TodolistService {

	String createTask(TodoTask task);

	TodoTask getTask(String taskId);

	TodoTask updateTask(String taskId, TodoTask task);

	TodoTask deleteTask(String taskId);

	TodoTask mark(String taskId, boolean b);

}
