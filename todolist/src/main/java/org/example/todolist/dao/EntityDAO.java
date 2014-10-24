package org.example.todolist.dao;


public interface EntityDAO<E, K> {

	String create(E e);

	E findOne(K id);

	E delete(K id);

	void update(E e);

}
