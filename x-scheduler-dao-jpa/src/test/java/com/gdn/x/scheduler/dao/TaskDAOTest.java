package com.gdn.x.scheduler.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdn.x.scheduler.constant.ThreadState;
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
		taskDAO.deleteTask("4");
		Task task = taskDAO.findById("4");
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
	public void findByTaskNameLike_severalRecordFound_moreThanOneRecordReturned() {
		List<Task> task = taskDAO.findByTaskNameLike("%task%");
		assertTrue(task.size() > 1);
	}
	
	@Test(timeout = 1000)
	public void restoreTask_success_markForDeleteIsFalse() {
		taskDAO.deleteTask("3");
		taskDAO.restoreTask("3");
		Task task = taskDAO.findById("3");		
		assertFalse(task.isMarkForDelete());
	}
	
	@Test(timeout = 1000)
	public void findAll_success_fetchAllRecordsInclusiveMarkForDelete() {
		taskDAO.deleteTask("2");
		assertTrue(taskDAO.findAll().size() == 4);
	}
	
	@Test(timeout = 1000)
	public void exists_existingId_trueIsReturned() {
		taskDAO.deleteTask("3");
		assertTrue(taskDAO.exists("3"));
	}
	
	@Test(timeout = 1000)
	public void exists_nonExistingId_falseIsReturned() {
		assertFalse(taskDAO.exists("100"));
	}
	
	@Test(timeout = 1000)
	public void count_inclusiveMarkForDelete_4RecordsReturned() {
		taskDAO.deleteTask("3");
		assertTrue(taskDAO.count() == 4);
	}
	
	@Test(timeout = 1000)
	public void countExclDelete_2RecordsDeleted_2RecordsReturned() {
		taskDAO.deleteTask("3");
		taskDAO.deleteTask("4");
		assertTrue(taskDAO.countExclDelete() == 2);
	}
	
	@Test(timeout = 1000)
	public void updateTaskState_updateToScheduled_taskStateChangedToScheduled() {
		taskDAO.updateTaskState(ThreadState.SCHEDULED, "1");
		Task task = taskDAO.findById("1");
		assertEquals(ThreadState.SCHEDULED, task.getState());
	}
	
	@Test(timeout = 1000)
	public void updateTaskMachineId_setMachineIdIntoTux_machineIdChangedToTux() {
		taskDAO.updateTaskMachineId("TUX", "2");
		Task task = taskDAO.findById("2");
		assertEquals("TUX", task.getMachineId());
	}
	
	@Test(timeout = 1000)
	public void deleteExpiredTasks_noTaskWithExpiryDateLessThanProvidedDate_noRecordsDeleted() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.YEAR, 2013);
		int affected = taskDAO.deleteExpiredTasks(cal.getTime());
		assertTrue(affected == 0);
	}
	
	@Test(timeout = 1000)
	public void deleteExpiredTasks_oneTaskWithExpiryDateLessThanProvidedDate_oneRecordDeleted() {
		assertTrue(taskDAO.deleteExpiredTasks(new Date()) == 1);
	}
}