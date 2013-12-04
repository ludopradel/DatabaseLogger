package fr.astek.kata.databaselogger;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 */
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

		try {
			IDataSet databaseDataSet = getConnection().createDataSet();
			QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
			queryDataSet.addTable("LOGGER_CONFIG", "SELECT 1 as id, 'WARNING' as level FROM DUAL");

			Assertion.assertEquals(queryDataSet, databaseDataSet);
		
		} catch (Exception e) {
			e.printStackTrace();
		} 

		assertFalse(logger.isDebug());
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		QueryDataSet queryDataSet = new QueryDataSet(super.getConnection());
		queryDataSet.addTable("LOGGER_CONFIG", "SELECT 1 as id, 'DEBUG' as level FROM DUAL");
		return queryDataSet;
	}
}
