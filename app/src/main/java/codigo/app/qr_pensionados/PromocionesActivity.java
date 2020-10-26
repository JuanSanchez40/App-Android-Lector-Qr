package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import codigo.app.qr_pensionados.clases.DbManager;

public class PromocionesActivity extends AppCompatActivity {

    private DbManager db;
    private GridView gridView;
    private Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);
        setTitle("PROMOCIONES");
        db = new DbManager(getApplicationContext());

        gridView = findViewById(R.id.grid);

        c = db.getCursor("promociones","1");
        if (c.moveToFirst())
            gridView.setAdapter(new PromocionesAdapter(getApplicationContext(),c));
        else
            Log.d("ERROR VACIO", "promociones");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor item = (Cursor) gridView.getItemAtPosition(i);
                int id= item.getInt(item.getColumnIndexOrThrow("_id"));


                startActivity(
                        new Intent(getApplicationContext()
                                ,PromocionActivity.class)
                                .putExtra("id",id)
                );
            }
        });
    }

}



class PromocionesAdapter extends CursorAdapter {
    public PromocionesAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.gridpromocion,viewGroup,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView urlfoto_ = view.findViewById(R.id.urlfoto);
        TextView nombre_ = view.findViewById(R.id.nombre);
        ProgressBar pb_ = view.findViewById(R.id.pb);
        nombre_.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));

        Picasso.with(context)
                .load("http://192.168.1.71/Proyecto-IA/img/promociones/"+cursor.getString(cursor.getColumnIndexOrThrow("foto")))
                .into(urlfoto_, new Callback() {
                        @Override
                    public void onSuccess() {
                      pb_.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        pb_.setVisibility(View.GONE);
                    }
                });


    }
}