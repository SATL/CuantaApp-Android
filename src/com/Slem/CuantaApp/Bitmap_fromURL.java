package com.Slem.CuantaApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import android.app.Activity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Window;

public class Bitmap_fromURL extends AsyncTask<String, Void, Bitmap> {
		
		
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
			
		
				 HttpGet httpRequest = null;
				 
				 httpRequest = new HttpGet(params[0]);
				 
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
				return null;
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

