package f.repositories;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
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

//        String categoryName = model.getName();
        String categoryName = "Мат";
// Сначала вставляем данные в таблицу категорий.
        contentValues.put (dbHelper.COLUMN_PACK_NAME, categoryName);
        database.insert (dbHelper.TABLE_PACK, null, contentValues);
        database.close();
    }

    @Override
    public void update(Pack model) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Pack find(Long aLong) {
        return null;
    }

    @Override
    public List<Pack> findAll() {
        return null;
    }
}
