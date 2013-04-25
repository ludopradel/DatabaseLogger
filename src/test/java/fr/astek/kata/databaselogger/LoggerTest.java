package fr.astek.kata.databaselogger;

import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoggerTest extends DBTestCase {

	private Session session;

	@Override
	@Before
	protected void setUp() throws Exception {
		session = TestConfiguration.openSession();
		super.setUp();
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		session.close();
	}

	@Test
	public void testLoggerIsDebug() {
		Logger logger = new Logger(session);
		assertTrue(logger.isDebug());
	}

	@Test
	public void testLoggersetLevelWarning() {
		Logger logger = new Logger(session);
		logger.setLevel("WARNING");

		// Fetch database data after executing your code

		try {
			IDataSet databaseDataSet = getConnection().createDataSet();
			QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
			queryDataSet.addTable("LOGGER_CONFIG",
					"SELECT 1 as id, 'WARNING' as level FROM DUAL");
			ITable expectedTable = queryDataSet.getTable("LOGGER_CONFIG");
			Assertion.assertEquals(expectedTable, databaseDataSet.getTable("LOGGER_CONFIG"));
		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} 

		assertFalse(logger.isDebug());
	}

	
	@Test
	public void testDebugMessage() {
		Logger logger = new Logger(session);
		logger.debug("Debug Message");
		
		
		IDataSet actualDataSet;
		ITable actualTable;
		try {
			actualDataSet = getConnection().createDataSet();
			actualTable = actualDataSet.getTable("LOG_TRACE");
			QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
			queryDataSet.addTable("LOG_TRACE",
					"SELECT 1 as id, 'Debug Message' as message FROM DUAL");
			ITable expectedTable = queryDataSet.getTable("LOG_TRACE");
			Assertion.assertEquals(expectedTable, actualTable);
			
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testWarningModeWithMessage() {
		Logger logger = new Logger(session);
		logger.setLevel("WARNING");
		logger.debug("Debug Message");
		
		IDataSet actualDataSet;
		ITable actualTable;
		try {
			actualDataSet = getConnection().createDataSet();
			actualTable = actualDataSet.getTable("LOG_TRACE");
			QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
			queryDataSet.addTable("LOG_TRACE", "select 1 from dual where 1=2");
			ITable expectedTable = queryDataSet.getTable("LOG_TRACE");
			Assertion.assertEquals(expectedTable, actualTable);
			
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDebugModeWithMessage() {
		Logger logger = new Logger(session);
		logger.setLevel("DEBUG");
		logger.debug("Debug Message");
		logger.warn("Warn Message");
		
		IDataSet actualDataSet;
		ITable actualTable;
		try {
			actualDataSet = getConnection().createDataSet();
			actualTable = actualDataSet.getTable("LOG_TRACE");
			
			DefaultDataSet defaultDataSet = new DefaultDataSet();
			DefaultTable logTraceTable = new DefaultTable("LOG_TRACE", new Column[] {new Column("id", DataType.INTEGER), new Column("message", DataType.VARCHAR)});
			defaultDataSet.addTable(logTraceTable);
			logTraceTable.addRow(new Object[] {new Integer(1), new String("Debug Message")});
			logTraceTable.addRow(new Object[] {new Integer(2), new String("Warn Message")});
			
			ITable expectedTable = defaultDataSet.getTable("LOG_TRACE");
			Assertion.assertEquals(expectedTable, actualTable);
			
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
		queryDataSet.addTable("LOGGER_CONFIG",
				"SELECT 1 as id, 'DEBUG' as level FROM DUAL");
		queryDataSet.addTable("LOG_TRACE");
		return queryDataSet;
	}
}
