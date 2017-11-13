package com.app.comecamino.controlador;

import com.app.comecamino.internal.Constants;
import com.app.comecamino.internal.Utils;
import com.app.comecamino.modelo.Restaurante;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class FiltrarRestaurantes extends ListActivity {
 //private String jsonResult;
 private ListView lv;
 //private ArrayList<Restaurante> restaurantesAvaiable;
 private Handler handler = new Handler(new ResultMessageCallback());
 private String filtro="";
 private ProgressDialog pDialog;
 //private Button atras;
 private Spinner spinnerFiltro;
 //private AsyncTaskDialog task;
 public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
 private List<Restaurante> m_restaurantes = null;
private ArrayList<Restaurante> m_restaurantesAux = null;


 private static final String url2 = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";
 private static final String url = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_filtro_restaurantes";
 private static final String urltodos = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_restaurantes";
 private static final int TWO_MINUTES = 1000 * 60 * 2;
//*****Posicion GPS*****//

	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;

  
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
 
  setContentView(R.layout.activity_traer_filtro);
  
  spinnerFiltro = (Spinner)findViewById(R.id.spinnerFiltro) ;
  ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.filtro_arrays, android.R.layout.simple_spinner_item);
  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
	//spinnerTipos.setActivated(false);
  spinnerFiltro.setAdapter(adapter);
//Localizacion
  mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	CustomLocationListener customLocationListener = new CustomLocationListener();
	if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
  	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
  	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
  }
	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	 spinnerFiltro.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
										
						
				//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
			if(position==0) 
			   {				 
				mostrar("todos");		
				
					
				}
			if(position==1) 
				{				 
				mostrar("celiacos");		
				
					
				}
			if(position==2) 
				{
				mostrar("vegana");
				
				}
		
			if(position==3) 
			{
			    mostrar("vegetariana");
			
			}
			if(position==4) 
			{
			    mostrar("tradicional");
			
			}
	
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				mostrar("todos");
			}
			
		});
	 
	   
	 
	 	
		
		
		//task = new AsyncTaskDialog();
		//task.execute();
		lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//int fixedpos = arg2 - 1;
				int fixedpos = arg2;
				
				
				Intent intent= new Intent(FiltrarRestaurantes.this,DetailRestauranteActivity.class); 
				
				intent.putExtra("restaurante", m_restaurantes.get(fixedpos));
                startActivity(intent);
                               
			}
		});

 // atras = (Button)findViewById(R.id.atras);

  /*atras.setOnClickListener(new View.OnClickListener(){		      
	     public void onClick(View view) {
	         finish();   	 
	     }
	 });*/

 // url = "http://" + IP + "/appcomecamino/datos.class.php?tipo=mostrar_restaurantes";
  
 

 
 } 
 
 private void mostrar(String tipofiltro)
 {
	 filtro = tipofiltro;
	
 	 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
	  
		
		 Thread thread = new Thread(new LoadTapentosWorker());
		 thread.start();
 }

 /**
	 * actualiza la localizacion
	 * implementa LocationListener
	 * @see android.location.LocationListener
	 */


		private class CustomLocationListener implements LocationListener{

			  public void onLocationChanged(Location argLocation) {
				
				  m_DeviceLocation = argLocation;
				  mLocationManager.removeUpdates(this);
			  }

			  public void onProviderDisabled(String provider) {}

			  public void onProviderEnabled(String provider) {}

			  public void onStatusChanged(String provider,
			    int status, Bundle extras) {}
		 }
	 
	/**
	 * Carga las Tapas y eventos en background. Implementa Runnable
	 * @see java.lang.Runnable
	 * 
	 */
	private class LoadTapentosWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				
				Thread.sleep(1000);
				if (filtro.equals("todos"))
				{
					m_restaurantes =( ArrayList<Restaurante>)obtenerListaTodosRestaurantes();
					Log.i("m_restaurantes size",String.valueOf(m_restaurantes.size()));
				
					obtenerDatosRestaurantes(m_restaurantes);
					Log.i("despues de obtener datos m_restaurantes size",String.valueOf(m_restaurantes.size()));
					//Log.i("se han obtenido todos los restaurantes, total: ",String.valueOf(m_restaurantes.size()) );
				}else 
				{
					m_restaurantes =( ArrayList<Restaurante>)obtenerListaTodosRestaurantes();
					
					obtenerDatosRestaurantes(m_restaurantes);
					
					m_restaurantesAux = Utils.listaFinalFiltrada(m_restaurantes, filtro); 
					m_restaurantes= m_restaurantesAux;
					//Log.i("se han obtenido todos los restaurantes, total: ",String.valueOf(m_restaurantes.size()) );
				}
				

			} catch (Exception e) {
				messageReturn =  MESSAGE_ERROR;
			
			}

			handler.sendEmptyMessage(messageReturn);
		}
	}
	
	/**
	 * Metodo resultCalback LoadTapentosWorker
	 */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(FiltrarRestaurantes.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case  MESSAGE_OK:
			
				setListAdapter(new Adapter(FiltrarRestaurantes.this,R.layout.lista_item, m_restaurantes));			
				break;

			}

			return true; // lo marcamos como procesado
		}
	}
	


