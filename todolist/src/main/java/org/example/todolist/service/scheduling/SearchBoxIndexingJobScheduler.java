package org.example.todolist.service.scheduling;

import org.jvnet.hk2.annotations.Contract;

import io.searchbox.action.Action;
import io.searchbox.client.JestResult;

@Contract
public interface SearchBoxIndexingJobScheduler extends
		IndexingJobScheduler<Action<? extends JestResult>> {

}
