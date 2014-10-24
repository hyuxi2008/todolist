package org.example.todolist.dao;

import java.net.UnknownHostException;

import javax.inject.Inject;

import org.example.todolist.config.ApplicationConfig;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

public abstract class MongoDBEntityDAO<E, K> implements EntityDAO<E, K> {

	protected DBCollection collection;
	
	@Inject
	protected ApplicationConfig config;

	protected DB db; 

	public void connect(){
		MongoURI mongoURI = new MongoURI(config.getProperty("mongo.url"));
		try {
			db = mongoURI.connectDB();
			db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());		
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}
