package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdn.x.scheduler.model.Workflow;

/**
 * 
 * @author yauritux
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowDAOTest extends BaseDAOTest {

	@Autowired
	private WorkflowDAO workflowDAO;
	
	@Test(timeout = 1000)
	public void findById_existingId_notNullRecord() {
		Workflow workflow = workflowDAO.findById("1");
		assertNotNull(workflow);
	}
	
	@Test(timeout = 1000)
	public void findById_idNotExist_nullRecord() {
		Workflow workflow = workflowDAO.findById("5");
		assertNull(workflow);
	}
	
	@Test(timeout = 1000)
	public void findByIdExclDelete_recordIsDeleted_recordNotFound() {
		workflowDAO.deleteWorkflow("2");
		assertNull(workflowDAO.findByIdExclDelete("2"));
	}
	
	@Test(timeout = 1000)
	public void fetchAll_dataExist_recordNotEmpty() {
		assertTrue(workflowDAO.fetchAll().size() > 0);
	}
	
	@Test(timeout = 1000)
	public void fetchAll_exclusiveMarkForDelete_1RecordFound() {
		workflowDAO.deleteWorkflow("2");
		assertTrue(workflowDAO.fetchAll().size() == 1);
	}
	
	@Test(timeout = 1000)
	public void findAll_inclusiveMarkForDelete_2RecordsFound() {
		workflowDAO.deleteWorkflow("2");
		assertTrue(workflowDAO.findAll().size() == 2);
	}
	
	@Test(timeout = 1000)
	public void exists_existingID_trueIsReturned() {
		assertTrue(workflowDAO.exists("1"));
	}
	
	@Test(timeout = 1000)
	public void exists_recordDeleted_recordStillExists() {
		workflowDAO.deleteWorkflow("2");
		assertTrue(workflowDAO.exists("2"));
	}
	
	@Test(timeout = 1000)
	public void count_inclusiveMarkForDelete_2RecordsFound() {
		workflowDAO.deleteWorkflow("2");
		assertTrue(workflowDAO.count() == 2);
	}
	
	@Test(timeout = 1000)
	public void count_exclusiveMarkForDelete_1RecordFound() {
		workflowDAO.deleteWorkflow("2");
		assertTrue(workflowDAO.countExclDelete() == 1);
	}
	
	@Test(timeout = 1000)
	public void findByWorkflowName_nameExist_1RecordFound() {
		assertTrue(workflowDAO.findByWorkflowName("Workflow-1").size() == 1);
	}
	
	@Test(timeout = 1000)
	public void findByWorkflowName_nameDoesNotExist_noRecordFound() {
		assertTrue(workflowDAO.findByWorkflowName("Workflow").size() == 0);
	}
	
	@Test(timeout = 1000)
	public void findByWorkflowNameLike_nameLikeExist_2RecordsFound() {
		assertTrue(workflowDAO.findByWorkflowNameLike("%Workflow%").size() == 2);
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_nonExistingRecord_noAffectedRows() {
		assertEquals(0, workflowDAO.deleteWorkflow("100"));
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_existingRecord_numOfAffectedRowReturned() {
		assertTrue(workflowDAO.deleteWorkflow("1") > 0);
	}
	
	@Test(timeout = 1000)
	public void deleteWorkflow_existingRecord_markForDeleteIsTrue() {
		workflowDAO.deleteWorkflow("1");
		assertTrue(workflowDAO.findById("1").isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void restoreWorkflow_success_markForDeleteIsFalse() {
		workflowDAO.deleteWorkflow("1");
		workflowDAO.restoreWorkflow("1");
		assertFalse(workflowDAO.findById("1").isMarkForDelete());
	}	
}