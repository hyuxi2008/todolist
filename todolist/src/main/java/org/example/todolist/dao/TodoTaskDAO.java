package org.example.todolist.dao;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface TodoTaskDAO extends EntityDAO<TodoTask, String> {
	
}
