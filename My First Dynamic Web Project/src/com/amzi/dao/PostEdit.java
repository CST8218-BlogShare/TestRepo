package com.amzi.dao;

import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;

public class PostEdit {

	private int postEditId; 
	private int postId;
	private String author;
	private String editDateTime;
	private String titleBeforeEdit;
	private String contentBeforeEdit;
	
	public PostEdit() {
		
	}

	public String getContentBeforeEdit() {
		return contentBeforeEdit;
	}

	protected void setContentBeforeEdit(String contentBeforeEdit) {
		this.contentBeforeEdit = contentBeforeEdit;
	}

	public int getPostEditId() {
		return postEditId;
	}

	protected void setPostEditId(int postEditId) {
		this.postEditId = postEditId;
	}

	public int getPostId() {
		return postId;
	}

	protected void setPostId(int postId) {
		this.postId = postId;
	}
	
	public void insertPostEditIntoDatabase(){
		
	}
	
	public String getAuthor() {
		return author;
	}

	protected void setAuthor(String author) {
		this.author = author;
	}
	
	public String getEditDateTime() {
		return editDateTime;
	}

	protected void setEditDateTime(String editDateTime) {
		this.editDateTime = editDateTime;
	}

	public String getTitleBeforeEdit() {
		return titleBeforeEdit;
	}

	protected  void setTitleBeforeEdit(String titleBeforeEdit) {
		this.titleBeforeEdit = titleBeforeEdit;
	}
	
	public static int insertPostEditInDatabase(int userId, int postId,String titleBeforeEdit,String contentBeforeEdit){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		int postEditId = -1;
		
		connectionManager = DbConnection.getInstance();
		
		if(DbConnection.testConnection(connectionManager) == false){
			System.out.println("Error with insertion of post edit into database: Unable to establish connection with database.");
			return -1;
		}
			
		try{
		
			//Inserting the new postEdit row into the postEdit table.
	        pst = connectionManager.getConnection().prepareStatement("insert into postEdit (postEditId, postId, editDateTime, titleBeforeEdit, contentBeforeEdit)"
	        												       + " values (0,?,now(),?,?)");
	        pst.setInt(1, postId);
	        pst.setString(2, titleBeforeEdit);
	        pst.setString(3, contentBeforeEdit);
	        
	        pst.execute();
	        pst.close();
	        
	        //Retrieving the generated value for PostEdit id from the last insert into the postEdit table.
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as postEditId");
	        rs = pst.executeQuery();
	        
	        rs.first();
	        postEditId = rs.getInt("postEditId");
	        
	        rs.close();
	        pst.close();
	        
	        //Inserting a new row into the User_PostEdit table. 
	        
	        pst = connectionManager.getConnection().prepareStatement("insert into user_postedit (userId, postEditId) values(?,?)");
	        pst.setInt(1,userId);
	        pst.setInt(2, postEditId);
	        pst.execute();
	        pst.close();
	        
		}catch(SQLException sqlE){
			System.out.println("Error with insertion of post edit into database: SQL error.");
			sqlE.printStackTrace();
			return -2;
		}finally{
			try {
				if(rs != null){
					rs.close();
				}
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		}
		return 0;
	}
	
	public static ArrayList<PostEdit> getPostEditsFromDatabaseByPostId(int postId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		ArrayList<PostEdit> postEdits = new ArrayList<PostEdit>();
		
		connectionManager = DbConnection.getInstance();
		
		if(DbConnection.testConnection(connectionManager) == false){
			System.out.println("Error with post edit retrieval using post id: Unable to establish connection with database.");
			return null;
		}
		
		/* selecting all postEdits based on PostId*/
		try {
			pst = connectionManager.getConnection().prepareStatement("select postEditId, postId, editDateTime, titleBeforeEdit, contentBeforeEdit" 
					  												 + " from postEdit where Postid = ? " 
																	 + " order by editDateTime desc");
																
			pst.setInt(1, postId);
			rs = pst.executeQuery();
			
			while(rs.next()){
				PostEdit pe = new PostEdit();
				PreparedStatement authorPst = null;
				ResultSet authorRs = null;
				
				pe.setPostEditId(rs.getInt("postEditId"));
				pe.setPostId(rs.getInt("postId"));
				pe.setEditDateTime(rs.getString("editDateTime"));
				pe.setTitleBeforeEdit(rs.getString("titleBeforeEdit"));
				pe.setContentBeforeEdit(rs.getString("contentBeforeEdit"));
				
				try{
					
					authorPst = connectionManager.getConnection().prepareStatement("select u.username as username from user u, postedit pe, user_postedit upe"
																				+ " where u.userId = upe.userid AND"
																				+ " pe.postEditid = upe.postEditid AND"
																				+ " pe.postEditId = ?;");
					authorPst.setInt(1, pe.getPostEditId());
					authorRs = authorPst.executeQuery();
					authorRs.first();
					
					pe.setAuthor(authorRs.getString("username"));
				}catch(SQLException sqlE) {
					sqlE.printStackTrace();
				}finally{
					try {
						authorPst.close();
						authorRs.close();
					}catch(SQLException sqlE){
						sqlE.printStackTrace();
					}
				}
				
				postEdits.add(pe);
			}
		} catch (SQLException sqlE){
			System.out.println("Error with post edit retrieval using post id: SQL Error");
			sqlE.printStackTrace();
			return null;
		}finally{
			try {
				pst.close();
				rs.close();
				
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		}
		return postEdits;
	}


}
