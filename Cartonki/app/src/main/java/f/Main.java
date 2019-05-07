package f;

import android.database.sqlite.SQLiteDatabase;

import f.models.Card;
import f.repositories.CardsRepository;
import f.repositories.CardsRepositoryJdbcImpl;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import java.util.Properties;

import static android.content.Context.MODE_PRIVATE;

public class Main {

    private static final String dbName = "card.db";
    private static final String createDBCard = "\"card\" (" +
            "\"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "\"question\" TEXT," +
            "\"answer\" TEXT," +
            "\"done\" BLOB NOT NULL," +
            "\"pack\" INTEGER NOT NULL);";
    private static final String createDBPack = "\"pack\" (" +
            "\"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\"name\" TEXT NOT NULL UNIQUE);";
    private static final String createDBSqlite_sequence = "\"sqlite_sequence\" (" +
            "\"name\" TEXT," +
            "\"seq\" TEXT);";

    public static void main(String[] args) {
        Connection connection = addConnection();

        CardsRepository cardsRepository = new CardsRepositoryJdbcImpl(connection);
    }

    private static Connection addConnection() {
//        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(dbName, MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + createDBSqlite_sequence);
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + createDBCard);
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + createDBPack);
//        Properties properties = new Properties();
//        try {
//            properties.load(new FileReader("db.properties"));
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//        String username = properties.getProperty("db.username");
//        String password = properties.getProperty("db.password");
//        String url = properties.getProperty("db.url");
//
//        Connection connection;
//
//        try {
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            throw new IllegalStateException(e);
//        }
//        return connection;
        return null;
    }
}

