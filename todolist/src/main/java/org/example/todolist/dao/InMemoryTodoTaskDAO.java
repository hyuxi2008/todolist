package org.example.todolist.dao;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Service;

@Service
public class InMemoryTodoTaskDAO implements TodoTaskDAO {

	private static Map<String, TodoTask> tasks = new ConcurrentSkipListMap<String, TodoTask>();

	@Override
	public String create(TodoTask task) {
		String id = UUID.randomUUID().toString();
		task.setId(id);
		tasks.put(id, task);
		return id;
	}

	@Override
	public TodoTask findOne(String id) {
		return tasks.get(id);
	}

	@Override
	public TodoTask delete(String id) {
		return tasks.remove(id);
	}

	@Override
	public void update(TodoTask task) {
		tasks.put(task.getId(), task);
	}

}
