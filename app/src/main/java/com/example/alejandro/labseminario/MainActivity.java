package com.example.alejandro.labseminario;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.alejandro.labseminario.ItemMenu.ItemMenuStructure;
import com.example.alejandro.labseminario.ItemMenu.LoaderImg;

import com.example.alejandro.labseminario.ItemMenu.MenuBaseAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemMenuStructure> listInfo;
    private Context root;
    private MenuBaseAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy( policy );


        root = this;
        listInfo = new ArrayList<ItemMenuStructure>( );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //loaddataResdata();
        loadcomponents();

    }

    private void loadcomponents()
    {
        LIST = (ListView)this.findViewById(R.id.foodList);
        EditText search = (EditText)this.findViewById(R.id.edidtxt);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String str = s.toString();
                loaddataResdata(str);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        LIST.setAdapter(ADAPTER);

    }

    private void loaddataResdata(String keystr)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://181.114.179.122:7070//api/v1.0/"+keystr, new JsonHttpResponseHandler(){

            @Override
            public  void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                    JSONArray listData = response.getJSONArray( "info" );
                    for (int i = 0; i < listData.length(); i++)
                    {
                        JSONObject obj = listData.getJSONObject( i );

                        String name = obj.getString( "name" );
                        String img = obj.getString(("img"));
                        int cantidad = obj.getInt("quantity");
                        String id = obj.getString("id");
                        ItemMenuStructure itemMenuStructure = new ItemMenuStructure(name, img, cantidad, id);
                        listInfo.add(itemMenuStructure);
                    }
                    ADAPTER = new MenuBaseAdapter(root, listInfo);
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }       



        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
