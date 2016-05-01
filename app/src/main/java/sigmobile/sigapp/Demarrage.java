package sigmobile.sigapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sigmobile.sigapp.signet.Client;
import sigmobile.sigapp.signet.Server;
import sigmobile.sigapp.sigproc.*;

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
        handle.postDelayed(delay,1000);
    }
}
