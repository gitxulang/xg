package iie.tools;

import java.sql.Timestamp;

public class RoleFunctionBean {
	private int id;
	private String role;
	private String functions;
	private Timestamp itime;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	public Timestamp getItime() {
		return itime;
	}

	public void setItime(Timestamp itime) {
		this.itime = itime;
	}	
}
