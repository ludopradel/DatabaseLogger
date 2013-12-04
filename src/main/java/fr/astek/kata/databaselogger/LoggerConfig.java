package fr.astek.kata.databaselogger;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "LOGGER_CONFIG")
public class LoggerConfig implements Serializable {
	
	private static final long serialVersionUID = -5938936493292971717L;

	private int id;
	private String level;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Column
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}

}
