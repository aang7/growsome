package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableTipoGasto {

    public static final String TABLE_NAME = "TipoGasto";
    public static final String COL_ICOD = "iCodTipoGasto";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_DATE = "dtCreacion";

    private static final String DB_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_DESC + " text not null, "
            + COL_DATE + " text not null"
            + ");";

    private static final String[] DB_DEFAULT = new String[] {"insert into "
            + TABLE_NAME
            + "("
            + COL_DESC+ ", "
            + COL_DATE
            + ")"
            + " values "
            + "('Servicio', "
            + "datetime('now', 'localtime')) ", "insert into "
            + TABLE_NAME
            + "("
            + COL_DESC+ ", "
            + COL_DATE
            + ")"
            + " values "
            + "('Producto', "
            + "datetime('now', 'localtime')); "};

    public static void  onCreate(SQLiteDatabase database)
    {
        database.execSQL(DB_CREATE);
        for (String query : DB_DEFAULT)
        {
            database.execSQL(query);
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }
}
