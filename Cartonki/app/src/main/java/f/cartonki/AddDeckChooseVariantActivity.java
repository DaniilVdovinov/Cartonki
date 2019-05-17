package f.cartonki;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;
import f.models.Card;
import f.models.Pack;
import f.repositories.CardsRepository;
import f.repositories.CardsRepositoryJdbcImpl;
import f.repositories.ExcelMapper;
import f.repositories.PacksRepositoryJdbcImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AddDeckChooseVariantActivity extends AppCompatActivity implements View.OnClickListener {


    Button addButton, fileButton;
    private static final int REQUEST_CODE = 43;
    Uri uri = Uri.parse("ff");

    File file;
    private static final String FILENAME_SD = "words.xlsx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_choose_variant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_adding_choose_variant_toolbar);
        setSupportActionBar(toolbar);

        addButton = findViewById(R.id.choose_add_button);
        addButton.setOnClickListener(this);

        fileButton = findViewById(R.id.choose_file_button);
        fileButton.setOnClickListener(this);
    }

    private void displaySelectedScreen(int itemID) {
        try {
            if (itemID == R.id.choose_add_button) {
                createDialog();
            } else if (itemID == R.id.choose_file_button) {

                startSearch();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_adding_choose_variant);
//        drawer.closeDrawer(GravityCompat.START);
    }

    private void startSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Урл", RESULT_OK + "");
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                synchronized (uri) {
                    uri = data.getData();
                }
                Log.d("Урл", uri + "");
                Log.d("Урл", uri.getPath() + "");
                Toast.makeText(this, "Uri: " + uri, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Path: " + uri.getPath(), Toast.LENGTH_LONG).show();
            }
        }
        readFileSD();
    }


    private void createFileDialog() {
        Log.d("ыаро", "запускается");
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
                                try {
                                    ExcelMapper excelMapper = new ExcelMapper(file, packsRepositoryJdbc.save(new Pack(userInput.getText().toString()), AddDeckChooseVariantActivity.this));
                                    List<Card> cards = excelMapper.getCardsAsList();
                                    CardsRepository cardsRepository = new CardsRepositoryJdbcImpl();
                                    for (Card c : cards
                                    ) {
                                        cardsRepository.save(c, AddDeckChooseVariantActivity.this);
                                    }
                                    Log.d("Список", cards.size() + "");
                                } catch (IOException e) {
                                    throw new Error(e);
                                }
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();
    }


    public void readFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("SD", "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }

        String path = uri.getPath();
        path = "sdcard" + path.substring(path.indexOf('0')+1);



        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(path);

        file = sdFile;
//        file = Main.Companion.convert(uri);
        Log.d("SD", "SD-карта: " + path);
        createFileDialog();
    }


    private String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void createDialog() {
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
