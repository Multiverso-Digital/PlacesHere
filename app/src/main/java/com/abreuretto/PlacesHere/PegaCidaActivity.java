package com.abreuretto.PlacesHere;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;



public class PegaCidaActivity extends  ListActivity implements FetchDataListener{
    private ProgressDialog dialog;
	  SharedPreferences shared_preferences;
	  SharedPreferences.Editor shared_preferences_editor;
    String latx = null;
    String lonx = null;
    String passou = null;
    boolean acabou = false;

    String xfoursquare = null ;
    String xgoogle = null ;
    String xnokia = null ;
    String xyelp =  null;
    String xnokiatipo =  null;
    String valorKM = "5000";
    int sortdist = 1;

    ApplicationAdapter adapter;
    ArrayAdapter<String> categoriesAdapter;
    List<Application> dataw;


	 ConnectionDetector cd;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	ListView listView;
	ArrayList<AppMapa> lista = new ArrayList<AppMapa>();
	ArrayList<Application> listapp = new ArrayList<Application>();
	
	private Handler handler = new Handler();
	FetchDataTask task  ;
    FetchDataTask task2 ;
    FetchDataTask task3 ;



	int conta = 0;

    Button local;
    EditText local2;
    Locale current;
    String mtext1;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_pega_cida);


  //      ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
  //      actionBar.setDisplayHomeAsUpEnabled(true);

   //      txtQuery = (TextView) findViewById(R.id.txtQuery);

   //     handleIntent(getIntent());


  //      getActionBar().setDisplayHomeAsUpEnabled(true);


       // ActionBar actionBar = getActionBar();
       // actionBar.show();




        shared_preferences = getSharedPreferences("abreuretto", MODE_PRIVATE);
	    latx = shared_preferences.getString("lat", "");
	    lonx = shared_preferences.getString("lon", ""); 
	    passou =  shared_preferences.getString("detpassou", "");
        xgoogle = shared_preferences.getString("google", "");
        xfoursquare = shared_preferences.getString("foursquare", "");
        xnokia = shared_preferences.getString("nokia", "");
        xyelp = shared_preferences.getString("yelp", "");
        xnokiatipo = shared_preferences.getString("nokiatipo", "");








        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.actionbar_view);
        EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
        search.setFocusable(false);
        search.setFocusableInTouchMode(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        current = getResources().getConfiguration().locale;



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {




                try
                {
                    deleteFile("mapa.dat");
                    deleteFile("places.dat");
                }
                catch (Exception e) {
                    // TODO: handle exception
                }


                if (v.getText().toString().trim().length() == 0) {
                    dialog = ProgressDialog.show(PegaCidaActivity.this, "", PegaCidaActivity.this.getResources().getString(R.string.carregandoplaces));
                    PegaTudo();

                }  else {

                    dialog = ProgressDialog.show(PegaCidaActivity.this, "", PegaCidaActivity.this.getResources().getString(R.string.carregandoplaces));
                    mtext1 = v.getText().toString();
                    PegaTudoPesq();
                }






                return false;
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);





        InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
      //  local = (Button) findViewById(R.id.button1);
      //  local2 = (EditText) findViewById(R.id.edit_local);
      //  local2.setFocusable(false);
      //  local2.setFocusableInTouchMode(true);
      //  inputMgr.hideSoftInputFromWindow(local2.getWindowToken(), 0);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      //  current = getResources().getConfiguration().locale;
      //  mtext1 = local2.getText();





	    
	    /*
	    
	    if (passou == "true") {
	    	
	    	
	    	try
	    	{
	    	
	    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream("lista.dat"));
	    	listapp = (ArrayList<Application>) ois.readObject();
	    	ois.close();
	    	onFetchComplete(listapp);
	    	}
	        catch (Exception e) {
				// TODO: handle exception
			}
	        
	        
	        
	        
	    }
	    
	    */
	    
		cd = new ConnectionDetector(getApplicationContext());
		 
        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(PegaCidaActivity.this, this.getResources().getString(R.string.interneterror),
            		this.getResources().getString(R.string.interneterrorpede), false);
            // stop executing code by return
            return;
        }
	    

        
        initView();  
        

       


        
    }





    private Runnable runnable = new Runnable() {
    	
    	
    	
    	@Override
    	   public void run() {
    	      /* do what you need to do */
    	      conta = conta + 1;
    	      if (acabou == true) {return;}
    	      if ((conta > 60) && (acabou == false)) 
    	      
    	      {    	      
    	    	  dialog.dismiss();
    	    	  task.cancel(true);
    	    	  finish();
    	      }	
    	    	  else
    	      {
    	      handler.postDelayed(this, 1000);
              }
    	   }
    	};
    
    
    
    
    
    
    
    
    
    private void initView() {

    	
    	
    	
        dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregandoplaces));


        String catego = null;
    	 
    	 
    	 
    	 try {
            // this.deleteFile("mapa.dat");
             FileInputStream fis2 = this.openFileInput("mapa.dat");
             ObjectInputStream is2 = new ObjectInputStream(fis2);
             Object readObject2 = null;
             try {
 				readObject2 = is2.readObject();
                 lista = (ArrayList<AppMapa>) readObject2;


                AppMapa teste = new AppMapa();
                teste = lista.get(0);
                catego = teste.getCatego();

                
 			} catch (ClassNotFoundException e) {
 				e.printStackTrace();
 			}
             is2.close();
         } catch (IOException e)
         {
         }     
    	 
    	 
    	 
    	 
    	 
    	 


             try {
                // this.deleteFile("places.dat");
                 FileInputStream fis = this.openFileInput("places.dat");
                 ObjectInputStream is = new ObjectInputStream(fis);
                 Object readObject = null;


                 try {

                     readObject = is.readObject();
                     listapp = (ArrayList<Application>) readObject;
                     ApplicationAdapter adapter = new ApplicationAdapter(this, listapp);
                     Collections.sort(listapp, new EmploeeComparator());
                     adapter.notifyDataSetChanged();
                     setListAdapter(adapter);
                     if (dialog != null) dialog.dismiss();
                     // dialog.dismiss();
                     return;
                 } catch (ClassNotFoundException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                     //dialog.dismiss();
                 }
                 is.close();


             } catch (IOException e) {
                 //dialog.dismiss();

             }


    	
       	 acabou = false;
    	 conta = 0;
    

       	StringBuffer sb=new StringBuffer();
        sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latx + "," + lonx + "&radius="+valorKM+"&sensor=true&types="+xgoogle+"&key=AIzaSyAxP-Y9_a60ou83yXTowy8Usow4icLKDyM");
        String url=sb.toString();  
        url = url.replaceAll("\\|", "%7C");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String datax = sdf.format(cal.getTime());
        String url1 = "https://api.foursquare.com/v2/venues/search?ll=" + latx + "," + lonx + "&categoryId="+xfoursquare+"&client_id=WYVK3C5JYYSKWLYKMHZFQUAGZYXRBAFXMLARQHLJBFFC05IX&client_secret=GJO5FLB20ZI03LLYBEBST55NI5OMLPG4IRIHXRBPLYB1S4M3&v="+datax+"&radius="+valorKM;
        String url2 = null;

        if (xnokiatipo.equals("c")) {

            url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/explore?in=" + latx + "," + lonx + ";r="+valorKM+"&tf=plain&pretty=y&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg&cat="+xnokia;
        }
        else
        {
            url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/search?q="+xnokia+"&at="+ latx + "," + lonx + "&tf=plain&pretty=y&size=50&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg";
        }


        Log.d("URL", url1);

        task = new FetchDataTask(this);


        task.execute(url,url1,url2,latx,lonx, xyelp, "N", valorKM);







    }





        
        

    
    
    private void oncantask() {
		// TODO Auto-generated method stub

	}
    
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
      //  String item = (String) getListAdapter().getItem(position);
      //  Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
      }

   
    

    
    class EmploeeComparator implements Comparator<Application> {

        public int compare(Application e1, Application e2) {
        	
        	    double lhsDistance = e1.getDistance();
        	    double rhsDistance = e2.getDistance();
        		    if (lhsDistance < rhsDistance) return -1;
        	    else if (lhsDistance > rhsDistance) return 1;
            else return 0;

        }

    }








      public void classificar() {

        if (sortdist == 1) {

            adapter = new ApplicationAdapter(this, dataw);
            Collections.sort(dataw, new EmploeeComparator());
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);

        }


        if (sortdist == 2) {

            adapter = new ApplicationAdapter(this, dataw);

            Collections.sort(dataw, new Comparator<Application>() {
                public int compare(Application obj1, Application obj2) {
                    // TODO Auto-generated method stub
                    return obj1.getName().compareToIgnoreCase(obj2.getName());
                }
            });

            adapter.notifyDataSetChanged();
            setListAdapter(adapter);


        }

        if (sortdist == 3) {

            adapter = new ApplicationAdapter(this, dataw);

            Collections.sort(dataw, new Comparator<Application>() {
                public int compare(Application obj1, Application obj2) {
                    // TODO Auto-generated method stub
                    return obj1.getCategoria().compareToIgnoreCase(obj2.getCategoria());
                }
            });

            adapter.notifyDataSetChanged();
            setListAdapter(adapter);

        }




    }





    public void onFetchComplete(List<Application> data) {
        


    	
    	acabou = true;
    	
    	
    	if (task.isCancelled()) {
    		
    		 dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.interneterror));
    		 finish() ;
    	       	
    			
    	}
    	
    	
    	// dismiss the progress dialog
    
        // create new adapter
        ArrayList<String> categoryList = new ArrayList<String>();

        
        for(int i=0; i<data.size(); i++) {
        
        	
        	
        	
      	AppMapa appM = new AppMapa();        	
        appM.setaLat(data.get(i).getaLat());
        appM.setaLon(data.get(i).getaLon());
        appM.setDistance(data.get(i).getDistance());
        appM.setIcon(data.get(i).getIcon());
        appM.setId(data.get(i).getId());
        appM.setName(data.get(i).getName());
        appM.setRating(data.get(i).getRating());
        appM.setReference(data.get(i).getReference());
        appM.setTipo(data.get(i).getTipo());
        appM.setVicinity(data.get(i).getVicinity());
        appM.setCatego(xfoursquare);
        lista.add(appM);
          if  (categoryList.indexOf(data.get(i).getCategoria()) == -1) {

              categoryList.add(data.get(i).getCategoria());
            }
        
        }
        	
        dataw  = data;


         classificar();





        //getListView().setTextFilterEnabled(true);
