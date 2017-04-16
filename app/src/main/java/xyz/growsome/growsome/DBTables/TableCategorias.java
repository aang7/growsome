package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableCategorias {

    public static final String TABLE_NAME = "Categorias";
    public static final String COL_ICOD = "iCodCategoria";
    public static final String COL_ICODUSUARIO = "iCodUsuario";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_DATE = "dtCreacion";

    private static final String DB_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_ICODUSUARIO + " integer not null, "
            + COL_DESC + " text not null, "
            + COL_DATE + " text not null"
            + ");";

    public static void  onCreate(SQLiteDatabase database)
    {
        database.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }
}
