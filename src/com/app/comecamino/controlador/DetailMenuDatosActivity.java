package com.app.comecamino.controlador;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.comecamino.internal.Constants;
import com.app.comecamino.modelo.Menu;
import com.app.comecamino.modelo.Plato;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailMenuDatosActivity extends Activity implements OnClickListener{
	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	


	//private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	
	
	//layout
	
	private TextView nombre, titRestaurante, fecha, precio, incluye,tipo, carac,entrantes, primeros, segundos, postres, descripcion, horario, tipos_desayuno;
	private TextView textfechaMenu, textprecioMenu, textincluyeMenu,texttipoMenu, textcaracMenu, textentrantesMenu, textprimerosMenu, textsegundosMenu, textpostresMenu, textdescripciond, texthorario, texttiposd;
	private ListView listadoPrimerosPlatos, listadoEntrantesPlatos, listadoSegundosPlatos, listadoPostresPlatos;

	private ImageView share;

	private Activity activity;
	private Typeface asethin;
	//private ArrayList platos;

	private Menu mMenu;
	
	

	
	public final int MESSAGE_ERROR = -1;
    public final int MESSAGE_OK = 1;
    private ProgressDialog pDialog;	
	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		setContentView(R.layout.detail_menu_layout);
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		CustomLocationListener customLocationListener = new CustomLocationListener();
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
        	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
		m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  
		//pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
		
		//mRestaurante= (Restaurante) getIntent().getSerializableExtra("restaurante");
		
		nombre = (TextView) findViewById(R.id.nombreMenu);
		titRestaurante = (TextView) findViewById(R.id.nombreRestaurante);
		
		fecha =(TextView) findViewById(R.id.fechaMenu);
		textfechaMenu=(TextView) findViewById(R.id.textfechaMenu);		
		precio=(TextView)findViewById(R.id.precio);
		textprecioMenu=(TextView) findViewById(R.id.textprecioMenu);
		incluye=(TextView) findViewById(R.id.incluye);
		textincluyeMenu=(TextView) findViewById(R.id.textincluyeMenu);
		tipo=(TextView) findViewById(R.id.tipoMenu);
		texttipoMenu=(TextView) findViewById(R.id.texttipoMenu);
		carac =(TextView) findViewById(R.id.caracMenu);
		textcaracMenu=(TextView) findViewById(R.id.textcaracMenu);
		//entrantes=(TextView) findViewById(R.id.entrantes);
		textentrantesMenu=(TextView) findViewById(R.id.textentrantesMenu);
		//primeros=(TextView) findViewById(R.id.primeros);
		textprimerosMenu=(TextView) findViewById(R.id.textprimerosMenu);
		//segundos=(TextView) findViewById(R.id.segundos);
		textsegundosMenu=(TextView) findViewById(R.id.textsegundosMenu);
		//postres=(TextView) findViewById(R.id.postres);
		textpostresMenu=(TextView) findViewById(R.id.textpostresMenu);
		descripcion =(TextView) findViewById(R.id.descripciond);
		textdescripciond=(TextView) findViewById(R.id.textdescripciond);
		horario=(TextView) findViewById(R.id.horario);
		texthorario=(TextView) findViewById(R.id.texthorario);
		tipos_desayuno=(TextView) findViewById(R.id.tiposd);
		texttiposd=(TextView) findViewById(R.id.texttiposd);
		listadoPrimerosPlatos = (ListView)findViewById(R.id.listPrimerosPlatos);
		listadoEntrantesPlatos = (ListView)findViewById(R.id.listEntrantesPlatos);
		listadoSegundosPlatos = (ListView)findViewById(R.id.listSegundosPlatos);
		listadoPostresPlatos = (ListView)findViewById(R.id.listPostresPlatos);
		//myWebView = (WebView) this.findViewById(R.id.webView);
		
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		
		//description.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		mMenu= (Menu) getIntent().getExtras().getSerializable("menu");
		
		if(mMenu!=null)
		{
			
			//Log.i("mMenu",mMenu.getNombre());
			ponerDatosEnView();

		}
	
		
	
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
		
		private void ponerDatosEnView()
		{
			//asethin = Typeface.createFromAsset(getAssets(),"fonts/Azrael.ttf");
			//nombre.setTypeface(asethin);
			nombre.setTextSize(30);		
			nombre.setText(mMenu.getNombre());
			titRestaurante.setTextSize(20);
			titRestaurante.setText("Restaurante: " + mMenu.getRestaurante());
			if( mMenu.getPrecio()!=null)
			{
				textprecioMenu.setTextSize(20);
				textprecioMenu.setText("Precio");
				//precio.setTextSize(20);
				precio.setText(mMenu.getPrecio());
				
			}
			
			
			if(mMenu.getTipoComanda().equals(Constants.MENU))
			{
				String listaEntrantes="";
				String listaPrimeros="";
				
				String listaSegundos="";
				String listaPostres="";
				ArrayList<Plato> listaPrimerosPlatos = new ArrayList<Plato>();
				ArrayList<Plato> listaEntrantesPlatos = new ArrayList<Plato>();
				ArrayList<Plato> listaSegundosPlatos = new ArrayList<Plato>();
				ArrayList<Plato> listaPostresPlatos = new ArrayList<Plato>();
				
				if(mMenu.getFecha()!=null)
				{
					textfechaMenu.setTextSize(20);
					textfechaMenu.setText("Fecha");
					//fecha.setTextSize(20);
					fecha.setText(mMenu.getFecha());
				}
				
				
				
							
				if(mMenu.getIncluye()!=null) 
					{
					textincluyeMenu.setTextSize(20);
					textincluyeMenu.setText("Incluye");
					//incluye.setTextSize(20);
					incluye.setText(mMenu.getIncluye());				
					}
				if(mMenu.getTipoMenu()!=null) 
					{
					texttipoMenu.setTextSize(20);
					texttipoMenu.setText("Tipo de Menu");
					//tipo.setTextSize(20);
					tipo.setText(mMenu.getTipoMenu());
					}
				if(mMenu.getCaracteristicas()!=null) 
					{
					textcaracMenu.setTextSize(20);
					textcaracMenu.setText("Caracteristicas");
					//carac.setTextSize(20);
					carac.setText(mMenu.getCaracteristicas());
					}
				if(!mMenu.getLista_todos_platos().isEmpty())
				{
					
					ArrayList<Plato> lista = mMenu.getLista_todos_platos();
					
					
					
					Iterator it = lista.iterator();
					while(it.hasNext())
					{
						Plato p = (Plato) it.next();
						if(p.getOrden().equals(Constants.ENTRANTE))
							{
							listaEntrantesPlatos.add(p);
							listaEntrantes = listaEntrantes + p.getDescripcion()+ ", ";
							}
						if(p.getOrden().equals(Constants.PRIMERO))
							{
							listaPrimerosPlatos.add(p);
							listaPrimeros = listaPrimeros + p.getDescripcion()+ ", ";
							}
						if(p.getOrden().equals(Constants.SEGUNDO))
							{
							listaSegundosPlatos.add(p);
							listaSegundos = listaSegundos + p.getDescripcion()+ ", ";
							}
						if(p.getOrden().equals(Constants.POSTRE))
							{
							listaPostresPlatos.add(p);
							listaPostres = listaPostres +  p.getDescripcion()+ ", ";
							}
					}
					
				}
				if(!listaEntrantes.equals(""))
					{
					textentrantesMenu.setTextSize(20);
					textentrantesMenu.setText("Entrantes");
					//Log.i("tamaño entrantes platos: ",String.valueOf(listaEntrantesPlatos.size()));
					CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.lista_item_plato,listaEntrantesPlatos);
					listadoEntrantesPlatos.setAdapter(adapter);
					
					}
				if(!listaPrimeros.equals(""))
					{
					textprimerosMenu.setTextSize(20);
					textprimerosMenu.setText("Primeros");
					//Log.i("tamaño primeros platos: ",String.valueOf(listaPrimerosPlatos.size()));
					CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.lista_item_plato,listaPrimerosPlatos);
					listadoPrimerosPlatos.setAdapter(adapter);
				
					}
				if(!listaSegundos.equals(""))
					{
					textsegundosMenu.setTextSize(20);
					textsegundosMenu.setText("Segundos");
					//Log.i("tamaño segundos platos: ",String.valueOf(listaSegundosPlatos.size()));
					CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.lista_item_plato,listaSegundosPlatos);
					listadoSegundosPlatos.setAdapter(adapter);
				
					}
				if(!listaPostres.equals(""))
					{
					textpostresMenu.setTextSize(20);
					textpostresMenu.setText("Postres");
					//Log.i("tamaño postres platos: ",String.valueOf(listaPostresPlatos.size()));
					CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.lista_item_plato,listaPostresPlatos);
					listadoPostresPlatos.setAdapter(adapter);
					
					}
			}else if(mMenu.getTipoComanda().equals(Constants.DESAYUNO))
			{
				textdescripciond.setTextSize(20);
				textdescripciond.setText("Desccripcion");
				descripcion.setTextSize(20);
				descripcion.setText(mMenu.getDescripcion());
				if(mMenu.getHorario()!=null) 
					{
					texthorario.setTextSize(20);
					texthorario.setText("Horario");
					horario.setTextSize(20);
					horario.setText(mMenu.getHorario());
					}
				if(!mMenu.getTipos_desayuno().equals(""))
					{
					texttiposd.setTextSize(20);
					texttiposd.setText("Tipo desayuno");
					tipos_desayuno.setTextSize(20);
					tipos_desayuno.setText(mMenu.getTipos_desayuno());
					}
				
			}
		
			
	
			
			
		}
		
	
		private class CustomListViewAdapter extends ArrayAdapter<Plato> {
			 
		    Context context;
		 
		    public CustomListViewAdapter(Context context, int resourceId,
		            List<Plato> items) {
		        super(context, resourceId, items);
		        this.context = context;
		    }
		     
		    /*private view holder class*/
		    private class ViewHolder {
		        
		        TextView nombrePlato;
		        TextView descPlato;
		    }
		     
		    public View getView(int position, View convertView, ViewGroup parent) {
		        ViewHolder holder = null;
		        Plato rowItem = getItem(position);
		         
		        LayoutInflater mInflater = (LayoutInflater) context
		                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		        if (convertView == null) {
		            convertView = mInflater.inflate(R.layout.lista_item_plato, null);
		            holder = new ViewHolder();
		            holder.nombrePlato = (TextView) convertView.findViewById(R.id.nombrePlato);
		            holder.descPlato = (TextView) convertView.findViewById(R.id.descPlato);
		           
		            convertView.setTag(holder);
		        } else
		            holder = (ViewHolder) convertView.getTag();
		                 
		        holder.nombrePlato.setText(rowItem.getNombre());
		        holder.descPlato.setText(rowItem.getTipos());
		     
		         
		        return convertView;
		    }
		}

	

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Thread thread;
		switch (v.getId()) {

		

		case R.id.share:
			showOneDialog();
		default:
			break;
		}
		
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
				Toast.makeText(DetailMenuDatosActivity.this, "Error",
						Toast.LENGTH_LONG).show();
				break;
			case WORKER_MSG_OK:
				
				ponerDatosEnView();
				break;
			}

			pDialog.dismiss();
			return true; 
		}
	}
	
	private void showOneDialog() {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);	
		
		sharingIntent.setType("text/plain");
		
											
		
	
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTOMENU +mMenu.getNombre()+ Constants.TEXTOENLACE + mMenu.getGuid());
			    
				
		
		
		
		
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


	}

}
