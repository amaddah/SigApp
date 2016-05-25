package sigmobile.sigapp.sigproc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sigmobile.sigapp.R;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
