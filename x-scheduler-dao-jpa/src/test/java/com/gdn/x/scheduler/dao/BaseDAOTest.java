package com.gdn.x.scheduler.dao;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.ConfigurationTesting;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * 
 * @author yauritux
 *
 */
@ContextConfiguration(classes = { ConfigurationTesting.class })
@TestExecutionListeners(listeners = { //
DependencyInjectionTestExecutionListener.class, //
		DirtiesContextTestExecutionListener.class, //
		TransactionalTestExecutionListener.class, //
})
@Transactional(readOnly = false)
public class BaseDAOTest {

	public static final Operation DELETE_ALL = deleteAllFrom("workflow_schedule", "workflow", "task", "command");

	public static final Operation INSERT_COMMAND =
		insertInto("COMMAND")
			.columns("ID", "COMMAND", "PARAMETERS", "CONTENTS", "COMMAND_TYPE", "CREATED_BY", "CREATED_DATE", "STORE_ID", "MARK_FOR_DELETE", "OPTLOCK")
			.values("1", "{\"url\":\"http://www.google.com\",\"method\":\"GET\"}", "", "", "WEB_SERVICE", "yauritux", "2015-01-01", "store-123", false, 0)
			.values("2", "{\"class\":\"OrderManagementService\",\"method\":\"createOrder\"}", "1,2015-01-01,100000,100000,CASH", "", "CLIENT_SDK", "yauritux", "2015-01-01", "store-123", false, 0)
			.values("3", "{\"scriptName\":\"cleaner.sh\", \"type\":\"shell-script\", \"path\": \".\"}", "", "", "COMMAND_SCRIPT", "yauritux", "2015-01-01", "store-123", false, 0)
			.build();
	
	public static final Operation INSERT_TASK = 
        insertInto("TASK")
            .columns("ID", "TASK_NAME", "COMMAND_ID", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK")
            .values("1", "task1", "1", "yauritux", "2015-01-01","store-123",false,0)
            .values("2", "task2", "2", "yauritux", "2015-01-02","store-123",false,0)
            .values("3", "task3", "3", "yauritux", "2015-01-03","store-123",false,0)
            .values("4", "task4", "3", "yauritux", "2015-01-04","store-123",false,0)
            .values("5", "task5", "2", "yauritux", "2015-01-05","store-123",false,0)
            .values("6", "task6", "1", "yauritux", "2015-01-06","store-123",false,0)
            .build();
	
	public static final Operation INSERT_WORKFLOW =
        insertInto("WORKFLOW")
            .columns("ID", "WORKFLOW_NAME", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK")
            .values("1", "Workflow-1", "yauritux", "2015-03-01","store-123",false,0)
            .build();
	
	public static final Operation INSERT_WORKFLOW_SCHEDULE = 
		insertInto("WORKFLOW_SCHEDULE")
			.columns("ID", "WORKFLOW_ID", "MINUTES", "HOURS", "DAY_OF_MONTH", "MONTHS", "DAY_OF_WEEK", 
					 "CREATED_BY", "CREATED_DATE", "STORE_ID", "MARK_FOR_DELETE", "OPTLOCK")
			.values("1", "1", 60, 2, 0, 0, 0, "yauritux", "2015-03-01", "store-123", false, 0)
			.build();
	
	/*
	public static final Operation INSERT_WORKFLOW_TASKS = 
		insertInto("WORKFLOW_TASKS")
			.columns("WORKFLOW_ID", "TASK_ID")
			.values("1", "1")
			.values("1", "2")
			.build();
	*/
	
	@Autowired
	private DataSource dataSource;

	@Before
	public void prepare() {
		Operation operation = sequenceOf(DELETE_ALL, INSERT_COMMAND, INSERT_TASK, INSERT_WORKFLOW, INSERT_WORKFLOW_SCHEDULE);
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
		dbSetup.launch();
	}
}