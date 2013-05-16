package com.Slem.CuantaApp;


import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.Slem.CuantaApp.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyAdapter extends ArrayAdapter<Imagen>{
	Activity context;
    List<Imagen> imagenes;
    public Bitmap byurl;
    ImageView imageView;

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
  			
  			holder.pbar=(ProgressBar) item.findViewById(R.id.progressBar1);
  			
  			holder.imagen= (ImageView) item.findViewById(R.id.imagen);
  			item.setTag(holder);

  			
  			
  		}
  		else
  			holder = (ViewHolder)item.getTag();
  			holder.Title.setText(imagenes.get(position).getName());
  			
  			
  			/*/byurl=BitmapFactory.decodeResource(item.getResources(), R.drawable.troll);
			try {
				holder.imagen.setImageBitmap( new Bitmap_fromURL().execute(imagenes.get(position).getUrl()).get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
  			
  			//holder.imagen.setImageBitmap(imagenes.get(position).getImagen());*/
  			
  			
  			holder.imagen.setTag(imagenes.get(position).getUrl());
  			holder.imagen.setImageDrawable(null);  
  		  //starting async task for image loading  
  		  new ImageTask(holder).execute(holder);  
  			
			return(item);
			
		}
  	
  	
  	private class ImageTask extends AsyncTask<ViewHolder, Void, Boolean> {  
  	    
  	  private ImageView avatarImage;  
  	  private Bitmap av = null;  
  	  private String url; 
  	  private ViewHolder holder;
  	  
  	
	ImageTask (ViewHolder vh){
  		  avatarImage=vh.imagen;
  		  this.holder=vh;
  	  }
  	    
  	protected void onPreExecute() {  
   	   super.onPreExecute();  
   	    //avatarImage.setImageResource(R.drawable.icon_recargar);  
   	   this.holder.pbar.setVisibility(View.VISIBLE);
 	  
   	  }  
  	  
  	  @Override  
  	  protected void onPostExecute(Boolean result) {  
  	   super.onPostExecute(result);  
  	   if(result && av != null) {  
  	    if(avatarImage.getTag().toString().equals(url)) {  
  	     avatarImage.setImageBitmap(av);  
  	   this.holder.pbar.setVisibility(View.GONE);
  	    }  
  	   }  
  	  }  
  	  
  	  @Override  
  	  protected Boolean doInBackground(ViewHolder... params) {  
  	   ViewHolder item = (ViewHolder) params[0];  
  	   boolean result = false;  
  	   if(item != null) {  
  	    try {  
  	     avatarImage = item.imagen;  
  	     url = item.imagen.getTag().toString();  
  	     av = getBit(url);  
  	     result = true;  
  	    } catch (Exception e) {  
  	     e.printStackTrace();  
  	    }  
  	   }  
  	   return result;  
  	  }  
  	    
  	 }  
  	
  	
  	
  	public Bitmap getBit(String string){
  		 HttpGet httpRequest = null;
		 
		 httpRequest = new HttpGet(string);
		 
		 HttpClient httpclient = new DefaultHttpClient();
		 
		 HttpResponse response = null;
		try {
			response = (HttpResponse) httpclient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 HttpEntity entity = response.getEntity();
		 
		 BufferedHttpEntity bufHttpEntity = null;
		try {
			bufHttpEntity = new BufferedHttpEntity(entity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 InputStream instream = null;
		try {
			instream = bufHttpEntity.getContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			return decodeFile(instream, 350);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byurl;
  	}

  	 	  	
  	public static Bitmap decodeFile(InputStream instream, int reqWidth)
			 throws IOException {
			 // Decodificar tamaño de imagen
			 BitmapFactory.Options o = new BitmapFactory.Options();
			 o.inJustDecodeBounds = true;
			 BitmapFactory.decodeStream(instream, null, o);
			 
			 final int width = o.outWidth;
			 @SuppressWarnings("unused")
			final int height = o.outHeight;
			 // Find the correct scale value. It should be the power of 2.
			 int inSampleSize = 1;
			 
			 if (width > reqWidth) {
			   inSampleSize = Math.round((float) width / (float) reqWidth);
			 }
			 
			 // Decodificar con inSampleSize
			 BitmapFactory.Options o2 = new BitmapFactory.Options();
			 o2.inSampleSize = inSampleSize;
			 
			 
			 instream.reset();
			 return BitmapFactory.decodeStream(instream, null, o2);
			 
			}
  	
  	
  
}
class ViewHolder{
TextView Title;
Button boton;
ImageView imagen;
ProgressBar pbar;
}


















	


