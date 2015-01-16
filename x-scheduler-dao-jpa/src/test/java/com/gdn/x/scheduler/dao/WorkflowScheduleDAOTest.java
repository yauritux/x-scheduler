package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdn.x.scheduler.model.WorkflowSchedule;

/**
 * 
 * @author yauritux
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowScheduleDAOTest extends BaseDAOTest {

	@Autowired
	private WorkflowScheduleDAO workflowScheduleDAO;
	
	@Test
	public void findById_nonExistingId_nullRecord() {
		WorkflowSchedule workflowSchedule = workflowScheduleDAO.findById("100");
		assertNull(workflowSchedule);
	}
	
	@Test
	public void findById_existingId_notNullRecord() {
		WorkflowSchedule workflowSchedule = workflowScheduleDAO.findById("1");
		assertNotNull(workflowSchedule);
	}
}