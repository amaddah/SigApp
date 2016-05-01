package sigmobile.sigapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class Fin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);
        Inscription.socketClose(); // Fermeture de la socket UDP (port: 5000)
        Toast.makeText(Fin.this, "A bientot", Toast.LENGTH_SHORT).show();
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
