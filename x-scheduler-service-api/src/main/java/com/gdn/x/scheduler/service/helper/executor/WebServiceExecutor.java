package com.gdn.x.scheduler.service.helper.executor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.common.base.shade.org.apache.http.client.ClientProtocolException;
import com.gdn.common.base.shade.org.apache.http.client.config.RequestConfig;
import com.gdn.common.base.shade.org.apache.http.client.methods.CloseableHttpResponse;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpRequestBase;
import com.gdn.common.base.shade.org.apache.http.impl.client.CloseableHttpClient;
import com.gdn.common.base.shade.org.apache.http.impl.client.HttpClientBuilder;
import com.gdn.common.exception.ApplicationRuntimeException;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.wrapper.ProcessResponse;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public abstract class WebServiceExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(WebServiceExecutor.class);
	
	private HttpRequestBase request;
	
	public WebServiceExecutor() {}
	
	public ProcessResponse callWS(WSCommandResponse wsCommand, int timeout) throws ApplicationRuntimeException {
		this.request = generateRequest(wsCommand);
		return execute(timeout);
	}	
	
	protected abstract HttpRequestBase generateRequest(WSCommandResponse wsCommand) throws ApplicationRuntimeException;
		
	private ProcessResponse execute(int timeout) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		ProcessResponse processResponse = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout).setSocketTimeout(timeout).build();
			this.request.setConfig(requestConfig);
			
			response = httpClient.execute(request);
			
			System.out.println("Response Code = " + response.getStatusLine().getStatusCode());
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed to execute WS. Status Code: "
						+ response.getStatusLine().getStatusCode());
			}
			
			processResponse = new ProcessResponse("200", "success");
		} catch (ClientProtocolException e) {
			LOG.error(e.getMessage(), e);
			processResponse = new ProcessResponse("400", e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			processResponse = new ProcessResponse("400", e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			processResponse = new ProcessResponse("400", e.getMessage());
		} finally {
			try {
				if (request != null) {
					request.releaseConnection();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException ioe) {
				LOG.error(ioe.getMessage(), ioe);
			} catch (Exception e1) {
				LOG.error(e1.getMessage(), e1);
			}				
		}
		
		return processResponse;
	}
}