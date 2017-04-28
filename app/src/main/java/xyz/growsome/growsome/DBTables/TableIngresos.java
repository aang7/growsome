package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import xyz.growsome.growsome.Ingresos.*;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableIngresos {

    public static final String TABLE_NAME = "Ingresos";
    public static final String COL_ICOD = "iCodIngreso";
    public static final String COL_ICODUSUARIO = "iCodUsuario";
    public static final String COL_ICODTIPO = "iCodTipoIngreso";
    public static final String COL_ICODCAT = "iCodCategoria";
    public static final String COL_NOMBRE = "vchNombre";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_MONTO = "fltMonto";
    public static final String COL_FECHA = "dtFecha";
    public static final String COL_DELETE = "bEliminado";
    public static final String COL_DATE = "dtCreacion";

    public static final int COL_ICOD_ID = 0;
    public static final int COL_ICODUSUARIO_ID = 1;
    public static final int COL_ICODTIPO_ID = 2;
    public static final int COL_ICODCAT_ID = 3;
    public static final int COL_NOMBRE_ID = 4;
    public static final int COL_DESC_ID = 5;
    public static final int COL_MONTO_ID = 6;
    public static final int COL_FECHA_ID = 7;
    public static final int COL_DELETE_ID = 8;
    public static final int COL_DATE_ID = 9;

    private static final String DB_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COL_ICOD + " integer not null primary key autoincrement, "
            + COL_ICODUSUARIO + " integer not null, "
            + COL_ICODTIPO + " integer not null, "
            + COL_ICODCAT + " integer not null, "
            + COL_NOMBRE + " text not null, "
            + COL_DESC + " text, "
            + COL_MONTO + " real not null, "
            + COL_FECHA + " text not null, "
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

    public static boolean insert(SQLiteDatabase db, JSONObject obj)
    {
        try
        {
            String query = "insert into "
                    + TABLE_NAME
                    + "("
                    + COL_ICODUSUARIO + ", "
                    + COL_ICODTIPO + ", "
                    + COL_ICODCAT + ", "
                    + COL_NOMBRE + ", "
                    + COL_DESC + ", "
                    + COL_MONTO + ", "
                    + COL_FECHA + ", "
                    + COL_DELETE + ", "
                    + COL_DATE
                    + ")"
                    + " values "
                    + "(" + obj.getString(COL_ICODUSUARIO) + ", "
                    + "" + obj.getString(COL_ICODTIPO) + ", "
                    + "" + obj.getString(COL_ICODCAT) + ", "
                    + "'" + obj.getString(COL_NOMBRE) + "', "
                    + "'" + obj.getString(COL_DESC) + "', "
                    + "" + obj.getString(COL_MONTO) + ", "
                    + "'" + obj.getString(COL_FECHA) + "', "
                    + "0,"
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

    public static boolean insert(SQLiteDatabase db, Ingresos obj)
    {
        if(obj instanceof Pago || obj instanceof Salario)
        {
            String query = "insert into "
                    + TABLE_NAME
                    + "("
                    + COL_ICODUSUARIO + ", "
                    + COL_ICODTIPO + ", "
                    + COL_ICODCAT + ", "
                    + COL_NOMBRE + ", "
                    + COL_DESC + ", "
                    + COL_MONTO + ", "
                    + COL_FECHA + ", "
                    + COL_DELETE + ", "
                    + COL_DATE
                    + ")"
                    + " values "
                    + "(" + obj.getUserid() + ", "
                    + "" + obj.getTipoid() + ", "
                    + "" + obj.getCatid() + ", "
                    + "'" + obj.getNombre().replaceAll("'", "''") + "', "
                    + "'" + obj.getDesc().replaceAll("'", "''") + "', "
                    + "" + obj.getMonto() + ", "
                    + "'" + obj.getFecha("yyyy-MM-dd HH:mm:ss") + "', "
                    + "0,"
                    + "datetime('now', 'localtime')"
                    + ");";

            db.execSQL(query);
        }
        else
        {
            return  false;
        }

        return true;
    }

    public static boolean update(SQLiteDatabase db, Ingresos obj, long iCod)
    {
        if(obj instanceof Pago || obj instanceof Salario)
        {
            String query = "update "
                    + TABLE_NAME
                    + " set "
                    + COL_ICODTIPO + " = " + obj.getTipoid() + ", "
                    + COL_ICODCAT + " = " + obj.getCatid() + ", "
                    + COL_NOMBRE + " = '" + obj.getNombre().replaceAll("'", "''") + "', "
                    + COL_DESC + " = '" + obj.getDesc().replaceAll("'", "''") + "', "
                    + COL_MONTO + " = " + obj.getMonto() + ", "
                    + COL_FECHA + " = '" + obj.getFecha("yyyy-MM-dd HH:mm:ss") + "', "
                    + COL_DATE + " = datetime('now', 'localtime')"
                    + " where "
                    + COL_ICOD + " = " + iCod
                    + ";";

            db.execSQL(query);
        }
        else
        {
            return  false;
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
}
