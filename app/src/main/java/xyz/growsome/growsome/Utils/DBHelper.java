package xyz.growsome.growsome.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aang on 3/27/17.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "GrowSomeDB";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        updateMyDatabase(sqLiteDatabase, oldVersion, newVersion);
    }

    public void updateMyDatabase(SQLiteDatabase db, int oldVersion,int newVersion){

        if (oldVersion < 1) {
            /* Creacion de la tabla */
            db.execSQL("CREATE TABLE " +  DB_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
            //insertando algo --para ejemplo
            insertSQL(db,  "INSERT INTO " + DB_NAME +
                    "(NAME) VALUES ('PrimerNombre');");
        }
    }

    public static void insertSQL(SQLiteDatabase db, String SQLCommand){
        db.execSQL(SQLCommand);
    }
}
