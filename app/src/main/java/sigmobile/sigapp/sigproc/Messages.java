package sigmobile.sigapp.sigproc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sigmobile.sigapp.Inscription;
import sigmobile.sigapp.R;

import static java.lang.Thread.sleep;

public class Messages extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton balise = (FloatingActionButton) findViewById(R.id.balise);
        assert balise != null;
        FloatingActionButton quit = (FloatingActionButton) findViewById(R.id.quit);
        assert quit != null;
        balise.setOnClickListener(this);
        quit.setOnClickListener(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Les row
        if(Balise.volts.size() != 10 || Balise.ids.size() != 10) {
            Toast.makeText(getApplicationContext(), "Probleme de reception de données, réessayez plus tard.", Toast.LENGTH_LONG).show();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
        else {
            addNewListener(Balise.ids.get(0), String.valueOf(Math.round(Balise.volts.get(0))), R.id.d_id1, R.id.d_trame1, R.id.d_rl1_mod, R.id.d_rl1_suppr);
            addNewListener(Balise.ids.get(1), String.valueOf(Math.round(Balise.volts.get(1))), R.id.d_id2, R.id.d_trame2, R.id.d_rl2_mod, R.id.d_rl2_suppr);
            addNewListener(Balise.ids.get(2), String.valueOf(Math.round(Balise.volts.get(2))), R.id.d_id3, R.id.d_trame3, R.id.d_rl3_mod, R.id.d_rl3_suppr);
            addNewListener(Balise.ids.get(3), String.valueOf(Math.round(Balise.volts.get(3))), R.id.d_id4, R.id.d_trame4, R.id.d_rl4_mod, R.id.d_rl4_suppr);
            addNewListener(Balise.ids.get(4), String.valueOf(Math.round(Balise.volts.get(4))), R.id.d_id5, R.id.d_trame5, R.id.d_rl5_mod, R.id.d_rl5_suppr);
            addNewListener(Balise.ids.get(5), String.valueOf(Math.round(Balise.volts.get(5))), R.id.d_id6, R.id.d_trame6, R.id.d_rl6_mod, R.id.d_rl6_suppr);
            addNewListener(Balise.ids.get(6), String.valueOf(Math.round(Balise.volts.get(6))), R.id.d_id7, R.id.d_trame7, R.id.d_rl7_mod, R.id.d_rl7_suppr);
            addNewListener(Balise.ids.get(7), String.valueOf(Math.round(Balise.volts.get(7))), R.id.d_id8, R.id.d_trame8, R.id.d_rl8_mod, R.id.d_rl8_suppr);
            addNewListener(Balise.ids.get(8), String.valueOf(Math.round(Balise.volts.get(8))), R.id.d_id9, R.id.d_trame9, R.id.d_rl9_mod, R.id.d_rl9_suppr);
            addNewListener(Balise.ids.get(9), String.valueOf(Math.round(Balise.volts.get(9))), R.id.d_id10, R.id.d_trame10, R.id.d_rl10_mod, R.id.d_rl10_suppr);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.balise:
                Intent balise = new Intent(Messages.this, Balise.class);
                startActivity(balise);
                finish();
                break;
            case R.id.quit:
                Intent intent = new Intent(getApplicationContext(), Inscription.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                break;
            case R.id.d_rl1_mod:
                // Modif de la donnée 1
                break;
            case R.id.d_rl1_suppr:
                // Suppr de la donnée 1
                break;
            case R.id.d_rl2_mod:
                // Modif de la donnée 2
                break;
            case R.id.d_rl2_suppr:
                // Suppr de la donnée 2
                break;
            case R.id.d_rl3_mod:
                // Modif de la donnée 3
                break;
            case R.id.d_rl3_suppr:
                // Suppr de la donnée 3
                break;
            case R.id.d_rl4_mod:
                // Modif de la donnée 4
                break;
            case R.id.d_rl4_suppr:
                // Suppr de la donnée 4
                break;
            case R.id.d_rl5_mod:
                // Modif de la donnée 5
                break;
            case R.id.d_rl5_suppr:
                // Suppr de la donnée 5
                break;
            case R.id.d_rl6_mod:
                // Modif de la donnée 6
                break;
            case R.id.d_rl6_suppr:
                // Suppr de la donnée 6
                break;
            case R.id.d_rl7_mod:
                // Modif de la donnée 7
                break;
            case R.id.d_rl7_suppr:
                // Suppr de la donnée 7
                break;
            case R.id.d_rl8_mod:
                // Modif de la donnée 8
                break;
            case R.id.d_rl8_suppr:
                // Suppr de la donnée 8
                break;
            case R.id.d_rl9_mod:
                // Modif de la donnée 9
                break;
            case R.id.d_rl9_suppr:
                // Suppr de la donnée 9
                break;
            case R.id.d_rl10_mod:
                // Modif de la donnée 10
                break;
            case R.id.d_rl10_suppr:
                // Suppr de la donnée 10
                break;

        }
    }

    public void addNewListener(String sid, String did, int idid, int trameid, int modid, int supprid){
        TextView id = (TextView) findViewById(idid);
        TextView trame = (TextView) findViewById(trameid);
        Button mod = (Button) findViewById(modid);
        Button suppr = (Button) findViewById(supprid);

        assert mod != null;
        assert suppr != null;
        assert trame != null;
        assert id != null;

        id.setText(sid);
        trame.setText(did);
        mod.setOnClickListener(this);
        suppr.setOnClickListener(this);
    }

}
