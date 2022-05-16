package com.abreuretto.PlacesHere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;



public class DetalheGOActivity extends  FragmentActivity  { 

	private ProgressDialog dialog;
	String refe = null;
	String lat = null;
	String lon = null;
	String nome = null;
	String id = null;
	String rat = null;
	String url = null;
	
	
	int heightPixels = 0;
	
	//private WebView webView;
	  SharedPreferences shared_preferences;
	  SharedPreferences.Editor shared_preferences_editor;
  String latx = null;
  String lonx = null;
  int fotos = 0;
  int tips = 0;
  int spe = 0;
	WebView webView;
  int widthPixels = 0;
	
  JSONArray aJson = null;
  
  AlertDialogManager alert = new AlertDialogManager();
 ConnectionDetector cd;	
  
  ArrayList<AppDet> listadet= new ArrayList<AppDet>();
  
  
	private Handler handler = new Handler();
	int conta = 0;
	boolean acabou = false;
	PegaFSTask task;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		
		
		
		DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);		
	    heightPixels = metrics.heightPixels;
	    widthPixels = metrics.widthPixels;
	    float density = metrics.density;
	    int densityDpi = metrics.densityDpi;
	    

			setContentView(R.layout.activity_detalhe_google);

			
			cd = new ConnectionDetector(getApplicationContext());
			 
	        // Check for internet connection
	        if (!cd.isConnectingToInternet()) {
	            // Internet Connection is not present
	            alert.showAlertDialog(DetalheGOActivity.this, this.getResources().getString(R.string.interneterror),
	            		this.getResources().getString(R.string.interneterrorpede), false);
	            // stop executing code by return
	            return;
	        }
			
			
			
			
		
		
		 
		     
		
		
		Bundle extras = getIntent().getExtras();
		
		
		
		if (extras == null)
		{
			
			 alert.showAlertDialog(DetalheGOActivity.this, this.getResources().getString(R.string.interneterror),
	            		this.getResources().getString(R.string.interneterrorpede), false);
    	   return;	
			
			
		}
		
		
		 dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregandoselecionado));
		
	  
	       refe = extras.getString("ref");
	       lat = extras.getString("lat");
	       lon = extras.getString("lon");
	       nome = extras.getString("nome");
	       id = extras.getString("id");
	       rat = extras.getString("rating");
		      
	       TextView titleText = (TextView)findViewById(R.id.tnome);
	       titleText.setText(nome);
			
	        shared_preferences = getSharedPreferences("abreuretto", MODE_PRIVATE);
		    latx = shared_preferences.getString("lat", "");
		    lonx = shared_preferences.getString("lon", ""); 
	       
	       
	       
	       
	       try {
	            FileInputStream fis1 = this.openFileInput("det.dat");
	            ObjectInputStream is1 = new ObjectInputStream(fis1);
	            Object readObject1 = null;
	            
	            

	            
				try {
					readObject1 = is1.readObject();
	                listadet = (ArrayList<AppDet>) readObject1;
	                poedados();   
	                return;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            is1.close();

	            
	            
	        } catch (IOException e)
	        {
	            //
	        
	        }
	       
	       
	       
	       
	       
	       
	       acabou = false;
	    	 conta = 0;
	         handler.postDelayed(runnable, 1000); 
	       
	       
	       
	       

	       
	       task = new PegaFSTask(this);
	       String url2 = refe + "&sensor=true&key=AIzaSyAxP-Y9_a60ou83yXTowy8Usow4icLKDyM";
	       task.execute(url2,latx,lonx);
	       
	       
	    
	   

		

	    
	    
	   
	    
	    
	


		
	}

	
	
	
