package com.app.comecamino.controlador;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.app.comecamino.internal.Constants;
import com.app.comecamino.internal.Utils;
import com.app.comecamino.modelo.Menu;
import com.app.comecamino.modelo.Plato;
import com.app.comecamino.modelo.Restaurante;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DetailMenuRestauranteActivity extends ListActivity{
	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	private static final int WORKER_NO_MENUS = -2;
	
	 private static final String urltodosmenus = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_menusRestauranteX";
	 private static final String urltodosmenusdesayunos = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_menusDesayunosRestauranteX";
	 //private static final String urltodosdesayunos = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_desayunosRestauranteX";
	 private static final String url_menu = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_datos_menuX";
	// private static final String url_desayuno = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_datos_desayunoX";
	 private static final String url_plato ="http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_datos_platoX";
	 

	//private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	
	 private Button atras;
	//layout
	
	


	private ImageView share;
	private ListView lv;

	private Activity activity;
	
	
	//private Activity activity;
	private Restaurante mRestaurante;	
	private String idRestaurante;
	
	private List<Menu> l_menus_total = null;
	private List<Menu> l_menus = null;
	private List<Menu> l_celiacos = null;
	private List<Menu> l_diabeticos = null;
	private boolean celiacos, diabeticos = false;

	public final int MESSAGE_ERROR = -1;
    public final int MESSAGE_OK = 1;
    private ProgressDialog pDialog;	
	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//activity = this;
		setContentView(R.layout.activity_traer_menus);
		
		
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		CustomLocationListener customLocationListener = new CustomLocationListener();
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
        	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
		m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		mRestaurante= (Restaurante) getIntent().getExtras().getSerializable("restaurante");
		pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoMenus));
		 Thread thread = new Thread(new LoadMenusRestaurante());
		 thread.start();
		 lv = getListView();
		//mRestaurante= (Restaurante) getIntent().getSerializableExtra("restaurante");
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					//int fixedpos = arg2 - 1;
					int fixedpos = arg2;
					//Log.i("fixedpos: " , String.valueOf(fixedpos));
					
					
					
					
					if(l_menus.get(fixedpos).getTipoComanda().equals(Constants.DESAYUNO))
					{
						//Log.i("ES TIPO DESAYUNO","ES TIPO DESAYUNO");
						Intent intent_desayuno= new Intent(DetailMenuRestauranteActivity.this,DetailDesayunoDatosActivity.class); 
						intent_desayuno.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent_desayuno.putExtra("menu", l_menus.get(fixedpos));
						 startActivity(intent_desayuno);
					}else if(l_menus.get(fixedpos).getTipoComanda().equals(Constants.MENU))
					{
						//Log.i("ES TIPO MENU","ES TIPO MENU");
						Intent intent= new Intent(DetailMenuRestauranteActivity.this,DetailMenuDatosActivity.class); 
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("menu", l_menus.get(fixedpos));
						 startActivity(intent);
					}
					
					
	               
	                overridePendingTransition(R.anim.scale_from_corner,
							R.anim.scale_to_corner);
	                               
				}
			});
		
			
			
	
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
		
	
		
		
		private class LoadMenusRestaurante implements Runnable  {
	        @SuppressLint("NewApi")
			@Override
	        
	        public void run() {

				int messageReturn = WORKER_MSG_OK;

				try {
					Thread.sleep(1000);
					l_menus =( ArrayList<Menu>)obtenerListaMenus();
					//Log.i("l_menus conseguido","l_menus conseguido");
					
					obtenerDatosMenus(l_menus);
					l_menus_total = l_menus;
					obtenerCeliacosDiabeticos(l_menus);
					//if(l_celiacos!=null)Log.i("lista de celiacos obtenida: ", String.valueOf(l_celiacos.size()));
					//if(l_diabeticos!=null)Log.i("lista de diabeticos obtenida: ", String.valueOf(l_diabeticos.size()));
					
					
					if(l_menus.size()==0) messageReturn =  WORKER_NO_MENUS;
					
					//Log.i("se han obtenido todos los menus , total: ",String.valueOf(l_menus.size()) );

				} catch (Exception e) {
					e.printStackTrace();
					messageReturn =  WORKER_MSG_ERROR;
				
				}

				handler.sendEmptyMessage(messageReturn);
			}
	      

	    }
		
		private class AdapterMenu extends ArrayAdapter<Menu> {

			private ArrayList<Menu> items;
			//private DecimalFormat df = new DecimalFormat("0.00"); 

			public AdapterMenu(Context context, int textViewResourceId, List<Menu> items) {
				super(context, textViewResourceId, items);
				this.items = (ArrayList<Menu>) items;

			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				 View v = convertView;

				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.lista_item_menu, null);
				}

				final Menu p = items.get(position);
				//if(p.getName().equals("evento")) v.setBackgroundColor(Color.parseColor("#2BB14F"));
				//else v.setBackgroundColor(Color.parseColor("#8DC1C1"));

				if (p != null) {
					
					
					if(p.getTipoComanda().equals(Constants.DESAYUNO))
				    {
						 final ImageView imagen = (ImageView) v.findViewById(R.id.image);
				    	 int id = getResources().getIdentifier("icono_desayuno", "drawable", getPackageName());						      
				    	 imagen.setImageResource(id);
				    	
				    	
				    }

					final TextView des = (TextView) v.findViewById(R.id.description);
					

					


					if (des != null) {
						
					
						
												//des.setText("POIid " + p.getId() + " " + p.getDescription());
						des.setText(p.getNombre());
					}

					

				}

				return v;

			}

		}
		 
		 private List<Menu> obtenerListaMenus()
		 {
		 	 ArrayList<Menu> lista_menus = new ArrayList<Menu>();
		 	 
		 	
		 	 String idMenu, post_title,post_author, post_type, guid="";
		 	
		 	
		 	 try {
		 		   
		 		   HttpGet httpGet = new HttpGet(urltodosmenusdesayunos+"&idRest="+mRestaurante.getID());
		 		  
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
		 		   JSONArray menus = jsonObject.getJSONArray("menus_desayunos_restaurante");
		 		  
		 		   for(int i = 0; i < menus.length(); i++) {
		 			   JSONObject menu_datos = menus.getJSONObject(i);
		 			   
		 			    idMenu = menu_datos.getString("ID");
		 			    post_title = menu_datos.getString("post_title");
		 			     post_author= menu_datos.getString("post_author");
		 			     post_type = menu_datos.getString("post_type");
		 			   guid = menu_datos.getString("guid");
		 			   
		 			    Menu menu = new Menu(idMenu);
		 			    if(post_type.equals("menude")) menu.setTipoComanda(Constants.MENU);
		 			    else if(post_type.equals("desayuno")) menu.setTipoComanda(Constants.DESAYUNO);
		 			    menu.setNombre(post_title);
		 			    menu.setAutor(post_author);
		 			    menu.setGuid(guid);
		 			    menu.setRestaurante(mRestaurante.getName());
		 			    lista_menus.add(menu);
		 			    }
		 		 

		 	   }catch (Exception e){
		 		   e.printStackTrace();
		 	   }

		 	 
		 	   return lista_menus;
		 }
		 
		

	   /**
     * Clase privada para la gestion de la actividad DetailTapentoActivity
     * @author Victoria Marcos
     *
     */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			// Cerramos la pantalla de progreso

			switch (arg0.what) {
			case WORKER_MSG_ERROR:
				Toast.makeText(DetailMenuRestauranteActivity.this, "Error",
						Toast.LENGTH_LONG).show();
				break;
			case WORKER_MSG_OK:
				
				setListAdapter(new AdapterMenu(DetailMenuRestauranteActivity.this,R.layout.lista_item_menu, l_menus));			
				break;
			
			case WORKER_NO_MENUS:
				
				Toast.makeText(DetailMenuRestauranteActivity.this, "Este restaurante no tienes menús y o desayunos publicados",
						Toast.LENGTH_LONG).show();		
				break;
			}

			pDialog.dismiss();
			return true; 
		}
	}
	
	@SuppressWarnings("unused")
	private void obtenerDatosMenus(List<Menu> lista_menus)
	{
		 int idMenu;
		 Menu menu;
		 
		 try {
			 for(int b = 0; b < lista_menus.size(); b++) {
				 	menu=lista_menus.get(b);
		 			idMenu = Integer.valueOf(menu.getID());
		 			HttpGet httpGet = new HttpGet(url_menu+"&idMenu="+idMenu);
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
				    JSONArray listaMetadata = jsonObject.getJSONArray("datos_menuX");
				    for(int i = 0; i < listaMetadata.length(); i++) {
					   JSONObject metadatos = listaMetadata.getJSONObject(i);
					   String metakey = metadatos.getString("meta_key");
					   String metavalue = metadatos.getString("meta_value");
					   
					   if(metakey.equals("fecha_menu"))menu.setFecha(metavalue);
					   if(metakey.equals("precio"))menu.setPrecio(metavalue);				   
					   if(metakey.equals("incluye"))menu.setIncluye(metavalue);
					   if(metakey.equals("lista_entrantes"))
						   {
						   List<String> entrantes = Utils.obtenerIdsListaEntrada(metavalue);
						   menu.setEntrantes(entrantes.toString());
						   }
					   if(metakey.equals("lista_primeros"))
						   {
						   List<String> primeros = Utils.obtenerIdsListaEntrada(metavalue);
						   menu.setPrimeros(primeros.toString());
						   }
					   if(metakey.equals("lista_segundos"))
						   {
						   List<String> segundos = Utils.obtenerIdsListaEntrada(metavalue);
						   menu.setSegundos(segundos.toString());
						   }
					   if(metakey.equals("lista_postres"))
						   {
						   List<String> postres = Utils.obtenerIdsListaEntrada(metavalue);
						   menu.setPostres(postres.toString());	 
						   }
					   if(metakey.equals("todos_platos"))
						   {
						   List<String> todos_platos = Utils.obtenerIdsListaEntrada(metavalue);
						   ArrayList<Plato> lista_todos_platos = obtenerDatosPlatos(todos_platos);
						   menu.setLista_todos_platos(lista_todos_platos);
						   menu.setPlatos(todos_platos.toString());	 
						   }
					   if(metakey.equals("tipos_menu"))
						   {
						   String lista_tiposmenus = Utils.obtenerLista(metavalue,Constants.tipoMenu);						   
						   menu.setTipoMenu(lista_tiposmenus);
						   }
					   if(metakey.equals("caracs_menu"))
						   {
						   String lista_caracsmenus = Utils.obtenerLista(metavalue,Constants.caracMenu);	
						   menu.setCaracteristicas(lista_caracsmenus);			   
						   }
					   if(metakey.equals("descripcion"))menu.setDescripcion(metavalue);
					   if(metakey.equals("horario"))menu.setHorario(metavalue);
					  
					   if(metakey.equals("tipos_desayuno"))
						   {
						   String lista_tiposdesayuno = Utils.obtenerLista(metavalue,Constants.tipoDesayuno);						   
						   menu.setTipos_desayuno(lista_tiposdesayuno);
						   }
					   
					   //if(metakey.equals("sel_restaurante"))menu.setRestaurante(metavalue);			   
					
					    }
			 
			 }
			  
			   

		   }catch (Exception e){
			   e.printStackTrace();
			   
		   }

		
		   
	}
	private void obtenerCeliacosDiabeticos(List<Menu> lista_menus)
	{
		l_celiacos = new ArrayList<Menu>();
		l_diabeticos = new ArrayList<Menu>();
		Iterator it_menu = lista_menus.iterator();
		while(it_menu.hasNext())
		{
			Menu menu = (Menu) it_menu.next();
			if(menu.getTipoComanda().equals(Constants.MENU))
			{
				
				if(menu.getLista_todos_platos()!=null)
				{
					
					
					ArrayList<Plato> lista_platos = menu.getLista_todos_platos();
					for(int i =0; i<lista_platos.size(); i++)
					{
					
						if(lista_platos.get(i).getTipos().contains(Constants.TPLATO1))
						{
							
						
												
							if(!l_celiacos.contains(menu))l_celiacos.add(menu);
						}if(lista_platos.get(i).getTipos().contains(Constants.TPLATO2))
						{
							
							
							if(!l_diabeticos.contains(menu))l_diabeticos.add(menu);
						}
					}
				}
				
			}else if(menu.getTipoComanda().equals(Constants.DESAYUNO))
			{
			
				if(!menu.getTipos_desayuno().equals(""))
				{
				
					if(menu.getTipos_desayuno().contains(Constants.TDESAYUNO1))
					{
						if(!l_celiacos.contains(menu))l_celiacos.add(menu);
					}if(menu.getTipos_desayuno().contains(Constants.TDESAYUNO2))
					{
						if(!l_diabeticos.contains(menu))	l_diabeticos.add(menu);
					}
				}
				
			}
		}
		
		
	}


	private ArrayList<Plato> obtenerDatosPlatos(List<String> lista_ids_platos)
	{
		
		 int idPlato;
		 ArrayList<Plato> lista_platos = new ArrayList<Plato>();
	
		
		 try {
			 for(int b = 0; b < lista_ids_platos.size(); b++) {
				 	//menu=lista_menus.get(b);
		 			idPlato = Integer.valueOf(lista_ids_platos.get(b));
		 			Plato plato = new Plato(lista_ids_platos.get(b));
		 			HttpGet httpGet = new HttpGet(url_plato+"&idPlato="+idPlato);
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
				    JSONArray listaMetadata = jsonObject.getJSONArray("datos_platoX");
				    for(int i = 0; i < listaMetadata.length(); i++) {
					   JSONObject metadatos = listaMetadata.getJSONObject(i);
					   String metakey = metadatos.getString("meta_key");
					   String metavalue = metadatos.getString("meta_value");
					   String post_title = metadatos.getString("post_title");
					   plato.setNombre(post_title);
					   if(metakey.equals("calorias"))plato.setCalorias(metavalue);
					   if(metakey.equals("descripcion"))plato.setDescripcion(metavalue);
					   
					   if(metakey.equals("tipos"))
					   {
					   String lista_tipos_plato = Utils.obtenerLista(metavalue,Constants.tipoPlato);
					   plato.setTipos(lista_tipos_plato);
					   }
					   if(metakey.equals("orden"))plato.setOrden(metavalue);  
					
					    }
			    
				    lista_platos.add(plato);
			 }
			  
			   

		   }catch (Exception e){
			   e.printStackTrace();
			   
		   }
		return lista_platos;
		   
	}
	
}
