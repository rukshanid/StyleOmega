package com.example.rukshani.styleomega;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rukshani on 10/08/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "style_omega.db";
    public final static String DATABASE_PATH = "/data/data/com.example.rukshani.styleomega/databases/";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) throws IOException {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.myContext=context;
        createDatabase();
    }

    private void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();
        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been bumped
        }
        if(!dbExist)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {

        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //delete database
    private void db_delete()  {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");

                db_delete();

        }
    }

    public void executeSQL(String sql) {
        myDataBase= SQLiteDatabase.openDatabase("/data/data/com.example.rukshani.styleomega/databases/style_omega.db",
                null, SQLiteDatabase.OPEN_READWRITE);
        myDataBase =this.getWritableDatabase();
        myDataBase.execSQL(sql);
        myDataBase.close();

    }

    public Cursor getData(String sql) {
        myDataBase = SQLiteDatabase.openDatabase("/data/data/com.example.rukshani.styleomega/databases/style_omega.db",null, SQLiteDatabase.OPEN_READWRITE);
        myDataBase = this.getReadableDatabase();
        Cursor rs =  myDataBase.rawQuery(sql,null);
        return rs;
    }
}