private List<Restaurante> obtenerListaTodosRestaurantes()
{
	 ArrayList<Restaurante> lista_restaurantes = new ArrayList<Restaurante>();
	 
	
	 String idRest, post_title,post_author,post_content, guid="";
	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(urltodos);
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
		   HttpEntity entity = response.getEntity();
		   BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
		   InputStream iStream = buffer.getContent();
		                    
		   String aux = "";
		            
		   BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
		   new StringBuilder();
		   String line;
		   while ((line = r.readLine()) != null) {
		     aux += line;
		   }
		   JSONObject jsonObject = new JSONObject(aux);
		   JSONArray restaurantes = jsonObject.getJSONArray("datos");
		  
		   for(int i = 0; i < restaurantes.length(); i++) {
			   JSONObject restaurante_datos = restaurantes.getJSONObject(i);
			   
			    idRest = restaurante_datos.getString("ID");
			    post_title = restaurante_datos.getString("post_title");
			    post_author = restaurante_datos.getString("post_author");
			    post_content = restaurante_datos.getString("post_content");
			    guid = restaurante_datos.getString("guid");
			    Restaurante res = new Restaurante(idRest);
			    res.setContenido(post_content);
			    res.setAuthor(post_author);
			    res.setName(post_title);
			    res.setGuid(guid);
			    res.setRatings_average("0");
			    res.setRating_users("0");
			    lista_restaurantes.add(res);
			    
			    
			   
			    
			
			    
			   
			    //m_restaurantes.add(res);
			    }
	

	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_restaurantes;
}
	

private void obtenerDatosRestaurantes(List<Restaurante> lista_cercanos)
{
	// ArrayList<Restaurante> lista_restaurantes = new ArrayList<Restaurante>();
	 //ArrayList<String> lista_metadato = new ArrayList<String> ();
	 int idRest;
	 Restaurante res;
	 String  post_title, post_author, post_content,guid;
	
	 
	 //int id = Integer.valueOf(idRest);
	 
	 try {
		   
		 	for(int b = 0; b < lista_cercanos.size(); b++) {
		 			res=lista_cercanos.get(b);
		 			idRest = Integer.valueOf(res.getID());
		 			Log.i("idRest",String.valueOf(idRest));
		 			HttpGet httpGet = new HttpGet(url2+"&idRest="+idRest);
		 			HttpClient httpClient = new DefaultHttpClient();
				    HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
				    HttpEntity entity = response.getEntity();
				    BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
				    InputStream iStream = buffer.getContent();
				       
				    String aux = "";
				
				            
				    BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
				    new StringBuilder();
				    String line;
				    while ((line = r.readLine()) != null) {
				     aux += line;
				   }
				    JSONObject jsonObject = new JSONObject(aux);
				    JSONArray listaMetadata = jsonObject.getJSONArray("datos_restauranteX");
				    for(int i = 0; i < listaMetadata.length(); i++) {
					   JSONObject metadatos = listaMetadata.getJSONObject(i);
					   String metakey = metadatos.getString("meta_key");
					   String metavalue = metadatos.getString("meta_value");
					   
					   post_title = metadatos.getString("post_title");
					   res.setName(post_title);
					   post_author = metadatos.getString("post_author");
					   res.setAuthor(post_author);
					   post_content = metadatos.getString("post_content");
					   res.setContenido(post_content);
					   guid = metadatos.getString("guid");
					   res.setGuid(guid);
					   if(metakey.equals("ratings_average"))res.setRatings_average(metavalue);
					   if(metakey.equals("ratings_users"))res.setRatings_average(metavalue);					   
					   if(metakey.equals("web"))res.setWeb(metavalue);
					   if(metakey.equals("telefono"))res.setTfno(metavalue);				   
					   if(metakey.equals("direccion"))res.setDireccion(metavalue);
					   if(metakey.equals("localidad"))res.setLocalidad(metavalue);
					   if(metakey.equals("provincia"))res.setProvincia(metavalue);
					   if(metakey.equals("codigo_postal"))res.setCp(metavalue);
					   if(metakey.equals("lat"))res.setLat(metavalue);
					   if(metakey.equals("long"))res.setLon(metavalue);
					   if(metakey.equals("lista_cocina"))
						   {
						   String lista_cocina = Utils.obtenerLista(metavalue,Constants.cocina);
						   res.setLista_cocina(lista_cocina);
						   }
					   if(metakey.equals("lista_cheque"))
					   		{
						   	String lista_cheques =  Utils.obtenerLista(metavalue,Constants.cheque);
						   	res.setLista_cheque(lista_cheques);
					   		}
					   if(metakey.equals("lista_tiporest"))
						   {
						   String lista_tipoRest= Utils.obtenerLista(metavalue,Constants.tipoRest);
						   res.setLista_tiporest(lista_tipoRest);			   
						   }
					
					    }
				    
		 	}
		  
	
	   }catch (Exception e){
		   e.printStackTrace();
		  
	   }

	
	   
}

	
	private class Adapter extends ArrayAdapter<Restaurante> {

		private ArrayList<Restaurante> items;
		//private DecimalFormat df = new DecimalFormat("0.00"); 

		public Adapter(Context context, int textViewResourceId,
				List<Restaurante> items) {
			super(context, textViewResourceId, items);
			this.items = (ArrayList<Restaurante>) items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lista_item, null);
			}

			final Restaurante p = items.get(position);
		
			if (p != null) {
				
				final TextView nom = (TextView) v.findViewById(R.id.nombreRest);
				nom.setTextSize(20);

				final TextView des = (TextView) v.findViewById(R.id.description);
				

				


				if (des != null) {
					
					nom.setText(p.getName());
				
					
											//des.setText("POIid " + p.getId() + " " + p.getDescription());
					if(p.getContenido().length()>=100)
					des.setText(p.getContenido().substring(0, 100) + Constants.LEERMAS);
					else des.setText(p.getContenido() + Constants.LEERMAS);
					
					
				}

				

			}

			return v;

		}

	}
 

 

 }
