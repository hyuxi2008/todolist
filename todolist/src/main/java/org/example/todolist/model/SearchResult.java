package org.example.todolist.model;

import java.util.List;
import java.util.Map;

public class SearchResult {

	private List<TodoTask> content;

	private Map<String, String> links;

	public SearchResult() {

	}

	public SearchResult(List<TodoTask> content, Map<String, String> links) {
		this.content = content;
		this.links = links;
	}

	public List<TodoTask> getContent() {
		return content;
	}

	public void setContent(List<TodoTask> content) {
		this.content = content;
	}

	public Map<String, String> getLinks() {
		return links;
	}

	public void setLinks(Map<String, String> links) {
		this.links = links;
	}

}
