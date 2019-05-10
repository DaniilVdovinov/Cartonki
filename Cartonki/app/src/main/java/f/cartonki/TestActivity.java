package f.cartonki;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import f.models.Card;
import f.repositories.CardsRepository;
import f.repositories.CardsRepositoryJdbcImpl;
import f.repositories.DBHelper;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.IOException;
import java.util.List;

public class TestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Long deckId;
    TextView question, answer;
    Button showAnswer, rightAnswer, wrongAnswer;
    LinearLayout linearLayout1, linearLayout2;
    Card card;
    CardsRepository cardsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_test);
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

        Intent intent = getIntent();
        deckId = intent.getLongExtra("deckId", 0);
        Log.d("Колода: ", deckId + "");

        cardsRepository = new CardsRepositoryJdbcImpl();
        question = findViewById(R.id.question_text);
        answer = findViewById(R.id.answer_text);
        card = cardsRepository.findNewCard(deckId,this);

        showAnswer = findViewById(R.id.show_answer);
        showAnswer.setOnClickListener(this);

        if(card == null){
            question.setText("Все вопросы разобраны");
            showAnswer.setText("Сбросить статистику колоды");
            showAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardsRepositoryJdbcImpl cardsRepositoryJdbc = new CardsRepositoryJdbcImpl();
                    List<Card> cards_done = cardsRepositoryJdbc.findDoneInPack(deckId,TestActivity.this);
                    for (Card card:cards_done
                    ) {
                        card.setDone(0);
                        cardsRepositoryJdbc.update(card, TestActivity.this);
                    }
                    Toast.makeText(TestActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TestActivity.this, TestActivity.class);
                    intent.putExtra("deckId", deckId);
                    startActivity(intent);
                }
            });
        } else {
            question.setText(card.getQuestion());
        }

        rightAnswer = findViewById(R.id.right_answer);
        rightAnswer.setOnClickListener(this);

        wrongAnswer = findViewById(R.id.wrong_answer);
        wrongAnswer.setOnClickListener(this);

        linearLayout1 = findViewById(R.id.question_view);
        linearLayout2 = findViewById(R.id.answer_view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_test);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            } else if (itemID == R.id.show_answer){
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                showAnswerLayout();
            } else if (itemID == R.id.right_answer){
                card.setDone(1);
                cardsRepository.update(card,this);
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra("deckId", deckId);
                startActivity(intent);
            } else if (itemID == R.id.wrong_answer){
                cardsRepository.delete(card.getId(), this);
                cardsRepository.save(card, this);
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra("deckId", deckId);
                startActivity(intent);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_test);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showAnswerLayout() {
        answer.setText(card.getAnswer());
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