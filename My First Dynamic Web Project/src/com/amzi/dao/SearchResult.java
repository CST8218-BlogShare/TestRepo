package com.amzi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchResult {

	private ArrayList<Integer> searchResultsBlog = null;//blogId
	private ArrayList<Integer> searchResultsPost = null;//postId
	private ArrayList<Integer> searchResultsUser = null;//userId
	private ArrayList<Integer> resultsToDisplay = null;//results currently shown on screen.
	
	private String resultType = "";
	private String searchTerm = ""; /* will allow the error message within SearchResult.jsp, 
									   to show the message "Unable to find any content relating to search term " ", 
	   								   if a match with the search term is not found within all Blogs, Posts and Users. 
	 								*/
	
	
	public SearchResult() {
		
	}
	
	
	public SearchResult(String searchTerm, ArrayList<Integer> searchResultsBlog, ArrayList<Integer> searchResultsPost, ArrayList<Integer> searchResultsUser ){
		this.searchTerm = searchTerm;
		this.searchResultsBlog = searchResultsBlog;
		this.searchResultsPost = searchResultsPost;
		this.searchResultsUser = searchResultsUser;	
		
		if(searchResultsBlog != null && searchResultsBlog.size() > 0){
			resultsToDisplay = searchResultsBlog;
			resultType = "Blog";
			return;
		}
		
		if(searchResultsPost != null && searchResultsPost.size() > 0){
			resultsToDisplay = searchResultsPost;
			resultType = "Post";
			return;
		}
		
		if(searchResultsUser != null && searchResultsUser.size() > 0){
			resultsToDisplay = searchResultsUser;
			resultType ="User";
		}
	}
	
	public String getSearchTerm(){
		return searchTerm;
	}
	
	protected void setSearchTerm(String s){
		searchTerm = s;
	}
	
	public String getResultType(){
		return resultType;
	}
	
	protected void setResultType(String s){
		resultType = s;
	}
	
	public int getResultIdAt(int i){
		return resultsToDisplay.get(i);
	}
	
	public int getResultCount(){
		if(resultsToDisplay == null){
			return 0;
		}
		
		return resultsToDisplay.size();
	}
	
	
	public void setResults(ArrayList<Integer> results, String resultType){
		this.resultsToDisplay = results;
		this.resultType = resultType; 
	}
	
	public ArrayList<Integer> getResultsBlog(){
		return searchResultsBlog;
	}
	
	public ArrayList<Integer> getResultsPost(){
		return searchResultsPost;
	}
	
	public ArrayList<Integer> getResultsUser(){
		return searchResultsUser;
	}
	
	public static ArrayList<Integer> parseSearchResults(ArrayList<Integer> listToReturn, String idType, ResultSet resultsToParse){
		
		try{
			if(listToReturn == null){
				resultsToParse.last();
				if(resultsToParse.getRow() > 0 ){
					listToReturn = new ArrayList<Integer>();
				}
			}
			
			resultsToParse.beforeFirst();
			
			while(resultsToParse.next()){
				listToReturn.add(resultsToParse.getInt(idType));
			}
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
			return null;
		}
		
		
		return listToReturn;
	}
	
	
}
