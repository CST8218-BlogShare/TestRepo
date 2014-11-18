package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

  
public class Login { 
	
	public static String errorMessege = null;
	public static String errorMessegeFR = null;
	
	//could return a user object instead, and if null then there was an error.
    public static User validate(String name, String pass) {          
        User u = null;

        name = name.trim();
        pass = pass.trim();
            
        if(name == ""){
            System.out.println("Username was not entered\n");
            errorMessege = "Error with previous login attempt. Username was not entered.";
            errorMessegeFR = "Il y a eu une erreur de connection. Veuiller entre un Nom D'utilisateur.";
            return null;
        }
            
        if(pass == ""){
        	System.out.println("Password was not entered\n");
        	errorMessege = "Error with previous login attempt. User password was not entered.";
            errorMessegeFR = "Il y a eu une erreur de connection. Veuiller entre un Mot de passe.";
            return null;
        }
            
            
        /*if(connectionManager.getConnection() == null){
        	errorMessege = "Error communicating with database. Login cannnot be completed.";
            errorMessegeFR = "Problem de connection avec la base de données. Veuiller essayer à nouveau.";

        }*/
            
        u = User.getUserFromDatabaseByCredentials(name, pass);
            
        if(u == null){
        	System.out.println("The entered username and password do not match registered users, throwing SQLException\n");
            errorMessege = "Error with previous login attempt. Incorrect Username and Password.";
            errorMessegeFR = "Il y a eu une erreur de connection. Verifier votre Nom d'Utilisateur et votre Mot de passe.";
            return null;
        }
        return u;   
    }  
}  