package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codigo.app.qr_pensionados.clases.DbManager;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private Button btn_entrar;
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        db = new DbManager(getApplicationContext());


        btn_entrar = findViewById(R.id.btnentrar);

        btn_entrar.setOnClickListener((view) -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
        String sesion = GetSession("nombre");
        if (!sesion.isEmpty())
            startActivity(new Intent(getApplicationContext(), ListadoActivity.class));
            getProductos();
        }

    private String GetSession(String nombre){
        SharedPreferences sharedPref = getSharedPreferences("loginsesion", Context.MODE_PRIVATE);
        return sharedPref.getString(nombre,"");
    }
    private void getProductos() {
        RequestParams params = new RequestParams();
        params.put("app", "ok");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.1.71/Proyecto-IA/ajax/api.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d("PRODUCTOS",response.toString());
                    db.borrarTable("productos");

                    //llenar la db sqlite
                    if (response.getBoolean("success")){
                        JSONArray pro= response.getJSONArray("listaproductos");
                        for(int i=0; i< pro.length(); i++){
                            try {
                                JSONObject o = pro.getJSONObject(i);
                                db.insertarProductos(
                                        o.getInt("id"),
                                        o.getString("nombre"),
                                        o.getString("descripcion"),
                                        o.getString("foto"),
                                        o.getDouble("precio"),
                                        o.getString("stock"),
                                        o.getDouble("totalconsulta"),
                                        o.getDouble("totalmedicina"),
                                        o.getDouble("total")

                                );
                                Log.d("insercion","correcta");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        db.borrarTable("promociones");
                        JSONArray prm= response.getJSONArray("listapromociones");
                        for(int i=0; i< prm.length(); i++){
                            try {
                                JSONObject o = prm.getJSONObject(i);
                                db.insertarPromociones(
                                        o.getInt("id"),
                                        o.getString("nombre"),
                                        o.getString("descripcion"),
                                        o.getString("foto"),
                                        o.getInt("productos_id")
                                );
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                    //llenar el listview
                }catch(Exception e){
                    e.getMessage();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("ERROR", errorResponse.toString());
            }
        });
    }
}
