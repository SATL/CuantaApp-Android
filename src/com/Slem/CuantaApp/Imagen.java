package com.Slem.CuantaApp;

public class Imagen {

	private String name;
	private String url;
	
	
	public Imagen(String name, String url){
		this.name = name;
		this.url=url;
		
	}
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getUrl(){
		return this.url;
	}
	
}
