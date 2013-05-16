package com.Slem.CuantaApp;

import android.graphics.Bitmap;


public class Imagen {

	private String name;
	private String url;
	private Bitmap imagen;
	
	
	public Imagen(String name, String url, Bitmap bitmap){
		this.name = name;
		this.url=url;
		this.imagen=bitmap;
		
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
	public Bitmap getImagen() {
		return imagen;
	}
	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}
	
}
