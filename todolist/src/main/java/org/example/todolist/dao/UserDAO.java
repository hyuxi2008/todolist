package org.example.todolist.dao;

import org.example.todolist.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserDAO extends EntityDAO<User, String>{

	User findByKey(String key);
	
}
