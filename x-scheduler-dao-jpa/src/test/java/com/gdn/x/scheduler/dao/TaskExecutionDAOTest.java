package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}