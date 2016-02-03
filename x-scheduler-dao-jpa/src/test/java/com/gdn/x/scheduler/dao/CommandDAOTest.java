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
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
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
		commandDAO.deleteCommand("2");
		assertNull(commandDAO.findByIdExclDelete("2"));
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_nonExistingRecord_noRecordFound() {
		assertNull(commandDAO.findByIdExclDelete("3"));
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_existingRecord_recordFound() {
		assertNotNull(commandDAO.findByIdExclDelete("1"));
	}
	
	@Test(timeout = 1000)
	public void count_inclusiveMarkForDelete_2RecordsFound() {
		assertTrue(commandDAO.count() == 2);
	}
	
	@Test
	public void countExclDelete_2RecordsDeleted_0RecordFound() {
		commandDAO.deleteCommand("2");
		commandDAO.deleteCommand("1");
		assertTrue(commandDAO.countExclDelete() == 0);
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_noRecordsDeleted_2RecordsFound() {
		assertTrue(commandDAO.countExclDelete() == 2);
	}
	
	@Test(timeout = 1000)
	public void fetchAll_noRecordsDeleted_2RecordsFound() {
		assertTrue(commandDAO.fetchAll().size() == 2);
	}
	
	@Test(timeout = 1000)
	public void fetchAll_1RecordDeleted_1RecordsFound() {
		commandDAO.deleteCommand("1");
		assertEquals(1, commandDAO.fetchAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_1RecordDeleted_2RecordsFound() {
		commandDAO.deleteCommand("1");
		assertEquals(2, commandDAO.findAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_noRecordsDeleted_2RecordsFound() {
		assertEquals(2, commandDAO.findAll().size());
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
		commandDAO.deleteCommand("2");
		assertTrue(commandDAO.findById("2").isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void restoreCommand_existingRecord_markForDeleteIsFalse() {
		commandDAO.deleteCommand("2");
		commandDAO.restoreCommand("2");
		assertFalse(commandDAO.findById("2").isMarkForDelete());
	}	
	
	@Test(timeout = 1000)
	public void exists_idExists_trueIsReturned() {
		assertTrue(commandDAO.exists("1"));
	}
	
	@Test(timeout = 1000)
	public void exists_existingRecordTemporaryDeleted_recordStillExists() {
		commandDAO.deleteCommand("2");
		assertTrue(commandDAO.exists("2"));
	}
}