private Runnable runnable = new Runnable() {
    	
    	
    	
    	@Override
    	   public void run() {
    	      /* do what you need to do */
    	      conta = conta + 1;
    	      if (acabou == true) {return;}
    	      if ((conta > 40) && (acabou == false)) 
    	      
    	      {    	      
    	    	  dialog.dismiss();
    	    	  task.cancel(true);
    	    		 //dialog = ProgressDialog.show(DetalheFSActivity, "", DetalheFSActivity.getResources().getString(R.string.interneterror));
    	    		 finish() ;
    	    	  finish();
    	      }	
    	    	  else
    	      {
    	      handler.postDelayed(this, 1000);
              }
    	   }
    	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	OnTouchListener onTouchListener = new View.OnTouchListener() {
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {

		 int action = event.getAction();
		 if (action == MotionEvent.ACTION_UP) {
		     String uri = "geo:"+ lat + "," + lon+"?q="+lat+","+lon;
				 startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		 }

		 return false;
		 }
		 };
	
	   public void Mapaclick(View view) {  
	   
		   dialog.dismiss();
		   //Toast.makeText(this,"Lat: "+lat, Toast.LENGTH_LONG).show();
		   
		     String uri = "geo:"+ lat + "," + lon+"?q="+lat+","+lon;
			 startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		   
	   }
	
	   private class PegaFSTask extends AsyncTask<String, Integer, String> {
		     
		   public PegaFSTask(DetalheGOActivity detalheGOActivity) {
			// TODO Auto-generated constructor stub
		}

		protected String doInBackground(String... params) {
			  
			      String urlF = params[0];
			      String result = null;
			      
			      latx =  params[1];
			      lonx =  params[2];
			   
			   
				  	 if (isCancelled()) 
				        	
				        {
				  		    acabou = true;	
				        	return null;
				        }
			    	
			      
			      
			   try {
			          HttpClient clientF = new DefaultHttpClient();
			          HttpGet httpgetF = new HttpGet(urlF);
			          HttpResponse responseF = clientF.execute(httpgetF);
			          HttpEntity entityF = responseF.getEntity();
			      
			          if(entityF == null) {
			              //msg = "No response from server";
			              result =" ";
			             // return null;        
			          }
			          InputStream isF = entityF.getContent();
			         // return 
			          
			          String convF = streamToString(isF);
			          
			         	result = convF;
			         	//Log.d("valorori", streamToString(isF));
			      }
			      catch(IOException e){
			          //msg = "No Network Connection";
			          result = " ";
			      }
			     
			      
			      
			          
			        
			      return result;  

		     }

		     protected void onProgressUpdate(Integer... progress) {
		         //setProgressPercent(progress[0]);
		     }

		     protected void onPostExecute(String sJson) {
		    	 
		    	 
		    	 String[] apps = new String[13];
		    	
		    	 
		    	 
		    	 

		    	   JSONObject jsonF  = null;
		    	   JSONObject jArray = null;
		    	
		    	if(sJson == null) {
		            return;
		        }        
		         
		        
		        
		    
		    	try
		        {   
		        String four = sJson.toString();
		  		jArray = new JSONObject(four);
		          
		        } catch (JSONException e) {
		              System.err.println("erro");
		        }
		      	
		  		  
		  		  
		  		  
		  		  try 
		         
		          {
		        	  jsonF  = (JSONObject) jArray.get("result");
		          } catch (JSONException e) {
		              System.err.println("erro");
		          }
		      	
		  		  
		  		  AppDet appdet = new AppDet();
		  		  
		          	  try
		                {
		                appdet.setNome(jsonF.getString("name"));
		                
		                
		                } catch (JSONException e) {
		                	appdet.setNome(" ");
			            }    
		          
		          	 try
		                {
		                appdet.setUrl(jsonF.getString("url"));
		                url = jsonF.getString("url");
		                
		                
		                } catch (JSONException e) {
		                	appdet.setUrl(" ");
			            }    
		        	  
		        	  
		        	  
		        	  try
		        	  {
		        		  
		              appdet.setFone(jsonF.getString("formatted_phone_number"));		  
		        	  

		        	  
		        	  
		        	  } catch (JSONException e) {
		        		  appdet.setFone(" ");
		              }    
	               
		        	   
		     	       
		   
		     	       
		     	       
		     	       
		     	       
		        	  try
		        	  {
		        		  
		        		  appdet.setEnde(jsonF.getString("formatted_address"));
		                
		        	  } catch (JSONException e) {
		        		  appdet.setEnde(" ");
		              }    
	                 
		        	  
		        	  
		        	  try
		        	  {
		                appdet.setLat(jsonF.getJSONObject("geometry").getJSONObject("location").getString("lat"));
		                
		        	  } catch (JSONException e) {
		        		  appdet.setLat(" ");
		              }    
	                 
		                
		        	  
		                
		        	  try
		        	  {
		               appdet.setLon(jsonF.getJSONObject("geometry").getJSONObject("location").getString("lng"));
		               
		        	  } catch (JSONException e) {
		        		  appdet.setLon(" ");
		              }    
	                  
		                
		        	  
		                
                      appdet.setCheckin("0");
		        	  appdet.setUsers("0");
		        	  
		        	  
		        	
		        	  
		        	  
		                
		        	    
		            		        	  
		        	  
		        	  
		        	  
		        	  
		        	 	  
		        		  
		        		  appdet.setRating(rat);
		                
		                
		                
		               
		                
		                
		        	  
		        	  
		        	 	 
			     	   
			     	   
			     	   appdet.setEspecial("0");
			     	   
			     	    
	                    
		        	  try
		        	  {
		              
		        		  aJson = jsonF.getJSONArray("photos");
		        		  int tama = aJson.length();
		        		 
		        		  appdet.setFotosconta(tama);
		        		  
		        		  
		                
		        	  } catch (JSONException e) {
		        		  appdet.setFotosconta(0);
		              }    
	                    
		        	  
		        	 
		        
		        	  
		        	  
			     	
			     	    
			     	    
			     	    
		        	  
		              
		        	  try
		        	  {
		              
		        		  
		        		  JSONArray aJson = jsonF.getJSONArray("reviews");
		        		  int tama = aJson.length();
		        		  
		        		  appdet.setTipsconta(tama);
		        		  
		                
		        	  
		                
		                
		        	  } catch (JSONException e) {
		        		  appdet.setTipsconta(0);
		              }    
	                    
		        	  
		        	  
		        	listadet.add(appdet);
		        	  
		         			     	 
			     	
			     	
			     	
			     	
                     poedados();
				    
				    
				    try {
				  		 
				  		

				  		 
				  		 FileOutputStream fos1 = getBaseContext().openFileOutput("det.dat", Context.MODE_PRIVATE);
			             ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
			             oos1.writeObject(listadet);
			             oos1.close();
			             
			             
			         } catch (IOException e) {
			             e.printStackTrace();
			             
			         }
				    
				  
			     
		 }}
	
	
	   
	   public void poedados()
	   {
		   
		   webView = (WebView) findViewById(R.id.webview);
		    webView.getSettings().setJavaScriptEnabled(true);
		    webView.setWebViewClient(new WebViewClient());
		    
		    webView.setOnTouchListener(onTouchListener);
		    
		    String w = Integer.toString(widthPixels);
			   String h = Integer.toString(heightPixels/2);
			    
			    String url1= "http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lon+"&zoom=16&size="+w+"x"+h+"&markers=size:mid|color:red|"+lat+","+lon+"&sensor=false&key=AIzaSyBRhULv84uFwIW0JYyGonkTM5sr5dvOR9I";
			    //String url1 = url.replaceAll(" ", "%20");
			    webView.loadUrl(url1);
			    
		   
		   TextView telText = (TextView)findViewById(R.id.ttel);
 	       telText.setText(listadet.get(0).getFone());
		   
 	       
 	      TextView endeText = (TextView)findViewById(R.id.tende);
	       endeText.setText(listadet.get(0).getEnde());
 	       
		   
	       NumberFormat nf = NumberFormat.getNumberInstance();
     	  
      
   	        TextView checkText = (TextView)findViewById(R.id.tcheck);
    	    checkText.setText(nf.format(0));
         
    	  
      	    TextView userText = (TextView)findViewById(R.id.tuser);
	         userText.setText(nf.format(0));
            
	     	   double myNum9 = 0;

	        	  try {
	        	      myNum9 = Double.parseDouble(listadet.get(0).getRating());
	        	  } catch(NumberFormatException nfe) {
	        	    // Handle parse error.
	        	  }     
	        	  
	        	  
	         	  TextView likeText = (TextView)findViewById(R.id.tlike);
		     	  likeText.setText(nf.format(myNum9));
	       
		     	    
		     	   
		        	  
		        	  fotos = listadet.get(0).getFotosconta();
		        	  tips = listadet.get(0).getTipsconta(); 	    
		     	    
		     	    
		     	    
	       
		   shared_preferences_editor = shared_preferences.edit();
		    shared_preferences_editor.putString("detpassou", "true");
		    shared_preferences_editor.commit();
		    
		    
		    shared_preferences_editor = shared_preferences.edit();
		    shared_preferences_editor.putString("fotos", Integer.toString(fotos));
		    shared_preferences_editor.commit();				    
		    
		    shared_preferences_editor = shared_preferences.edit();
		    shared_preferences_editor.putString("tips", Integer.toString(tips));
		    shared_preferences_editor.commit();	
		    
		    
		    shared_preferences_editor = shared_preferences.edit();
  		    shared_preferences_editor.putString("latby", listadet.get(0).getLat());
  		    shared_preferences_editor.commit();
  		    shared_preferences_editor = shared_preferences.edit();
  		    shared_preferences_editor.putString("lonby", listadet.get(0).getLon());
  		    shared_preferences_editor.commit();
		    
  		  dialog.dismiss();
  		  acabou = true;	
	   }
	   
	   
	   
	   
	   public void fotoClick(View view) {
	    	
	    	
	    	if ( fotos > 0)
	    	{
	    		
	    	
	    	 Intent intent= new Intent(this, FotoGOActivity.class);
            intent.putExtra("ref",refe);
            intent.putExtra("lat",lat);
            intent.putExtra("lon",lon);
            intent.putExtra("nome",nome);
         	 startActivity(intent);
         	 
	    	}
         	 
		 } 
	   
	    public void reviewClick(View view) {
	    	 
	    	if (tips > 0) {
	    	
	       Intent intent= new Intent(this, TipsFSActivity.class);
           intent.putExtra("ref",refe);
           intent.putExtra("lat",lat);
           intent.putExtra("lon",lon);
           intent.putExtra("nome",nome);
           intent.putExtra("tipo", "2");
           intent.putExtra("id", id);
           startActivity(intent);
	    	}
		 } 	    
	    
	    public void especialClick(View view) {
	    	 
	    	 Intent intent = new Intent(this, PegaSugestaoActivity.class);
		        startActivity(intent);
		 } 		    
	   
	   
	   
	   
	   
	   
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalhe_f, menu);
		return true;
	}
	
	public String streamToString(final InputStream is) throws IOException{
	      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	      StringBuilder sb = new StringBuilder(); 
	      String line = null;
	       
	      try {
	          while ((line = reader.readLine()) != null) {
	              sb.append(line + "\n");
	          }
	      } 
	      catch (IOException e) {
	          throw e;
	      } 
	      finally {           
	          try {
	              is.close();
	          } 
	          catch (IOException e) {
	              throw e;
	          }
	      }
	       
	      return sb.toString();
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
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
	}	
	
	 public void pegagoogle(View view)   
		{

	 	
	        Intent intent= new Intent(this, WebTipsActivity.class);
	        intent.putExtra("ref",listadet.get(0).getUrl());
	        intent.putExtra("lat",lat);
	        intent.putExtra("lon",lon);
	        intent.putExtra("nome",listadet.get(0).getNome());
	        intent.putExtra("id",listadet.get(0).getId());
	        intent.putExtra("tipo","1");
	        startActivity(intent);                     
	     	               
		
		}

}

