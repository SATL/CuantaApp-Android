package com.Slem.CuantaApp;


import java.util.List;
import java.util.concurrent.ExecutionException;

import com.Slem.CuantaApp.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MyAdapter extends ArrayAdapter<Imagen>{
	Activity context;
    List<Imagen> imagenes;

  	public MyAdapter(Activity context, List<Imagen> imagenes) {
  		super(context, R.layout.item, imagenes);
  		this.context = context;
  		this.imagenes = imagenes;
  	}
  	
  	public View getView(int position, View convertView, ViewGroup parent) 
  	{
			View item = convertView;
			ViewHolder holder;
			
  		
  		if(item == null)
  		{
  			LayoutInflater inflater = context.getLayoutInflater();
  			item = inflater.inflate(R.layout.item, null);
  			
  			holder = new ViewHolder();
  			holder.Title = (TextView)item.findViewById(R.id.titulo);
  			
  			holder.imagen= (ImageView) item.findViewById(R.id.imagen);
  			item.setTag(holder);
  		}
  		else
  			holder = (ViewHolder)item.getTag();
  		
			
			holder.Title.setText(imagenes.get(position).getName());
			Bitmap bit = null;
			try {
				bit = new Bitmap_fromURL().execute(imagenes.get(position).getUrl()).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.imagen.setImageBitmap(bit);
			return(item);
			
		}
  		
  	
  	
  }

class ViewHolder{
TextView Title;
Button boton;
ImageView imagen;
}





	


