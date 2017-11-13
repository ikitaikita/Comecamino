package com.app.comecamino.controlador;

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


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.app.comecamino.internal.Constants;
import com.app.comecamino.internal.Utils;
import com.app.comecamino.modelo.Restaurante;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Contenido de la actividad Pestaña 3 del menú que realiza la visualizacion en el mapa de las tapas y eventos cercanos al usuario
 * Extiende de CustomMapActivity e implementa el interfaz OnMarkerClickListener 
 * @see es.queapps.quebar.CustomMapActivity
 * @see com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class MapFiltrarRestaurantes extends android.support.v4.app.FragmentActivity implements OnMarkerClickListener{
	
	final int RQS_GooglePlayServices = 1;
	

	//private ImageThreadLoader imageLoader = new ImageThreadLoader();


	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;

	private LocationManager mLocationManager;
	private Location m_DeviceLocation = null;
	 private Spinner spinnerFiltro;
	private SupportMapFragment fm;
	
	private GoogleMap mMap = null;
	private Handler handler = new Handler(new ResultMessageCallback());

	private String filtro="";
	private ProgressDialog pDialog = null;
	private List<Restaurante> m_restaurantes = null;
	private ArrayList<Restaurante> m_restaurantesAux = null;
    
	private static final String url2 = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";
	 private static final String urltodos = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_restaurantes";
	//private List<POI> m_pois = null;

		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide titlebar of application must be before setting the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.pestana3);		
		  spinnerFiltro = (Spinner)findViewById(R.id.spinnerFiltro) ;
		  ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.filtro_arrays, android.R.layout.simple_spinner_item);
		  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
			//spinnerTipos.setActivated(false);
		  spinnerFiltro.setAdapter(adapter);
		


		//obtenemos mapa
		fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview); 
	    mMap = fm.getMap();
	    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //establecemos tipo de mapa       
	    mMap.setMyLocationEnabled(true);

	    mMap.getUiSettings().setZoomControlsEnabled(true);
	    mMap.getUiSettings().setCompassEnabled(true);


        mMap.setOnMarkerClickListener(this);

        mMap.setInfoWindowAdapter(new InfoWindowAdapter(){ //adaptador de la ventana que sale al pulsar el marcador

			@Override
			public View getInfoContents(Marker marker) {
				// TODO Auto-generated method stub
				View popup =getLayoutInflater().inflate(R.layout.popup_layout, null);

				//ImageView imagen = (ImageView)popup.findViewById(R.id.image);
					
				
				
				TextView tv=(TextView)popup.findViewById(R.id.title);
				TextView tr=(TextView)popup.findViewById(R.id.restdir);
				TextView ts=(TextView)popup.findViewById(R.id.caracteristicasRes);
				TextView tc=(TextView)popup.findViewById(R.id.cocinaRes);
				Restaurante res = m_restaurantes.get(Integer.parseInt(marker
                        .getSnippet()));
				
				
				tv.setText(marker.getTitle());
				ts.setText("caracteristicas: " + res.getLista_tiporest());
				tr.setText("direccion: " + res.getDireccion());	
				tc.setText("cocina: " + res.getLista_cocina());
			    
			   
			    
			
				return popup;
			}

			@Override
			public View getInfoWindow(Marker arg0) {
				
				return null;
			}
        	
        }       
        
        );
    
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
		//pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
	
		//Thread thread = new Thread(new LoadTapentosWorker());
		// thread.start();

		
	}


	private void mostrar(String tipofiltro)
	 {
		 filtro = tipofiltro;
		
	 	 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
		  
			
			 Thread thread = new Thread(new LoadTapentosWorker());
			 thread.start();
	 }


	/**
	 * clase privada para la gestion y actualizacion de la localizacion
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

	

	
	 @Override
	 protected void onResume() {
	
	  super.onResume();

	  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	  
	  if (resultCode == ConnectionResult.SUCCESS){
	   Toast.makeText(getApplicationContext(), 
	     "isGooglePlayServicesAvailable SUCCESS", 
	     Toast.LENGTH_LONG).show();
	  }else{
	   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
	  }
	  
	 }

	    @Override
	    protected void onPause() {
	        fm.onPause();
	        super.onPause();
	    }

	   /* @Override
	    protected void onDestroy() {
	        fm.onDestroy();
	        super.onDestroy();
	    }*/

	    @Override
	    public void onLowMemory() {
	        super.onLowMemory();
	        fm.onLowMemory();
	    }


	


	/**
	 * Añade los marcadores de las tapas y eventos en el mapa después de cargarlas
	 */
	private void drawTapas() {
        
		mMap.clear();
		int i = 0;
		
		if (m_restaurantes != null) {
			for (final Restaurante p : m_restaurantes) {
				
				LatLng markerPosition = new LatLng (Double.parseDouble(p.getLat()) ,Double.parseDouble(p.getLon()));	
				
		
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition,5));				
				String descrip =""; //para la descripcion de la tapa y/o evento
				String id = p.getID();
				descrip = p.getGuid();
				
				//añade el marcador al mapa
				mMap.addMarker(new MarkerOptions()			        
			        .position(markerPosition)
			        .title(p.getName())
			        .snippet(""+i)
			        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_naranja))
			        .anchor(0.5f, 0.5f));
				i++;
				
			}
		}
		
	}
	

	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(MapFiltrarRestaurantes.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case MESSAGE_OK:
				//Log.i("drawTapas:", "drawTapas");
				drawTapas();
				break;
			}

			return true; // lo marcamos como procesado
		}
	}




	/**
	 * clase privada para la carga de tapas y eventos en segundo plano o background
	 * implementa Runnable
	 * @see  java.lang.Runnable
	 */
	private class LoadTapentosWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				Thread.sleep(1000);
				if (filtro.equals("todos"))
				{
					
					m_restaurantes =( ArrayList<Restaurante>)obtenerListaTodosRestaurantes();
					//Log.i("m_restaurantes size",String.valueOf(m_restaurantes.size()));
				
					obtenerDatosRestaurantes(m_restaurantes);
					//Log.i("despues de obtener datos m_restaurantes size",String.valueOf(m_restaurantes.size()));
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
				Log.e("Upload", e.getMessage());
			}
           
			handler.sendEmptyMessage(messageReturn);
		}
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

	@Override
	public boolean onMarkerClick(Marker marcador) {

	  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador.getPosition(),9));
      marcador.showInfoWindow();
      
	  return true;
	}
	

}