/*
        Spinner spinnerCategory = (Spinner)findViewById(R.id.search);
        categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoriesAdapter);
        //spinnerCategory.setSelection(0);



        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                // Here go your instructions when the user chose something

                String escolha = parentView.getSelectedItem().toString();



            }

            public void onNothingSelected(AdapterView<?> arg0) { }
        });
*/


        try {
            this.deleteFile("places.dat");
            FileOutputStream fos = this.openFileOutput("places.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            
        }




        
        try {

            this.deleteFile("mapa.dat");
            FileOutputStream fos2 = this.openFileOutput("mapa.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(lista);
            oos2.close();
        } catch (IOException e) {
            e.printStackTrace();
            
        }
        


        if(dialog != null)  dialog.dismiss();










    }









    

    
    
    

    
    
  
    public void onFetchFailure(String msg) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
  
        
        // show failure message
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();        
    }


    
		
		
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.pega_cida, menu);


       // MenuItem searchItem = menu.findItem(R.id.action_search);
       // SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


       // restoreActionBar();

      //  SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);



       // return super.onCreateOptionsMenu(menu);



        return true;

/*

        MenuInflater inflater = getMenuInflater();
	     
		getMenuInflater().inflate(R.menu.pega_cida, menu);



        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered
                adapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                adapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);




*/



		//return true;
	}










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_tips:
                pegaTips();
                return true;


           /*
            case R.id.action_refresh:
                pegaRefresh();
                return true;
*/
            /*
            case R.id.action_about:
                pegaAbout();
                return true;
            */

            case R.id.action_map:
                pegaMap();
                return true;

            case R.id.action_nome:
                sortdist = 2;
                classificar();
                return true;

            case R.id.action_cat:
                sortdist = 3;
                classificar();
                return true;

            case R.id.action_dist:
                sortdist = 1;
                classificar();
                return true;

            case R.id.action_dialogo:
                MostrarDialogo();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
	

    	
	public void pegaTips()
	{
		
		
  	  
		    shared_preferences_editor = shared_preferences.edit();
		    shared_preferences_editor.putString("latby", String.valueOf(latx));
		    shared_preferences_editor.commit();
		    shared_preferences_editor = shared_preferences.edit();
		    shared_preferences_editor.putString("lonby", String.valueOf(lonx));
		    shared_preferences_editor.commit();
		
		
        Intent intent = new Intent(this, PegaSugestaoActivity.class);
        startActivity(intent);
	}
	
	public void pegaRefresh()
	{
		initView();		
		
	}
	
	public void pegaAbout()
	{
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
	}	
	
	public void pegaMap()
	{
		
		

		Intent intent = new Intent(this, MapaGeralActivity.class);
		Bundle b = new Bundle();
		

			b.putSerializable("StudentObject", lista);
		
		
		
		
		
		intent.putExtras(b);
		startActivity(intent);

		
		
		
		
		
       
		
        
        
        
	}	
	
	
	 @Override
	  protected void onResume() {
	    super.onResume();
	    
	    
	    
	    
	  }

	  
	  @Override
	  protected void onPause() {
	    super.onPause();
	    
	    acabou = true;
	    
	   
	   
	  }


    public void otherlocClick(View view) {


    /*
        InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        local2.setFocusable(false);
        local2.setFocusableInTouchMode(true);
        inputMgr.hideSoftInputFromWindow(local2.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        try
        {
            deleteFile("mapa.dat");
            deleteFile("places.dat");
        }
        catch (Exception e) {
            // TODO: handle exception
        }


        if (local2.getText().toString().trim().length() == 0) {
            dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregandoplaces));
            PegaTudo();
            return;
        }

        dialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.carregandoplaces));
        mtext1 = local2.getText();
        PegaTudoPesq();


*/

    }




