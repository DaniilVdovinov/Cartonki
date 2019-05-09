package f.repositories;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import f.cartonki.MainActivity;
import f.models.Pack;

import java.util.ArrayList;
import java.util.List;

public class PacksRepositoryJdbcImpl extends Application implements PacksRepository {

    private DBHelper dbHelper;

    @Override
    public ArrayList<Pack> findAll(Long id) {
        return null;
    }

    @Override
    public void save(Pack model, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put (dbHelper.COLUMN_PACK_NAME, model.getName());
        database.insert (dbHelper.TABLE_PACK, null, contentValues);
        database.close();
    }

    @Override
    public void update(Pack model, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COLUMN_PACK_NAME, model.getName());
        database.update(dbHelper.TABLE_PACK, contentValues," " + dbHelper.COLUMN_ID + " = " + model.getId(),
                null);
        database.close();
    }

    @Override
    public void delete(Long id, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        database.delete(dbHelper.TABLE_PACK," " + dbHelper.COLUMN_ID + " = " + id, null);
    }

    @Override
    public Pack find(Long id, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.query(dbHelper.TABLE_PACK, null, dbHelper.COLUMN_ID + " = "  + id, null, null, null, null);
        if(!cursor.isNull(1)) {
            return new Pack(cursor.getInt(1),cursor.getString(2));
        }
        return new Pack();
    }

    @Override
    public ArrayList<Pack> findAll(Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.query(dbHelper.TABLE_PACK, null, null, null, null, null, null);
        ArrayList<Pack> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Pack pack = new Pack(cursor.getInt(0),cursor.getString(1));
            list.add(pack);
        }
        return list;
    }
}
