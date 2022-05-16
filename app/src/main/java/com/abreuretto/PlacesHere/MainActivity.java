package com.abreuretto.PlacesHere;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.*;

import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;



public class MainActivity extends Activity implements LocationListener{

	
	
	 private static final int GPS_ERRORDIALOG_REQUEST = 0;
	 String latx = "";
	 String lonx = "";
	 String latold = "";
	 String lonold = "";
	 Button local;
	 EditText local2;
	 String Endereco = null;
	 Editable mtext1 = null;
     private LocationManager locationManager;
	 private String provider;
	 SharedPreferences shared_preferences;
	 SharedPreferences.Editor shared_preferences_editor;
	 ConnectionDetector cd;
	 int conta = 0;
	 boolean acabou = false;
	 private Handler handler = new Handler();
	 Animation animRotate;
	 private ProgressDialog dialog;
	 AlertDialogManager alert = new AlertDialogManager();
	 DownloadWebPageTask task2 = new DownloadWebPageTask();
	 DownloadWebPageTask task1 = new DownloadWebPageTask();
	private Locale myLocale;
	 Locale current;
    GPSTracker gps;
	double latitude = 0;
	double longitude = 0;

    
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);









    //PegaFS();


	 //Inicio();


	   local = (Button) findViewById(R.id.button1);
	   local2 = (EditText) findViewById(R.id.edit_local);
	   current = getResources().getConfiguration().locale;
	   mtext1 = local2.getText();
	   shared_preferences = getSharedPreferences("abreuretto", MODE_PRIVATE);
	   latold = shared_preferences.getString("lat", "0");
	   lonold = shared_preferences.getString("lon", "0");
	   animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
	   acabou = false;
	   cd = new ConnectionDetector(getApplicationContext());
	   local.requestFocus();




	   Pegalocal();





 
   }




   
   private void inicio()
   {

	  if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(this.getResources().getString(R.string.interneterrorpede))
                .setMessage(this.getResources().getString(R.string.interneterror))
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                   	 MainActivity.this.finish();
                    }
                });
              builder.create().show();  
            return;
        }

        if (servicesOk() == false) return;
        dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregando));


	   /*
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
*/

	    if (latitude != 0) {
	     // onLocationChanged(location);
	    } else {
	      local.setText(this.getResources().getString(R.string.semgps));
	      dialog.dismiss();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "GPS NOT ACTIVE. Please active it!", Toast.LENGTH_SHORT)
                    .show();
	    }
   }



   public void Pegalocal()
   {


		gps = new GPSTracker(MainActivity.this);







		if(gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();




			double lat = gps.getLatitude();
			double lng = gps.getLongitude();
			if (latx == "") {
				latx = (String.valueOf(lat));
				lonx = (String.valueOf(lng));
				shared_preferences_editor = shared_preferences.edit();
				shared_preferences_editor.putString("lat", String.valueOf(lat));
				shared_preferences_editor.commit();
				shared_preferences_editor = shared_preferences.edit();
				shared_preferences_editor.putString("lon", String.valueOf(lng));
				shared_preferences_editor.commit();
				shared_preferences_editor = shared_preferences.edit();
				shared_preferences_editor.putString("detpassou", "false");
				shared_preferences_editor.commit();
				PegaEnde();


				//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}
		}




		inicio();

	}



	public void PegaEnde()  { //throws IOException {
		StringBuffer sb1=new StringBuffer();
		sb1.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=").append(latx).append(",").append(lonx).append("&sensor=false");
		String url1=sb1.toString();
		url1 = url1.replaceAll(" ", "%20");
		acabou = false;
		handler.postDelayed(runnable, 1000);
		task1 = new DownloadWebPageTask();
		task1.execute(url1);
	}



/*
private boolean gpsok()
{




    String provider = Settings.Secure.getString(getContentResolver(),
            Settings.Secure.LOCATION_PROVIDERS_ALLOWED);



    if(provider.equals("")){

        local.setText(this.getResources().getString(R.string.semgps));
        Toast.makeText(this, "GPS NOT ACTIVE. Please active it!", Toast.LENGTH_SHORT)
                .show();

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);

        return false;

    } else
    {
        return true;
    }


}
*/



private boolean servicesOk() {

    int isAvailable = GooglePlayServicesUtil
	            .isGooglePlayServicesAvailable(this);
	    if (isAvailable == ConnectionResult.SUCCESS) {
	        return true;
	    } else 
	   
	    	if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
	                this, GPS_ERRORDIALOG_REQUEST);
	        
	        dialog.show();
	    } else {
	        Toast.makeText(this, "O seu celular não possui o Google Play atualizado", Toast.LENGTH_SHORT)
	                .show();

	    }
	    return false;
	}

   