public void PegaTudo(){


    acabou = true;
    conta = 0;


    StringBuffer sb=new StringBuffer();
    sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latx + "," + lonx + "&radius=5000&sensor=true&types="+xgoogle+"&key=AIzaSyAxP-Y9_a60ou83yXTowy8Usow4icLKDyM");
    String url=sb.toString();
    url = url.replaceAll("\\|", "%7C");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    String datax = sdf.format(cal.getTime());
    String url1 = "https://api.foursquare.com/v2/venues/search?ll=" + latx + "," + lonx + "&categoryId="+xfoursquare+"&client_id=WYVK3C5JYYSKWLYKMHZFQUAGZYXRBAFXMLARQHLJBFFC05IX&client_secret=GJO5FLB20ZI03LLYBEBST55NI5OMLPG4IRIHXRBPLYB1S4M3&v="+datax+"&radius=5000";
    String url2 = null;

    if (xnokiatipo.equals("c")) {

        url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/explore?in=" + latx + "," + lonx + ";r=5000&tf=plain&pretty=y&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg&cat="+xnokia;
    }
    else
    {
        url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/search?q="+xnokia+"&at="+ latx + "," + lonx + "&tf=plain&pretty=y&size=50&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg";
    }


    Log.d("URL", url1);



    task2 = new FetchDataTask(this);

    task2.execute(url,url1,url2,latx,lonx, xyelp, "N", valorKM);




}


    public void PegaTudoPesq(){



        try
        {


            Thread.sleep(1000);

        }
        catch (Exception e)
        {
            // TODO: handle exception
        }



        acabou = true;
        conta = 0;


        StringBuffer sb=new StringBuffer();

        sb.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+mtext1+"&location=" + latx + "," + lonx + "&radius=5000&sensor=true&key=AIzaSyAxP-Y9_a60ou83yXTowy8Usow4icLKDyM");
       // sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latx + "," + lonx + "&radius=5000&sensor=true&types="+xgoogle+"&key=AIzaSyAxP-Y9_a60ou83yXTowy8Usow4icLKDyM");
        String url=sb.toString();
        url = url.replaceAll(" ", "+");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String datax = sdf.format(cal.getTime());
        String url1 = "https://api.foursquare.com/v2/venues/search?query="+mtext1+"&ll=" + latx + "," + lonx + "&client_id=WYVK3C5JYYSKWLYKMHZFQUAGZYXRBAFXMLARQHLJBFFC05IX&client_secret=GJO5FLB20ZI03LLYBEBST55NI5OMLPG4IRIHXRBPLYB1S4M3&v="+datax+"&radius=5000";
        String url2 = null;

        url1 = url1.replaceAll(" ", "+");



        if (xnokiatipo.equals("c")) {

            url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/explore?in=" + latx + "," + lonx + ";r=5000&tf=plain&pretty=y&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg&cat="+xnokia;
        }
        else
        {
            url2 = "http://demo.places.nlp.nokia.com/places/v1/discover/search?q="+xnokia+"&at="+ latx + "," + lonx + "&tf=plain&pretty=y&size=50&app_id=al3SR82BNt_apHzbF-b9&app_code=oEW8ZxpiQU7lsr2XkYZAjg";
        }

          String texto = mtext1.toString();


        task3 = new FetchDataTask(this);

        task3.execute(url,url1,url2,latx,lonx,texto, "S", valorKM);




    }






