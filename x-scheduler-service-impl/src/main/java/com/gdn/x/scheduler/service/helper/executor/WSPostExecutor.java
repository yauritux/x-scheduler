package com.gdn.x.scheduler.service.helper.executor;

import com.gdn.common.base.shade.org.apache.http.HttpHeaders;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpPost;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpRequestBase;
import com.gdn.common.base.shade.org.apache.http.entity.ContentType;
import com.gdn.common.base.shade.org.apache.http.entity.StringEntity;
import com.gdn.common.enums.ErrorCategory;
import com.gdn.common.exception.ApplicationRuntimeException;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class WSPostExecutor extends WebServiceExecutor {

	@Override
	protected HttpRequestBase generateRequest(WSCommandResponse wsCommand) throws ApplicationRuntimeException {
		if (wsCommand.getContents() == null || wsCommand.getContents().isEmpty()) {
			throw new ApplicationRuntimeException(ErrorCategory.REQUIRED_PARAMETER);
		}				
		StringBuilder strRequest = new StringBuilder();
		strRequest.append(wsCommand.getEndPoint());
		if (wsCommand.getParameters() != null && !wsCommand.getParameters().isEmpty()) {
			strRequest.append("?").append(wsCommand.getParameters());
		}		
		HttpPost request = new HttpPost(strRequest.toString());
		request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		request.addHeader(HttpHeaders.ACCEPT, "application/json");
		
		request.setEntity(new StringEntity(wsCommand.getContents(), ContentType.create("application/json")));		
		
		return request;
	}
}