private Runnable runnable = new Runnable() {
   	
   	@Override
   	   public void run() {
   	      conta = conta + 1;
   	      if (acabou == true) {return;}
   	      if ((conta > 60) && (acabou == false)) 
   	      {    	      
   	    	  if (dialog != null) dialog.dismiss();
   	    	  task1.cancel(true);
   	      }	
   	    	  else
   	      {
   	      handler.postDelayed(this, 1000);
             }
   	   }
   	};





	public void ListaClick(View view) {




       local2.setFocusable(true);
   	
   	
   	if (latx == "") {
   		Toast.makeText(this, this.getResources().getString(R.string.semgps), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
   	    return;
   	}
   	
   	if (local.getText().toString().trim().length() == 0) {

   		Toast.makeText(this, this.getResources().getString(R.string.localdigita), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);


   	    return;
   	}
   	
   	
   	
   	
   	
   	
   	float distance=0;
   	if ((latold.equals("0")) && (lonold.equals("0")))
   	{
   		distance = 100;
   	}
   	else
   	{
   	  Location locationA = new Location("point A");
         locationA.setLatitude(Double.parseDouble(latx));
         locationA.setLongitude(Double.parseDouble(lonx));
         Location locationB = new Location("point B");
         locationB.setLatitude(Double.parseDouble(latold));
         locationB.setLongitude(Double.parseDouble(lonold));
         distance = locationA.distanceTo(locationB);
   	}

   	
   		try
       	{
       	deleteFile("places.dat");
       	}
           catch (Exception e) 
       	{
       		
       	}	
   		
   		
   		try
       	{
       	deleteFile("mapa.dat");
       	}
           catch (Exception e) 
       	{
       		
       	}






       Intent intent = new Intent(this, PegaTipo.class);
       startActivity(intent);


   	
       //Intent intent = new Intent(this, PegaCidaActivity.class);
       //startActivity(intent);
   	
 	 }
   
   
   
   public void mylocClick(View view) {  
 	
   	PegaLatLon();

   } 
	
   public void otherlocClick(View view) {  

   	if (local2.getText().toString().trim().length() == 0) {
   		Toast.makeText(this, this.getResources().getString(R.string.cidadigita), Toast.LENGTH_SHORT).show();
   	    return;          
   	}
   	
   	dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregandooutro));
   	mtext1 = local2.getText();
   	PegaLatLon();
   	
	 } 

   
   public void ClickMyLocation(View view) {  
	 } 
   
   
   
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main, menu);
       
       String testa = current.getISO3Language().toString();
       
       if (testa.equals("eng")) {
      	 
       	MenuItem registrar = menu.findItem(R.id.action_idioma);      
           registrar.setVisible(false);

      	 
       }
       
       
       return true;
   }
   
   
	  @Override
	  protected void onResume() {
	    super.onResume();
	    
	    if (cd.isConnectingToInternet()) {
	    acabou = false;	   
	  //  locationManager.requestLocationUpdates(provider, 0, 100, this);
	    }
	    
	    
	    
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    if (cd.isConnectingToInternet()) {
	    acabou = true;
	    //locationManager.removeUpdates(this);
	    }
	  }
   
   
   
   @Override
	  public void onLocationChanged(Location location) {
	    double lat = location.getLatitude();
	    double lng = location.getLongitude();
	    if (latx == "") {
	    	latx = (String.valueOf(lat));
		    lonx = (String.valueOf(lng));
	    shared_preferences_editor = shared_preferences.edit();
	    shared_preferences_editor.putString("lat", String.valueOf(lat));
	    shared_preferences_editor.commit();
	    shared_preferences_editor = shared_preferences.edit();
	    shared_preferences_editor.putString("lon", String.valueOf(lng));
	    shared_preferences_editor.commit();
	    shared_preferences_editor = shared_preferences.edit();
	    shared_preferences_editor.putString("detpassou", "false");
	    shared_preferences_editor.commit();
	    }  else
	    {
	    }
	    
	    PegaEnde();

	  }
	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	  }
	
	  private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		    @Override
		    protected String doInBackground(String... urls) {
		      String response = "";
		      if (isCancelled()) 
		        	
		        {
		        	return null;
		        }
		      
		      for (String url : urls) {
		        DefaultHttpClient client = new DefaultHttpClient();
		        HttpGet httpGet = new HttpGet(url);
		        try {
		          HttpResponse execute = client.execute(httpGet);
		          InputStream content = execute.getEntity().getContent();

		          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		          String s = "";
		          while ((s = buffer.readLine()) != null) {
		            response += s;
		          }

		        } catch (Exception e) {
		          e.printStackTrace();
		        }
		      }
		      return response;
		    }

		    @Override
		    protected void onPostExecute(String result) {

		    	JSONObject predictions;
		      try {
		          predictions = new JSONObject(result);

		          
		          double lat = ((JSONArray)predictions.get("results")).getJSONObject(0)
		                  .getJSONObject("geometry").getJSONObject("location")
		                  .getDouble("lat");		          
		          
		          double lon = ((JSONArray)predictions.get("results")).getJSONObject(0)
		                  .getJSONObject("geometry").getJSONObject("location")
		                  .getDouble("lng");
		          
		          Endereco = ((JSONArray)predictions.get("results")).getJSONObject(0)
		                  .getString("formatted_address");
		          
		          local.setText(Endereco);
		          
		          local.requestFocus();
		          
		          
		  	    shared_preferences_editor = shared_preferences.edit();
			    shared_preferences_editor.putString("lat", String.valueOf(lat));
			    shared_preferences_editor.commit();
			    shared_preferences_editor = shared_preferences.edit();
			    shared_preferences_editor.putString("lon", String.valueOf(lon));
			    shared_preferences_editor.commit();    
			    
				latx = (String.valueOf(lat));
			    lonx = (String.valueOf(lon));
		         
			    local.setAnimation(animRotate);
			    animRotate.start();
			    acabou = true;
		         dialog.dismiss();






		      } catch (JSONException e) {
		          // TODO Auto-generated catch block
		    	   dialog.dismiss();
		          e.printStackTrace();
		      } 
		    }
		  }


	    public void PegaLatLon()  { //throws IOException {  
	    	StringBuffer sb=new StringBuffer();
	        sb.append("http://maps.googleapis.com/maps/api/geocode/json?address=").append(mtext1).append("&sensor=false");
	        String url=sb.toString();  
	        url = url.replaceAll(" ", "%20");
	        acabou = false;
	        handler.postDelayed(runnable, 1000);
	        task2 = new DownloadWebPageTask();
		    task2.execute(url);
	    	}
	    
	    


		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		     
		        case R.id.action_refresh:
		        	try
		        	{
		        	deleteFile("places.dat");
		        	}
		            catch (Exception e) 
		        	{
		        		
		        	}	
		        	conta=0;
		        	acabou = false;
		            inicio();
		            return true;
		       
		        case R.id.action_about:
		            pegaAbout();
		            return true;
		        
		        case R.id.action_idioma:
		        	changeLang("en");
		            return true;    
		            
		            
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
	    
		
		public void pegaAbout()
		{
	        Intent intent = new Intent(this, SobreActivity.class);
	        startActivity(intent);
		}	
		
		
		
		
		
		
		
		
		public void restartApplication(Context context)
		{
		    finish();
		    Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
		    startActivity(myIntent);

		}


		/** This activity shows nothing; instead, it restarts the android process */
		public class MagicAppRestart extends Activity {
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    finish();
		}

		protected void onResume() {
		    super.onResume();
		    startActivityForResult(new Intent(this, MainActivity.class), 0);         
		}
		}
		
		public void saveLocale(String lang) {     
			String langPref = "Language";     
			SharedPreferences prefs = getSharedPreferences("abreuretto", Activity.MODE_PRIVATE);     
			SharedPreferences.Editor editor = prefs.edit();     
			editor.putString(langPref, lang);     
			editor.commit();
			}
		
		public void loadLocale() {     
			String langPref = "Language";     
			SharedPreferences prefs = getSharedPreferences("abreuretto", Activity.MODE_PRIVATE);     
			String language = prefs.getString(langPref, "");     
			changeLang(language); 
		
		}
		
		public void changeLang(String lang) {     
			if (lang.equalsIgnoreCase(""))      
				return;     
			myLocale = new Locale(lang);     
		//	saveLocale(lang);     
			Locale.setDefault(myLocale);     
			android.content.res.Configuration config = new android.content.res.Configuration();     
			config.locale = myLocale;     
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());     
			restartApplication(this); 
			}



	public void PegaFS()
	{




		FoursquareApi foursquareApi = new FoursquareApi("WYVK3C5JYYSKWLYKMHZFQUAGZYXRBAFXMLARQHLJBFFC05IX", "GJO5FLB20ZI03LLYBEBST55NI5OMLPG4IRIHXRBPLYB1S4M3", "https://api.foursquare.com/v2/");

		foursquareApi.setVersion("20130815");

		try {
			Result<fi.foyt.foursquare.api.entities.Category[]> result =	foursquareApi.venuesCategories();
		} catch (FoursquareApiException e) {
			e.printStackTrace();
		}

	}





	public class BasicExample {

		public void main(String[] args) {
			String ll = args.length > 0 ? args[0] : "32.8400, -117.2769";
			//String ll = "40.7903, 73.9597";
			try {
				(new BasicExample()).searchVenues(ll);
			} catch (FoursquareApiException e) {
				// TODO: Error handling
				System.out.println(e.getMessage());
			}
		}

		public void searchVenues(String ll) throws FoursquareApiException {
			// First we need a initialize FoursquareApi.
			FoursquareApi foursquareApi = new FoursquareApi("WYVK3C5JYYSKWLYKMHZFQUAGZYXRBAFXMLARQHLJBFFC05IX", "GJO5FLB20ZI03LLYBEBST55NI5OMLPG4IRIHXRBPLYB1S4M3", "https://api.foursquare.com/v2/");

			// After client has been initialized we can make queries.
			foursquareApi.setVersion("20130815");

			String  Autoriza = foursquareApi.getAuthenticationUrl();

			//System.out.println(foursquareApi.getAuthenticationUrl());


			Result<fi.foyt.foursquare.api.entities.Category[]> result =	foursquareApi.venuesCategories();



			Result<VenuesSearchResult> result1 = foursquareApi.venuesSearch(ll,null,null,null,null,null,null,null,null,null,null);
			System.out.println("testingddddd");
			if (result1.getMeta().getCode() == 200) {
				// if query was ok we can finally we do something with the data
				for (CompactVenue venue : result1.getResult().getVenues()) {
					// TODO: Do something we the data
					System.out.println(result1.getResult().getVenues().length);
					System.out.println(venue.getName());
				}
			} else {
				// TODO: Proper error handling
				System.out.println("Error occured: ");
				System.out.println("  code: " + result.getMeta().getCode());
				System.out.println("  type: " + result.getMeta().getErrorType());
				System.out.println("  detail: " + result.getMeta().getErrorDetail());
			}
		}

	}


	}
