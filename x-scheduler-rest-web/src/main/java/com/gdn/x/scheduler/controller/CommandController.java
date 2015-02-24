package com.gdn.x.scheduler.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.gdn.common.enums.ErrorCategory;
import com.gdn.common.web.wrapper.response.GdnBaseRestResponse;
import com.gdn.common.web.wrapper.response.GdnRestListResponse;
import com.gdn.common.web.wrapper.response.GdnRestSingleResponse;
import com.gdn.common.web.wrapper.response.PageMetaData;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.ClientSDKCommand;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.CSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.domain.CommandCommandService;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.wordnik.swagger.annotations.Api;

/**
 * 
 * @author yauritux
 *
 */
@RestController
@Api(value = "command", description = "Command Service API")
@RequestMapping(value = "/command", 
	produces = { 
		MediaType.APPLICATION_JSON_VALUE, 
		MediaType.APPLICATION_XML_VALUE 
})
public class CommandController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommandController.class);

	private CommandQueryService commandQueryService;
	private CommandCommandService commandActionService;
		
	@Autowired
	public CommandController(CommandQueryService commandQueryService, CommandCommandService commandActionService) {
		this.commandQueryService = commandQueryService;
		this.commandActionService = commandActionService;
	}
	
	@RequestMapping(value = "/ws/{id}", method = RequestMethod.GET)
	public GdnRestSingleResponse<WSCommandResponse> getWSCommand(@PathVariable("id") String id,
			@RequestParam String storeId, @RequestParam String requestId) throws Exception {
		try {
			Command command = commandQueryService.findById(id);
			if (command.getCommandType() == CommandType.WEB_SERVICE) {
				CommandResponse commandResponse = commandQueryService.wrapCommand(command);
				if (commandResponse == null) {
					return null;
				}
		    
				return new GdnRestSingleResponse<WSCommandResponse>(
					(WSCommandResponse) commandResponse, requestId
				);	
			}
		} catch (JsonParseException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
		
		return null;
	}
	
	@RequestMapping(value = "/ws", method = RequestMethod.GET)
	public GdnRestListResponse<WSCommandResponse> getWSCommands(
			@RequestParam String storeId, @RequestParam String requestId,
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize) 
					throws ReflectiveOperationException, NullPointerException, 
						JsonParseException, IOException {

		List<WSCommandResponse> listResponse = new ArrayList<WSCommandResponse>();
		
		PageMetaData pageMetaData = null;
		
		try {		
			Page<Command> pageList = commandQueryService.fetchAll(pageNumber, pageSize);
		
			List<Command> commands = pageList.getContent();
		
			LOG.debug("total commands found: " + (commands != null ? commands.size() : 0));
				
			for (Command command : commands) {
				listResponse.add((WSCommandResponse) commandQueryService.wrapCommand(command));
			}
			
			pageMetaData = new PageMetaData(pageSize, pageNumber, pageList.getTotalElements());
			
		} catch (NullPointerException e) { 
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (JsonParseException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
				
		return new GdnRestListResponse<WSCommandResponse>(listResponse, pageMetaData, requestId);
	}
	
	@RequestMapping(value = "/ws", method = RequestMethod.POST, consumes = "application/json")
	public GdnBaseRestResponse submitCommand(@Valid @RequestBody WSCommandRequest wsCommandRequest,
			BindingResult result, @RequestParam String storeId, @RequestParam String requestId) {		
		if (result.hasErrors()) {
			return errorValidation(result);
		}		

		WebServiceCommand wsCommand = (WebServiceCommand) commandActionService.buildCommandFromRequest(wsCommandRequest);
		
		commandActionService.save(wsCommand);
		
		GdnBaseRestResponse response = new GdnBaseRestResponse(true);
		response.setRequestId(requestId);
		
		return response;
	}
	
	@RequestMapping(value = "/cs", method = RequestMethod.POST, consumes = "application/json")
	public GdnBaseRestResponse submitCommand(
			@Valid @RequestBody CSCommandRequest csCommandRequest, BindingResult result,
			@RequestParam String storeId, @RequestParam String requestId) {
		
		if (result.hasErrors()) {
			return errorValidation(result);
		}				
		
		ClientSDKCommand csCommand = (ClientSDKCommand) commandActionService.buildCommandFromRequest(csCommandRequest);
				
		commandActionService.save(csCommand);
		
		GdnBaseRestResponse response = new GdnBaseRestResponse(true);
		response.setRequestId(requestId);
		
		return response;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public GdnBaseRestResponse uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("fileName") String fileName,
			@RequestParam String storeId, @RequestParam String requestId) {

		GdnBaseRestResponse response = null;
		
		if (!file.isEmpty()) {
			String uploadedDir = (System.getenv("UPLOAD_DIR") == null ? "" : System.getenv("UPLOAD_DIR"));
			System.out.println("uploadedDir = " + uploadedDir);
			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(uploadedDir + fileName)));
				stream.write(bytes);
				stream.close();
				System.out.println("Successfully uploaded.");
			} catch (Exception e) {
				System.out.println("Failed to upload file " + fileName);
				response = new GdnBaseRestResponse("Failed to upload file " + fileName, "UPLOAD_FAILED", false, requestId);
				return response;			
			}
		} else {
			System.out.println("No File uploaded.");
			response = new GdnBaseRestResponse("Please upload the ClientSDK!", "NO_SDK_UPLOADED", false, requestId);				
			return response;		
		}
		
		response = new GdnBaseRestResponse(true);
		response.setRequestId(requestId);
		
		return response;		
	}		
	
	private GdnBaseRestResponse errorValidation(BindingResult result) {
		GdnBaseRestResponse response = new GdnBaseRestResponse(false);
		response.setErrorCode(ErrorCategory.VALIDATION.getCode());
		
		List<FieldError> errors = result.getFieldErrors();
		StringBuffer sb = new StringBuffer();
		for (FieldError error : errors) {
			sb.append(error.getObjectName() + ":" + error.getDefaultMessage() + ", ");
		}
		LOG.debug(sb.toString());
		
		response.setErrorMessage("not valid parameters");
		return response;
	}
}