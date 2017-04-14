package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import static android.R.attr.data;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableUsuarios {

    public static final String TABLE_NAME = "Usuarios";
    public static final String COL_ICOD = "iCodUsuario";
    public static final String COL_ICODTIPO = "iCodTipoUsuario";
    public static final String COL_CORREO = "vchCorreo";
    public static final String COL_PWD = "vchPwd";
    public static final String COL_NOMBRE = "vchNombre";
    public static final String COL_ACTIVO = "iActivo";
    public static final String COL_HASH = "vchHash";
    public static final String COL_DATE = "dtCreacion";

    private static final String DB_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_ICODTIPO + " integer not null, "
            + COL_CORREO + " text not null unique, "
            + COL_PWD + " text not null, "
            + COL_NOMBRE + " text not null, "
            + COL_ACTIVO + " integer not null, "
            + COL_HASH + " text not null, "
            + COL_DATE + " text not null"
            + ");";

    public static void  onCreate(SQLiteDatabase database)
    {
        database.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exits" + TABLE_NAME);
        onCreate(database);
    }

    public static boolean insert(SQLiteDatabase db, JSONObject obj)
    {
        try
        {
            String var = obj.getString("iCodTipoUsuario");

            String query = "insert into "
                    + TABLE_NAME
                    + "("
                    + COL_ICODTIPO + ", "
                    + COL_CORREO + ", "
                    + COL_PWD + ", "
                    + COL_NOMBRE + ", "
                    + COL_ACTIVO + ", "
                    + COL_HASH + ", "
                    + COL_DATE
                    + ")"
                    + " values "
                    + "(" + obj.getString(COL_ICODTIPO) + ", "
                    + "'" + obj.getString(COL_CORREO) + "', "
                    + "'" + obj.getString(COL_PWD) + "', "
                    + "'" + obj.getString(COL_NOMBRE) + "', "
                    + "" + obj.getString(COL_ACTIVO) + ", "
                    + "'" + obj.getString(COL_HASH) + "', "
                    + "'" + obj.getString(COL_DATE) + "'"
                    + ");";

            db.execSQL(query);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }
}
