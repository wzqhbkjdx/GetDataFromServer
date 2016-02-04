package com.cgtrc.bym.testapplication;

public class Article {
	private String title;
	private String summary;
	private String url;
	private String imageUrl;
	private String postTime;
 
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
	
	public String getUrl(){
		return url;
	}	
	
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}		
	
	public void setPostTime(String postTime){
		this.postTime = postTime;
	}
	
	public String getPostTime(){
		return postTime;
	}	
	
}
