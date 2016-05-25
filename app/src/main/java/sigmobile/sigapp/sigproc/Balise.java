package sigmobile.sigapp.sigproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sigmobile.sigapp.Inscription;
import sigmobile.sigapp.R;

public class Balise extends Activity implements OnClickListener {
    static public String balise;
    private Spinner spinner2;
    private Button btnSubmit;
    private FloatingActionButton quit;

    static public ArrayList<Float> volts = new ArrayList<>(); // Admin/superadmin
    static public ArrayList<Float> rssis = new ArrayList<>(); // user
    static public ArrayList<String> times = new ArrayList<>();
    static public ArrayList<String> ids = new ArrayList<>(); // id data
    static public String droit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balise);
        if ( Inscription.Balises.size() > 0) {
            addItemsOnSpinner2();
            addListenerOnButton();
        }
        else {
            finish();
        }
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.clear();
        for(int i=0;i< Inscription.Balises.size();i++)
            // Ajoute au menu des balises nous concernant
            list.add(Inscription.Balises.get(i));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        quit = (FloatingActionButton) findViewById(R.id.quit);

        btnSubmit.setOnClickListener(this);
        quit.setOnClickListener(this);
    }
    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                int type = 2;
                String bal = String.valueOf(spinner2.getSelectedItem());
                balise = bal;
                Inscription.sent = Inscription.server.createMsg(Inscription.token, bal); // Create du paquet
                Inscription.received = Inscription.sendAndReceiveMessage(type); // Envoi/reception data de type getData
                // data receive
                if (!Inscription.received.equals("None") && Inscription.received.split(Inscription.seps).length > 1) {
                    String[] tab = Inscription.received.split(Inscription.seps);
                    droit = tab[0];
                    String datas = tab[1]; // Tableau de balise : B1,B2,B3 ... etc.
                    String[] bs = datas.split(Inscription.sepb);

                    // Gestion erreurs balise vide pr nous
                    String err;
                    Log.i("test", bs[0].split(Inscription.sepd)[0]);
                    try {
                        err = bs[0].split(Inscription.sepd)[0];
                    } catch (Exception e) {
                        err = "None";
                    }

                    if (err.equals("None")) {
                        Toast.makeText(getApplicationContext(), "Pas de données disponibles sur cette balise.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < bs.length; i++) {
                            // Maj des datas
                            if (droit.equals("Superadmin") || droit.equals("Admin")) {
                                String[] str = bs[i].split(Inscription.sepd);
                                String[] d = str[2].split(" ");
                                String day = d[0];
                                String hour = d[1];
                                volts.add(i, Float.valueOf(str[0]));
                                rssis.add(i, Float.valueOf(str[1]));
                                times.add(i, hour);
                                ids.add(i, str[3]);
                            } else if (droit.equals("User")) {
                                String[] str = bs[i].split(Inscription.sepd);
                                String[] d = str[1].split(" ");
                                String day = d[0];
                                String hour = d[1];
                                rssis.add(i, Float.valueOf(str[0]));
                                times.add(i, hour);
                            } else {
                                Toast.makeText(Balise.this, "Un probleme est survenu, réessayez plus tard", Toast.LENGTH_LONG);
                                finish();
                            }
                        }
                        Intent board = new Intent(Balise.this, Board.class);
                        startActivity(board);
                        finish();
                    }
                }
                break;
            case R.id.quit:
                Intent intent = new Intent(getApplicationContext(), Inscription.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                break;
        } // Fin switch
    }
}