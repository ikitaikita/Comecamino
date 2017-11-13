package com.app.comecamino.controlador;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.app.comecamino.modelo.Menu;
import com.app.comecamino.modelo.Restaurante;
import android.annotation.SuppressLint;
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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailRestauranteActivity extends Activity implements OnClickListener{
	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	private static final String url = "http://www.esunescandalo.com/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";


	//private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	
	
	//layout
	
	private TextView nombre, description, direccion, cocina, carac,cheques, tfno;
	private TextView textDireccion, texttfnoRestaurante,textcocinaRestaurante, textcaracRestaurante,textchequeRestaurante   ;
	Button Menu, bWeb;
	
	
	//private ImageView pointPlus;
	//private ImageView pointLess;
	private ImageView share;
	//private TextView nVotesPlus;
	//private TextView nVotesMinus;
	private RatingBar ratingBar;
	private Activity activity;
	
	private ImageButton gotoMap;
	//private Activity activity;
	private Restaurante mRestaurante;
	
	private float puntos_rating;
	public final int MESSAGE_ERROR = -1;
    public final int MESSAGE_OK = 1;
    private ProgressDialog pDialog;	
	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		setContentView(R.layout.detail_restaurante_layout);
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
		description = (TextView) findViewById(R.id.descriptionRestaurante);
		nombre = (TextView) findViewById(R.id.nombreRest);
		direccion =(TextView) findViewById(R.id.dirRestaurante);
		textDireccion = (TextView) findViewById(R.id.textdirRestaurante);
		
		cocina=(TextView) findViewById(R.id.cocinaRestaurante);
		textcocinaRestaurante = (TextView) findViewById(R.id.textcocinaRestaurante);
		carac =(TextView) findViewById(R.id.caractRestaurante);
		textcaracRestaurante = (TextView) findViewById(R.id.textcaracRestaurante);
		cheques=(TextView) findViewById(R.id.chequeRestaurante);
		textchequeRestaurante= (TextView) findViewById(R.id.textchequeRestaurante);
		//web=(TextView) findViewById(R.id.webRestaurante);
		//textwebRestaurante = (TextView) findViewById(R.id.textwebRestaurante);
		tfno=(TextView) findViewById(R.id.tfnoRestaurante);
		texttfnoRestaurante= (TextView) findViewById(R.id.texttfnoRestaurante);
		Menu = (Button)findViewById(R.id.buttonMenus);
		bWeb = (Button)findViewById(R.id.buttonWeb);
		//Desayuno = (Button)findViewById(R.id.buttonDesayunos);
		//myWebView = (WebView) this.findViewById(R.id.webView);
		ratingBar = (RatingBar) findViewById(R.id.ratingRestaurante);
		puntos_rating=0;
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		
		description.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		mRestaurante= (Restaurante) getIntent().getExtras().getSerializable("restaurante");
		if(mRestaurante!=null)
		{
			
			ponerDatosEnView();
		
		}
		gotoMap = (ImageButton) findViewById(R.id.gotoMap);
		gotoMap.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		

			Intent intent = new Intent(getApplicationContext(),
					DetailMapRestauranteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("restaurante", mRestaurante);		
			

			startActivity(intent);
			overridePendingTransition(R.anim.scale_from_corner,
					R.anim.scale_to_corner);
			activity.finish();

		}
	});
		
		Menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			

				Intent intent = new Intent(getApplicationContext(),
						DetailMenuRestauranteActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				
				intent.putExtra("restaurante", mRestaurante);		
				
				

				startActivity(intent);
				overridePendingTransition(R.anim.scale_from_corner,
						R.anim.scale_to_corner);
				activity.finish();

			}
		});
	
		
		bWeb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			

				Intent intent = new Intent(getApplicationContext(),
						DetailWebRestauranteActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				
				intent.putExtra("restaurante", mRestaurante);		
				
				

				startActivity(intent);
				overridePendingTransition(R.anim.scale_from_corner,
						R.anim.scale_to_corner);
				activity.finish();

			}
		});

	
	}
	
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
		
		@SuppressLint("ResourceAsColor")
		private void ponerDatosEnView()
		{
			//asethin = Typeface.createFromAsset(getAssets(),"fonts/Azrael.ttf");
			//nombre.setTypeface(asethin);
			nombre.setTextSize(30);			
			nombre.setText(mRestaurante.getName());
			
			description.setText(mRestaurante.getContenido());
			//description.setTextSize(20);
			
			
						
			if(mRestaurante.getDireccion()!=null) 
				{
				textDireccion.setTextSize(20);
				textDireccion.setText("Direccion");
				//direccion.setTextSize(20);
				direccion.setText(mRestaurante.getDireccion() + " " + mRestaurante.getLocalidad());				
				}
			
			if(mRestaurante.getTfno()!=null) 
				{
				texttfnoRestaurante.setTextSize(20);
				texttfnoRestaurante.setText("Teléfono");
				//tfno.setTextSize(20);
				tfno.setText(mRestaurante.getTfno());
				}
			if(mRestaurante.getLista_cocina()!=null) 
			{
				if(!mRestaurante.getLista_cocina().equals("")) 
				{
				textcocinaRestaurante.setTextSize(20);
				textcocinaRestaurante.setText("Tipo de Cocina");
				//cocina.setTextSize(20);
				cocina.setText(mRestaurante.getLista_cocina());
				}
			}
			
				
			if(mRestaurante.getLista_tiporest()!=null) 
			{
				if(!mRestaurante.getLista_tiporest().equals("")) 
				{
				textcaracRestaurante.setTextSize(20);
				textcaracRestaurante.setText("Caracteristicas");
				//carac.setTextSize(20);
				carac.setText(mRestaurante.getLista_tiporest());
				}
			}
			
			if(mRestaurante.getLista_cheque()!=null) 
			{
				if(!mRestaurante.getLista_cheque().equals("")) 
				{
				textchequeRestaurante.setTextSize(20);
				textchequeRestaurante.setText("Acepta Cheques");
				//cheques.setTextSize(20);
				cheques.setText(mRestaurante.getLista_cheque());
				}
				
			}
			
			
			
			if(mRestaurante.getRatings_average()!=null)
			{
				puntos_rating = Float.parseFloat(mRestaurante.getRatings_average());
				ratingBar.setRating(puntos_rating);
			}
			
		}
		


	@SuppressWarnings("unused")
	private Boolean obtenerDatosRestaurante(String idRest, Restaurante res)
	{
		// ArrayList<Restaurante> lista_restaurantes = new ArrayList<Restaurante>();
		 //ArrayList<String> lista_metadato = new ArrayList<String> ();
		 String lista_metadato = "";
		 String metavalue="";
		 boolean resultadoObtener = false;
		 int id = Integer.valueOf(idRest);
		 
		 try {
			   
			  
			   HttpGet httpGet = new HttpGet(url+"&idRest="+id);
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
				 
				   if(metakey.equals("lista_cocina")|| metakey.equals("lista_cheque")||metakey.equals("lista_tiporest"))
					   lista_metadato =  metadatos.getString("meta_value");
				   else  metavalue = metadatos.getString("meta_value");
				   if(metakey.equals("web"))res.setWeb(metavalue);
				   if(metakey.equals("guid"))res.setGuid(metavalue);
				   if(metakey.equals("telefono"))res.setTfno(metavalue);				   
				   if(metakey.equals("direccion"))res.setDireccion(metavalue);
				   if(metakey.equals("localidad"))res.setLocalidad(metavalue);
				   if(metakey.equals("provincia"))res.setProvincia(metavalue);
				   if(metakey.equals("codigo_postal"))res.setCp(metavalue);
				   if(metakey.equals("lat"))res.setLat(metavalue);
				   if(metakey.equals("long"))res.setLon(metavalue);
				   if(metakey.equals("lista_cocina"))res.setLista_cocina(lista_metadato);
				   if(metakey.equals("lista_cheque"))res.setLista_cheque(lista_metadato);
				   if(metakey.equals("lista_tiporest"))res.setLista_tiporest(lista_metadato);			   
				
				    }
			   resultadoObtener = true;
			   
			
		   }catch (Exception e){
			   e.printStackTrace();
			   return false;
		   }

		 return resultadoObtener;
		   
	}

	@Override
	public void onClick(View v) {
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
				Toast.makeText(DetailRestauranteActivity.this, "Error",
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
		
											
		
	
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTORESTAURANTE + mRestaurante.getName()+ Constants.TEXTOENLACE+ mRestaurante.getGuid());
			    
				
		
		
		
		
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


	}

}
