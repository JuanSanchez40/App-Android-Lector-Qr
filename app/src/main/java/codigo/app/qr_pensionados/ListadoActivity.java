package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codigo.app.qr_pensionados.clases.DbManager;
import cz.msebera.android.httpclient.Header;

public class ListadoActivity extends AppCompatActivity {

    private Button btnScanner, btnPromociones;
    private DbManager db;
    private ListView lista;
    private ProductosAdapter adapter;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        db = new DbManager(getApplicationContext());
        btnScanner= findViewById(R.id.btnScanner);
        btnPromociones= findViewById(R.id.btnPromociones);
        lista = findViewById(R.id.listaProductos);

        btnScanner.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LectorActivity.class)));

        btnPromociones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View view){
              startActivity(new Intent(getApplicationContext(),PromocionesActivity.class));
                    }
        });

        c = db.getCursor("productos", "1");
        if(c.moveToFirst()){
            adapter = new ProductosAdapter(getApplicationContext(),c);
            lista.setAdapter(adapter);
        }else{
            Log.d("ERROR VACIO", "pro");
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor item = (Cursor) lista.getItemAtPosition(i);
                int id= item.getInt(item.getColumnIndexOrThrow("_id"));
                //Toast.makeText(getApplicationContext(),"id: "+id,Toast.LENGTH_LONG).show();

                startActivity(
                        new Intent(getApplicationContext()
                                ,PensionadosActivity.class)
                                .putExtra("id",id)
                );
            }
        });
        checkPermission();

    }
    private void checkPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //onfig.mensaje(this., "This version is not Android 6 or later ") + Build.VERSION.SDK_INT);
        }else{
            int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.CAMERA);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                //Config.mensaje(this, "Requesting permissions");
            }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){
                //Config.mensaje(this, "The permissions are alredy granted ");
                //openCamera();
            }
        }
        return;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
       if (REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
           if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

           } else {

           }
       }else{
           super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       }
    }


   //boton cerrar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    ///boton al dar click en cerrar
    public boolean onOptionsItemSelected(MenuItem item) {
          switch (item.getItemId()) {
              case R.id.Cerrar:
                  SharedPreferences sharedPref = getSharedPreferences("loginsesion", Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedPref.edit();
                  editor.putString("nombre", "");
                  editor.apply();
                  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  finish();
                  return  true;
              default:
                  return super.onOptionsItemSelected(item);



          }
    }
}


class ProductosAdapter extends CursorAdapter{
    public ProductosAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.listaproducto,viewGroup,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView urlfoto_ = view.findViewById(R.id.urlfoto);
        TextView titulo_ = view.findViewById(R.id.titulo);
        TextView precio_ = view.findViewById(R.id.precio);
        titulo_.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
        precio_.setText(cursor.getString(cursor.getColumnIndexOrThrow("precio")));
        Picasso.with(context)
                .load("http://192.168.1.71/Proyecto-IA/img/"+cursor.getString(cursor.getColumnIndexOrThrow("foto")))
                .into(urlfoto_, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });


    }
}
