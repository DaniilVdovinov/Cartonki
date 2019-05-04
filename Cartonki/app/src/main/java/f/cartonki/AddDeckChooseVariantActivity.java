package f.cartonki;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class AddDeckChooseVariantActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_choose_variant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_adding_choose_variant_toolbar);
        setSupportActionBar(toolbar);

    }

}
