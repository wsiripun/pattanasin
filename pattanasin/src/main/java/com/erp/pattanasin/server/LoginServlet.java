package com.erp.pattanasin.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.erp.pattanasin.db.dao.EmployeeDAO;
import com.erp.pattanasin.db.model.Employee;
import com.erp.pattanasin.common.Password;

import com.erp.pattanasin.common.errormsg.*;

@Path("loginmgr")
public class LoginServlet {
	
	private static final int KEY_LENGTH = 256; 			// 256 bits = 32 bytes (=64 hex number)
	private static final int PBKDF2_ITERATIONS = 10000;	// Depending on hardware. Set to ~0.5 second
	
    // CALL EXAMPLE 1:   http://localhost:591/pattanasin/loginmgr/create
	// CALL EXAMPLE 2:
	//		$.post("/pattanasin/loginmgr/create",
	//	        {
	//				loginID: "johndoe",
	//				password: "johnPW",
	//	        },
	//	        function(data,status){
	//	            alert("RESULT returnMessage=" + data.returnMessage + "   \nreturnCode=" + data.resultCode);
	//	      });
	// CALL EXAMPLE 3:
	//			$.ajax({
	//			    type: 'POST',
	//			    url: '/pattanasin/loginmgr/create',
	//			    contentType: 'application/x-www-form-urlencoded',	// CALLING DATA TYPE
	//			    data: { loginID: "johnDoe2", password: "johnPW2" },
	//			    dataType: 'json',									// RETURN DATA TYPE
	//			    success:  function(data,status){
	//			    	alert("RESULT_2:  returnMessage=" + data.returnMessage + "   \nreturnCode=" + data.resultCode);
	//		        }
	//		
	//			});
	
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public ReturnResult createNewUser( @Context HttpServletRequest request, @Context HttpServletResponse response) 
    														throws ServletException, IOException {
    	ReturnResult returnResult = new ReturnResult();
    	String loginID = request.getParameter("loginID");
    	String password = request.getParameter("password");
    	System.out.println("createNewUser method:" + "   ID=" + loginID + "    PW=" + password);
    	
    	EmployeeDAO empDAO = new EmployeeDAO();
    	Employee emp = empDAO.getEmployeeInfoWithLoginID(loginID);
    	if (emp != null) {
    		System.out.println("loginServlet: Employee Found.  WILL NOT ADD. Return error message");
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.USER_EXIST);
    		return returnResult;
    	}
 
    	char[] passwordChars = password.toCharArray();
    	
    	// saltBytes (16 bytes) example:  [52, -51, -97, -57, 26, 116, 40, -110, 22, -29, 86, 12, -22, -13, -85, 38]
        byte[] saltBytes = Password.getSalt();
	
    	// saltString example: "34cd9fc71a74289216e3560ceaf3ab26"
        String saltString = org.jboss.resteasy.util.Hex.encodeHex(saltBytes);
        
        
        //String saltString = "34cd9fc71a74289216e3560ceaf3ab26";
        //byte[] saltBytes = org.jboss.resteasy.util.Hex.decodeHex(saltString);
        System.out.println("saltByte LEN (should be 16)=" + saltBytes.length);   
        System.out.println("SaltString=" + saltString + "=    SaltString Len (should be 32)=" + saltString.length());

        
        // Password class is com.erp.pattanasin.common.Password
        byte[] passwordBytes = Password.hashPassword(passwordChars, saltBytes, PBKDF2_ITERATIONS, KEY_LENGTH);
        String passwordString = org.jboss.resteasy.util.Hex.encodeHex(passwordBytes);
        System.out.println("hashedpasswordString=" + passwordString + "=    Its length=" + passwordString.length());
    	
    	Employee newEmp = new Employee();
    	newEmp.setLoginID(request.getParameter("loginID"));
    	newEmp.setPassword(passwordString);
    	newEmp.setSalt(saltString);
    	newEmp.setFirstName(loginID);
    	newEmp.setLastName(loginID);
    	newEmp.setEmail(loginID);
    	newEmp.setPhoneNumber(loginID);
    	newEmp.setActive(true);
    	
