package fr.astek.kata.databaselogger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Logger {

	private Session session;

	public Logger(Session pSession) {
		this.session = pSession;
	}

	public boolean isDebug() {
		Criteria criteria = this.session.createCriteria(LoggerConfig.class);
		LoggerConfig loggerConfigDAO = (LoggerConfig) criteria.uniqueResult();
		return "DEBUG".equals(loggerConfigDAO.getLevel());
	}
	
	public void setLevel(String level) {
		Transaction transaction = this.session.beginTransaction();
		Criteria criteria = this.session.createCriteria(LoggerConfig.class);
		LoggerConfig loggerConfig = (LoggerConfig) criteria.uniqueResult();
		loggerConfig.setLevel(level);
		this.session.update(loggerConfig);
		transaction.commit();			
	}
	
}
