package org.example.todolist.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.todolist.model.SearchResult;
import org.example.todolist.model.TodoTask;

public class SearchResultBuilder {

	private int from = 0;

	private int limit = 10;

	private List<TodoTask> content;

	private String query;

	private String url;

	public SearchResultBuilder content(List<TodoTask> content) {
		this.content = content;
		return this;
	}

	public SearchResultBuilder query(String query) {
		this.query = query;
		return this;
	}

	public SearchResultBuilder from(int from) {
		this.from = from;
		return this;
	}

	public SearchResultBuilder limit(int limit) {
		this.limit = limit;
		return this;
	}

	public SearchResultBuilder baseURL(String url) {
		this.url = url;
		return this;
	}

	public SearchResult build() {
		Map<String, String> links = new HashMap<>();
		
		if (from > 0) {
			int index = (from - limit > 0) ? (from - limit) : 0;
			String prev = createLink(query, limit, index);
			links.put("prev", prev);
		}
		if (content.size() == limit) {
			int index = from + limit;
			String next = createLink(query, limit, index);
			links.put("next", next);
		}

		return new SearchResult(content, links);
	}

	private String createLink(String query, int limit, int index) {
		String link = url + "?query=" + query + "&from=" + index
				+ "&limit=" + limit;
		return link;
	}

}
