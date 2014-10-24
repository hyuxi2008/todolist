package org.example.todolist.dao;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.example.todolist.model.User;
import org.jvnet.hk2.annotations.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class MongoDBUserDAO extends MongoDBEntityDAO<User, String> implements
		UserDAO {

	@PostConstruct
	public void connect() {
		super.connect();
		String collectionName = config
				.getProperty("mongo.collections.users.name");
		collection = db.getCollection(collectionName);
	}

	@Override
	public String create(User user) {
		String id = UUID.randomUUID().toString();
		user.setId(id);
		BasicDBObject obj = new BasicDBObject().append("id", id)
				.append("username", user.getUsername())
				.append("key", user.getKey());

		collection.insert(obj);
		return id;
	}

	@Override
	public User findOne(String id) {
		return find(new BasicDBObject("id", id));
	}

	@Override
	public User findByKey(String key) {
		return find(new BasicDBObject("key", key));
	}

	private User find(DBObject query) {
		DBObject obj = collection.findOne(query);
		User result = mapResult(obj);
		return result;
	}

	@Override
	public User delete(String id) {
		DBObject obj = collection.findAndRemove(new BasicDBObject("id", id));
		User result = mapResult(obj);
		return result;
	}

	@Override
	public void update(User user) {
		DBObject obj = collection
				.findOne(new BasicDBObject("id", user.getId()));
		obj.put("username", user.getUsername());
		obj.put("key", user.getKey());
		collection.save(obj);
	}

	private User mapResult(DBObject obj) {
		User result = null;
		if (obj != null) {
			result = new User();
			result.setId((String) obj.get("id"));
			result.setUsername((String) obj.get("username"));
			result.setKey((String) obj.get("key"));
		}
		return result;
	}
}