package com.gdn.x.scheduler.service.schedengine.impl;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gdn.common.base.shade.org.apache.http.client.config.RequestConfig;
import com.gdn.common.base.shade.org.apache.http.client.methods.CloseableHttpResponse;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpGet;
import com.gdn.common.base.shade.org.apache.http.impl.client.CloseableHttpClient;
import com.gdn.common.base.shade.org.apache.http.impl.client.HttpClientBuilder;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.domain.TaskExecutionCommandService;
import com.gdn.x.scheduler.service.schedengine.TaskExecutor;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Component("taskExecutor")
@Scope("prototype")
public class TaskExecutorImpl implements TaskExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(TaskExecutorImpl.class);
	
	private CommandQueryService commandQueryService;
	private TaskExecutionCommandService taskExecutionCommandService;
	private Task task;

	@Autowired
	public TaskExecutorImpl(CommandQueryService commandQueryService, TaskExecutionCommandService taskExecutionCommandService) {
		this.commandQueryService = commandQueryService;
		this.taskExecutionCommandService = taskExecutionCommandService;
	}

	@Override
	public void setTask(Task task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		if (task == null || task.getCommand() == null) {
			LOG.error("No Task to be executed...");
			return;
		}
		
		System.out.println("Running Task " + task.getTaskName());

		CloseableHttpClient httpClient = null;
		HttpGet getRequest = null;
		CloseableHttpResponse response = null;		
		//TODO :: implement command/strategy pattern for this particular operation
		try {
			if (task.getCommand().getCommandType() == CommandType.WEB_SERVICE) {
				System.out.println("Calling web service...");

				TaskExecution taskExecution = taskExecutionCommandService.createTaskExecutionFromTask(task, true);
				
				WebServiceCommand command = (WebServiceCommand) task.getCommand();
				WSCommandResponse webService = (WSCommandResponse) commandQueryService.wrapCommand(command);
				httpClient = HttpClientBuilder.create().build();
				if (webService.getHttpMethod().equalsIgnoreCase(WSMethod.GET.name())) {
					StringBuilder strRequest = new StringBuilder();
					strRequest.append(webService.getEndPoint());
					if (webService.getParameters() != null && !webService.getParameters().isEmpty()) {
						strRequest.append("?" + webService.getParameters());
					}
					RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(11000)
							.setConnectTimeout(11000).setSocketTimeout(11000).build();
					getRequest = new HttpGet(strRequest.toString());
					getRequest.addHeader("accept", "application/json");
					getRequest.setConfig(requestConfig);
					
					response = httpClient.execute(getRequest);
					
					System.out.println("Response Code = " + response.getStatusLine().getStatusCode());
					
					if (response.getStatusLine().getStatusCode() != 200) {
						throw new RuntimeException("Failed to execute WS. Status Code: "
								+ response.getStatusLine().getStatusCode());
					}
				}
				
				taskExecution.setEnd(new Date());
				taskExecutionCommandService.save(taskExecution);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);			
			e.printStackTrace();
		} finally {
			try {
				if (getRequest != null) {
					getRequest.releaseConnection();
				}
				response.close();
			} catch (IOException ioe) {
				LOG.error(ioe.getMessage(), ioe);
				ioe.printStackTrace();
			}
		}
	}
}