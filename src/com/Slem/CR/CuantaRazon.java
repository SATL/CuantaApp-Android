package com.Slem.CR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.Slem.CC.CuantoCabron;
import com.Slem.CuantaApp.Htmlparser;
import com.Slem.CuantaApp.Imagen;
import com.Slem.CuantaApp.MyAdapter;
import com.Slem.CuantaApp.SaveImage;
import com.Slem.CuantaApp.onClick;
import com.Slem.Vf.VistoEnFB;
import com.Slem.CuantaApp.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;



@SuppressWarnings("deprecation")
public class CuantaRazon extends TabActivity {
	int contador=1;
	public List<Imagen> imagenesCR = new ArrayList<Imagen>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_PROGRESS);  
		setProgressBarVisibility(true);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_cuanta_razon);
		
		Intent intent = new Intent(this, AleatorioCR.class);
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		 
		TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Ultimos");
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(intent);
		spec.setIndicator("Aleatorio");
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab3");
		
		Intent intent2 = new Intent(this, MejoresCR.class);
		spec.setContent(intent2);
		spec.setIndicator("Mejores");
		tabs.addTab(spec);
		
		
		final String url = "http://www.cuantarazon.com";
		
		new downloadData().execute(url);	
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				contador++;
				String mas=url+"/ultimos/p/"+contador;
				ListView lista1= (ListView) findViewById(android.R.id.list);
				lista1.setAdapter(null);
				new downloadData().execute(mas);
			}
			
		});
		
		Button CC = (Button) findViewById(R.id.CCButton);
		CC.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imagenesCR=null;
				clear();
				Intent intentCR = new Intent( CuantaRazon.this, CuantoCabron.class);
				startActivity(intentCR);
			}
			
		});
		
		Button VF = (Button) findViewById(R.id.VFButton);
		VF.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imagenesCR=null;
				clear();
				Intent intentCR = new Intent( CuantaRazon.this, VistoEnFB.class);
				startActivity(intentCR);
			}
			
		});
		
		ListView lista = (ListView) findViewById(android.R.id.list);
	    lista.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(CuantaRazon.this, onClick.class);
				Imagen imagen = (Imagen) arg0.getItemAtPosition(arg2);
				String url =imagen.getUrl();
				i.putExtra("imag", url);
				i.putExtra("title", imagen.getName());
				startActivity(i);
			}
	    	
	    });
	    
	    lista.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				registerForContextMenu(arg0);
				return false;
			}
	    	
	    });
	}
	
	
	@Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    super.onCreateContextMenu(menu, v, menuInfo);  
        menu.setHeaderTitle("Seleccion:");  
        menu.add(0, v.getId(), 0, "Compartir");  
        menu.add(0, v.getId(), 0, "Guardar en SD");  
        menu.add(0, v.getId(), 0, "Ver pantalla completa");
	}  
	
	
	public boolean onContextItemSelected(MenuItem item) {  
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
		Imagen imagen =imagenesCR.get(menuInfo.position);
		if(item.getTitle()=="Compartir"){ 
			SaveImage save= new SaveImage();
        	String dir = null;
			try {
				dir = save.saveTemp(imagen);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			share.putExtra(Intent.EXTRA_SUBJECT, "Compartido CuantaApp");
			share.putExtra(Intent.EXTRA_TEXT, "Compartido CuantaApp");
			share.putExtra(Intent.EXTRA_TITLE, "Compartido CuantaApp");
			share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(dir)));
			startActivity(Intent.createChooser(share,   
					 "Compartir via:"));
		
				}
          
        else if(item.getTitle()=="Guardar en SD"){
        	// Carpeta dónde guardamos la captura
        	SaveImage save= new SaveImage();
        	try {
				save.save(imagen);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String text = null;
			try {
				text = save.save(imagen);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    
        }  
       
        else if(item.getTitle()=="Ver pantalla completa"){
        	Intent i = new Intent();
			i.setClass(CuantaRazon.this, onClick.class);
			String url =imagen.getUrl();
			i.putExtra("imag", url);
			i.putExtra("title", imagen.getName());
			startActivity(i);
        }
        else {return false;}  
    return true;  
    }  
		
	
	
	
	public void updateViewImagenes(List<Imagen> imagenes){
	  	MyAdapter adaptador = new MyAdapter( CuantaRazon.this, imagenes );
		ListView lstClasses = (ListView)findViewById(android.R.id.list);
        lstClasses.setTextFilterEnabled(true);
        lstClasses.setClickable(false);
        lstClasses.setFocusable(false);
		lstClasses.setAdapter(adaptador);	
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_cuanta_razon, menu);
		return true;
	}
		public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.About:
			AlertDialog dialogAbout = null;
			final AlertDialog.Builder builder;

			LayoutInflater li = LayoutInflater.from(this);
			View view = li.inflate(R.layout.acercade, null);

			builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
					.setTitle(getString(R.string.app_name))
					.setPositiveButton("Ok", null).setView(view);

			dialogAbout = builder.create();
			dialogAbout.show();
			
			
			return true;
		case R.id.clear:
			imagenesCR=null;
			ListView lista = (ListView) findViewById(android.R.id.list);
			lista.setAdapter(null);
						return true;
		
		case R.id.Reload:
			ListView lista1 = (ListView) findViewById(android.R.id.list);
			lista1.setAdapter(null);
			final String url = "http://www.cuantarazon.com";
			new downloadData().execute(url);	
			return true;
			
		case R.id.CC:
			clear();
			Intent intent=new Intent(this, CuantoCabron.class);
			startActivity(intent);
			
			return true;
			
		case R.id.VF:
			clear();
			Intent intent_=new Intent(this, VistoEnFB.class);
			startActivity(intent_);
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
		
	public class downloadData extends AsyncTask<String, Void, Void>{
		
        protected void onPreExecute() {
        	setProgressBarIndeterminate(true);
			setProgressBarVisibility(true);
        }
		@Override
		protected Void doInBackground(String... url) {
			// TODO Auto-generated method stub
			
			
			try {
            	Htmlparser parser = new Htmlparser(url[0]);
      		  imagenesCR = parser.run();
               } catch (Exception e) {    }
            return null;
			
		}
		
		 protected void onPostExecute(Void result) {

			 
		            updateViewImagenes(imagenesCR);
		            setProgressBarVisibility(false);
		            
		 }
		
	}
	
	public void clear(){
		ListView lista = (ListView) findViewById(android.R.id.list);
		lista.setAdapter(null);
		imagenesCR=null;
	}
	
	


	

	
	
	

}
