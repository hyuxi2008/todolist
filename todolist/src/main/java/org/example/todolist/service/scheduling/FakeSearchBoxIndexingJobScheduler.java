package org.example.todolist.service.scheduling;

import io.searchbox.action.Action;
import io.searchbox.client.JestResult;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.hk2.annotations.Service;

@Service
public class FakeSearchBoxIndexingJobScheduler implements
		SearchBoxIndexingJobScheduler {

	private static final Logger logger = Logger
			.getLogger(FakeSearchBoxIndexingJobScheduler.class.getName());

	@Override
	public void scheduleIndexingJob(Action<? extends JestResult> action) {
		logger.log(Level.WARNING, "I should execute a "
				+ action.getClass().getSimpleName()
				+ " Jest action. Hopefully a working me will be provided.");
	}

}
