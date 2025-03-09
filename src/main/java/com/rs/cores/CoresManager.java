package com.rs.cores;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.*;

public final class CoresManager {

	protected static volatile boolean shutdown;
	public static WorldThread worldThread;
	public static ExecutorService serverWorkerChannelExecutor;
	public static ExecutorService serverBossChannelExecutor;
	public static Timer fastExecutor;
	public static ScheduledExecutorService slowExecutor;
	public static int serverWorkersCount;
	private static ServiceProvider serviceProvider;
	public static ExecutorService fastExecutorV2;

	public static void init() {
		worldThread = new WorldThread();
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		serverWorkersCount = availableProcessors >= 6 ? availableProcessors
				- (availableProcessors >= 12 ? 7 : 5) : 1;
		serverWorkerChannelExecutor = availableProcessors >= 6 ? Executors
				.newFixedThreadPool(availableProcessors
						- (availableProcessors >= 12 ? 7 : 5),
						new DecoderThreadFactory()) : Executors
				.newSingleThreadExecutor(new DecoderThreadFactory());
		serverBossChannelExecutor = Executors
				.newSingleThreadExecutor(new DecoderThreadFactory());
		fastExecutorV2 = new FastThreadPoolExecutor(availableProcessors >= 12 ? 4 : availableProcessors >= 6 ? 2 : 1,
				new FastThreadFactory(new FastThreadHandler()));

		fastExecutor = new Timer("Fast Executor");
		slowExecutor = new SlowThreadPoolExecutor(availableProcessors >= 12 ? 4 : availableProcessors >= 6 ? 2 : 1,
				new SlowThreadFactory(new SlowThreadHandler()));
		serviceProvider = new ServiceProvider(false);
		worldThread.start();
		serviceProvider.scheduleAndTrackRepeatingTask(new TrackedRunnable() {
			@Override
			public void run() {
				//Logger.log(serviceProvider.log("Service Provider status report"));
				//Logger.log("Total requests received since server start: " + serviceProvider.requests);
				//Logger.log("Tracked future keys:");
				//serviceProvider.trackedFutures.keySet().forEach(Logger::log);
				//Logger.log("Total tracked futures: " + serviceProvider.trackedFutures.size());
				//Logger.log(serviceProvider.log("Status: Healthy"));
			}
		}, 30, 3600, TimeUnit.SECONDS);
	}

	public static void shutdown() {
		serverWorkerChannelExecutor.shutdown();
		serverBossChannelExecutor.shutdown();
		fastExecutor.cancel();
		slowExecutor.shutdown();
		fastExecutorV2.shutdown();
		shutdown = true;
	}

	 /** Returns the core's {@code ServiceProvider} used for accessing the executor services.
			* @return the core {@link ServiceProvider}
     */
	public static ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	static void purgeSlowExecutor() {
		((SlowThreadPoolExecutor) slowExecutor).purge();
	}

	public static Timer getFastExecutor() {
		return fastExecutor;
	}

	public static void setFastExecutor(Timer fastExecutor) {
		CoresManager.fastExecutor = fastExecutor;
	}

	private CoresManager() {

	}
	public static void scheduleRepeatedTask(FixedLengthRunnable r, long startDelay, long delayCount, TimeUnit unit) {
		Future<?> f = slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, unit);
		r.assignFuture(f);
	}
	/**
	 * Serves as a centralized hub for executor services in the
	 * context of the game engine. New developers should not
	 * have to know which executor to use, but should rather be
	 * able to call wrapper methods with generic names and descriptions,
	 * and let the {@code ServiceProvider} choose the correct
	 * {@link java.util.concurrent.ExecutorService};
	 * @author David O'Neill
	 */
	public static class ServiceProvider {

		private Map<String, Future<?>> trackedFutures;
		private int requests = 0;
		private boolean verbose;

		private ServiceProvider(boolean verbose) {
			trackedFutures = new ConcurrentHashMap<>();
			this.verbose = verbose;
			//Logger.log("ServiceProvider active and waiting for requests.");
		}

		/**
		 * Schedules a {@code Runnable} to be executed after the supplied
		 * start delay, and continuously executed thereafter at some
		 * specified frequency. This method should be used when there is
		 * no intention of stopping the task before server shutdown.<br/>
		 * The start delay and repetition frequency
		 * time unit must be supplied.
		 * @param r a {@link Runnable} to repeat
		 * @param startDelay time delay before execution begins
		 * @param delayCount frequency at which the {@code run()} method is called.
		 * @param unit the specified time unit
		 */
		public void scheduleRepeatingTask(Runnable r, long startDelay, long delayCount, TimeUnit unit) {
			CoresManager.slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, unit);
			requests++;
		}

		/**
		 * Schedules a {@code Runnable} to be executed after the supplied
		 * start delay, and continuously executed thereafter at some
		 * specified frequency. This method should be used when there is
		 * no intention of stopping the task before server shutdown.<br/>
		 * The start delay and repetition frequency
		 * time unit is assumed to be {@link TimeUnit#SECONDS}.
		 * @param r a {@link Runnable} to repeat
		 * @param startDelay time delay before execution begins
		 * @param delayCount frequency at which the {@code run()} method is called.
		 */
		public void scheduleRepeatingTask(Runnable r, long startDelay, long delayCount) {
			CoresManager.slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, TimeUnit.SECONDS);
			requests++;
		}


		public void scheduleFixedLengthTask(com.rs.cores.FixedLengthRunnable r, long startDelay, long delayCount, TimeUnit unit) {
			Future<?> f = CoresManager.slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, unit);
			r.assignFuture(f);
			requests++;
		}


		public void scheduleFixedLengthTask(com.rs.cores.FixedLengthRunnable r, long startDelay, long delayCount) {
			Future<?> f = CoresManager.slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, TimeUnit.SECONDS);
			r.assignFuture(f);
			requests++;
		}



		public void scheduleAndTrackRepeatingTask(TrackedRunnable r, long startDelay, long delayCount, TimeUnit unit) {
			if (trackedFutures.containsKey(r.getTrackingKey())) {
				System.err.println(log("Attempted to add Future to tracking map, but duplicate key was found. Aborting."));
				return;
			}
			Future<?> future = CoresManager.slowExecutor.scheduleWithFixedDelay(r, startDelay, delayCount, unit);
			trackedFutures.put(r.getTrackingKey(), future);
			//if(verbose)
			//Logger.log(log("Tracking new future with key: " + r.getTrackingKey()));
			requests++;
		}


		public void cancelTrackedTask(String key, boolean interrupt) {
			Future<?> future = trackedFutures.remove(key);
			if (future != null) {
				future.cancel(interrupt);
				CoresManager.purgeSlowExecutor();
				//if(verbose)
				//Logger.log(log("Cancelled future with key: " + key));
			}
		}


		public void executeWithDelay(Runnable r, long startDelay, TimeUnit unit) {
			CoresManager.slowExecutor.schedule(r, startDelay, unit);
			requests++;
		}

		
		public void executeNow(Runnable r) {
			CoresManager.fastExecutorV2.execute(r);
			requests++;
		}

		private String log(String message) {
			String prefix = "[Service Provider] => ";
			return prefix + message;
		}

	}
}
