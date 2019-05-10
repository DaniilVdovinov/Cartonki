package f.cartonki;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import f.models.Card;
import f.models.Pack;
import f.repositories.CardsRepository;
import f.repositories.CardsRepositoryJdbcImpl;
import f.repositories.PacksRepositoryJdbcImpl;

import java.util.List;

public class AddCradsActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Button addButton, changeName;
    TextView deck, noQuestions;
    ConstraintLayout constraintLayout1, constraintLayout2;
    private List<Card> mAppList;
    private AddCradsActivity.AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    Long deckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_deck);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add);
//        setSupportActionBar(toolbar);


        deck = findViewById(R.id.deck_name);

        Intent intent = getIntent();
        deckId = intent.getLongExtra("deckId", 0);
        noQuestions = findViewById(R.id.no_question_text_view);

        addButton = findViewById(R.id.add_card);
        addButton.setOnClickListener(this);

        changeName = findViewById(R.id.change_deck_name);
        changeName.setOnClickListener(this);

        constraintLayout1 = findViewById(R.id.no_decks_layout_content_adding_deck);
        constraintLayout2 = findViewById(R.id.some_decks_layout_content_adding_deck);

        setData();
    }

    private void setData() {
        deck.setText(new PacksRepositoryJdbcImpl().find(deckId, AddCradsActivity.this).getName());
        CardsRepositoryJdbcImpl cardsRepositoryJdbc = new CardsRepositoryJdbcImpl();
        Log.d("ид", deckId + "");
        mAppList = cardsRepositoryJdbc.findAllInPack(deckId, this);


        if (mAppList.size() != 0) {
            constraintLayout1.setVisibility(View.INVISIBLE);
            constraintLayout2.setVisibility(View.VISIBLE);
            mListView = (SwipeMenuListView) findViewById(R.id.list_view_adding_deck);

            mAdapter = new AddCradsActivity.AppAdapter();
            mListView.setAdapter(mAdapter);

            // step 1. create a MenuCreator
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {

                    //кнопка редактирования
                    SwipeMenuItem editItem = new SwipeMenuItem(
                            getApplicationContext());
                    editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    editItem.setWidth(dp2px(90));
                    editItem.setIcon(R.drawable.edit);
                    menu.addMenuItem(editItem);

                    //кнопка удаления
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.delete);
                    menu.addMenuItem(deleteItem);
                }
            };
            mListView.setMenuCreator(creator);

            // step 2. listener item click event
            mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    Card item = mAppList.get(position);
                    switch (index) {
                        case 0:
                            edit(item);
                            break;
                        case 1:
                            delete(item);
                            mAppList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                    return false;
                }
            });

            // set SwipeListener
            mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
                }
            });

            // set MenuStateChangeListener
            mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });

            // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

            // test item long click
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                    Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

        }
    }

    private void displaySelectedScreen(int itemID) {
        try {
            if (itemID == R.id.add_card) {
                createAddingDialog();
            } else if (itemID == R.id.change_deck_name) {
                createDialog();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void createAddingDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_adding_dialog, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(layout);

        final EditText questionField = (EditText) layout.findViewById(R.id.input_text);
        final EditText answerField = (EditText) layout.findViewById(R.id.input_text2);

        mDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Добавить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CardsRepository cardsRepository = new CardsRepositoryJdbcImpl();
                                cardsRepository.save(new Card(null, questionField.getText().toString(), answerField.getText().toString(), 0, deckId), AddCradsActivity.this);
                                setData();
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();
    }

    private void createDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(layout);

        final EditText userInput = (EditText) layout.findViewById(R.id.input_text);

        mDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Изменить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PacksRepositoryJdbcImpl packsRepositoryJdbc = new PacksRepositoryJdbcImpl();
                                Log.d("Текст", deck.getText().toString());
                                Pack found = packsRepositoryJdbc.findByName(deck.getText().toString(), AddCradsActivity.this);
                                found.setName(userInput.getText().toString());
                                packsRepositoryJdbc.update(found, AddCradsActivity.this);
                                setData();
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_adding_cards);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        displaySelectedScreen(id);
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


    private void delete(Card item) {
        CardsRepositoryJdbcImpl cardsRepositoryJdbc = new CardsRepositoryJdbcImpl();
        cardsRepositoryJdbc.delete(item.getId(), this);
    }

    private void edit(Card item) {
//        Log.d("Айди ", item.getId().toString());
//        Intent intent = new Intent(this, AddCradsActivity.class);
//        intent.putExtra("deckId", item.getId());
//        startActivity(intent);
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public String getItem(int position) {
            return mAppList.get(position).getQuestion();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new AddCradsActivity.AppAdapter.ViewHolder(convertView);
            }
            AddCradsActivity.AppAdapter.ViewHolder holder = (AddCradsActivity.AppAdapter.ViewHolder) convertView.getTag();
            String item = getItem(position);
//            holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
            holder.tv_name.setText(item);
//            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(DecksActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
//                }
//            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AddCradsActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

//        @Override
//        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
//            return true;
//        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
