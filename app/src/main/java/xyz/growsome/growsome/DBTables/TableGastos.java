package xyz.growsome.growsome.DBTables;

import android.database.sqlite.SQLiteDatabase;

import xyz.growsome.growsome.Gastos.Gastos;
import xyz.growsome.growsome.Gastos.Producto;
import xyz.growsome.growsome.Gastos.Servicio;
import xyz.growsome.growsome.Ingresos.Ingresos;
import xyz.growsome.growsome.Ingresos.Pago;
import xyz.growsome.growsome.Ingresos.Salario;

/**
 * Created by cheko on 11/04/2017.
 */

public class TableGastos {

    public static final String TABLE_NAME = "Gastos";
    public static final String COL_ICOD = "iCodGasto";
    public static final String COL_ICODUSUARIO = "iCodUsuario";
    public static final String COL_ICODTIPO = "iCodTipoGasto";
    public static final String COL_ICODCAT = "iCodCategoria";
    public static final String COL_NOMBRE = "vchNombre";
    public static final String COL_DESC = "vchDesc";
    public static final String COL_COSTO = "fltCosto";
    public static final String COL_CANT = "iCantidad";
    public static final String COL_FECHA = "dtFecha";
    public static final String COL_DATE = "dtCreacion";

    public static final int COL_ICOD_ID = 0;
    public static final int COL_ICODUSUARIO_ID = 1;
    public static final int COL_ICODTIPO_ID = 2;
    public static final int COL_ICODCAT_ID = 3;
    public static final int COL_NOMBRE_ID = 4;
    public static final int COL_DESC_ID = 5;
    public static final int COL_COSTO_ID = 6;
    public static final int COL_CANT_ID = 7;
    public static final int COL_FECHA_ID = 8;
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
            + COL_COSTO + " real not null, "
            + COL_CANT + " integer not null, "
            + COL_FECHA + " text not null, "
            + COL_DATE + " text not null"
            + ");";

    public static void  onCreate(SQLiteDatabase database)
    {
        database.execSQL(DB_CREATE);
    }

    public static final String SELECT_ALL = "select * from " + TABLE_NAME;

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }

    public static boolean insert(SQLiteDatabase db, Gastos obj)
    {
        if(obj instanceof Servicio || obj instanceof Producto)
        {
            String query = "insert into "
                    + TABLE_NAME
                    + "("
                    + COL_ICODUSUARIO + ", "
                    + COL_ICODTIPO + ", "
                    + COL_ICODCAT + ", "
                    + COL_NOMBRE + ", "
                    + COL_DESC + ", "
                    + COL_COSTO + ", "
                    + COL_CANT + ", "
                    + COL_FECHA + ", "
                    + COL_DATE
                    + ")"
                    + " values "
                    + "(" + obj.getUserid() + ", "
                    + "" + obj.getTipoid() + ", "
                    + "" + obj.getCatid() + ", "
                    + "'" + obj.getNombre().replaceAll("'", "''") + "', "
                    + "'" + obj.getDesc().replaceAll("'", "''") + "', "
                    + "" + obj.getCosto() + ", "
                    + "" + obj.getCantidad() + ", "
                    + "'" + obj.getFecha("yyyy-MM-dd HH:mm:ss") + "', "
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

    public static boolean update(SQLiteDatabase db, Gastos obj, long iCod)
    {
        if(obj instanceof Producto || obj instanceof Servicio)
        {
            String query = "update "
                    + TABLE_NAME
                    + " set "
                    + COL_ICODTIPO + " = " + obj.getTipoid() + ", "
                    + COL_ICODCAT + " = " + obj.getCatid() + ", "
                    + COL_NOMBRE + " = '" + obj.getNombre().replaceAll("'", "''") + "', "
                    + COL_DESC + " = '" + obj.getDesc().replaceAll("'", "''") + "', "
                    + COL_COSTO + " = " + obj.getCosto() + ", "
                    + COL_CANT + " = " + obj.getCantidad() + ", "
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
}
