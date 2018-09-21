package com.erp.pattanasin.db.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.erp.pattanasin.db.common.DbUtil;
import com.erp.pattanasin.db.common.Version;
import com.erp.pattanasin.db.model.Employee;

import java.util.logging.Level;
import java.util.logging.Logger;


public class EmployeeDAO {
	private Connection connection = null;
	
	public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        try {
        	connection = DbUtil.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Employees");
            while (rs.next()) {
            	Employee employee = new Employee();
            	employee.setLoginID(rs.getString("loginID"));
            	employee.setSalt(rs.getString("salt"));
            	employee.setPassword(rs.getString("password"));
            	employee.setActive(rs.getBoolean("active"));     	
            	employee.setFirstName(rs.getString("firstName"));
            	employee.setLastName(rs.getString("lastName"));
            	employee.setEmail(rs.getString("email"));
            	employee.setPhoneNumber(rs.getString("phoneNumber"));
            	
            	//System.out.println(employee.toString());

            	employees.add(employee);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;           

    }
	
	// 'synchronized' for mutual exclusion between multiple creations.
	public synchronized boolean  createOrUpdateEmployee(Employee employee) {     
		boolean returnResult = false;
        try {

        	connection = DbUtil.getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into Employees(loginID, salt, password,  active, firstName, lastName, email, phoneNumber) " +
					"values (?, ?, ?, ?, ?, ?, ?, ?) " +
					"on duplicate key update " +
					"loginID = values(loginID), " +
					"salt = values(salt), " +
					"password = values(password), " +
					"active = values(active), " +						
		    		"firstName = values(firstName), " +
		    		"lastName = values(lastName), " +
		    		"email = values(email), " +
					"phoneNumber = values(phoneNumber)", Statement.RETURN_GENERATED_KEYS);
			
			// Parameters start with 1
			preparedStatement.setString(1, employee.getLoginID());
			preparedStatement.setString(2, employee.getSalt());
			preparedStatement.setString(3, employee.getPassword());
			preparedStatement.setBoolean(4, employee.isActive());
			preparedStatement.setString(5, employee.getFirstName());
			preparedStatement.setString(6, employee.getLastName());
			preparedStatement.setString(7, employee.getEmail());
			preparedStatement.setString(8, employee.getPhoneNumber());

			
			// rowCount = 1 for success insert.  rowCount = 2 for success update
			int rowCount = preparedStatement.executeUpdate();
			System.out.println("createOrUpdateEmployee():  rowCount=" + rowCount);
            if(rowCount == 1 || rowCount == 2)
            {
            	returnResult = true;
      
            }
            
            
            
        }catch (SQLException ex) {
        
            Logger lgr = Logger.getLogger(EmployeeDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
        
        return returnResult;
        
	} 
	
	// 'synchronized' for mutual exclusion between multiple creations.
	public synchronized boolean  createOrUpdateEmployeeRole(Employee employee, String role) {     
		boolean returnResult = false;
        try {

        	connection = DbUtil.getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into Employees_Roles(loginID, role_name) " +
					"values (?, ?) " +
					"on duplicate key update " +
					"loginID = values(loginID), " +
					"role_name = values(role_name) ", Statement.RETURN_GENERATED_KEYS);
			
			// Parameters start with 1
			preparedStatement.setString(1, employee.getLoginID());
			preparedStatement.setString(2, role);

			
			// rowCount = 1 for success insert.  rowCount = 2 for success update
			int rowCount = preparedStatement.executeUpdate();
			System.out.println("createOrUpdateEmployeeRole():  rowCount=" + rowCount);
            if(rowCount == 1 || rowCount == 2)
            {
            	returnResult = true;
      
            }
            
        }catch (SQLException ex) {
            Logger lgr = Logger.getLogger(EmployeeDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return returnResult;
        
	} 
	
//	// 'synchronized' for mutual exclusion between multiple creations.
//		public synchronized boolean  createOrUpdateLoginInfo(Employee employee, String password, String salt) {     
//			boolean returnResult = false;
//	        try {
//
//	        	connection = DbUtil.getConnection();
//				
//				PreparedStatement preparedStatement = connection.prepareStatement(
//						"insert into Employees(loginID, password, salt) " +
//						"values (?, ?) " +
//						"on duplicate key update " +
//						"loginID = values(loginID), " +
//						"password = values(password) " +
//						"salt = values(salt), ", Statement.RETURN_GENERATED_KEYS);
//				
//				// Parameters start with 1
//				preparedStatement.setString(1, employee.getLoginID());
//				preparedStatement.setString(2, password);
//				preparedStatement.setString(3, salt);
//
//				
//				// rowCount = 1 for success insert.  rowCount = 2 for success update
//				int rowCount = preparedStatement.executeUpdate();
//				System.out.println("createOrUpdateLoginInfo():  rowCount=" + rowCount);
//	            if(rowCount == 1 || rowCount == 2)
//	            {
//	            	returnResult = true;
//	      
//	            }
//	            
//	        }catch (SQLException ex) {
//	            Logger lgr = Logger.getLogger(EmployeeDAO.class.getName());
//	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
//	        }
//	        return returnResult;
//	        
//		} 
	
	public Employee getEmployeeInfoWithLoginID(String empLoginID) {
		Employee employee =  new Employee();
		try {
        	connection = DbUtil.getConnection();      	
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select * from Employees" +
					" WHERE loginID = ?"
			);
			preparedStatement.setString(1, empLoginID);
			ResultSet rs = preparedStatement.executeQuery();

			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
            	employee.setLoginID(rs.getString("loginID"));
            	employee.setSalt(rs.getString("salt"));
            	employee.setPassword(rs.getString("password"));
            	employee.setActive(rs.getBoolean("active"));     	
            	employee.setFirstName(rs.getString("firstName"));
            	employee.setLastName(rs.getString("lastName"));
            	employee.setEmail(rs.getString("email"));
            	employee.setPhoneNumber(rs.getString("phoneNumber"));
				System.out.println(employee.toString());
			}
			if (rowCount == 0) {
				// The loginID is not in DB.
				employee = null;
			}
		} catch (SQLException ex) {
        	System.out.println("getEmployeeInfoWithLoginID():  ERROR SQLException  error_message=" + ex.getMessage());
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}		
		return employee;
	}	
	
	public String getEmployeeRole(String empLoginID) {
		String empRole = "";
		try {
        	connection = DbUtil.getConnection();      	
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select * from Employees_Roles" +
					" WHERE loginID = ?"
			);
			preparedStatement.setString(1, empLoginID);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				empRole = rs.getString("role_name");
			}
			
		} catch (SQLException ex) {
        	System.out.println("getEmployeeRole():  ERROR SQLException  error_message=" + ex.getMessage());
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		
		return empRole;
	}

}


