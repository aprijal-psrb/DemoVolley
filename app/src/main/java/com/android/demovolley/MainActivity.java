package com.android.demovolley;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    TextView output;
    String url = "http://192.168.137.1/asa/volley/queue.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView)findViewById(R.id.output);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int success = 0;
                List<String> listNama_barang = new ArrayList<>();
                try {
                    success = response.getInt("success");
                    if(success == 1){
                        JSONArray all = response.getJSONArray("all_barang");
                        for (int x = 0; x < all.length(); x++){
                            JSONObject c = all.getJSONObject(x);
                            String nama_barang = c.getString("nama_barang");
                            listNama_barang.add(nama_barang);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                String text = "";
                for (int i = 0; i < listNama_barang.size(); i++){
                    text = text + listNama_barang.get(i) + "\n";
                }
                output.setText(text);
                SharedPreferences mData = getApplicationContext().getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = mData.edit();
                editor.putString("response", response.toString());
                editor.apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Get Cache",Toast.LENGTH_SHORT).show();
                SharedPreferences mData = getApplicationContext().getSharedPreferences("data",MODE_PRIVATE);
                String stringObj = mData.getString("response", "Cache Tidak Ada");
                int success = 0;
                List<String> listNama_barang = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(stringObj);
                    success = jsonObject.getInt("success");
                    if(success == 1){
                        JSONArray all = jsonObject.getJSONArray("all_barang");
                        for (int x = 0; x < all.length(); x++){
                            JSONObject c = all.getJSONObject(x);
                            String nama_barang = c.getString("nama_barang");
                            listNama_barang.add(nama_barang);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                String text = "";
                for (int i = 0; i < listNama_barang.size(); i++){
                    text = text + listNama_barang.get(i) + "\n";
                }
                output.setText(text);
            }
        });
        queue.add(jsonObjectRequest);
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
