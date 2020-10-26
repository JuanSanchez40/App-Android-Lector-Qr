package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity {

    private Button btnregistrarse;
    private EditText txtemail, txtpassword, txtnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        btnregistrarse = findViewById(R.id.btnregistrarse);
        txtnombre = findViewById(R.id.txtnombre);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);

        btnregistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtnombre.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"INGRESE SU NOMBRE",Toast.LENGTH_LONG).show();
                else if(txtemail.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"INGRESE SU EMAIL",Toast.LENGTH_LONG).show();
                else if(txtpassword.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"INGRESE SU PASSWORD",Toast.LENGTH_LONG).show();

                registrarse(
                        txtnombre.getText().toString(),
                        txtemail.getText().toString(),
                        txtpassword.getText().toString()
                );
            }
        });
    }

    private void registrarse(String nombre, String email, String password) {
        Log.d("CAMPOS:",nombre + email + password);
        RequestParams params = new RequestParams();
        params.put("btnregistro", "ok");
        params.put("txtnombre", nombre);
        params.put("txtemail", email);
        params.put("txtpassword", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.1.71/Proyecto-IA/ajax/apiregistro.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("REGISTRO OK",response.toString());

                try {
                    if (response.getBoolean("success")) {
                        Log.d("MENSAJE", " Bienvenido " + response.getString("nombre"));
                        SaveSession(response.getString("nombre"));
                        startActivity(new Intent(getApplicationContext(),ListadoActivity.class));
                        finish();
                    }
                    else
                        Log.d("MENSAJE ", "no existe usuario");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.v("REGISTRO ERROR",errorResponse.toString());

            }
        });
    }
    private void SaveSession(String nombre){
        SharedPreferences sharedPref = getSharedPreferences("loginsesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nombre", nombre);
        editor.apply();

    }
}
