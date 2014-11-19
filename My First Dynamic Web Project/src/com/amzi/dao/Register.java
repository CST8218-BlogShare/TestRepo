package com.amzi.dao;  
    
public class Register {  
	
	public static String errorMessege = null;
	
	//could return a user object instead and if null an error occured.
	public static User validate(String name, String pass, String pass2) {          
        User u = null;
        int errorCode = 0;
        
        name = name.trim();
        pass = pass.trim();
        pass2 = pass2.trim();
        	
        if(name.equals("")){
        	System.out.println("Username was not entered.");
        	errorMessege = "Error with registration. Username was not entered";
        	return null;
        }
        	
        if(pass.equals("")){
        	System.out.println("Password was not entered.");
        	errorMessege = "Error with registration. Password was not entered";
        	return null;
        }
        	
        if(pass2.equals("")){
        	System.out.println("Password was not rentered.");
        	errorMessege = "Error with registration. Password was not reentered";
        	return null;
        }
        	
        if(!pass.equals(pass2)){
        	System.out.println("The passwords that were entered do not match.");
        	errorMessege = "Error with registration. The passwords that were entered do not match";
        	return null;
        }
        	        	
        errorCode = User.insertUserIntoDatabase(name, pass);
        
        if(errorCode == -1){
        	System.out.println("Unable to establish connection with database.");
        	errorMessege = "Error with registration. Unable to establish connection with database.";
        	return null;
        }
        
        if(errorCode == -2){
        	System.out.println("A user with the same name already exists within BlogShare.");
        	errorMessege = "Error with registration. A user with the name name already exists within BlogShare";
        	return null;
        }
        
        if(errorCode == -3){
        	System.out.println("Error with database interaction.");
        	errorMessege = "Error with registration. Error with database interaction";
        	return null;
        }
        
        u = User.getUserFromDatabaseByCredentials(name, pass);
          
        return u;  
    }  
}  