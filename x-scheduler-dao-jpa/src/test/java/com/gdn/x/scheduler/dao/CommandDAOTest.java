package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	
	@Test(timeout = 1000)
	public void findById_existingID_commandReturned() {
		Command command = commandDAO.findById("1");
		assertNotNull(command.getCommand());
	}
	
	@Test(timeout = 1000)
	public void findById_nonExistingID_nullRecordReturned() {
		Command command = commandDAO.findById("100");
		assertNull(command);
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_recordDeleted_noRecordFound() {
		commandDAO.deleteCommand("3");
		assertNull(commandDAO.findByIdExclDelete("3"));
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_existingRecord_recordFound() {
		assertNull(commandDAO.findByIdExclDelete("4"));
	}
	
	@Test(timeout = 1000)
	public void count_inclusiveMarkForDelete_3RecordsFound() {
		assertTrue(commandDAO.count() == 3);
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_2RecordsDeleted_1RecordFound() {
		commandDAO.deleteCommand("3");
		commandDAO.deleteCommand("2");
		assertTrue(commandDAO.countExclDelete() == 1);
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_noRecordsDeleted_3RecordsFound() {
		assertTrue(commandDAO.countExclDelete() == 3);
	}
	
	@Test(timeout = 1000)
	public void fetchAll_noRecordsDeleted_3RecordsFound() {
		assertTrue(commandDAO.fetchAll().size() == 3);
	}
	
	@Test(timeout = 1000)
	public void fetchAll_1RecordDeleted_2RecordsFound() {
		commandDAO.deleteCommand("1");
		assertEquals(2, commandDAO.fetchAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_1RecordDeleted_3RecordsFound() {
		commandDAO.deleteCommand("1");
		assertEquals(3, commandDAO.findAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_noRecordsDeleted_3RecordsFound() {
		assertEquals(3, commandDAO.findAll().size());
	}
	
	@Test(timeout = 1000)
	public void findByCommandType_oneRecordExist_commandReturned() {
		List<Command> commands = commandDAO.findByCommandType(CommandType.WEB_SERVICE);
		assertNotNull(commands.get(0).getCommand());
	}	
	
	@Test(timeout = 1000)
	public void deleteCommand_nonExistingRecord_noAffectedRows() {
		assertEquals(0, commandDAO.deleteCommand("100"));
	}
	
	@Test(timeout = 1000)
	public void deleteCommand_existingRecord_numOfAffectedRowsReturned() {
		assertEquals(1, commandDAO.deleteCommand("2"));
	}
	
	@Test(timeout = 1000)
	public void deleteCommand_existingRecord_markForDeleteIsTrue() {
		commandDAO.deleteCommand("3");
		assertTrue(commandDAO.findById("3").isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void restoreCommand_existingRecord_markForDeleteIsFalse() {
		commandDAO.deleteCommand("3");
		commandDAO.restoreCommand("3");
		assertFalse(commandDAO.findById("3").isMarkForDelete());
	}	
}