package com.gdn.x.scheduler.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;

/**
 * 
 * @author yauritux
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CommandDAOTest extends BaseDAOTest {

	@Autowired
	private CommandDAO commandDAO;
	
	@Test
	public void findById_existingID_commandReturned() {
		Command command = commandDAO.findById("1");
		assertNotNull(command.getCommand());
	}
	
	@Test
	public void findById_nonExistingID_nullRecordReturned() {
		Command command = commandDAO.findById("100");
		assertNull(command);
	}
	
	@Test
	public void fetchAll_dataExist_moreThanOneRecordFound() {
		assertTrue(commandDAO.fetchAll().size() > 1);
	}
	
	@Test
	public void findByCommandType_oneRecordExist_commandReturned() {
		List<Command> commands = commandDAO.findByCommandType(CommandType.WEB_SERVICE);
		assertNotNull(commands.get(0).getCommand());
	}
	
	@Test
	public void deleteCommand_recordNotExist_noRecordDeleted() {
		assertEquals(0, commandDAO.deleteCommand("100"));
	}
}