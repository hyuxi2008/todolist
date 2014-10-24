package org.example.todolist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.example.todolist.config.ApplicationConfig;
import org.example.todolist.model.TodoTask;
import org.example.todolist.service.scheduling.NotificationScheduler;
import org.jvnet.hk2.annotations.Service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;

@Service
public class TwilioSMSNotificationService implements NotificationService {

	private TwilioRestClient client;

	@Inject
	private ApplicationConfig config;

	@Inject
	private NotificationScheduler scheduler;

	private String from;

	private String to;

	private String bodyTemplate;

	private static final Logger logger = Logger
			.getLogger(TwilioSMSNotificationService.class.getName());

	@PostConstruct
	public void postConstruct() {
		String account_sid = config
				.getProperty("notification.twilio.account_sid");
		String auth_token = config
				.getProperty("notification.twilio.auth_token");

		if ((account_sid != null) && (auth_token != null)) {
			client = new TwilioRestClient(account_sid, auth_token);
		}
		
		from = config.getProperty("notification.twilio.sms.from");
		to = config.getProperty("notification.twilio.sms.to");
		bodyTemplate = config
				.getProperty("notification.twilio.sms.bodytemplate");

	}

	@Override
	public void sendNotification(TodoTask task) {

		if ((from != null) && (to != null)) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			String body = bodyTemplate.replaceAll(
					Pattern.quote("${task.title}"),
					Matcher.quoteReplacement(task.getTitle()));

			params.add(new BasicNameValuePair("Body", body));
			params.add(new BasicNameValuePair("To", to));
			params.add(new BasicNameValuePair("From", from));

			MessageFactory messageFactory = client.getAccount()
					.getMessageFactory();

			try {
				messageFactory.create(params);
			} catch (TwilioRestException e) {
				logger.log(Level.SEVERE,
						"Could not sent SMS notification. Scheduling recovery...");
				scheduler.scheduleNotification(params);
			}
		} else {
			logger.log(
					Level.WARNING,
					"Configuration parameters missing.  SMS notification skipped. Check your /WEB-INF/todolist.properties file.");
		}
	}

}
