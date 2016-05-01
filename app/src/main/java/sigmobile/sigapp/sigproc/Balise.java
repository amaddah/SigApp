package sigmobile.sigapp.sigproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sigmobile.sigapp.Fin;
import sigmobile.sigapp.Inscription;
import sigmobile.sigapp.R;

public class Balise extends Activity {

    private Spinner spinner2;
    private Button btnSubmit;

    static public ArrayList<String> volts = new ArrayList<String>(); // Admin/superadmin
    static public ArrayList<String> rssis = new ArrayList<String>(); // user
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

        btnSubmit.setOnClickListener(new OnClickListener() {

            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                int type=2;
                String bal = String.valueOf(spinner2.getSelectedItem());
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
                        try{
                            err = bs[0].split(Inscription.sepd)[0];
                        }
                        catch(Exception e){
                            err = "None";
                        }

                        if(err.equals("None")) {
                            Toast.makeText(getApplicationContext(), "Pas de données disponibles sur cette balise.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (int i = 0; i < bs.length; i++) {
                                // Maj des datas
                                if (droit.equals("Superadmin") || droit.equals("admin")) {
                                    String[] str = bs[i].split(Inscription.sepd);
                                    volts.add(i, str[0]);
                                    rssis.add(i, str[1]);
                                } else if (droit.equals("User")) {
                                    rssis.add(i, bs[i]);
                                } else {
                                    Toast.makeText(Balise.this, "Un probleme est survenu, réessaye plus tard", Toast.LENGTH_SHORT);
                                    Intent fin = new Intent(Balise.this, Fin.class);
                                    startActivity(fin);
                                }
                            }
                            Intent board = new Intent(Balise.this, Board.class);
                            startActivity(board);
                        }
                    }
                }

        });
    }
}