    	boolean result = empDAO.createOrUpdateEmployee(newEmp);
    	if (result) {
    		System.out.println("************  ADDING EMP SUCCESS *********");
    		returnResult.setup(ErrorCode.SUCCESS, ErrorMessage.ADD_USER_SUCCESS);
    	} else {
    		System.out.println("************  ADDING EMP FAILED  FAILED FAILED  *********");
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.USER_EXIST);
    	}
    	
    	return returnResult;
    	
    }
    

    
    // CALL EXAMPLE:   http://localhost:591/pattanasin/loginmgr/create
    @POST
    @Path("create")  
    public void createNewUser1( @Context HttpServletRequest request, @Context HttpServletResponse response) 
    														throws ServletException, IOException {

    	String loginID = request.getParameter("loginID");
    	String password = request.getParameter("password");
    	System.out.println("createNewUser method:" + "   ID=" + loginID + "    PW=" + password);
    	
    	EmployeeDAO empDAO = new EmployeeDAO();
//    	Employee emp = empDAO.getEmployeeInfoWithLoginID(loginID);
//    	if (emp == null) {
//    		System.out.println("loginServlet: emp not found xxxxx ");
//    	} else {
//    		System.out.println("loginServlet: emp found yyyyy");
//    	}
    	

    	String pass = "pass";
    	char[] passwordChars = pass.toCharArray();
    	
    	// saltBytes example:  [52, -51, -97, -57, 26, 116, 40, -110, 22, -29, 86, 12, -22, -13, -85, 38]
        //byte[] saltBytes = Password.getSalt();		//  16 bytes salt.
    	
    	// saltString example: "34cd9fc71a74289216e3560ceaf3ab26"
        //String saltString = org.jboss.resteasy.util.Hex.encodeHex(saltBytes);
        String saltString = "34cd9fc71a74289216e3560ceaf3ab26";
        byte[] saltBytes = org.jboss.resteasy.util.Hex.decodeHex(saltString);
        System.out.println("saltByte LEN (should be 16)=" + saltBytes.length);   
        System.out.println("SaltString=" + saltString + "=    SaltString Len (should be 32)=" + saltString.length());

        
        // Password class is com.erp.pattanasin.common.Password
        byte[] hashedBytes = Password.hashPassword(passwordChars, saltBytes, PBKDF2_ITERATIONS, KEY_LENGTH);
        String hashedString = org.jboss.resteasy.util.Hex.encodeHex(hashedBytes);
        System.out.println("hashedString=" + hashedString);
        System.out.println("hashedStringLenght=" + hashedString.length() );


    	
    	Employee emp = new Employee();
    	emp.setLoginID(request.getParameter("loginID"));
    	

    	
    }    
    
    
    
    
    
    
    
    
	//http://localhost:591/tipsuwan/loginmgr/tutorial/helloname/abc_444  chrome
	//http://localhost:591/tipsuwan/loginmgr/tutorial/helloname/abc_444
	
	// http://localhost:591/tipsuwan/loginmgr/login
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED )
    @POST
    @Path("login")  
    public void login( @Context HttpServletRequest request, @Context HttpServletResponse response) 
    														throws ServletException, IOException {
        // The redirection to "222 also worked in May 2018
    	//response.setHeader("Location", "/tipsuwan/tutorial/helloname/222");
        //response.sendRedirect("/tipsuwan/tutorial/helloname/222");
        
    	System.out.println("IN login method");
        response.setHeader("Location", "/pattanasin/index.html");
        response.sendRedirect("/pattanasin/index.html");
    	
    }    
    
    
    
//	How to get ID and Role on client side    
//    
//		$.post("/tipsuwan/loginmgr/getIDandRole",
//		        function(data,status){
//		            alert("Data: " + data + "\nStatus: " + status);
//		         });  
    @GET
    @Path("getIDandRole")
    public String getIDandRole( @Context HttpServletRequest request, @Context HttpServletResponse response) 
			throws ServletException, IOException {
    	// Get userName from the login session.
    	String userName = request.getRemoteUser();		// e.g., userName=sale1
    	System.out.println("USER NAME=" + userName);
    	EmployeeDAO empDAO = new EmployeeDAO();
    	Employee emp = empDAO.getEmployeeInfoWithLoginID(userName);
    	// TODO: Use 'userName' to retrieve the roles from DB.  	<===   TODO
    	// Return hard code for now.
    	
//    	return emp.getLoginID() + ":" + empDAO.getEmployeeRole(userName);
    	return "sale1:Sale";
    }
    
    
    @GET
    @Path("logout")
    public String logout( @Context HttpServletRequest request, @Context HttpServletResponse response) 
			throws ServletException, IOException {
    	
    	request.getSession().invalidate();		// user needs to login again after invalidate
    	return "logoutSuccess";
    	
    }
    
    
//	How to get ID and Role on client side    
//  
//		$.post("/tipsuwan/loginmgr/newUser",
//		        function(data,status){
//		            alert("Data: " + data + "\nStatus: " + status);
//		         });  
  @POST
  @Path("newUser")
  public String createNewUser(@FormParam("userID") String userID,
		  					@FormParam("firstName") String firstName,
		  					@FormParam("lastName") String lastName,
		  					@FormParam("email") String email,
		  					@FormParam("phone") String phone,
		  					@FormParam("role")  String role) 
	{
	  String returnMessage = "";
	  
  	// Get userName from the login session.
	  Employee emp = new Employee();
	  emp.setLoginID(userID);
	  emp.setFirstName(firstName);
	  emp.setLastName(lastName);
	  emp.setEmail(email);
	  emp.setPhoneNumber(phone);
	  emp.setPassword("temporary");
	  // return 
	  
	  System.out.println(emp.toString() + " role = " + role);
	  
	  
  	  EmployeeDAO empDAO = new EmployeeDAO();
  	  if(empDAO.createOrUpdateEmployee(emp) && empDAO.createOrUpdateEmployeeRole(emp, role)) {
  		  returnMessage = "New User " + userID+ " has been successfully created!";
  	  } else {
  		  returnMessage = "ERROR: Unable to create user.";
  	  }
  	
  	  return returnMessage;
  	
	}
    

}







