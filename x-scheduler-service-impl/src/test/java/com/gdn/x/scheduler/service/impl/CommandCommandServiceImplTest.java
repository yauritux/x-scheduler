package com.gdn.x.scheduler.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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
import com.gdn.x.scheduler.service.CommandCommandService;

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
public class CommandCommandServiceImplTest {

	//SUT
	private CommandCommandService commandService;
	
	//Collaborator
	private CommandDAO mockCommandDAO;
	
	@Before
	public void setup() throws Exception {
		mockCommandDAO = mock(CommandDAO.class, withSettings().name("Command DAO").verboseLogging());
		commandService = spy(new CommandCommandServiceImpl(mockCommandDAO));		
	}
	
	@Test(timeout = 1000)
	public void save_nullIsPassed_nullIsReturned() {
		assertNull(commandService.save(null));
	}
	
	@Test(timeout = 1000)
	public void save_insertSuccess_commandObjectReturned() {
		when(mockCommandDAO.save(buildSampleCommand("1"))).thenReturn(buildSampleCommand("1"));
		Command command = commandService.save(buildSampleCommand("1"));
		assertNotNull(command);
	}
	
	@Test(timeout = 1000)
	public void save_updateStoreIdSuccess_storeIdChanged() {
		Command command = buildSampleCommand("1");
		when(mockCommandDAO.save(command)).thenReturn(command);		
		command.setStoreId("store-999");
		Command updatedCommand = commandService.save(command);
		assertEquals("store-999", updatedCommand.getStoreId());
	}
	
	@Test(timeout = 1000)
	public void delete_noEntityPassed_falseIsReturned() {
		assertFalse(commandService.delete(null));
	}
	
	@Test(timeout = 1000)
	public void delete_entityAlreadyMarkedAsDeleted_falseIsReturned() {
		Command command = buildDeletedSampleCommand("5");
		assertFalse(commandService.delete(command));
	}
	
	@Test(timeout = 1000)
	public void delete_noRecordDeleted_falseIsReturned() {
		Command command = buildSampleCommand("1");
		when(mockCommandDAO.deleteCommand(command.getId())).thenReturn(0);
		assertFalse(commandService.delete(command));
	}
	
	@Test(timeout = 1000, expected = Exception.class)
	public void delete_exceptionRaisedOnDBOperation_exceptionThrown() {
		Command command = buildSampleCommand("1");
		when(mockCommandDAO.deleteCommand(command.getId())).thenThrow(new Exception("Cannot delete object"));
	}
	
	@Test(timeout = 1000)
	public void delete_recordDeletedSuccessfully_trueIsReturned() {
		Command command = buildSampleCommand("1");
		when(mockCommandDAO.deleteCommand(command.getId())).thenReturn(1);
		assertTrue(commandService.delete(command));
	}
	
	@Test(timeout = 1000)
	public void restore_noEntityPassed_falseIsReturned() {
		assertFalse(commandService.restore(null));
	}
	
	@Test(timeout = 1000)
	public void restore_entityNotMarkedAsDeleted_falseIsReturned() {
		Command command = buildSampleCommand("1");
		assertFalse(commandService.restore(command));
	}
	
	@Test(timeout = 1000)
	public void restore_noRecordRestored_falseIsReturned() {
		Command command = buildDeletedSampleCommand("5");
		when(mockCommandDAO.restoreCommand(command.getId())).thenReturn(0);
		assertFalse(commandService.restore(command));
	}
	
	@Test(timeout = 1000, expected = Exception.class)
	public void restore_exceptionRaisedOnDBOperation_exceptionThrown() {
		Command command = buildDeletedSampleCommand("5");
		when(mockCommandDAO.restoreCommand(command.getId())).thenThrow(new Exception("Cannot restore object"));		
	}
	
	@Test(timeout = 1000)
	public void restore_recordRestoredSuccessfully_trueIsReturned() {
		Command command = buildDeletedSampleCommand("5");
		when(mockCommandDAO.restoreCommand(command.getId())).thenReturn(1);
		assertTrue(commandService.restore(command));
	}
	
	private Command buildSampleCommand(String id) {
		Command command = new WebServiceCommand();
		command.setId(id);
		command.setCommandType(CommandType.WEB_SERVICE);
		command.setStoreId("store-123");
		command.setMarkForDelete(false);
		return command;
	}
	
	private Command buildDeletedSampleCommand(String id) {
		Command command = new WebServiceCommand();
		command.setId(id);
		command.setCommandType(CommandType.WEB_SERVICE);
		command.setMarkForDelete(true);
		return command;
	}
}