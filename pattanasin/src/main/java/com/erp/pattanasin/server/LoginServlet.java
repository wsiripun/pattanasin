// 9/24/18:  STATUS:  Finished these functionalities (end-to-end) browser to db
//	A: Create new user
//  B: login
//	C: logout

//	TESTING:	http://localhost:591/pattanasin/logindemo.html

//	TODO:   D: Change password
//			E: Delete user



package com.erp.pattanasin.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
	//			// $.ajax() is flexible. Can do either POST, GET, etc
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
    	
    	String remoteUser = request.getRemoteUser();
    	String loginID = request.getParameter("loginID");
    	String password = request.getParameter("password");
    	System.out.println("createNewUser method:" + "   ID=" + loginID + "    PW=" + password + "=   remoteUser=" + remoteUser );
    	if (loginID.equals("") || password.equals("")) {
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.EMPTY_ID_PASSWORD);
    		return returnResult;
    	}
    	
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
    	
    	// Create a session for the new user. It will also create JSESSIONID
    	HttpSession session = request.getSession();
    	
    	session.setAttribute("userName", loginID);		// remember the loginID
    	session.setMaxInactiveInterval(60);		// invalidate the session in 60 seconds
    	
    	return returnResult;
    	
    }
    

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public ReturnResult login( @Context HttpServletRequest request, @Context HttpServletResponse response) 
    														throws ServletException, IOException {
        // The redirection to "222 also worked in May 2018
    	//response.setHeader("Location", "/tipsuwan/tutorial/helloname/222");
        //response.sendRedirect("/tipsuwan/tutorial/helloname/222");
        
    	System.out.println("IN login method");
    	
    	// Check if userID and password are not empty string
    	ReturnResult returnResult = new ReturnResult();
    	String loginID = request.getParameter("loginID");
    	String password = request.getParameter("password");
    	if (loginID.equals("") || password.equals("")) {
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.EMPTY_ID_PASSWORD);
    		return returnResult;
    	}
    	
    	// Check if loginID is in DB
    	EmployeeDAO empDAO = new EmployeeDAO();
    	Employee emp = empDAO.getEmployeeInfoWithLoginID(loginID);
    	if (emp == null) {
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.NO_LOGINID);
    		return returnResult;
    	}
    	
    	char[] passwordChars = password.toCharArray();
        String saltString = emp.getSalt();
        byte[] saltBytes = org.jboss.resteasy.util.Hex.decodeHex(saltString);
        
        byte[] passwordBytes = Password.hashPassword(passwordChars, saltBytes, PBKDF2_ITERATIONS, KEY_LENGTH);
        String passwordString = org.jboss.resteasy.util.Hex.encodeHex(passwordBytes);
        
        // Compare Hash password string to the one from DB
        if (! emp.getPassword().equals(passwordString)) {
        	// wrong password
        	returnResult.setup(ErrorCode.FAIL, ErrorMessage.INCORRECT_PASSWORD);
        	return returnResult;
        }
            	
    	// "false" => Retrieve the session, but do not create a new session if there is none.
    	HttpSession session = request.getSession(false);
    	if (session == null) {
    		System.out.println("TEST 1: THERE IS NO SESSION.  Create one");
        	session = request.getSession();		// Retrieve the session. Create a new one if not exist.
        	if (session == null) {
        		// Should not get here.
        		System.out.println("TEST 2:  STILL THERE IS NO SESSION");
        	} else {
        		System.out.println("TEST 2: JUST CREATE A NEW SESSION");
            	session.setAttribute("userName", loginID);		// remember the loginID
            	session.setMaxInactiveInterval(60);		// invalidate the session in 60 seconds
        	}
    	} else {
    		System.out.println("TEST 1: SESSION EXIST.  DO NOTHING");
    		String sessionID = session.getId();				// This is JSESSIONID cookie
    		String remoteUserId = request.getRemoteUser();
    		String wsCreateUserName = (String) session.getAttribute("userName");
    		String extraMessage = "    \nsessionID=" + sessionID + "   remoteUserId=" + remoteUserId + "   wsCreateUserName=" + wsCreateUserName;		
    		returnResult.setup(ErrorCode.SUCCESS, ErrorMessage.ALREADY_LOGIN + " loginID=" + loginID + extraMessage);
    		return returnResult;
    	}
    	
    	String sessionID = session.getId();
		String remoteUserId = request.getRemoteUser();
		String wsCreateUserName = (String) session.getAttribute("userName");
		String extraMessage = "     \nsessionID=" + sessionID + "   remoteUserId=" + remoteUserId + "   wsCreateUserName=" + wsCreateUserName;
    	
    	returnResult.setup(ErrorCode.SUCCESS, ErrorMessage.LOGIN_SUCCESS + " loginID=" + loginID + extraMessage);
    	return returnResult;

    	
        //response.setHeader("Location", "/pattanasin/index.html");
        //response.sendRedirect("/pattanasin/index.html");
    	
    }    
    
    
    @POST
    @Path("logoutABCD")
    @Produces(MediaType.APPLICATION_JSON)
    public ReturnResult logout( @Context HttpServletRequest request, @Context HttpServletResponse response) 
			throws ServletException, IOException {
    	
    	ReturnResult returnResult = new ReturnResult();
    	HttpSession session = request.getSession(false);	// Retrieve a session. Do not create a new one if session not exist
    	if (session == null) {
    		returnResult.setup(ErrorCode.FAIL, ErrorMessage.LOGOUT_FAIL_NOTLOGIN);
    	} else {
    		String sessionID = session.getId();
    		String remoteUserId = request.getRemoteUser();
    		String wsCreateUserName = (String) session.getAttribute("userName");
    		String extraMessage = "\nsessionID=" + sessionID + "   remoteUserId=" + remoteUserId + "   wsCreateUserName=" + wsCreateUserName;
    		
        	session.invalidate();		// user needs to login again after invalidate
        	
        	returnResult.setup(ErrorCode.SUCCESS, ErrorMessage.LOGOUT_SUCCESS + extraMessage);
        	System.out.println("LOGOUT:   Have called invalidate()");   		
    	}


    	return returnResult;
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
    
}







