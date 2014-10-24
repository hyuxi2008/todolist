package org.example.todolist.dao;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.example.todolist.model.TodoTask;
import org.jvnet.hk2.annotations.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class MongoDBTodoTaskDAO extends MongoDBEntityDAO<TodoTask, String>
		implements TodoTaskDAO {

	@PostConstruct
	public void connect() {
		super.connect();
		String collectionName = config
				.getProperty("mongo.collections.tasks.name");
		collection = db.getCollection(collectionName);
	}

	@Override
	public String create(TodoTask task) {
		String id = UUID.randomUUID().toString();
		task.setId(id);
		BasicDBObject obj = new BasicDBObject().append("id", id)
				.append("user", task.getUser())
				.append("title", task.getTitle())
				.append("body", task.getBody()).append("done", task.isDone());

		collection.insert(obj);
		return id;
	}

	@Override
	public TodoTask findOne(String id) {
		DBObject obj = collection.findOne(new BasicDBObject("id", id));
		TodoTask result = mapResult(obj);
		return result;
	}

	@Override
	public TodoTask delete(String id) {
		DBObject obj = collection.findAndRemove(new BasicDBObject("id", id));
		TodoTask result = mapResult(obj);
		return result;
	}

	@Override
	public void update(TodoTask task) {
		DBObject obj = collection
				.findOne(new BasicDBObject("id", task.getId()));
		obj.put("title", task.getTitle());
		obj.put("body", task.getBody());
		obj.put("done", task.isDone());
		collection.save(obj);
	}

	private TodoTask mapResult(DBObject obj) {
		TodoTask result = null;
		if (obj != null) {
			result = new TodoTask();
			result.setId((String) obj.get("id"));
			result.setUser((String) obj.get("user"));
			result.setTitle((String) obj.get("title"));
			result.setBody((String) obj.get("body"));
			result.setDone((boolean) obj.get("done"));
		}
		return result;
	}

}
