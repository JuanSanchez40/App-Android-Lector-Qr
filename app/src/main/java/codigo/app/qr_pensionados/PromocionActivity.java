package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import codigo.app.qr_pensionados.clases.DbManager;

public class PromocionActivity extends AppCompatActivity {

    private DbManager db;
    private Cursor c;
    private ImageView _foto;
    private TextView _nombre, _descripcion;
    private ProgressBar _pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocion);
        setTitle("PROMOCION");
        _foto = findViewById(R.id.urlfoto);
        _nombre = findViewById(R.id.nombre);
        _descripcion = findViewById(R.id.descripcion);
        _pb = findViewById(R.id.pb);

        int id = getIntent().getExtras().getInt("id");
        //Toast.makeText(getApplicationContext(),"ID : "+id,Toast.LENGTH_LONG).show();

        db= new DbManager(getApplicationContext());


        c = db.getCursor("promociones","_id="+id);
        if(c.moveToFirst()){
            do{
                _nombre.setText(c.getString(c.getColumnIndexOrThrow("nombre")));
                _descripcion.setText(c.getString(c.getColumnIndexOrThrow("descripcion")));

                Picasso.with(getApplicationContext())
                        .load("http://192.168.1.71/Proyecto-IA/img/promociones/"+c.getString(c.getColumnIndexOrThrow("foto")))
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
        }
    }
}
