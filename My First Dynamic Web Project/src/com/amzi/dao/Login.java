package com.amzi.dao;  
  
public class Login { 
	
	public static String error = null;
	
	//could return a user object instead, and if null then there was an error.
    public static User validate(String name, String pass) {          
        User u = null;

        name = name.trim();
        pass = pass.trim();
            
        if(name == ""){
            System.out.println("Username was not entered\n");
            error = "error.nousername";
            return null;
        }
            
        if(pass == ""){
        	System.out.println("Password was not entered\n");
        	error = "error.nopass";
            return null;
        }
            
        u = User.getUserFromDatabaseByCredentials(name, pass);
            
        if(u == null){
        	System.out.println("The entered username and password do not match registered users, throwing SQLException\n");
            error = "error.wrongcredentials";
            return null;
        }
        return u;   
    }  
}  