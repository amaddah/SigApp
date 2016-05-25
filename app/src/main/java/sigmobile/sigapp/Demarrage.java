package sigmobile.sigapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Demarrage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demarrage);

        final Handler handle = new Handler();
        // Attente de 5sec avant de basculer sur le choix de la balise
        Runnable delay = new Runnable() {
            public void run() {

                Intent intent = new Intent(Demarrage.this, Inscription.class);
                startActivity(intent);
                finish();
            }
        };
        // A modifier : 1 -> 5 sec
        handle.postDelayed(delay,5000);
    }
}
