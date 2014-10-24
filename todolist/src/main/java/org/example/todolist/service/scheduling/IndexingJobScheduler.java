package org.example.todolist.service.scheduling;


public interface IndexingJobScheduler<T> {

	void scheduleIndexingJob(T t);
	
}
