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

import com.gdn.x.scheduler.model.Workflow;
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
	@Autowired
	private WorkflowDAO workflowDAO;
	
	@Test(timeout = 1000)
	public void findById_nonExistingId_nullRecord() {
		WorkflowSchedule workflowSchedule = workflowScheduleDAO.findById("100");
		assertNull(workflowSchedule);
	}
	
	@Test(timeout = 1000)
	public void findById_existingId_notNullRecord() {
		WorkflowSchedule workflowSchedule = workflowScheduleDAO.findById("1");
		assertNotNull(workflowSchedule);
	}
	
	@Test(timeout = 1000)
	public void findByIdExcl_findRecordDeleted_noRecordFound() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertNull(workflowScheduleDAO.findByIdExcl("1"));
	}
	
	@Test(timeout = 1000)
	public void findByIdExcl_recordNotDeleted_recordFound() {
		assertNotNull(workflowScheduleDAO.findByIdExcl("1"));
	}
	
	@Test(timeout = 1000)
	public void exists_recordDeleted_recordStillExist() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertTrue(workflowScheduleDAO.exists("1"));
	}
	
	@Test(timeout = 1000)
	public void fetchAll_recordDeleted_noRecordFound() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertTrue(workflowScheduleDAO.fetchAll().isEmpty());
	}
	
	@Test(timeout = 1000)
	public void fetchAll_noRecordDeleted_recordFound() {
		assertEquals(1, workflowScheduleDAO.fetchAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_recordDeleted_recordFound() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertEquals(1, workflowScheduleDAO.findAll().size());
	}
	
	@Test(timeout = 1000)
	public void findAll_noRecordDelete_recordFound() {
		assertEquals(1, workflowScheduleDAO.findAll().size());
	}
	
	@Test(timeout = 1000)
	public void findByWorkflow_existingWorkflow_recordNotEmpty() {
		Workflow workflow = workflowDAO.findById("1");
		List<WorkflowSchedule> workflowSchedules = workflowScheduleDAO.findByWorkflow(workflow);
		assertNotNull(workflowSchedules.get(0));
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_recordNotExist_noAffectedRows() {
		assertEquals(0, workflowScheduleDAO.deleteWorkflowSchedule("100"));
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_recordExist_numOfAffectedRowsReturned() {
		assertEquals(1, workflowScheduleDAO.deleteWorkflowSchedule("1"));
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_recordExist_markForDeleteIsTrue() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertTrue(workflowScheduleDAO.findById("1").isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void restoreWorkflow_recordNotExist_noAffectedRows() {
		assertEquals(0, workflowScheduleDAO.restoreWorkflowSchedule("100"));
	}
	
	@Test(timeout = 1000)
	public void restoreWorkflow_recordExist_markForDeleteIsFalse() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		workflowScheduleDAO.restoreWorkflowSchedule("1");
		assertFalse(workflowScheduleDAO.findById("1").isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void restoreWorkflow_recordExist_numOfAffectedRowsReturned() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertEquals(1, workflowScheduleDAO.restoreWorkflowSchedule("1"));
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_exclusiveDeleted_emptyResult() {
		workflowScheduleDAO.deleteWorkflowSchedule("1");
		assertEquals(0, workflowScheduleDAO.countExclDelete());
	}
}