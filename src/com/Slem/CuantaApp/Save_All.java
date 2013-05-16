package com.Slem.CuantaApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.Slem.CC.CuantoCabron;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Save_All {

	
	public void Save(List<Imagen> imagenesCC){
		
		 List<Imagen> imagenes = imagenesCC;
		
		for (int x=0; x<imagenes.size(); x++){
			
		  		Imagen imagen=imagenes.get(x);
			
		  		File folder = new File(Environment.getExternalStorageDirectory().toString()+"/CuantaApp/Images");
		    	  folder.mkdirs();
		    	// En este caso, la raíz de la SD Card
		    	  String sd=folder.toString();
		    	  // El archivo que contendrá la captura
		        File file = new File(sd, imagen.getName()+".jpg");
		  		
		  		
		  		if (file.exists()) {
		  		   // tenemos el archivo
		  			Log.d("existe", "si");
		  			BitmapFactory.Options options = new BitmapFactory.Options();
		  			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		  			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
		  			
		  		}
		  		
		  		else{
		  			Log.d("existe", "no");
		  			//Toast.makeText(CuantoCabron.class.getClass().getAplicationContext(), "Descargando nueva imagen", Toast.LENGTH_SHORT).show();
		  			try {
						new SaveImage().save(imagen);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		  			BitmapFactory.Options options = new BitmapFactory.Options();
		  			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		  			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
		  			
		  		}

		  		
		  		
		  	}
		  	
		  
		}
		
	}
