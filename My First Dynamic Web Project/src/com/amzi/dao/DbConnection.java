package com.amzi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/* Adapted from design found at https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
 * used second example on page resembling
 * 
 * public class SingletonDemo {
  		private static SingletonDemo instance = null;
   		private SingletonDemo() { }
   		public static synchronized SingletonDemo getInstance() {
   		if (instance == null) {
   			instance = new SingletonDemo();
   		}
        return instance;
    }
 **/

public class DbConnection {
	
	private String url = "jdbc:mysql://localhost:3306/";  
    private String dbName = "blogsharedatatest";  
    private String driver = "com.mysql.jdbc.Driver";  
    private String dbUserName = "blogshareuser";  
    private String dbPassword = "password";
    
    private Connection conn = null;  
    private static DbConnection connectionHelper = null;
	
	private DbConnection() {
		
		 try{
			 Class.forName(driver).newInstance();  
			 conn = DriverManager.getConnection(url + dbName, dbUserName, dbPassword); 
		 }catch(SQLException sqlE){
			sqlE.printStackTrace(); 
		 }catch(IllegalAccessException iaE){
			 iaE.printStackTrace();
		 }catch(InstantiationException iE){
			 iE.printStackTrace();
		 }catch(ClassNotFoundException cnfE){
			 cnfE.printStackTrace();
		 }
	}
	
	public Connection getConnection(){
		if(conn == null){
			System.out.println("Connection object has not been initialized.\n");
			return null;
		}
		
		return this.conn;
	}
	
	public void closeConnection(){
		
		try{
    		conn.close();
    		
    		/*connectionHelper is set to null, since the connection is closed 
    		 * and the only way to create a new connection is to create a 
    		 * new instance of DbConnection and instantiate it into connectionHelper*/
    		connectionHelper = null;
    		
    	}catch(SQLException sqlCloseE){
    		sqlCloseE.printStackTrace();
    	}
	}
	
	
	public static synchronized DbConnection getInstance() {
		if (connectionHelper == null) {
			connectionHelper = new DbConnection();
		}
		return connectionHelper;
	}

}