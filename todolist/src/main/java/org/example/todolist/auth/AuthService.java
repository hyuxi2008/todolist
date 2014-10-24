package org.example.todolist.auth;

import org.example.todolist.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AuthService {

	User auth(String key);
	
	User currentUser();
	
}
