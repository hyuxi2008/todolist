package org.example.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.example.todolist.model.SearchResult;
import org.example.todolist.model.TodoTask;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;



public class TodolistTest {

	private static String url;
	private static WebTarget target;
	private static String customKeyHeader;
	private static String myKey;
	private static String resourcePath;

	@BeforeClass
	public static void setUpBeforeClass() {
		Properties props = new Properties();
		try {
			props.load(TodolistTest.class
					.getResourceAsStream("test.properties"));
			url = props.getProperty("todolist.endpoint");

			Client client = ClientBuilder.newClient();
			target = client.target(url);

			customKeyHeader = props.getProperty("todolist.customKeyHeader");
			myKey = props.getProperty("todolist.myKey");
			resourcePath = props.getProperty("todolist.resourcePath");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCRUD() {

		// create a new task
		final TodoTask task = new TodoTask();
		task.setTitle("New task");
		task.setBody("New task body");
		task.setDone(false);

		// save it
		String taskId = saveTask(task);
		assertNotNull(taskId);

		// retrieve the created task
		TodoTask createdTask = getTask(taskId);

		assertEquals(createdTask.getId(), taskId);
		assertEquals(createdTask.getTitle(), task.getTitle());
		assertEquals(createdTask.getBody(), task.getBody());
		assertEquals(createdTask.isDone(), task.isDone());

		// modify title and body
		createdTask.setTitle("Modified task");
		createdTask.setBody("Modified task body");

		// save it
		final TodoTask updatedTask = updateTask(createdTask);

		assertEquals(createdTask.getId(), updatedTask.getId());
		assertEquals(createdTask.getTitle(), updatedTask.getTitle());
		assertEquals(createdTask.getBody(), updatedTask.getBody());
		assertEquals(createdTask.isDone(), updatedTask.isDone());

		// mark it as done
		final TodoTask done = markTaskAs(updatedTask, "done");

		assertEquals(done.isDone(), true);

		// mark it as undone
		final TodoTask undone = markTaskAs(updatedTask, "undone");

		assertEquals(undone.isDone(), false);

		// delete it
		final Response response1 = deleteTask(updatedTask);
		assertEquals(response1.getStatus(), 204);

		// check the task is not there anymore
		final Response response2 = target.path(resourcePath).path(updatedTask.getId())
				.request().header(customKeyHeader, myKey).get();

		assertEquals(response2.getStatus(), 404);

	}

	@Test
	public void testSearch() {

		// create a new task
		final TodoTask task1 = new TodoTask();
		task1.setTitle("Keyword");
		task1.setBody("New task body");
		task1.setDone(false);

		// create a less relevant task
		final TodoTask task2 = new TodoTask();
		task2.setTitle("New Task");
		task2.setBody("New task body with keyword");
		task2.setDone(false);

		// save them
		final String id1 = saveTask(task1);
		final String id2 = saveTask(task2);

		final SearchResult result = target.path(resourcePath)
				.queryParam("query", "keyword").queryParam("from", 0)
				.queryParam("limit", 2).request()
				.header(customKeyHeader, myKey).get(SearchResult.class);
		
		assertNotNull(result);
		
		// Result must contain both tasks
		final List<TodoTask> content = result.getContent();
		assertEquals(content.size(), 2);
		
		
		// Task 1 must appear first since it contains "keyword" in the title
		final TodoTask firstResult = content.get(0);
		final TodoTask secondResult = content.get(1);
		
		assertEquals(firstResult.getId(), id1);
		assertEquals(secondResult.getId(), id2);
		
		deleteTask(firstResult);
		deleteTask(secondResult);
		
	}

	private Response deleteTask(TodoTask task) {
		Response response = target.path(resourcePath).path(task.getId())
				.request().header(customKeyHeader, myKey)
				.delete();
		return response;
	}

	private TodoTask markTaskAs(TodoTask task, String mark) {
		TodoTask done = target
				.path(resourcePath)
				.path(task.getId())
				.path(mark)
				.request()
				.header(customKeyHeader, myKey)
				.put(Entity
						.entity(new JSONObject(), MediaType.APPLICATION_JSON),
						TodoTask.class);
		return done;
	}

	private TodoTask updateTask(TodoTask task) {
		TodoTask updatedTask = target
				.path(resourcePath)
				.path(task.getId())
				.request()
				.header(customKeyHeader, myKey)
				.put(Entity.entity(task, MediaType.APPLICATION_JSON),
						TodoTask.class);
		return updatedTask;
	}

	private TodoTask getTask(String taskId) {
		TodoTask createdTask = target.path(resourcePath).path(taskId).request()
				.header(customKeyHeader, myKey).get(TodoTask.class);
		return createdTask;
	}

	private String saveTask(TodoTask task) {
		return target
				.path(resourcePath)
				.request()
				.header(customKeyHeader, myKey)
				.post(Entity.entity(task, MediaType.APPLICATION_JSON),
						String.class);
	}

}
