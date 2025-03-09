package com.rs.cores.tasks;

import com.rs.cores.tasks.impl.CleanUpTask;

public final class TaskHandler {

	private static TaskHandler INSTANCE;
	private Task[] tasks;

	public TaskHandler() {
		tasks = new Task[] {

		/*
		 * World Tasks
		 */

		/*
		 * Server Tasks
		 */
		new CleanUpTask() };

	}

	public Task[] tasks() {
		return tasks;
	}

	public static TaskHandler get() {
		if (INSTANCE == null)
			INSTANCE = new TaskHandler();
		return INSTANCE;
	}

}
