package com.erp.pattanasin.db.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="employeewrapper")
public class EmployeeWrapper {

	private List<Employee> employeeList;

	public EmployeeWrapper() {
	}

	public List<Employee> getList() {
		return employeeList;
	}

	public void setList(List<Employee> list) {
		this.employeeList = list;
	}
	
}