public void MostrarDialogo()
{

    AlertDialog.Builder alert = new AlertDialog.Builder(this);



    String titulo = getResources().getString(R.string.titulo_dialogo);
    String mens = getResources().getString(R.string.titulo_mens);



    alert.setTitle(titulo);
    alert.setMessage(mens);

// Set an EditText view to get user input
    final EditText input = new EditText(this);
    InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    input.setFocusable(false);
    input.setFocusableInTouchMode(true);
   // local2 = (EditText) findViewById(R.id.searchfield);
   // inputMgr.hideSoftInputFromWindow(local2.getWindowToken(), 0);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
    input.setSingleLine();

    alert.setView(input);

    System.out.println(alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            Editable value = input.getText();

            valorKM = value.toString();

         int valorver = 5000;
         try
         {
            valorver = Integer.parseInt(valorKM);
            if ((valorver < 1000) || (valorver > 50000))
            {
                valorver = 5000;
            }
        }
        catch (Exception e)
        {
            valorver = 5000;
        }


            valorKM = String.valueOf(valorver);


            try
            {
                deleteFile("mapa.dat");
                deleteFile("places.dat");
            }
            catch (Exception e) {
                // TODO: handle exception
            }



            pegaRefresh();
            // Do something with value!
        }
    }));

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
        }
    });

    alert.show();
}







}


