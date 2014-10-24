package org.example.todolist.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.example.todolist.dao.UserDAO;
import org.example.todolist.model.User;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jvnet.hk2.annotations.Service;

@Service
@RequestScoped
public class SimpleAuthService implements AuthService {

	@Context
	private HttpServletRequest request;

	@Inject
	private UserDAO dao;
	
	@Override
	public User auth(String key) {
		return dao.findByKey(key);
	}

	@Override
	public User currentUser() {
		return (User) request.getAttribute("currentUser");
	}

}
