package com.rs.cores.tasks;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldTasksManager {

	private static final ConcurrentLinkedQueue<WorldTaskInformation> tasks = new ConcurrentLinkedQueue<WorldTaskInformation>();

	public static void processTasks() {
		for (WorldTaskInformation taskInformation : tasks) {
			if (taskInformation.continueCount > 0) {
				taskInformation.continueCount--;
				continue;
			}
			taskInformation.task.run();
			if (taskInformation.task.needRemove)
				tasks.remove(taskInformation);
			else
				taskInformation.continueCount = taskInformation.continueMaxCount;
		}
	}

	public static void schedule(WorldTask task, int delayCount, int periodCount) {
		if (task == null || delayCount < 0 || periodCount < 0)
			return;
		tasks.add(new WorldTaskInformation(task, delayCount, periodCount));
	}

	public static void schedule(WorldTask worldTask, int delayCount) {
		if (worldTask == null || delayCount < 0)
			return;
		tasks.add(new WorldTaskInformation(worldTask, delayCount, -1));
	}

	public static void schedule(WorldTask task) {
		if (task == null)
			return;
		tasks.add(new WorldTaskInformation(task, 0, -1));
	}

	public static int getTasksCount() {
		return tasks.size();
	}

	private static final class WorldTaskInformation {

		private WorldTask task;
		private int continueMaxCount;
		private int continueCount;

		public WorldTaskInformation(WorldTask task, int continueCount,
				int continueMaxCount) {
			this.task = task;
			this.continueCount = continueCount;
			this.continueMaxCount = continueMaxCount;
			if (continueMaxCount == -1)
				task.needRemove = true;
		}
	}

}
