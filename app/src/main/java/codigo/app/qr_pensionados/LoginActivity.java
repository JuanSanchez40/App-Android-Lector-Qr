package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private Button btnlogin;
    private EditText txtemail, txtpassword;
    private TextView btnregistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin= findViewById(R.id.btnlogin);
        txtemail= findViewById(R.id.txtemail);
        txtpassword= findViewById(R.id.txtpassword);
        btnregistro = findViewById(R.id.btnregistro);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtemail.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Ingrese email",Toast.LENGTH_LONG).show();
                else if(txtpassword.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Ingrese password",Toast.LENGTH_LONG).show();
                else
                    login(txtemail.getText().toString(),txtpassword.getText().toString());
            }
        });
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));

            }
        });
    }

    private void login(String email,String password) {

        RequestParams params = new RequestParams();
        params.put("btnlogin", "ok");
        params.put("txtemail", email);
        params.put("txtpassword", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.1.71/Proyecto-IA/ajax/apilogin.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (response.getBoolean("success")) {
                        Log.d("MENSAJE", "Bienvenido " + response.getString("nombre"));
                        SaveSession(response.getString("nombre"));
                        startActivity(new Intent(getApplicationContext(), ListadoActivity.class));
                        finish();
                    }
                    else
                        Log.d("MENSAJE", "no existe usuario");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("MENSAJE ERROR", errorResponse.toString());
            }
        });
        String sesion = GetSession("nombre");
        if (!sesion.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), ListadoActivity.class));
            finish();
        }

    }

    private String GetSession(String nombre){
        SharedPreferences sharedPref = getSharedPreferences("loginsesion", Context.MODE_PRIVATE);
        return sharedPref.getString(nombre,"");
    }

    private void SaveSession(String nombre){
        SharedPreferences sharedPref = getSharedPreferences("loginsesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nombre", nombre);
        editor.apply();

    }


}
