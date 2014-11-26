package com.amzi.dao;  
    
public class Register {  
	
	public static String error = null;
	
	//could return a user object instead and if null an error occured.
	public static User validate(String name, String pass, String pass2) {          
        User u = null;
        int errorCode = 0;
        
        name = name.trim();
        pass = pass.trim();
        pass2 = pass2.trim();
        	
        if(name.equals("")){
        	System.out.println("Username was not entered.");
        	error = "errorregister.nousername";
        	return null;
        }
        	
        if(pass.equals("")){
        	System.out.println("Password was not entered.");
        	error = "errorregister.nopass";
        	return null;
        }
        	
        if(pass2.equals("")){
        	System.out.println("Password was not rentered.");
        	error = "errorregister.nopassreenter";
        	return null;
        }
        	
        if(!pass.equals(pass2)){
        	System.out.println("The passwords that were entered do not match.");
        	error = "errorregister.nopassmatch";
        	return null;
        }
        	        	
        errorCode = User.insertUserIntoDatabase(name, pass);
        
        if(errorCode == -1){
        	System.out.println("Unable to establish connection with database.");
        	error = "errorregister.sqlconnection";
        	return null;
        }
        
        if(errorCode == -2){
        	System.out.println("A user with the same name already exists within BlogShare.");
        	error = "errorregister.userexists";
        	return null;
        }
        
        if(errorCode == -3){
        	System.out.println("Error with database interaction.");
        	error = "errorregister.sql";
        	return null;
        }
        
        u = User.getUserFromDatabaseByCredentials(name, pass);
          
        return u;  
    }  
}  