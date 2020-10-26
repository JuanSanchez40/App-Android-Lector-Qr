package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import codigo.app.qr_pensionados.clases.DbManager;

public class PensionadosActivity extends AppCompatActivity {

    private DbManager db;
    private Cursor c;
    private ImageView _foto;
    private TextView _nombre, _descripcion,_stock,_totalconsulta, _totalmedicina, _total ;
    private ProgressBar _pb;
    private Button btnScanner2, btnRegrezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pensionados);
        _foto = findViewById(R.id.foto);
        _nombre = findViewById(R.id.nombre);
        _descripcion = findViewById(R.id.descripcion);
        _stock = findViewById(R.id.stock);
        _totalconsulta = findViewById(R.id.totalconsulta);
        _totalmedicina = findViewById(R.id.totalmedicina);
        _total = findViewById(R.id.total);
        _pb = findViewById(R.id.pb);
        db = new DbManager(getApplicationContext());
        btnScanner2= findViewById(R.id.btnScanner2);
        btnRegrezar= findViewById(R.id.btnRegrezar);
        int id = getIntent().getExtras().getInt("id");
        //Toast.makeText(getApplicationContext(),"ID : "+id,Toast.LENGTH_LONG).show();
        btnScanner2.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LectorActivity.class)));
        btnRegrezar.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ListadoActivity.class)));
        db= new DbManager(getApplicationContext());


        c = db.getCursor("productos","_id="+id);
        if(c.moveToFirst()){
            do{
                _nombre.setText(c.getString(c.getColumnIndexOrThrow("nombre")));
                _descripcion.setText(c.getString(c.getColumnIndexOrThrow("descripcion")));
                _stock.setText(c.getString(c.getColumnIndexOrThrow("stock")));
                _totalconsulta.setText(c.getString(c.getColumnIndexOrThrow("totalconsulta")));
                _totalmedicina.setText(c.getString(c.getColumnIndexOrThrow("totalmedicina")));
                _total.setText(c.getString(c.getColumnIndexOrThrow("total")));



                Picasso.with(getApplicationContext())
                        .load("http://192.168.1.71/Proyecto-IA/img/"+c.getString(c.getColumnIndexOrThrow("foto")))
                        .into(_foto, new Callback() {
                            @Override
                            public void onSuccess() {
                                _pb.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                _pb.setVisibility(View.GONE);

                            }
                        });


            }while (c.moveToNext());
        }else {
            Toast.makeText(getApplicationContext(),"NO EXISTE VALOR",Toast.LENGTH_LONG).show();
        }

    }
}
