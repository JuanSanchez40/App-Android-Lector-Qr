package codigo.app.qr_pensionados.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BdHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "observacion";
    private static final int DB_SCHEMA_VERSION = 15;

    public BdHelper( Context context) {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbManager.TABLA_PRODUCTOS_CREATE);
        sqLiteDatabase.execSQL(DbManager.TABLA_PROMOCIONES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS productos");
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS promociones");
        onCreate(sqLiteDatabase);
    }
}
