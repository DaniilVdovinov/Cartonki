package f.cartonki;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import org.apache.poi.*;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import f.models.Card;
import f.models.Pack;
import f.repositories.CardsRepositoryJdbcImpl;
import f.repositories.ExcelMapper;
import f.repositories.PacksRepositoryJdbcImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import f.repositories.DBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    TextView text;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Button addDeckChooseVariant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        // сторонняя библиотека для диаграммы на главной странице
        // только пример использования
        // https://github.com/blackfizz/EazeGraph


        dbHelper = new DBHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("Не удается обновить бд");
        }

        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = database.rawQuery("select * from " + dbHelper.TABLE_PACK, null);
        text = findViewById(R.id.decs_count);
        text.setText(String.valueOf(cursor.getCount()));

        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
        cursor = database.rawQuery("select * from " + dbHelper.TABLE_CARD + " where done = 0", null);
        Log.d("Кол-во ", "" + cursor.getCount());
        mPieChart.addPieSlice(new PieModel("Freetime", cursor.getCount(), Color.parseColor("#FE6DA8")));
        cursor = database.rawQuery("select * from " + dbHelper.TABLE_CARD + " where done = 1", null);
        Log.d("Кол-во ", "" + cursor.getCount());
        mPieChart.addPieSlice(new PieModel("Sleep", cursor.getCount(), Color.parseColor("#56B7F1")));

        mPieChart.startAnimation();

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView_decks_activity);

        addDeckChooseVariant = (Button) findViewById(R.id.add_deck_main_activity_button);
        addDeckChooseVariant.setOnClickListener(this);

        cursor = database.rawQuery("select * from " + dbHelper.TABLE_PACK, null);
        Log.d("Кол-во колод", "" + cursor.getCount());

    }

    @Override
    protected void onResume() {
        super.onResume();
//        startSearch();
//        readFileSD();
//        PacksRepositoryJdbcImpl packsRepositoryJdbc = new PacksRepositoryJdbcImpl();
//        try {
//            ExcelMapper excelMapper = new ExcelMapper(new File("/storage/sdcard1/words.xlsx"),packsRepositoryJdbc.save(new Pack("English"), MainActivity.this));
//            Log.d("Список",excelMapper.getCardsAsList().size()+"");
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
        CardsRepositoryJdbcImpl cardsRepositoryJdbc = new CardsRepositoryJdbcImpl();
//        Card card = cardsRepositoryJdbc.findNewCard(2L,this);
//        Log.d("Кол-во записей ",cardsRepositoryJdbc.findAllInPack(2L, this).size()+"");
//        PacksRepositoryJdbcImpl packsRepositoryJdbc = new PacksRepositoryJdbcImpl();
//        packsRepositoryJdbc.findByName("Английский", this);
//        Pack pack = new Pack("Матан");
//        packsRepositoryJdbc.save(pack, this);
//        CardsRepositoryJdbcImpl cardsRepositoryJdbc = new CardsRepositoryJdbcImpl();
//        Card card = new Card(null, "nose", "нос", false, 2);
//        cardsRepositoryJdbc.save(card, this);
//        Log.d("Кол-во ", "" + cursor.getCount());
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_add_deck) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemID) {
        try {
            if (itemID == R.id.to_main_page) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemID == R.id.to_decks) {
                startActivity(new Intent(this, DecksActivity.class));
            } else if (itemID == R.id.to_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (itemID == R.id.add_deck_main_activity_button) {
                startActivity(new Intent(this, AddDeckChooseVariantActivity.class));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        displaySelectedScreen(id);
    }
}
