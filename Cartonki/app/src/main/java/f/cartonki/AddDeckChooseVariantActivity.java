package f.cartonki;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import f.models.Pack;
import f.repositories.PacksRepositoryJdbcImpl;

import java.time.LocalDate;


public class AddDeckChooseVariantActivity extends AppCompatActivity implements View.OnClickListener {


    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_choose_variant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_adding_choose_variant_toolbar);
        setSupportActionBar(toolbar);

        addButton = findViewById(R.id.choose_add_button);
        addButton.setOnClickListener(this);
    }

    private void displaySelectedScreen(int itemID) {
        try {
            if (itemID == R.id.choose_add_button) {
                createDialog();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_adding_choose_variant);
//        drawer.closeDrawer(GravityCompat.START);
    }

    private void createDialog() {
        Log.d("Туть", "fuf");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(layout);

        final EditText userInput = (EditText) layout.findViewById(R.id.input_text);

        mDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Добавить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PacksRepositoryJdbcImpl packsRepositoryJdbc = new PacksRepositoryJdbcImpl();
                                Intent intent = new Intent(AddDeckChooseVariantActivity.this, AddCradsActivity.class);
                                intent.putExtra("deckId", packsRepositoryJdbc.save(new Pack(userInput.getText().toString()), AddDeckChooseVariantActivity.this));
                                startActivity(intent);
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        displaySelectedScreen(id);
    }
}
