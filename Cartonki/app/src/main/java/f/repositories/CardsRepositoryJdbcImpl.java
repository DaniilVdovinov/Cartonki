package f.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import f.cartonki.MainActivity;
import f.models.Card;
import f.models.Pack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardsRepositoryJdbcImpl implements CardsRepository {

    private Connection connection;

    private DBHelper dbHelper;

    //language=SQL
    private final String SQL_INSERT_USER = "insert into " +
            "card (question, answer, pack) values (?, ?, ?);";
    //language=SQL
    private final String SQL_UPDATE_USER = "update card set " +
            "(question, answer, pack) = (?, ?, ?) where id = ?;";

    public CardsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Card> userRowMapper = row -> {
        Long id = row.getLong("id");
        String question = row.getString("question");
        String answer = row.getString("answer");
        Integer pack = row.getInt("pack");
        return null;
//        return new Card(id,question,answer,period,instant,pack);
    };

    @Override
    public void save(Card model, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (android.database.SQLException mSQLException) {
            throw mSQLException;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COLUMN_CARD_QUESTION, model.getQuestion());
        contentValues.put(dbHelper.COLUMN_CARD_ANSWER, model.getAnswer());
        contentValues.put(dbHelper.COLUMN_CARD_DONE, model.getDone());
        contentValues.put(dbHelper.COLUMN_CARD_PACK, model.getPack());

        database.insert(dbHelper.TABLE_CARD, null, contentValues);
        database.close();
    }

    @Override
    public void update(Card model, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (android.database.SQLException mSQLException) {
            throw mSQLException;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COLUMN_CARD_QUESTION, model.getQuestion());
        contentValues.put(dbHelper.COLUMN_CARD_ANSWER, model.getAnswer());
        contentValues.put(dbHelper.COLUMN_CARD_DONE, model.getDone());
        contentValues.put(dbHelper.COLUMN_CARD_PACK, model.getPack());
        database.update(dbHelper.TABLE_CARD, contentValues, " " + dbHelper.COLUMN_ID + " = " + model.getId(),
                null);
        database.close();
    }

    @Override
    public void delete(Long id, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (android.database.SQLException mSQLException) {
            throw mSQLException;
        }
        database.delete(dbHelper.TABLE_CARD, " " + dbHelper.COLUMN_ID + " = " + id, null);
    }

    @Override
    public Card find(Long id, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (android.database.SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.query(dbHelper.TABLE_CARD, null, dbHelper.COLUMN_ID + " = " + id, null, null, null, null);
        if (!cursor.isNull(1)) {
            return new Card(cursor.getLong(1), cursor.getString(2),
                    cursor.getString(3),
                    new Byte(cursor.getBlob(4)[0]).equals(1), cursor.getInt(5));
        }
        return new Card();
    }

    @Override
    public ArrayList<Card> findAll(Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.query(dbHelper.TABLE_PACK, null, null, null, null, null, null);
        ArrayList<Card> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Card card = new Card(cursor.getLong(1), cursor.getString(2),
                    cursor.getString(3),
                    new Byte(cursor.getBlob(4)[0]).equals(1), cursor.getInt(5));
            list.add(card);
        }
        return list;
    }


    @Override
    public List<Card> findAllInPack(Long id, Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.query(dbHelper.TABLE_PACK, null,
                dbHelper.COLUMN_CARD_PACK + " = " + id,
                null, null, null, null);
        
        ArrayList<Card> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Card card = new Card(cursor.getLong(1), cursor.getString(2),
                    cursor.getString(3),
                    new Byte(cursor.getBlob(4)[0]).equals(1), cursor.getInt(5));
            list.add(card);
        }
        return list;
    }
}
