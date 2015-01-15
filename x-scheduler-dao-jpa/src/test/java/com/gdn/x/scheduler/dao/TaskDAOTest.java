package com.gdn.x.scheduler.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
	public void findByTaskId_existingTaskId_notNullRecord() {
		Task task = taskDAO.findByTaskId(1L);
		assertNotNull(task);
	}
	
	@Test
	public void findByTaskId_taskIdNotExist_nullRecord() {
		Task task = taskDAO.findByTaskId(5L);
		assertNull(task);
	}
	
	@Test
	public void findByTaskId_taskId1_taskNameEqualstask1() {
		Task task = taskDAO.findByTaskId(1L);
		assertTrue(task.getTaskName().equalsIgnoreCase("task1"));
	}
}