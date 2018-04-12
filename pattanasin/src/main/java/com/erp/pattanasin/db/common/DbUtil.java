package com.erp.pattanasin.db.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
	private static Connection connection = null;

    public static synchronized  Connection getConnection() {
        try {
        	if (connection != null && connection.isValid(0)) {	// 0 means no timeout in testing isValid()
        		System.out.println("DbUtil.java:   This is an EXISTING conneciton - REUSE IT");
        		return connection;
        	} else {
            	if (connection != null) {
            		// connection is not valid. close it.
            		System.out.println("DbUtil.java:getconnection():   connection is not valid. close it and open a new one");
            		System.out.println("DbUtil.java:   COLSE OLD CONNECTION  ***BEFORE**********  isValid(0 FAILED");
            		connection.close();
            		System.out.println("DbUtil.java:   COLSE OLD CONNECTION  ---AFTER--. isValid(0 FAILED");
            		connection = null;
            	}
            	// DEBUG CODE: Print all files in this dir. Good way to find out current dir
//            	File file = new File(".");
//            	for(String fileNames : file.list()) System.out.println(fileNames);
            	          	
            	Properties prop = new Properties();
            	InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("db.properties");
                prop.load(inputStream);
                
                // STEPS IN USING JDBC
                // 1. Register driver class. Two ways:
                //		1.1  Class.forName(driver);
                //		1.2  DriverManager.registerDriver(jdbcDriver);
                // 2. Create connecton object
                // 3. Use connecton obj to create 'Statement' or 'PrepareStatement'
                //	  	Statement is used for non-parameterized (the one without ?)
                //	  	PreparedStatement is for parameterized  (the one with ?)
                //    	Statement st = connection.createStatement()
                //    	PreparedStatement preparedStatement = connection.prepareStatement(...);
                //			boolean    result = preparedStatement.execute()  // any type of query
                //			ResultSet  result = preparedStatement.executeQuery()  // select * from ...
                //			int        result = preparedStatement.executeUpdate() // insert, update, delete
                //			
                // 4. Execute query
                // 5. Close connection
                //		connection.close()
                
                
                // Step 1.
                String driver = prop.getProperty("driver");
                Class.forName(driver);			// Register Driver
                // Another way to register driver
                //  Driver jdbcDriver = new com.mysql.jdbc.Driver ();		// myssql driver
            	//  DriverManager.registerDriver(jdbcDriver);				// register it
                
                
                
                // Step 2.
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password"); 
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("DbUtil.java:   This is a new conneciton - just create it");
        	}
        
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
