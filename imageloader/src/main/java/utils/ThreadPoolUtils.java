package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Administrator �̳߳ع���
 */

public class ThreadPoolUtils {
	private ExecutorService fixedThreadPool;

	public ThreadPoolUtils() {
		int maxThreadCount = Runtime.getRuntime().availableProcessors();
		fixedThreadPool = Executors.newFixedThreadPool(maxThreadCount);
	}

	public ExecutorService getExecutorService() {
		return fixedThreadPool;
	}

}
