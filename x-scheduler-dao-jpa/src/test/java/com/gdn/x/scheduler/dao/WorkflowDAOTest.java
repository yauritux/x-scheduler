package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
	
	public void findById_existingId_notNullRecord() {
		Workflow workflow = workflowDAO.findById("1");
		assertNotNull(workflow);
	}
	
	@Test
	public void findById_idNotExist_nullRecord() {
		Workflow workflow = workflowDAO.findById("5");
		assertNull(workflow);
	}
}