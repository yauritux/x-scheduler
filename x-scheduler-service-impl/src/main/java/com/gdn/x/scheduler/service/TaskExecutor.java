package com.gdn.x.scheduler.service;

import java.util.Date;

import com.gdn.common.base.shade.org.apache.http.HttpResponse;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpGet;
import com.gdn.common.base.shade.org.apache.http.impl.client.DefaultHttpClient;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 *
 */
public class TaskExecutor implements Runnable {

	private CommandQueryService commandQueryService;
	private TaskExecutionCommandService taskExecutionCommandService;
	private Task task;
	
	public TaskExecutor(CommandQueryService commandQueryService, TaskExecutionCommandService taskExecutionCommandService,
			Task task) {
		this.commandQueryService = commandQueryService;
		this.taskExecutionCommandService = taskExecutionCommandService;
		this.task = task;
	}
	
	@Override
	public void run() {
		if (task == null || task.getCommand() == null) {
			System.out.println("No Task to execute...");
			return;
		}

		DefaultHttpClient httpClient = null;
		try {
		if (task.getCommand().getCommandType() == CommandType.WEB_SERVICE) {
			System.out.println("Calling web service...");
			TaskExecution taskExecution = new TaskExecution();
			taskExecution.setCreatedBy(task.getCreatedBy());
			taskExecution.setCreatedDate(new Date());
			taskExecution.setStart(new Date());
			taskExecution.setEnd(null);
			taskExecution.setTask(task);
			taskExecutionCommandService.save(taskExecution);
			
			HttpResponse response = null;
			WebServiceCommand command = (WebServiceCommand) task.getCommand();
			WSCommandResponse webService = (WSCommandResponse) commandQueryService.wrapCommand(command);
			httpClient = new DefaultHttpClient();
			if (webService.getHttpMethod().equalsIgnoreCase(WSMethod.GET.name())) {
				StringBuilder strRequest = new StringBuilder();
				strRequest.append(webService.getEndPoint());
				if (webService.getParameters() != null && !webService.getParameters().isEmpty()) {
					strRequest.append("?" + webService.getParameters());
				}
				HttpGet getRequest = new HttpGet(strRequest.toString());
				getRequest.addHeader("accept", "application/json");
				
				response = httpClient.execute(getRequest);
				
				System.out.println("Response Code = " + response.getStatusLine().getStatusCode());
				
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed to execute WS. Status Code: "
							+ response.getStatusLine().getStatusCode());
				}
			}
			httpClient.getConnectionManager().shutdown();	
			
			taskExecution.setEnd(new Date());
			taskExecutionCommandService.save(taskExecution);
		}
		}catch (Exception e) {
			e.printStackTrace();
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
	}
}
