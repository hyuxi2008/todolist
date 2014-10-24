package org.example.todolist;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.example.todolist.model.SearchResult;
import org.example.todolist.model.TodoTask;
import org.example.todolist.service.SearchService;
import org.example.todolist.service.TodolistService;
import org.example.todolist.util.SearchResultBuilder;

@Path("tasks")
public class Todolist {

	@Context
	private HttpServletRequest request;

	@Inject
	private TodolistService todolistService;

	@Inject
	private SearchService searchService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Valid TodoTask task) {
		String taskId = todolistService.createTask(task);
		return Response.ok(taskId).status(201).build();
	}

	@GET
	@Path("/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@NotNull @PathParam("taskId") String taskId) {
		TodoTask task = todolistService.getTask(taskId);
		return (task != null) ? ok(task, 200) : notFound();
	}

	@Path("/{taskId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@NotNull @PathParam("taskId") String taskId,
			@Valid TodoTask task) {
		TodoTask updatedTask = todolistService.updateTask(taskId, task);
		return (updatedTask != null) ? ok(updatedTask, 200) : notFound();
	}

	@Path("/{taskId}/done")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response done(@NotNull @PathParam("taskId") String taskId) {
		TodoTask updatedTask = todolistService.mark(taskId, true);
		return (updatedTask != null) ? ok(updatedTask, 200) : notFound();
	}

	@Path("/{taskId}/undone")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response undone(@NotNull @PathParam("taskId") String taskId) {
		TodoTask updatedTask = todolistService.mark(taskId, false);
		return (updatedTask != null) ? ok(updatedTask, 200) : notFound();
	}

	@Path("/{taskId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@NotNull @PathParam("taskId") String taskId) {
		TodoTask task = todolistService.deleteTask(taskId);
		return (task != null) ? ok(task, 204) : notFound();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("query") String query,
			@QueryParam("from") int from, @QueryParam("limit") int limit) {
		Response response = null;
		SearchResult result;
		List<TodoTask> tasks;
		try {
			tasks = searchService.search(query, from, limit);
			result = new SearchResultBuilder()
					.baseURL(request.getRequestURL().toString()).content(tasks)
					.from(from).limit(limit).query(query).build();
			response = Response.ok(result).build();
		} catch (Exception e) {
			response = error("Could not perform the requested search.");
		}
		return response;
	}

	private Response error(String message) {
		return Response
				.serverError()
				.entity("{\"message\" : \"" + message
						+ "\" , \"status\" : \"500\"}").build();
	}

	private Response notFound() {
		return Response
				.status(Status.NOT_FOUND)
				.entity("{\"message\" : \"Task not found.\" , \"status\" : \"404\"}")
				.build();
	}

	private Response ok(Object o, int okCode) {
		return Response.ok(o).status(okCode).build();
	}

}
