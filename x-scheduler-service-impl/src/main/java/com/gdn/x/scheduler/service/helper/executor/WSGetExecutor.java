package com.gdn.x.scheduler.service.helper.executor;

import com.gdn.common.base.shade.org.apache.http.HttpHeaders;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpGet;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpRequestBase;
import com.gdn.common.exception.ApplicationRuntimeException;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class WSGetExecutor extends WebServiceExecutor {

	@Override
	protected HttpRequestBase generateRequest(WSCommandResponse wsCommand)
			throws ApplicationRuntimeException {
		StringBuilder strRequest = new StringBuilder();
		strRequest.append(wsCommand.getEndPoint());
		if (wsCommand.getParameters() != null && !wsCommand.getParameters().isEmpty()) {
			strRequest.append("?").append(wsCommand.getParameters());
		}		
		HttpGet request = new HttpGet(strRequest.toString());
		request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		request.addHeader(HttpHeaders.ACCEPT, "application/json");
		
		return request;
	}
}