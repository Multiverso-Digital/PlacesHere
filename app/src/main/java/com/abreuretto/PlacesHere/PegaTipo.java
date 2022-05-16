package com.abreuretto.PlacesHere;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class PegaTipo extends Activity {

    ListView listView ;
    String[] CatArray;
    public List<Categoria> categorias = null;
    public List<CatTipo> cattipo = null;
    SharedPreferences shared_preferences;
    SharedPreferences.Editor shared_preferences_editor;
    ArrayList<CatTipo> lista = new ArrayList<CatTipo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pega_tipo);


        listView = (ListView) findViewById(R.id.listView1);


        new LoadCategoriasTask().execute();

        shared_preferences = getSharedPreferences("abreuretto", MODE_PRIVATE);

        CatArray =  getResources().getStringArray(R.array.categorias);




        for(int i=0; i<CatArray.length; i++) {
            CatTipo wcat = new CatTipo();
            String pega = CatArray[i];
            String xid = pega.substring(0,2);
            int wid = Integer.parseInt(xid);
            String wnome = pega.substring(3,pega.length());
            wcat.setId(wid);
            wcat.setnome(wnome);
            lista.add(wcat);
        }

        Collections.sort(lista, new EmploeeComparator());


        for(int i=0; i<lista.size(); i++) {

            CatArray[i] = lista.get(i).getnome();

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, CatArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.parseColor("#680000"));
                return view;
            }
        };





        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
         public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Categoria clicou = new Categoria();

                try
                {
                int itemPosition     = position;
                int posi = lista.get(itemPosition).getId();
                String  itemValue    = (String) listView.getItemAtPosition(position);
                clicou = categorias.get(posi);
                }
                catch (Exception e) {
                 return;
                }


                shared_preferences_editor = shared_preferences.edit();
                shared_preferences_editor.putString("google", clicou._google);
                shared_preferences_editor.commit();
                shared_preferences_editor = shared_preferences.edit();
                shared_preferences_editor.putString("foursquare", clicou._foursquare);
                shared_preferences_editor.commit();
                shared_preferences_editor = shared_preferences.edit();
                shared_preferences_editor.putString("nokia", clicou._nokia);
                shared_preferences_editor.commit();
                shared_preferences_editor = shared_preferences.edit();
                shared_preferences_editor.putString("yelp", clicou._yelp);
                shared_preferences_editor.commit();
                shared_preferences_editor.putString("nokiatipo", clicou._nokiatipo);
                shared_preferences_editor.commit();



                try
                {
                    deleteFile("mapa.dat");
                    deleteFile("places.dat");
                }
                catch (Exception e) {
                    // TODO: handle exception
                }






                Intent intent = new Intent(getBaseContext(), PegaCidaActivity.class);
                startActivity(intent);




                //Toast.makeText(getApplicationContext(),
                //        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                //        .show();

            }

        });
    }




    class EmploeeComparator implements Comparator<CatTipo> {

        public int compare(CatTipo e1, CatTipo e2) {

            String nome = e1.getnome();
            String nome2 = e2.getnome();
            return (nome.compareToIgnoreCase(nome2));

        }

    }


  public class LoadCategoriasTask extends AsyncTask<String, Void, List<Categoria>> {


        protected List<Categoria> doInBackground(String... args) {


            CategoriaXmlParser parser = new CategoriaXmlParser();
            categorias = parser.parse(getBaseContext());

            return categorias;
        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pega_tipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

                pegaAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void pegaAbout()
    {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }
}
