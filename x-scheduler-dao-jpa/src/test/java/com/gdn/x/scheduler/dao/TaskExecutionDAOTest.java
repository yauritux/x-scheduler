package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdn.x.scheduler.model.TaskExecution;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskExecutionDAOTest extends BaseDAOTest {

	@Autowired
	private TaskExecutionDAO taskExecutionDAO;
	
	@Test(timeout = 1000)
	public void findById_existingId_recordIsNotNull() {
		assertNotNull(taskExecutionDAO.findById("1"));
	}
	
	@Test(timeout = 1000)
	public void findRunningTask_taskIdTwoIsStillRunning_oneRecordFound() {
		assertTrue(taskExecutionDAO.findRunningTask("2").size() == 1);
	}
	
	@Test(timeout = 1000)
	public void findRunningTask_taskIdOneAlreadyFinished_noRunningTaskFound() {
		assertEquals(0, taskExecutionDAO.findRunningTask("1").size());
	}		
	
	@Test(timeout = 1000)
	public void findObsoleteTaskExecutions_greaterThanOneMonthIsConsideredAsObsolete_oneRecordFound() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 2, 25);
		calendar.add(Calendar.MONTH, -1); 
		Date dateToLookBackAfter = calendar.getTime();
		List<TaskExecution> list = taskExecutionDAO.findObsoleteTaskExecutions(dateToLookBackAfter);
		assertEquals(1, list.size());
	}
	
	@Test(timeout = 1000)
	public void deleteObsoleteTaskExecutions_greaterThanOneMonthIsConsideredAsObsolete_oneRecordDeleted() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 2, 25);
		calendar.add(Calendar.MONTH, -1);
		Date dateToLookBackAfter = calendar.getTime();
		int affectedRows = taskExecutionDAO.deleteObsoleteTaskExecutions(dateToLookBackAfter);
		assertEquals(1, affectedRows);
	}
}