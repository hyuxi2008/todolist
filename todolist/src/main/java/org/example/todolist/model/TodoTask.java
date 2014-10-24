package org.example.todolist.model;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@XmlRootElement
public class TodoTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 818003661582988515L;

	@JestId
	private String id;

	private String user;
	
	@NotBlank
	@SafeHtml
	// so it fits in one twilio sms with default msg template
	@Length(min = 3, max = 135)
	private String title;

	@NotBlank
	@SafeHtml
	@Length(min = 3, max = 1024)
	private String body;

	private boolean done;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return "TodoTask [id=" + id + ", title=" + title + ", body=" + body
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TodoTask other = (TodoTask) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
