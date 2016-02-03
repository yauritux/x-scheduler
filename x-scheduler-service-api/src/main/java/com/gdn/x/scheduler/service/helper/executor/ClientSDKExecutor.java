package com.gdn.x.scheduler.service.helper.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.x.scheduler.wrapper.ProcessResponse;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public abstract class ClientSDKExecutor {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientSDKExecutor.class);
	
	protected Process process;
	
	public ClientSDKExecutor() {}
	
	public abstract ProcessResponse execute(long timeoutInSeconds) throws Exception;	

	protected ProcessResponse executeCommand(long timeoutInSeconds) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		int exitValue = 0;
		boolean success = true;
		try {
			Callable<Integer> call = new Callable<Integer>() {
				
				@Override
				public Integer call() throws Exception {
					return process.waitFor(); 
				}
			};
			Future<Integer> future = service.submit(call);
			exitValue = future.get(timeoutInSeconds, TimeUnit.SECONDS);
			if (exitValue != 0) {
				success = false;
				throw new Exception("Process did not exit correctly");
			}
		} catch (ExecutionException e) {
			LOG.error(e.getMessage(), e);
			exitValue = 4;
			success = false;			
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e);
			exitValue = 3;
			success = false;
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
			exitValue = 5;
			success = false;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			success = false;
		} finally {
			service.shutdown();
		}
		
		if (!success) {
			switch (exitValue) {
			case 3: 			
				return new ProcessResponse("3", "Process timed out");
			case 4:
				return new ProcessResponse("4", "Process failed to execute");
			case 5:
				return new ProcessResponse(String.valueOf(exitValue), "Process unexpectedly stopped.");
			default:
				return new ProcessResponse(String.valueOf(exitValue), "Process did not exit correctly");
			}
		}
		
		return new ProcessResponse(String.valueOf(0), "Success");
	}	
}