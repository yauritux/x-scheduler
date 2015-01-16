package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
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
 * @author yauritux
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskDAOTest extends BaseDAOTest {

	@Autowired
	private TaskDAO taskDAO;
	
	@Test
	public void findById_existingId_notNullRecord() {
		Task task = taskDAO.findById("1");
		assertNotNull(task);
	}
	
	@Test
	public void findById_idNotExist_nullRecord() {
		Task task = taskDAO.findById("100");
		assertNull(task);
	}
	
	@Test
	public void findById_id1_taskNameEqualstask1() {
		Task task = taskDAO.findById("1");
		assertTrue(task.getTaskName().equalsIgnoreCase("task1"));
	}
	
	@Test
	public void fetchAll_dataExist_recordNotEmpty() {
		assertNotNull(taskDAO.fetchAll());
	}
	
	@Test
	public void deleteTask_recordExist_oneRecordDeleted() {
		assertEquals(1, taskDAO.deleteTask("1"));
	}
	
	@Test
	public void deleteTask_success_markForDeleteIsTrue() {
		taskDAO.deleteTask("6");
		Task task = taskDAO.findById("6");
		assertTrue(task.isMarkForDelete());
	}
	
	@Test
	public void deleteTask_recordNotExist_noRecordDeleted() {
		assertEquals(0, taskDAO.deleteTask("100"));
	}
	
	@Test
	public void findByTaskName_oneRecordExist_oneRecordReturned() {
		List<Task> task = taskDAO.findByTaskName("task1");
		assertEquals(1, task.size());
	}
	
	@Test
	public void findByTaskName_severalRecordFound_moreThanOneRecordReturned() {
		List<Task> task = taskDAO.findByTaskNameLike("%task%");
		assertTrue(task.size() > 1);
	}
}