package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableGastos {

    public static final String TABLE_NAME = "Gastos";
    public static final String COL_ICOD = "iCodGasto";
    public static final String COL_ICODTIPO = "iCodTipoGasto";
    public static final String COL_NOMBRE = "vchNombre";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_COSTO = "fltCosto";
    public static final String COL_CANT = "iCantidad";
    public static final String COL_FECHA = "dtFecha";
    public static final String COL_DATE = "dtCreacion";

    private static final String DB_CREATE = "create table"
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_ICODTIPO + " integer not null, "
            + COL_NOMBRE + " text not null, "
            + COL_DESC + " text, "
            + COL_COSTO + " real not null, "
            + COL_CANT + " integer not null, "
            + COL_FECHA + " text not null, "
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
}
