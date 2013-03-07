package com.Slem.CuantaApp;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.Slem.CuantaApp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class onClick extends Activity{
	public Bitmap imagen;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onclick);
		ImageView imgView = (ImageView) findViewById(R.id.ImageView01);
		imgView.setOnTouchListener(new Touch());
		
		Button imageButton = (Button)findViewById(R.id.compartir);
		imageButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.share_icon, 0);
		imageButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bitmap icon = imagen;
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
				try {
				    f.createNewFile();
				    FileOutputStream fo = new FileOutputStream(f);
				    fo.write(bytes.toByteArray());
				} catch (IOException e) {                       
				        e.printStackTrace();
				}
				
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/*");
				share.putExtra(Intent.EXTRA_SUBJECT, "Compartido CuantaApp");
				share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
				startActivity(Intent.createChooser(share,   
						 "Compartir via:"));
				f.delete();
			}
		});
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();

			Bundle extras = getIntent().getExtras();
			ImageView imgView = (ImageView) findViewById(R.id.ImageView01);
			imgView.setOnTouchListener(new Touch());
			String url = (String) extras.get("imag");
			new loadFromUrl().execute(url);
			
		
	}
	
	protected void onPause(){
		super.onPause();
		ImageView imagen = (ImageView) findViewById(R.id.ImageView01);
		imagen.destroyDrawingCache();
		imagen.setImageDrawable(null);
		finish();
	}
	
	protected void onStop(){
		super.onStop();
		ImageView imagen = (ImageView) findViewById(R.id.ImageView01);
		imagen.destroyDrawingCache();
		imagen.setImageDrawable(null);
		finish();
	}
	
	public static Bitmap loadFromUrl(String link, int reqWidth)
			 throws IOException, URISyntaxException {
			 
			 HttpGet httpRequest = null;
			 
			 httpRequest = new HttpGet(link);
			 
			 HttpClient httpclient = new DefaultHttpClient();
			 
			 HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
			 
			 HttpEntity entity = response.getEntity();
			 
			 BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			 
			 InputStream instream = bufHttpEntity.getContent();
			 return BitmapFactory.decodeStream(instream);
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
	
	public class loadFromUrl extends AsyncTask<String, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... params) {
			
			
		    try {
				return loadFromUrl(params[0], 350);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null; 
		}
		
		protected void onPostExecute(Bitmap drawable) {
			ImageView imgView = (ImageView) findViewById(R.id.ImageView01);
			imagen=drawable;
			imgView.setImageBitmap(drawable);
			imgView.setOnTouchListener(new Touch());
		
			}
	}
	
	public byte[] ToPng (Bitmap bit)
	{
		Bitmap bitmap = bit;     
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		byte[] b = baos.toByteArray();
		return b;
	}

}
