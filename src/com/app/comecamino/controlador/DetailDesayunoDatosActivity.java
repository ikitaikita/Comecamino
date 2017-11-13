package com.app.comecamino.controlador;

import java.util.ArrayList;
import java.util.Iterator;
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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailDesayunoDatosActivity extends Activity implements OnClickListener{
	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	


	
	private Handler handler = new Handler(new ResultMessageCallback());
	
	
	//layout
	
	private TextView nombre, titRestaurante, fecha, precio, incluye,tipo, carac, descripcion, horario, tipos_desayuno;
	private TextView textfechaMenu, textprecioMenu, textincluyeMenu,texttipoMenu, textcaracMenu, textentrantesMenu, textprimerosMenu, textsegundosMenu, textpostresMenu, textdescripciond, texthorario, texttiposd;

	private ImageView share;

	private Activity activity;
	private Typeface asethin;
	

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
		setContentView(R.layout.detail_desayuno_layout);
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		CustomLocationListener customLocationListener = new CustomLocationListener();
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
        	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
		m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  
	
		
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
	
		textentrantesMenu=(TextView) findViewById(R.id.textentrantesMenu);
		
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
		
			nombre.setTextSize(30);		
			nombre.setText(mMenu.getNombre());
			titRestaurante.setTextSize(20);
			titRestaurante.setText("Restaurante: " + mMenu.getRestaurante());
			if( mMenu.getPrecio()!=null)
			{
				textprecioMenu.setTextSize(20);
				textprecioMenu.setText("Precio");
				precio.setTextSize(20);
				precio.setText(mMenu.getPrecio());
				
			}
			
			
			if(mMenu.getTipoComanda().equals(Constants.MENU))
			{
				String listaEntrantes="";
				String listaPrimeros="";
				String listaSegundos="";
				String listaPostres="";
				
				if(mMenu.getFecha()!=null)
				{
					textfechaMenu.setTextSize(20);
					textfechaMenu.setText("Fecha");
					fecha.setTextSize(20);
					fecha.setText(mMenu.getFecha());
				}
				
				
				
							
				if(mMenu.getIncluye()!=null) 
					{
					textincluyeMenu.setTextSize(20);
					textincluyeMenu.setText("Incluye");
					incluye.setTextSize(20);
					incluye.setText(mMenu.getIncluye());				
					}
				if(mMenu.getTipoMenu()!=null) 
					{
					texttipoMenu.setTextSize(20);
					texttipoMenu.setText("Tipo de Menu");
					tipo.setTextSize(20);
					tipo.setText(mMenu.getTipoMenu());
					}
				if(mMenu.getCaracteristicas()!=null) 
					{
					textcaracMenu.setTextSize(20);
					textcaracMenu.setText("Caracteristicas");
					carac.setTextSize(20);
					carac.setText(mMenu.getCaracteristicas());
					}
				if(!mMenu.getLista_todos_platos().isEmpty())
				{
					
					ArrayList<Plato> lista = mMenu.getLista_todos_platos();
					Iterator it = lista.iterator();
					while(it.hasNext())
					{
						Plato p = (Plato) it.next();
						if(p.getOrden().equals(Constants.ENTRANTE))listaEntrantes = listaEntrantes + p.getDescripcion();
						if(p.getOrden().equals(Constants.PRIMERO))listaPrimeros = listaPrimeros + p.getDescripcion();
						if(p.getOrden().equals(Constants.SEGUNDO))listaSegundos = listaSegundos + p.getDescripcion();
						if(p.getOrden().equals(Constants.POSTRE))listaPostres = listaPostres +  p.getDescripcion();
					}
					
				}
				
			}else if(mMenu.getTipoComanda().equals(Constants.DESAYUNO))
			{
				textdescripciond.setTextSize(20);
				textdescripciond.setText("Descripcion");
				
				descripcion.setText(mMenu.getDescripcion());
				if(mMenu.getHorario()!=null) 
					{
					texthorario.setTextSize(20);
					texthorario.setText("Horario");
					
					horario.setText(mMenu.getHorario());
					}
				if(!mMenu.getTipos_desayuno().equals(""))
					{
					texttiposd.setTextSize(20);
					texttiposd.setText("Tipo desayuno");
				
					tipos_desayuno.setText(mMenu.getTipos_desayuno());
					}
				
			}
		
		}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
				Toast.makeText(DetailDesayunoDatosActivity.this, "Error",
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
		
											
		
	
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTODESAYUNO +mMenu.getNombre()+ Constants.TEXTOENLACE + mMenu.getGuid());
			    
				
		
		
		
		
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


	}

}
