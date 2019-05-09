package f.repositories;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by KamillaKhairullina
 * SQLite
 */
public class DBHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "cartonki.db";
    public static String DB_PATH = "";
    public static final int DB_VERSION = 3;
    // название таблиц в бд
    public static final String TABLE_CARD = "card";
    public static final String TABLE_PACK = "pack";
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CARD_QUESTION = "question";
    public static final String COLUMN_CARD_ANSWER = "answer";
    public static final String COLUMN_CARD_DONE = "done";
    public static final String COLUMN_CARD_PACK = "pack";
    public static final String COLUMN_PACK_NAME = "name";

    public SQLiteDatabase mDataBase;
    public final Context mContext;
    public boolean mNeedUpdate = false;

    //Получение пути к бд
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            Log.d("фззШ ", context.getApplicationInfo().dataDir);
        } else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();
        this.

                getReadableDatabase();

    }

    //Обновление бд
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();
            copyDataBase();
            mNeedUpdate = false;
        }
    }

    //Проверку существования бд
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    //Копирование данных в бд
    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Копирование файла бд
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    //Открытие бд
    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    //Закрытие бд
    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //Обновление бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }


}


//package f.repositories;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    private static final int DATABASE_VERSION = 1;
//    private static final String DTABASE_NAME = "card";
//
//    public DBHelper(Context context, String name, int version) {
//        super(context, name,null, version);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//}
