package com.app.comecamino.controlador;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.app.comecamino.modelo.Restaurante;

import com.comecamino.directions.route.Routing;
import com.comecamino.directions.route.RoutingListener;
import com.comecamino.directions.route.Routing.TravelMode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;






/**
 * Clase para representar el punto de localizacion de la tapa y/o evento en el mapa.
 * Extiende de CustomMapActivity
 * @see es.queaplicacion.quebar.CustomMapActivity
 * @version 1.0
 * @author Victoria Marcos
 */
public class DetailMapRestauranteActivity extends  android.support.v4.app.FragmentActivity  implements RoutingListener {
	private ImageButton gotoDetail; 
	private Activity activity;
	private Restaurante mRestaurante;
	final int RQS_GooglePlayServices = 1;
	LatLng positionFinal;
	LatLng positionInicial;
	private Spinner spinner1;
	private TextView tdistancia;
	
	
	

	private GoogleMap mMap = null;
	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;
	private LatLng userLocation = null;
	private String modoTravel="";
	private int color=0;
	
	
	

	

	private SupportMapFragment fm;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		activity=this;
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_maprestaurante_layout);	
		tdistancia = (TextView)findViewById(R.id.distancia) ;
		spinner1 = (Spinner)findViewById(R.id.spinner1) ;
		
		ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.travel_arrays, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		
		
		//addListenerOnSpinnerItemSelection();
		mRestaurante = (Restaurante)getIntent().getExtras().getSerializable("restaurante"); 
		
		

		fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapviewdetail);
		

		mMap = fm.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.setMyLocationEnabled(true);
		
		
		 mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			CustomLocationListener customLocationListener = new CustomLocationListener();
			if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		  	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
		  	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		  }
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
			m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
			LatLng userLocation = new LatLng(m_DeviceLocation.getLatitude(),m_DeviceLocation.getLongitude());
			if(userLocation!=null)
			{
				
				//Log.i("userLocation: ",userLocation.toString());
				positionInicial= new LatLng(userLocation.latitude, userLocation.longitude);
				 mMap.addMarker(new MarkerOptions() 
			        .position(positionInicial)
			        .title("mi posicion")
			        .snippet("mi posicion")
			        .icon(BitmapDescriptorFactory
			               .fromResource(R.drawable.icono_mapa))
			        .anchor(0.5f, 0.5f));
			}
	
		
		positionFinal = new LatLng(Double.parseDouble(mRestaurante.getLat()),Double.parseDouble( mRestaurante.getLon()));
	
		 spinner1.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					
							
					//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
			if(position==0) 
				{
				modoTravel = "walking";
				mostrarRuta(Routing.TravelMode.WALKING,positionInicial, positionFinal );
				}
			if(position==1)
				{
				modoTravel = "biking";
				mostrarRuta(Routing.TravelMode.BIKING,positionInicial, positionFinal );
				}
			if(position==2)
				{
				modoTravel = "driving";
				mostrarRuta(Routing.TravelMode.DRIVING,positionInicial, positionFinal );
				}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					modoTravel="nothing";
					
				}
				
			});
		
	    	
		
		
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFinal,15));
		
		mMap.getUiSettings().setZoomControlsEnabled(true);
	
		mMap.getUiSettings().setCompassEnabled(true);
		
		String descrip ="";
		String title ="";
		
		descrip =  mRestaurante.getContenido();
		title = mRestaurante.getName();
			
		
		 mMap.addMarker(new MarkerOptions() 
	        .position(positionFinal)
	        .title(title)
	        .snippet(descrip)
	        .icon(BitmapDescriptorFactory
	               .fromResource(R.drawable.icono_mapa))
	        .anchor(0.5f, 0.5f));
	
				
		gotoDetail = (ImageButton) findViewById(R.id.gotoDetail);
		gotoDetail.setOnClickListener(new OnClickListener() {

		
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),DetailRestauranteActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("restaurante",mRestaurante);
				startActivity(intent);
				overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);
				activity.finish();
						
			}
		});

	}

	 private void mostrarRuta(TravelMode travelModo, LatLng start, LatLng end)
	 {
		
		 double distancia = redondear(getDistancia(start, end));
		 
		
		 
		 Routing routing = new Routing(travelModo);
		 
	     routing.registerListener(this);	 
	   
	     routing.execute(start, end);
	     
	     tdistancia.setText("distancia " + String.valueOf(distancia) + "kms");
		 
	 }
	 private double redondear(double numero)
	 {
	        return Math.rint(numero*100)/100;
	 }
	 private double getDistancia(LatLng start, LatLng end)
	 {
	 	/*
	 	 * función a la que le pasamos la latitud y longitud del restaurante y  la latitud y longitud de nuestra posicion y nos devolverá una distancia en metros. 
	 	 */
	 	
	    double miLat = start.latitude;
	    double miLong= start.longitude;
	 	double latEnd= end.latitude;
	 	double lonEnd = end.longitude;
	 	
	 
	 	double earthRadius = 6371; //kilometros
	 	double dLat = Math.toRadians(latEnd-miLat);
	 	double dLon = Math.toRadians(lonEnd-miLong);
	 	double sindLat = Math.sin(dLat / 2);  
	     double sindLng = Math.sin(dLon / 2);  
	     double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
	             * Math.cos(Math.toRadians(miLat)) * Math.cos(Math.toRadians(latEnd)); 
	 
	     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));  
	  
	     double dist = earthRadius * c;  
	 	
	 	return dist;

	 	 
	 }

	 @Override
	 protected void onResume() {

	  super.onResume();

	  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	  
	  if (resultCode == ConnectionResult.SUCCESS){
	   //Toast.makeText(getApplicationContext(),"isGooglePlayServicesAvailable SUCCESS",Toast.LENGTH_LONG).show();
	  }else{
	   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
	  }
	  
	 }

	//@Override
	public void onRoutingFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoutingStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoutingSuccess(PolylineOptions mPolyOptions) {
		// TODO Auto-generated method stub
		
		
		if(modoTravel.equals("walking"))color = Color.BLUE;
		if(modoTravel.equals("biking"))color = Color.GREEN;
		if(modoTravel.equals("driving"))color = Color.GRAY;
		if(modoTravel.equals("nothing"))color = Color.TRANSPARENT;
		  PolylineOptions polyoptions = new PolylineOptions();
	      polyoptions.color(color);
	      polyoptions.width(10);
	      polyoptions.addAll(mPolyOptions.getPoints());
	      mMap.addPolyline(polyoptions);

	      // Start marker
	      MarkerOptions options = new MarkerOptions();
	      options.position(positionInicial);
	      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
	      mMap.addMarker(options);

	      // End marker
	      options = new MarkerOptions();
	      options.position(positionFinal);
	      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));  
	      mMap.addMarker(options);
		
	}
	/*private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
	    @Override
	    public void onMyLocationChange(Location location) {
	        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
	        Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
	        if(mMap != null){
	            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
	        }
	    }
	};*/
	
	private class CustomLocationListener implements LocationListener{

		  public void onLocationChanged(Location argLocation) {
			
			  m_DeviceLocation = argLocation;
			  mLocationManager.removeUpdates(this);
			  LatLng loc = new LatLng(m_DeviceLocation.getLatitude(), m_DeviceLocation.getLongitude());
			  Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
		        if(mMap != null){
		            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
		        }
		  }

		  public void onProviderDisabled(String provider) {}

		  public void onProviderEnabled(String provider) {}

		  public void onStatusChanged(String provider,
		    int status, Bundle extras) {}
	 }



}
