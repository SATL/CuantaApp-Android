package com.Slem.CC;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import com.Slem.CR.CuantaRazon;
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
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;



@SuppressWarnings("deprecation")
public class CuantoCabron extends TabActivity {
	int contador=1;
	public List<Imagen> imagenesCC = new ArrayList<Imagen>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//Barras de progreso
		requestWindowFeature(Window.FEATURE_PROGRESS); 
		setProgressBarVisibility(true);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuanto_cabron);
		
		//Ejecuto el parseo
				final String url = "http://www.cuantocabron.com";
				new downloadData().execute(url);
		
		//Asigno las tabs con sus intents
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		 
		TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Ultimos");
		tabs.addTab(spec);
		
		Intent aleatorio = new Intent(this, AleatorioCC.class);
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(aleatorio);
		spec.setIndicator("Aleatorio");
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab3");
		Intent mejores = new Intent(this, MejoresCC.class);
		spec.setContent(mejores);
		spec.setIndicator("Mejores");
		tabs.addTab(spec);
		
			
		

		
	
		Button CR = (Button) findViewById(R.id.CRButton);
		CR.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				imagenesCC=null;
				Intent intentCR = new Intent( CuantoCabron.this, CuantaRazon.class);
				startActivity(intentCR);
				finish();
			}
			
		});
		
		Button VF = (Button) findViewById(R.id.VFButton);
		VF.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				imagenesCC=null;
				Intent intentCR = new Intent( CuantoCabron.this, VistoEnFB.class);
				startActivity(intentCR);
				finish();
			}
			
		});
		
		//Asigo las lista con su OnItemClick
		ListView lista = (ListView) findViewById(android.R.id.list);
	    lista.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(CuantoCabron.this, onClick.class);
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
	    
	 RelativeLayout footer = (RelativeLayout)this.getLayoutInflater().inflate(R.layout.foot, null);
	 
	    final Button prev=(Button) footer.findViewById(R.id.Prev);
	    
	       prev.setVisibility(View.GONE);
	    prev.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				contador--;
				String mas=url+"/ultimos/p/"+contador;
				clear();
				new downloadData().execute(mas);
			}});
	    
	    
	   final TextView page=(TextView) footer.findViewById(R.id.textFoot);
	   page.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
		   
	   });
	    page.setText("Pag "+contador);
	    Button next=(Button) footer.findViewById(R.id.Next);
		 next.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					contador++;
					 page.setText("Pag "+contador);
					String mas=url+"/ultimos/p/"+contador;
					clear();
					prev.setVisibility(View.VISIBLE);
					new downloadData().execute(mas);
				}
				
			});
	    
	  		lista.addFooterView(footer);
	  	
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
		Imagen imagen =imagenesCC.get(menuInfo.position);
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
			i.setClass(CuantoCabron.this, onClick.class);
			String url =imagen.getUrl();
			i.putExtra("imag", url);
			i.putExtra("title", imagen.getName());
			startActivity(i);
        }
        else {return false;}  
    return true;  
    }  
	
	//funcion para actualizar las Views
	public void updateViewImagenes(List<Imagen> imagenes){
	  	MyAdapter adaptador = new MyAdapter( CuantoCabron.this, imagenes );
		ListView lstClasses = (ListView)findViewById(android.R.id.list);
        lstClasses.setTextFilterEnabled(true);
        lstClasses.setClickable(false);
        lstClasses.setFocusable(false);
        lstClasses.setAdapter(adaptador);	
		
        
       
		
		 
		
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater= getMenuInflater();
		inflater.inflate(R.menu.activity_cuanto_cabron, menu);
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
			clear();
						return true;
		
		case R.id.Reload:
			ListView lista1 = (ListView) findViewById(android.R.id.list);
			lista1.setAdapter(null);
			final String url = "http://www.cuantocabron.com";
			new downloadData().execute(url);	
				return true;
		case R.id.CR:
			clear();
			Intent intent=new Intent(this, CuantaRazon.class);
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
		
	
		//Clase en backgroun encargado del parseo
	public class downloadData extends AsyncTask<String, Void, Void>{
		       
        protected void onPreExecute() {
        	setProgressBarIndeterminate(true);
			setProgressBarVisibility(true);
        }
		@Override
		protected Void doInBackground(String... url) {
			try {
            	Htmlparser parser = new Htmlparser(url[0]);
      		 imagenesCC = parser.run();
               } catch (Exception e) { 
            	   imagenesCC=null;  }
			
            return null;
			
		}		
		 protected void onPostExecute(Void result) {
			updateViewImagenes(imagenesCC);
	        setProgressBarVisibility(false);
	        setProgressBarIndeterminate(false);
	                                    }
	}
	
	public void clear(){
		ListView lista = (ListView) findViewById(android.R.id.list);
		lista.setAdapter(null);
		imagenesCC=null;
	}
	
	
	}
	
	
	
	
	
	 
	
	
