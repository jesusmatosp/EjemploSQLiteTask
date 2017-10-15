package pe.edu.sise.ejemplosqlitatareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jesus on 14/10/2017.
 */

public class DataBaseHelper{

    private Context context = null;
    private DataBaseHelperInternal mDbHelper = null;
    private SQLiteDatabase mDb = null;
    private static final String DATABASE_NAME = "TODOLIST";
    private static  final int DATABASE_VERSION = 1;

    // tablas y campos
    private static final String DATABASE_TABLE_TODOLIST = "todolist";
    public static final String SL_ID = "_id";
    public static final String SL_ITEM = "task";
    public static final String SL_PLACE = "place";
    public static final String SL_IMPORTANCE = "importance";
    public static final String SL_DESCRIPTION = "description";

    //SQL de Creación de la tabla
    private static final String DATABASE_CREATE_TODOLIST =
            "CREATE TABLE "+DATABASE_TABLE_TODOLIST+" ("+SL_ID+" integer primary key, "+SL_ITEM+" text not null, "+
                    SL_PLACE+" text not null, "+SL_IMPORTANCE+" integer not null, "+SL_DESCRIPTION+" TEXT)";

    // Constructor
    public DataBaseHelper(Context context){
            this.context = context;
    }

    private static class DataBaseHelperInternal extends SQLiteOpenHelper {

        public DataBaseHelperInternal(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            deleteTables(db);
            createTables(db);
        }

        private void createTables(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE_TODOLIST);
        }

        private  void deleteTables(SQLiteDatabase db){
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TODOLIST);
        }
    }

    public DataBaseHelper open() throws SQLException {
        mDbHelper = new DataBaseHelperInternal(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDbHelper.close();
    }

    public Cursor getItems(){
        return mDb.query(DATABASE_TABLE_TODOLIST, new String[]{SL_ID, SL_ITEM, SL_PLACE, SL_IMPORTANCE}, null, null, null, null, SL_IMPORTANCE);
    }

    public long insertItem(String item, String place, String description, int importance){
        ContentValues initialValues = new ContentValues();
        initialValues.put(SL_IMPORTANCE, importance);
        initialValues.put(SL_ITEM, item);
        initialValues.put(SL_PLACE, place);
        initialValues.put(SL_DESCRIPTION, description);
        return  mDb.insert(DATABASE_TABLE_TODOLIST, null, initialValues);
    }

}


