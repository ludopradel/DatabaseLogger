package fr.astek.kata.databaselogger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 
 */
public class Logger {

	private Session session;

	public Logger(Session pSession) {
		this.session = pSession;
	}


	public boolean isDebug() {

		Criteria criteria = this.session.createCriteria(LoggerConfig.class);

		LoggerConfig loggerConfigDAO = (LoggerConfig) criteria.uniqueResult();

		return loggerConfigDAO.getLevel().equals("DEBUG");
	}
	
	
	public void setLevel(String pLevel) {
		Transaction transaction = this.session.beginTransaction();
					
		Criteria criteria = this.session.createCriteria(LoggerConfig.class);

		LoggerConfig loggerConfig = (LoggerConfig) criteria.uniqueResult();

		loggerConfig.setLevel(pLevel);
						
		this.session.update(loggerConfig);
		
		transaction.commit();			
	}
	
}
