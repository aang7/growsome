package xyz.growsome.growsome.DBTables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import xyz.growsome.growsome.Categorias.Categoria;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableCategorias {

    public static final String TABLE_NAME = "Categorias";
    public static final String COL_ICOD = "iCodCategoria";
    public static final String COL_ICODUSUARIO = "iCodUsuario";
    public static final String COL_NOMBRE = "vchNombre";
    public static final String COL_COLOR = "vchColor";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_DELETE = "bEliminado";
    public static final String COL_DATE = "dtCreacion";

    public static final int COL_ICOD_ID = 0;
    public static final int COL_ICODUSUARIO_ID = 1;
    public static final int COL_NOMBRE_ID = 2;
    public static final int COL_COLOR_ID = 3;
    public static final int COL_DESC_ID = 4;
    public static final int COL_DELETE_ID = 5;
    public static final int COL_DATE_ID = 6;

    private static final String DB_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_ICODUSUARIO + " integer not null, "
            + COL_NOMBRE + " text not null, "
            + COL_COLOR + " text not null, "
            + COL_DESC + " text not null, "
            + COL_DELETE + " integer not null, "
            + COL_DATE + " text not null"
            + ");";

    public static final String SELECT_ALL = "select * from " + TABLE_NAME + " where "
            + COL_DELETE + " = 0;";

    public static void  onCreate(SQLiteDatabase database)
    {
        database.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }

    public static int getUserID(SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery("select " + COL_ICOD +" from " + TABLE_NAME, new String[]{});
        cursor.moveToFirst();
        return  cursor.getInt(COL_ICOD_ID);
    }

    public static boolean insert(SQLiteDatabase db, Categoria obj)
    {
        try
        {
            String query = "insert into "
                    + TABLE_NAME
                    + "("
                    + COL_ICODUSUARIO + ", "
                    + COL_NOMBRE + ", "
                    + COL_COLOR + ", "
                    + COL_DESC + ", "
                    + COL_DELETE + ", "
                    + COL_DATE
                    + ")"
                    + " values "
                    + "(" + obj.getUserid() + ", "
                    + "'" + obj.getName() + "', "
                    + "'" + obj.getColor() + "', "
                    + "'" + obj.getDesc() + "', "
                    + "0, "
                    + "datetime('now', 'localtime')"
                    + ");";

            db.execSQL(query);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    public static boolean update(SQLiteDatabase db, Categoria obj, long iCod)
    {
        String query = "update "
                + TABLE_NAME
                + " set "
                + COL_NOMBRE + " = '" + obj.getName().replaceAll("'", "''") + "', "
                + COL_COLOR + " = '" + obj.getColor().replaceAll("'", "''") + "', "
                + COL_DESC + " = '" + obj.getDesc().replaceAll("'", "''") + "', "
                + COL_DATE + " = datetime('now', 'localtime')"
                + " where "
                + COL_ICOD + " = " + iCod
                + ";";
        try
        {
            db.execSQL(query);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean delete(SQLiteDatabase db, long iCod)
    {
        String query = "update "
                + TABLE_NAME
                + " set "
                + COL_DELETE + " = 1"
                + " where "
                + COL_ICOD + " = " + iCod
                + ";";
        try
        {
            db.execSQL(query);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static int getCatID(SQLiteDatabase db, String name)
    {
        Cursor cursor = db.rawQuery("select " + COL_ICOD + " from " + TABLE_NAME
                + " where " + COL_NOMBRE + " = '" + name + "';", new String[]{});

        int count = cursor.getCount();

        if(count < 1)
        {
            return 0;
        }

        cursor.moveToFirst();

        return  cursor.getInt(0);
    }

    public static String getCatName(SQLiteDatabase db, long id)
    {
        Cursor cursor = db.rawQuery("select " + COL_NOMBRE + " from " + TABLE_NAME
                + " where " + COL_ICOD + " = " + id + ";", new String[]{});

        int count = cursor.getCount();

        if(count < 1)
        {
            return "";
        }

        cursor.moveToFirst();

        return  cursor.getString(0);
    }
}
