package com.Slem.CuantaApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;
import android.os.Environment;

public class SaveImage {
	
	
	public SaveImage() {
		// TODO Auto-generated constructor stub
	}

	public  String save(Imagen imagen) throws InterruptedException, ExecutionException{
		
		File folder = new File(Environment.getExternalStorageDirectory().toString()+"/CuantaApp/Images");
  	  folder.mkdirs();
  	// En este caso, la raíz de la SD Card
  	  String sd=folder.toString();
  	  // El archivo que contendrá la captura
      File f = new File(sd, imagen.getName()+".jpg");
      try {
		f.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      OutputStream os = null;
	try {
		os = new FileOutputStream(f);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	new Bitmap_fromURL().execute(imagen.getUrl()).get().compress(Bitmap.CompressFormat.JPEG, 100, os);
      try {
		os.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
		String path ="Imagen guardada en: \n\t"+f.getAbsolutePath();
		return path;			}
	
	public String saveTemp(Imagen imagen) throws InterruptedException, ExecutionException{

		if(checkSD()){
		File folder = new File(Environment.getExternalStorageDirectory().toString()+"/CuantaApp/Images");
  	    folder.mkdirs();
  	    String sd=folder.toString();
		
		Bitmap icon =  new Bitmap_fromURL().execute(imagen.getUrl()).get();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		File f = new File(sd, "temporary_file.jpg");
		
		try {
		    f.createNewFile();
		    FileOutputStream fo = new FileOutputStream(f);
		    fo.write(bytes.toByteArray());
		} catch (IOException e) {                       
		        e.printStackTrace();
		}
		return f.toString();
		}
		else
		{
			Bitmap icon =  new Bitmap_fromURL().execute(imagen.getUrl()).get();
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			File f = new File(Environment.getDownloadCacheDirectory(), "temporary_file.jpg");
			
			try {
			    f.createNewFile();
			    FileOutputStream fo = new FileOutputStream(f);
			    fo.write(bytes.toByteArray());
			} catch (IOException e) {                       
			        e.printStackTrace();
			}
			return f.toString();
		}
		
	}
	public boolean checkSD(){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    return true;
		} else {
		   return false;
		}
	}
	
}
