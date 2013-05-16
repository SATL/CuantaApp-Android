package com.Slem.CR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.Slem.CC.AleatorioCC.downloadData;
import com.Slem.CuantaApp.Htmlparser;
import com.Slem.CuantaApp.Imagen;
import com.Slem.CuantaApp.MyAdapter;
import com.Slem.CuantaApp.SaveImage;
import com.Slem.CuantaApp.onClick;
import com.Slem.CuantaApp.R;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;




public class AleatorioCR extends ListActivity{

	public List<Imagen> imagenesCR = new ArrayList<Imagen>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.alatorio_activity);
		final String url = "http://www.cuantarazon.com/aleatorio";
		new downloadData().execute(url);	
		
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener(){
			
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				ListView lista = (ListView) findViewById(android.R.id.list);
				lista.setAdapter(null);
				lista.destroyDrawingCache();
				new downloadData().execute(url);
				
			}
		});
		
		ListView lista = (ListView) findViewById(android.R.id.list);
		lista.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				registerForContextMenu(arg0);
				return false;
			}
	    	
	    });
		
		RelativeLayout footer = (RelativeLayout)this.getLayoutInflater().inflate(R.layout.boton, null);
		 
	    final Button Rel=(Button) footer.findViewById(R.id.Reload);
	    Rel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_recargar, 0);
	    Rel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
				new downloadData().execute(url);
			}});
		
		
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent();
		i.setClass(AleatorioCR.this, onClick.class);
		Imagen imagen = (Imagen) l.getItemAtPosition(position);
		String url =imagen.getUrl();
		i.putExtra("imag", url);
		i.putExtra("title", imagen.getName());
		startActivity(i);
		
		
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
			i.setClass(AleatorioCR.this, onClick.class);
			String url =imagen.getUrl();
			i.putExtra("imag", url);
			i.putExtra("title", imagen.getName());
			startActivity(i);
        }
        else {return false;}  
    return true;  
    }  
	
	public void updateViewImagenes(List<Imagen> imagenes){
	  	MyAdapter adaptador = new MyAdapter( AleatorioCR.this, imagenes );
		ListView lstClasses = (ListView)findViewById(android.R.id.list);
		lstClasses.setTextFilterEnabled(true);
        lstClasses.setClickable(false);
        lstClasses.setFocusable(false);
		lstClasses.setAdapter(adaptador);	
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


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cuanto_cabron, menu);
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
			ListView lista = (ListView) findViewById(android.R.id.list);
			lista.setAdapter(null);
						return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

		public void clear(){
			ListView lista = (ListView) findViewById(android.R.id.list);
			lista.setAdapter(null);
			imagenesCR=null;
		}
	
	

}
