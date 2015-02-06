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

import com.gdn.x.scheduler.model.Task;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskDAOTest extends BaseDAOTest {

	@Autowired
	private TaskDAO taskDAO;
	
	@Test(timeout = 1000)
	public void findById_existingId_notNullRecord() {
		Task task = taskDAO.findById("1");
		assertNotNull(task);
	}
	
	@Test(timeout = 1000)
	public void findById_idNotExist_nullRecord() {
		Task task = taskDAO.findById("100");
		assertNull(task);
	}
	
	@Test(timeout = 1000)
	public void findById_id1_taskNameEqualstask1() {
		Task task = taskDAO.findById("1");
		assertTrue(task.getTaskName().equalsIgnoreCase("task1"));
	}
	
	@Test(timeout = 1000)
	public void fetchAll_dataExist_recordNotEmpty() {
		assertNotNull(taskDAO.fetchAll());
	}
	
	@Test(timeout = 1000)
	public void deleteTask_recordExist_oneRecordDeleted() {
		assertEquals(1, taskDAO.deleteTask("1"));
	}
	
	@Test(timeout = 1000)
	public void deleteTask_success_markForDeleteIsTrue() {
		taskDAO.deleteTask("6");
		Task task = taskDAO.findById("6");
		assertTrue(task.isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_recordIsDeleted_recordNotFound() {
		taskDAO.deleteTask("6");
		assertNull(taskDAO.findByIdExclDelete("6"));
	}
	
	@Test(timeout = 1000)
	public void deleteTask_recordNotExist_noRecordDeleted() {
		assertEquals(0, taskDAO.deleteTask("100"));
	}
	
	@Test(timeout = 1000)
	public void findByTaskName_oneRecordExist_oneRecordReturned() {
		List<Task> task = taskDAO.findByTaskName("task1");
		assertEquals(1, task.size());
	}
	
	@Test(timeout = 1000)
	public void findByTaskName_severalRecordFound_moreThanOneRecordReturned() {
		List<Task> task = taskDAO.findByTaskNameLike("%task%");
		assertTrue(task.size() > 1);
	}
	
	@Test(timeout = 1000)
	public void restoreTask_success_markForDeleteIsFalse() {
		taskDAO.deleteTask("5");
		taskDAO.restoreTask("5");
		Task task = taskDAO.findById("5");		
		assertFalse(task.isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void findAll_success_fetchAllRecordsInclusiveMarkForDelete() {
		taskDAO.deleteTask("6");
		assertTrue(taskDAO.findAll().size() == 6);
	}
	
	@Test(timeout = 1000)
	public void exists_existingId_trueIsReturned() {
		taskDAO.deleteTask("5");
		assertTrue(taskDAO.exists("5"));
	}
	
	@Test(timeout = 1000)
	public void exists_nonExistingId_falseIsReturned() {
		assertFalse(taskDAO.exists("100"));
	}
	
	@Test(timeout = 1000)
	public void count_inclusiveMarkForDelete_6RecordsReturned() {
		taskDAO.deleteTask("5");
		assertTrue(taskDAO.count() == 6);
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_2RecordsDeleted_4RecordsReturned() {
		taskDAO.deleteTask("5");
		taskDAO.deleteTask("6");
		assertTrue(taskDAO.countExclDelete() == 4);
	}
}