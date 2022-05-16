package com.abreuretto.PlacesHere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;



public class MapaGeralActivity extends FragmentActivity  {

	  SharedPreferences shared_preferences;
	  SharedPreferences.Editor shared_preferences_editor;
    String latx = null;
    String lonx = null;

    ArrayList<com.abreuretto.PlacesHere.AppMapa> georef;
    GoogleMap googleMap;
    private static final LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);
    private StreetViewPanorama mSvp;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa_geral);	
		
		
		
        shared_preferences = getSharedPreferences("abreuretto", MODE_PRIVATE);
	    latx = shared_preferences.getString("lat", "");
	    lonx = shared_preferences.getString("lon", ""); 
		
		//ArrayList<AppMapa> georef = (ArrayList<AppMapa>) getIntent().getSerializableExtra("StudentObject");
		
		
		Intent intent=this.getIntent();
		Bundle bundle=intent.getExtras();

		georef =  (ArrayList<com.abreuretto.PlacesHere.AppMapa>)bundle.getSerializable("StudentObject");
		
		
		

        googleMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
       
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        
        LatLng latLng = null;
        
        for(int i=0; i<georef.size(); i++) {
        
        	
        latLng = new LatLng(georef.get(i).getaLat() , georef.get(i).getaLon());
        
        NumberFormat nf = NumberFormat.getNumberInstance();
        
        double valor    = 0.621371192;
        double dista    = georef.get(i).getDistance();
        double distakm  = dista/1000; 
        double distamil = distakm*valor;





        
        
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(String.valueOf(i)+"   "+georef.get(i).getName())
                
                .snippet(georef.get(i).getVicinity()+" \n "+ nf.format(distakm)+" km  "+nf.format(distamil)+" mil")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {

                    String Titulo = marker.getTitle();
                    int parar = Titulo.indexOf(" ");
                    if (parar == -1)  return;

                    String ind = Titulo.substring(0,parar);
                    int indice = Integer.parseInt(ind);

                    com.abreuretto.PlacesHere.AppMapa appw = georef.get(indice);
                    Intent intent = new Intent(MapaGeralActivity.this, com.abreuretto.PlacesHere.TabDetActivity.class);
                    intent.putExtra("ref", appw.getReference());
                    intent.putExtra("lat", Double.toString(appw.getaLat()));
                    intent.putExtra("lon", Double.toString(appw.getaLon()));
                    intent.putExtra("nome", appw.getName());
                    intent.putExtra("id", appw.getId());
                    intent.putExtra("tipo", Integer.toString(appw.getTipo()));
                    intent.putExtra("rating", Integer.toString(appw.getRating()));
                    startActivity(intent);





                }
            });









        }
        
        
        
        latLng = new LatLng(Double.parseDouble(latx) , Double.parseDouble(lonx));
        googleMap.addMarker(new MarkerOptions()
        .position(latLng)
        .title("YOU")
        .snippet("Here")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        
        
        
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));


		
	}







    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_geral, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	     

	       
	        case R.id.action_about:
	            pegaAbout();
	            return true;
	            
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
	
	public void pegaAbout()
	{
        Intent intent = new Intent(this, com.abreuretto.PlacesHere.SobreActivity.class);
        startActivity(intent);
	}

    private void setUpStreetViewPanoramaIfNeeded(Bundle savedInstanceState) {
        if (mSvp == null) {
            mSvp = ((SupportStreetViewPanoramaFragment)
                    getSupportFragmentManager().findFragmentById(R.id.map))
                    .getStreetViewPanorama();
            if (mSvp != null) {
                if (savedInstanceState == null) {
                    mSvp.setPosition(SAN_FRAN);

                }
            }
        }
    }


	

}
