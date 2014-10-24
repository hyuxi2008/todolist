package org.example.todolist.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.example.todolist.model.User;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	@Inject
	private AuthService authService;
	
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {

		String key = requestContext.getHeaderString("X-Todolist-Key");
		if (key != null) {
			User u = authService.auth(key);
			if(u != null){
				requestContext.setProperty("currentUser", u);
			} else {
				deny(requestContext);
			}
		} else {
			deny(requestContext);
		}
	}

	private void deny(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response
				.status(Response.Status.UNAUTHORIZED)
				.entity("User cannot access the resource.").build());
	}
}
