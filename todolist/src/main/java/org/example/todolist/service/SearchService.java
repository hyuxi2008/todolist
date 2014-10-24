package org.example.todolist.service;


import java.util.List;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface SearchService {

	List<TodoTask> search(String query, int from, int limit) throws Exception;
	
	void index(TodoTask task);
	
	void update(TodoTask task);
	
	void delete(String taskId);
}
