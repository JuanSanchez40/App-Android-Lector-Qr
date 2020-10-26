package codigo.app.qr_pensionados.clases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

    public static final String TABLA_PRODUCTOS = "productos";
    public static final String PRODUCTOS_ID = "_id";
    public static final String PRODUCTOS_NOMBRE = "nombre";
    public static final String PRODUCTOS_DESCRIPCION = "descripcion";
    public static final String PRODUCTOS_FOTO = "foto";
    public static final String PRODUCTOS_PRECIO = "precio";
    public static final String PRODUCTOS_STOCK = "stock";
    public static final String PRODUCTOS_TOTALCONSULTA = "totalconsulta";
    public static final String PRODUCTOS_TOTALMEDICINA = "totalmedicina";
    public static final String PRODUCTOS_TOTAL = "total";


    public static final String TABLA_PRODUCTOS_CREATE =
              "create table productos("+
                      "_id integer not null,"+
                      "nombre text not null,"+
                      "descripcion text not null,"+
                      "foto text not null,"+
                      "precio real not null,"+
                      "stock text not null,"+
                      "totalconsulta real not null,"+
                      "totalmedicina real not null,"+
                      "total real not null);";


    public static final String TABLA_PROMOCIONES = "promociones";
    public static final String PROMOCIONES_ID = "_id";
    public static final String PROMOCIONES_NOMBRE = "nombre";
    public static final String PROMOCIONES_DESCRIPCION = "descripcion";
    public static final String PROMOCIONES_FOTO = "foto";
    public static final String PROMOCIONES_PRODUCTOS_ID = "productos_id";
    public static final String TABLA_PROMOCIONES_CREATE =
              "create table promociones("+
                      "_id integer not null,"+
                      "nombre text not null,"+
                      "descripcion text not null,"+
                      "foto text not null,"+
                      "productos_id integer not null);";

        private BdHelper helper;
        private SQLiteDatabase db;

         public DbManager(Context context) {
                helper = new BdHelper(context);
                db = helper.getWritableDatabase();
         }
         public DbManager open() throws SQLException{
                db = helper.getWritableDatabase();
                return this;
         }
         public void close(){
             helper.close();
         }
         // CREAR PRODUCTOS
         private ContentValues generarProductos(int id, String nombre, String descripcion, String foto, double precio, String stock, double totalconsulta, double totalmedicina, double total)
         {
                 ContentValues init = new ContentValues();
                 init.put(PRODUCTOS_ID,id);
                 init.put(PRODUCTOS_NOMBRE,nombre);
                 init.put(PRODUCTOS_DESCRIPCION,descripcion);
                 init.put(PRODUCTOS_FOTO,foto);
                 init.put(PRODUCTOS_PRECIO,precio);
                 init.put(PRODUCTOS_STOCK,stock);
                 init.put(PRODUCTOS_TOTALCONSULTA,totalconsulta);
                 init.put(PRODUCTOS_TOTALMEDICINA,totalmedicina);
                 init.put(PRODUCTOS_TOTAL,total);

                 return init;
         }

             public long insertarProductos(int id,String nombre,String descripcion,String foto,double precio,String stock,double totalconsulta,double totalmedicina,double total){
                 return db.insert(TABLA_PRODUCTOS,null,generarProductos(id, nombre, descripcion, foto, precio, stock, totalconsulta, totalmedicina, total));
         }

         // CREAR PROMOCIONES
         private ContentValues generarPromociones(int id,String nombre,String descripcion,String foto, int productos_id)
         {
         ContentValues init = new ContentValues();
         init.put(PROMOCIONES_ID,id);
         init.put(PROMOCIONES_NOMBRE,nombre);
         init.put(PROMOCIONES_DESCRIPCION,descripcion);
         init.put(PROMOCIONES_FOTO,foto);
         init.put(PROMOCIONES_PRODUCTOS_ID,productos_id);
         return init;
         }
         public long insertarPromociones(int id,String nombre,String descripcion,String foto, int productos_id){
         return db.insert(TABLA_PROMOCIONES,null,generarPromociones(id, nombre, descripcion, foto, productos_id));
         }


         public Cursor getCursor(String tabla, String condicion) throws SQLException{
         String q=" SELECT * FROM " + tabla + " WHERE " + condicion;
         Cursor mc= db.rawQuery(q,null);
         if (mc!= null)
             mc.moveToFirst();
         return mc;
         }

         public void borrarTable(String table){
             db.delete(table, null, null);
         }




}
