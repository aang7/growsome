package xyz.growsome.growsome.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xyz.growsome.growsome.DBTables.TableGastos;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoGasto;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.DBTables.TableTipoUsuario;
import xyz.growsome.growsome.DBTables.TableUsuarios;

/**
 * Created by aang on 3/27/17.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "growsome.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        TableUsuarios.onCreate(database);
        TableGastos.onCreate(database);
        TableIngresos.onCreate(database);
        TableTipoUsuario.onCreate(database);
        TableTipoGasto.onCreate(database);
        TableTipoIngreso.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        TableUsuarios.onUpgrade(database, oldVersion, newVersion);
        TableGastos.onUpgrade(database, oldVersion, newVersion);
        TableIngresos.onUpgrade(database, oldVersion, newVersion);
        TableTipoUsuario.onUpgrade(database, oldVersion, newVersion);
        TableTipoGasto.onUpgrade(database, oldVersion, newVersion);
        TableTipoIngreso.onUpgrade(database, oldVersion, newVersion);
    }

}
