package com.gdn.x.scheduler.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.dao.CommandDAO;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.domain.impl.CommandQueryServiceImpl;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.slf4j.Logger")
@SuppressStaticInitializationFor("org.slf4j.Logger")
public class CommandQueryServiceImplTest {

	// SUT
	private CommandQueryService commandQueryService;
	
	// Collaborator
	private CommandDAO mockCommandDAO;
	
	@Before
	public void setUp() throws Exception {
		mockCommandDAO = mock(CommandDAO.class, withSettings().name("Command DAO").verboseLogging());
		commandQueryService = spy(new CommandQueryServiceImpl(mockCommandDAO));
	}
	
	@Test(timeout = 1000)
	public void findById_nullId_nullReturned() {
		when(mockCommandDAO.findById(null)).thenReturn(null);
		Command command = commandQueryService.findById(null);
		assertNull(command);
	}
	
	@Test(timeout = 1000)
	public void findById_existingId_commandReturned() {
		when(mockCommandDAO.findById("1")).thenReturn(new WebServiceCommand());
		Command command = commandQueryService.findById("1");
		assertNotNull(command);
	}
	
	@Test(timeout = 1000)
	public void wrapCommand_commandIsNull_nullReturned() throws Exception {
		CommandResponse commandResponse = commandQueryService.wrapCommand(null);
		assertNull(commandResponse);
	}
	
	@Test(timeout = 1500)
	public void wrapCommand_commandIsOfWSType_WSCommandResponseReturned() throws Exception {
		CommandResponse commandResponse = commandQueryService.wrapCommand(buildWSCommand());
		assertTrue(commandResponse instanceof WSCommandResponse);
	}
	
	@Test(timeout = 1000)
	public void wrapCommand_commandBadJsonFormat_nullIsReturned() throws Exception {
		assertNull(commandQueryService.wrapCommand(buildBadWSCommand()));
	}
		
	private Command buildWSCommand() {
		WebServiceCommand command = new WebServiceCommand();
		command.setCommand("{\"url\":\"http://www.google.com\", \"method\":\"GET\"}");
		command.setCommandType(CommandType.WEB_SERVICE);
		command.setContents("");
		return command;
	}
	
	private Command buildBadWSCommand() {
		WebServiceCommand command = new WebServiceCommand();
		command.setId("1");
		command.setCommand("{\"url\": \"http://www.google.com\"");		
		command.setContents("");
		command.setParameters("");
		command.setStoreId("store-123");
		return command;
	}	
}