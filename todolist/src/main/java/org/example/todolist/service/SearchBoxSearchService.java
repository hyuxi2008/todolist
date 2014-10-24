package org.example.todolist.service;

import io.searchbox.action.GenericResultAbstractDocumentTargetedAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.example.todolist.auth.AuthService;
import org.example.todolist.config.ApplicationConfig;
import org.example.todolist.model.TodoTask;
import org.example.todolist.service.scheduling.SearchBoxIndexingJobScheduler;
import org.jvnet.hk2.annotations.Service;

@Service
public class SearchBoxSearchService implements SearchService {

	private JestClient client;

	@Inject
	private ApplicationConfig config;

	@Inject
	private AuthService authService;
	
	@Inject
	private SearchBoxIndexingJobScheduler scheduler;

	private String indexName;

	private String documentType;

	private String template;

	private static final Logger logger = Logger
			.getLogger(SearchBoxSearchService.class.getName());

	@PostConstruct
	public void postConstruct() {
		String connectionUrl = config.getProperty("searchbox.url");

		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(connectionUrl)
				.multiThreaded(true).build());
		client = factory.getObject();

		indexName = config.getProperty("searchbox.indexname");
		documentType = config.getProperty("searchbox.documenttype");
		template = config.getProperty("searchbox.querytemplate");
	}

	@Override
	public List<TodoTask> search(String query, int from, int limit)
			throws Exception {

		Search search = buildSearch(query, from, limit);

		JestResult result = client.execute(search);
		List<TodoTask> tasks = result.getSourceAsObjectList(TodoTask.class);
		return tasks;
	}

	@Override
	public void index(TodoTask task) {
		Index index = new Index.Builder(task).index(indexName)
				.type(documentType).id(task.getId()).build();
		executeCommand(index);
	}

	@Override
	public void update(TodoTask task) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("doc", task);
		Update update = new Update.Builder(payload).index(indexName)
				.type(documentType).id(task.getId()).build();
		executeCommand(update);
	}

	@Override
	public void delete(String taskId) {
		Delete delete = new Delete.Builder(taskId).index(indexName)
				.type(documentType).build();
		executeCommand(delete);
	}

	private Search buildSearch(String query, int from, int limit) {
		query = template
				.replaceAll(Pattern.quote("${query}"),
						Matcher.quoteReplacement(query))
				.replaceAll(
						Pattern.quote("${user}"),
						Matcher.quoteReplacement(authService.currentUser()
								.getUsername()))
				.replaceAll(Pattern.quote("${from}"),
						Matcher.quoteReplacement(new Integer(from).toString()))
				.replaceAll(Pattern.quote("${size}"),
						Matcher.quoteReplacement(new Integer(limit).toString()));

		Search search = (Search) new Search.Builder(query).addIndex(indexName)
				.addType(documentType).build();
		return search;
	}

	private void executeCommand(
			GenericResultAbstractDocumentTargetedAction command) {
		try {
			JestResult result = client.execute(command);
			if(!result.isSucceeded()){
				logger.log(Level.WARNING, "Execution of command " + command.getType()
						+ " did not succeed. The following error has been returned: " + result.getErrorMessage());
				logger.log(Level.WARNING, "Scheduling recovery...");
				scheduler.scheduleIndexingJob(command);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not execute " + command.getType()
					+ " command due to the following reason " + e.getMessage());
			logger.log(Level.SEVERE, "Scheduling recovery...");
			scheduler.scheduleIndexingJob(command);
		}
	}

}
