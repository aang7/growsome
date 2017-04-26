package xyz.growsome.growsome.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import xyz.growsome.growsome.DBTables.*;

/**
 * Created by aang on 3/27/17.
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "growsome.db";
    private static final int DB_VERSION = 7;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        TableUsuarios.onCreate(database);
        TableGastos.onCreate(database);
        TableCategorias.onCreate(database);
        TableIngresos.onCreate(database);
        TableTipoUsuario.onCreate(database);
        TableTipoGasto.onCreate(database);
        TableTipoIngreso.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        TableUsuarios.onUpgrade(database, oldVersion, newVersion);
        TableGastos.onUpgrade(database, oldVersion, newVersion);
        TableCategorias.onUpgrade(database, oldVersion, newVersion);
        TableIngresos.onUpgrade(database, oldVersion, newVersion);
        TableTipoUsuario.onUpgrade(database, oldVersion, newVersion);
        TableTipoGasto.onUpgrade(database, oldVersion, newVersion);
        TableTipoIngreso.onUpgrade(database, oldVersion, newVersion);
    }

    public Cursor selectQuery (String query)
    {
        SQLiteDatabase db =  this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{});
        return cursor;
    }

    /*public List<String> selectQuery (String query, int col)
    {

    }*/

    public boolean execQuery (String query)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL(query);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }
}
