package org.example.todolist.service.scheduling;

import java.util.List;

import org.apache.http.NameValuePair;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface NotificationScheduler {

	void scheduleNotification(List<NameValuePair> params);
	
}
