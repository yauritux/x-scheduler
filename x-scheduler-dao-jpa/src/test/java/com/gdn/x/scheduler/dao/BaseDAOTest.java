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

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.ProcessStatus;
import com.gdn.x.scheduler.constant.ThreadState;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
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
	
	private static final String URL = "http://www.google.com";

	public static final Operation DELETE_ALL = deleteAllFrom("task_execution", "task", "command");

	public static final Operation INSERT_COMMAND =
		insertInto("COMMAND")
			.columns("ID", "COMMAND", "PARAMETERS", "CONTENTS", "ENTRY_POINT", "COMMAND_TYPE", "CREATED_BY", "CREATED_DATE", "STORE_ID", "MARK_FOR_DELETE", "OPTLOCK")
			.values("1", "{\"" + WSRequestHeader.URL.label() + "\":\"" + URL + "\",\"" + WSRequestHeader.METHOD.label() + "\":\""+ WSMethod.GET.name() + "\"}", "", "", "", CommandType.WEB_SERVICE.name(), "yauritux", "2015-01-01", "store-123", false, 0)
			.values("2", "{\"class\":\"OrderManagementService\",\"method\":\"createOrder\"}", "1,2015-01-01,100000,100000,CASH", "", "", CommandType.CLIENT_SDK.name(), "yauritux", "2015-01-01", "store-123", false, 0)
			.build();
	
	public static final Operation INSERT_TASK = 
        insertInto("TASK")
            .columns("ID", "TASK_NAME", "COMMAND_ID", "SECS", "MINS", "HRS", "DAY_OF_MONTH", "MTH", "DAY_OF_WEEK", "YR", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK", "START_DATE", "EXPIRY_DATE", "PROCESS_STATE", "MACHINE_ID", "PRIORITY")
            .values("1", "task1", "1", "0/55", "*", "*", "*", "*", "*", null, "yauritux", "2015-01-01", "store-123", false, 0, "2015-01-01", "2015-01-05", ThreadState.ACTIVE, "test-machine", 1)
            .values("2", "task2", "2", "0", "0", "12", "*", "*", "?", null, "yauritux", "2015-01-02", "store-123", false, 0, "2015-01-02", null, ThreadState.ACTIVE, "test-machine", 2)
            .values("3", "task3", "2", "0", "15", "10", "?", "*", "6L", "2015-2017", "yauritux", "2015-01-05", "store-123", false, 0, "2015-01-05", null, ThreadState.ACTIVE, "test-machine", 3)
            .values("4", "task4", "1", "0", "15", "10", "?", "*", "6#3", null, "yauritux", "2015-01-06", "store-123", false, 0, "2015-01-06", null, ThreadState.ACTIVE, "test-machine", 4)
            .build();
	
	public static final Operation INSERT_TASK_EXECUTION = 
		insertInto("TASK_EXECUTION")
			.columns("ID", "CREATED_BY", "CREATED_DATE", "MARK_FOR_DELETE", "STORE_ID", "UPDATED_BY", "UPDATED_DATE", "OPTLOCK", "END_TIME", "START_TIME", "STATUS", "TASK_ID", "MACHINE_ID")
			.values("1", "yauritux", "2015-01-02 00:00:00", false, "store-123", "yauritux", "2015-01-02 00:00:00", 0, null, "2015-01-02 00:00:00", ProcessStatus.IN_PROGRESS.name(), "2", "test-machine")
			.values("2", "yauritux", "2015-01-05 00:00:00", false, "store-123", "yauritux", "2015-01-05 00:00:00", 0, "2015-01-05 01:30:00", "2015-01-05 00:00:00", ProcessStatus.FINISHED, "3", "test-machine")
			.values("3", "yauritux", "2015-03-01 00:00:00", false, "store-123", "yauritux", "2015-03-01 00:00:00", 0, "2015-03-01 01:30:00", "2015-03-01 00:00:00", ProcessStatus.FINISHED, "3", "test-machine")
			.build();
		
	@Autowired
	private DataSource dataSource;

	@Before
	public void prepare() {
		Operation operation = sequenceOf(DELETE_ALL, INSERT_COMMAND, INSERT_TASK, INSERT_TASK_EXECUTION);
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
		dbSetup.launch();
	}
}