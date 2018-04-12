package com.erp.pattanasin.db.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Driver;

public class Version {

    public static void main(String[] args) {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
        	
            Driver jdbcDriver = new com.mysql.jdbc.Driver ();
        	DriverManager.registerDriver(jdbcDriver);
        	con = DbUtil.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                
                System.out.println("WIJS 000 7/26: MySQL Version is:  " + rs.getString(1));
            }
            DriverManager.deregisterDriver(jdbcDriver);;

        } catch (SQLException ex) {
        
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            
            try {
                
                if (rs != null) {
                    rs.close();
                }
                
                if (st != null) {
                    st.close();
                }
                
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
