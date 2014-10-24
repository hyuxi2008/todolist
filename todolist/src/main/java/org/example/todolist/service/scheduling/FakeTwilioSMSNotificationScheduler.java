package org.example.todolist.service.scheduling;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.jvnet.hk2.annotations.Service;

@Service
public class FakeTwilioSMSNotificationScheduler implements
		NotificationScheduler {

	private static final Logger logger = Logger
			.getLogger(FakeTwilioSMSNotificationScheduler.class.getName());

	@Override
	public void scheduleNotification(List<NameValuePair> params) {
		logger.log(Level.WARNING,
				"I should handle this notification. Hopefully a working me will be provided.");
	}

}
