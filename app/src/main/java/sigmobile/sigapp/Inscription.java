package sigmobile.sigapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

import sigmobile.sigapp.signet.Client;
import sigmobile.sigapp.signet.Server;
import sigmobile.sigapp.sigproc.Balise;

public class Inscription extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private boolean withEmail = false; // Si l'utilisateur veut être contacté par mail

    //static public String host = "93.113.206.146";
    // Modifier egalement les text par defaut dans les EditText de la presentation Inscription
    static public String host = "192.168.0.37";
    static public int port = 5000;
    static public Server server = new Server(host, port);
    static public Client cl;

    static public String sent = null, token = null, received = null;
    static public ArrayList<String> Balises = new ArrayList<String>();
    static public String sepc = "+";
    static public String seps = ";";
    static public String sepb = ",";
    static public String sepd = ":";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // permettre envoi/recep par socket perso
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_inscription);

        Switch emailVerif = (Switch) findViewById(R.id.emailVerif);
        Button ok = (Button) findViewById(R.id.ok);

        assert emailVerif != null;
        emailVerif.setOnCheckedChangeListener(this);
        assert ok != null;
        ok.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // Si check de l'email, on l'affiche, sinon, on le rend invisible
        EditText email = (EditText) findViewById(R.id.email);
        if (isChecked) {
            // Switch en mode on, l'utilisateur veut fournir son mail
            assert email != null;
            email.setVisibility(View.VISIBLE);
            withEmail = true;
        } else {
            assert email != null;
            email.setVisibility(View.GONE);
            withEmail = false;
        }
    }

    @Override
    public void onClick(View v) {

        /* Réagir au clic */
        switch (v.getId()) {
            case R.id.ok:
                EditText prenom, nom, email, societe, balise, _pass;
                String s_prenom, s_nom, s_email = null, s_societe, s_balise, s_pass, h_pass;
                TextView t = (TextView) findViewById(R.id.errors);

                String r;

                prenom = (EditText) findViewById(R.id.prenom);
                nom = (EditText) findViewById(R.id.nom);
                email = (EditText) findViewById(R.id.email);
                societe = (EditText) findViewById(R.id.societe);
                _pass = (EditText) findViewById(R.id.pass);

                // Recuperation des entrées de l'utilisateur
                assert t != null;
                t.clearComposingText(); // On supprime toutes les anciens alertes
                assert prenom != null;
                s_prenom = prenom.getText().toString();
                if (!verifyString(s_prenom)) {
                    t.append("Prenom manquant\n");
                    break;
                }

                assert nom != null;
                s_nom = nom.getText().toString();
                if (!verifyString(s_nom)) {
                    t.append("Nom manquant\n");
                    break;
                }

                assert societe != null;
                s_societe = societe.getText().toString();
                if (!verifyString(s_societe)) {
                    t.append("Societe manquante\n");
                    break;
                }

                assert _pass != null;
                s_pass = _pass.getText().toString();
                if (!verifyString(s_pass)) {
                    t.append("Mot de passe manquant\n");
                    break;
                } else {
                    h_pass = md5Sum(s_pass);
                }

                // Vérification pour le mail
                if(withEmail) {
                    // Si l'utilisateur desire fournir son mail
                    assert email != null;
                    s_email = email.getText().toString();
                    if(!verifyString(s_email)){
                        t.append("Email manquant\n");
                        break;
                    }
                }
                int type=1;
                Inscription.sent = Inscription.server.createMsg(s_nom, s_societe, h_pass, false, ""); // Create du paquet
                Inscription.cl = Inscription.socketOpen(); // Nouvelle socket udp
                Inscription.received = Inscription.sendAndReceiveMessage(type); // Envoi/reception data de type inscription

                // data receive
                if (!Inscription.received.equals("None") && Inscription.received.split (seps).length > 1) {
                    String[] tab = Inscription.received.split(seps);
                    token = tab[0];
                    s_balise = tab[1]; // Tableau de balise : B1,B2,B3 ... etc.
                    String[] bs = s_balise.split(sepb);
                    for(int i=0;i<bs.length;i++){
                        // Maj des balises
                        Balises.add(i, bs[i]);
                    }
                }

                // On a plus besoin de cette activité, on démarre le tableau de bord
                Intent i = new Intent(this, Balise.class);
                startActivity(i);
                finish();

        }

    }

    public String md5Sum(String mdp){
        // Renvoit le hash md5 du mot de passe
        String output;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5"); // Instance de hash
            digest.reset();
            digest.update(mdp.getBytes("UTF-8")); // Conversion au format binaire de notre mdp
            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            output = bigInt.toString(16);
            while ( output.length() < 32 ) {
                output = "0"+output;
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return output;
    }

    public boolean verifyString(String s){
        return !s.isEmpty();
    }

    public static Client socketOpen(){
        Client c = null;
        // Socket open
        try {
            c = new Client(host, port);
        } catch (IOException e) {
            Log.i("client", "prob udp socket");
            e.printStackTrace();
        }
        return c;
    }

    public static boolean socketClose() {
        boolean f=false;
        // socket close
        try {
            Inscription.cl.closeSocket();
            f=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static String sendAndReceiveMessage(int type){
        // envoi de nos données, et reception du token + balises
        Log.e("send/recv type", String.valueOf(type));
        String r;
        try {
            r = Server.communicate(cl, sent, type);
        } catch (IOException e) {
            r = "None";
            Log.i("Communicate server", "prob udp envoi/reception");
            e.printStackTrace();
        }
        return r;
    }

}