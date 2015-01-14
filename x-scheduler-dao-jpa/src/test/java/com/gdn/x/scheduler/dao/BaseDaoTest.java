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
public class BaseDaoTest {

	public static final Operation DELETE_ALL = deleteAllFrom("command", "task", "workflow");

	public static final Operation INSERT_COMMAND_DATA =
		insertInto("COMMAND")
			.columns("ID", "COMMAND", "PARAMETERS", "COMMAND_TYPE", "CREATED_BY", "CREATED_DATE", "STORE_ID", "MARK_FOR_DELETE", "OPTLOCK")
			.values("1", "GET http://localhost:8080", "WEB_SERVICE", "yauritux", "2015-01-01", "store-123", false, 0)
			.values("2", "OrderManagementService.createOrder", "API_JAR", "yauritux", "2015-01-01", "store-123", false, 0)
			.build();
	
	public static final Operation INSERT_TASK_DATA = 
        insertInto("TASK")
            .columns("ID", "TASK_NAME", "COMMAND_ID", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK")
            .values("1", "task1", 1, "yauritux", "2014-01-01","store-123",false,0)
            .values("2", "task2", 2, "yauritux", "2014-01-02","store-123",false,0)
            .build();
	
	public static final Operation INSERT_WORKFLOW_DATA =
        insertInto("WORKFLOW")
            .columns("ID", "WORKFLOW_NAME", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK")
            .values("1", "Workflow-1", "yauritux", "2014-03-01","store-123",false,0)
            .build();
	

	@Autowired
	private DataSource dataSource;

	@Before
	public void prepare() {
		Operation operation = sequenceOf(DELETE_ALL, INSERT_COMMAND_DATA, INSERT_TASK_DATA, INSERT_WORKFLOW_DATA);
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
		dbSetup.launch();
	}
}