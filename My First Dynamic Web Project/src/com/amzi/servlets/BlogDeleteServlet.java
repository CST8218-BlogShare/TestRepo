package com.amzi.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.DbConnection;

/**
 * Servlet implementation class BlogDeleteServlet
 */

public class BlogDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BlogDeleteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		String blogTitle = "";
		int blogId = -1;
		PreparedStatement pst = null; 
		ResultSet rs = null;
		DbConnection connectionManager = null;
		    
		blogTitle = request.getParameter("blogTitle");
		blogTitle = blogTitle.trim();
			
		if(!blogTitle.contentEquals("")){
			connectionManager = DbConnection.getInstance();
			try {
				
				pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = ?");
				pst.setString(1, blogTitle);
				
				rs = pst.executeQuery();
				rs.first();
				blogId = rs.getInt("blogId");
				
				rs.close();
				pst.close();
				
				pst = connectionManager.getConnection().prepareStatement("delete from blog where blogid = ?");
				pst.setInt(1, blogId);
				pst.executeUpdate();
				
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			} finally { 
	        	//we now have to manage closing the connection a different way...at logout...
	            if (pst != null) {  
	                try {  
	                    pst.close();  
	                } catch (SQLException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	            if (rs != null) {  
	                try {  
	                    rs.close();  
	                } catch (SQLException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
		}
			 
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
