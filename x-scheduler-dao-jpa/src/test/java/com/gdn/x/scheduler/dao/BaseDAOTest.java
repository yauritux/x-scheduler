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

	public static final Operation DELETE_ALL = deleteAllFrom("task", "command");

	public static final Operation INSERT_COMMAND =
		insertInto("COMMAND")
			.columns("ID", "COMMAND", "PARAMETERS", "CONTENTS", "ENTRY_POINT", "COMMAND_TYPE", "CREATED_BY", "CREATED_DATE", "STORE_ID", "MARK_FOR_DELETE", "OPTLOCK")
			.values("1", "{\"" + WSRequestHeader.URL.label() + "\":\"" + URL + "\",\"" + WSRequestHeader.METHOD.label() + "\":\""+ WSMethod.GET.name() + "\"}", "", "", "", CommandType.WEB_SERVICE.name(), "yauritux", "2015-01-01", "store-123", false, 0)
			.values("2", "{\"class\":\"OrderManagementService\",\"method\":\"createOrder\"}", "1,2015-01-01,100000,100000,CASH", "", "", CommandType.CLIENT_SDK.name(), "yauritux", "2015-01-01", "store-123", false, 0)
			.values("3", "{\"scriptName\":\"cleaner.sh\", \"type\":\"shell-script\", \"path\": \".\"}", "", "", "", CommandType.SHELL_SCRIPT.name(), "yauritux", "2015-01-01", "store-123", false, 0)
			.build();
	
	public static final Operation INSERT_TASK = 
        insertInto("TASK")
            //.columns("ID", "TASK_NAME", "COMMAND_ID", "SECS", "MINS", "HRS", "DAY_OF_MONTH", "MTH", "DAY_OF_WEEK", "YR", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK", "START_DATE", "PRIORITY", "PROCESS_STATE", "MACHINE_ID")
        	.columns("ID", "TASK_NAME", "COMMAND_ID", "SECS", "MINS", "HRS", "DAY_OF_MONTH", "MTH", "DAY_OF_WEEK", "YR", "CREATED_BY", "CREATED_DATE","STORE_ID","MARK_FOR_DELETE","OPTLOCK", "START_DATE", "EXPIRY_DATE")        
            .values("1", "task1", "1", "0/55", "*", "*", "*", "*", "*", null, "yauritux", "2015-01-01", "store-123", false, 0, "2015-01-01", null)
            .values("2", "task2", "2", "0", "0", "12", "*", "*", "?", null, "yauritux", "2015-01-02", "store-123", false, 0, "2015-01-02", null)
            .values("3", "task3", "3", "0", "15", "10", "*", "*", "?", "2017", "yauritux", "2015-01-03", "store-123", false, 0, "2015-01-03", null)
            .values("4", "task4", "3", "0", "10,44", "14", "?", "3", "WED", null, "yauritux", "2015-01-04", "store-123", false, 0, "2015-01-04", null)
            .values("5", "task5", "2", "0", "15", "10", "?", "*", "6L", "2015-2017", "yauritux", "2015-01-05", "store-123", false, 0, "2015-01-05", null)
            .values("6", "task6", "1", "0", "15", "10", "?", "*", "6#3", null, "yauritux", "2015-01-06", "store-123", false, 0, "2015-01-06", null)
            .build();
		
	@Autowired
	private DataSource dataSource;

	@Before
	public void prepare() {
		Operation operation = sequenceOf(DELETE_ALL, INSERT_COMMAND, INSERT_TASK);
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
		dbSetup.launch();
	}
}