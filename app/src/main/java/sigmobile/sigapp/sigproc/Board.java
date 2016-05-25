package sigmobile.sigapp.sigproc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import sigmobile.sigapp.Inscription;
import sigmobile.sigapp.R;


public class Board extends AppCompatActivity implements View.OnClickListener {
    final private int MAX_POINTS_DISPLAYED = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton quit = (FloatingActionButton) findViewById(R.id.quit);
        assert quit != null;
        quit.setOnClickListener(this);

        FloatingActionButton info = (FloatingActionButton) findViewById(R.id.info);
        assert info != null;
        info.setOnClickListener(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout l = (LinearLayout) findViewById(R.id.admin);
        if(Balise.droit.equals("Admin") || Balise.droit.equals("Superadmin") ) {
            // Si admin/superadmin, on affiche les 2 graphique
            assert l != null;
            l.setVisibility(View.VISIBLE);
        }

        if(Balise.droit.equals("Superadmin")){
            // Si superadmin, on affiche le bouton pr les messages
            FloatingActionButton msg = (FloatingActionButton) findViewById(R.id.msg);
            assert msg != null;
            msg.setVisibility(View.VISIBLE);
            msg.setOnClickListener(this);
        }

        TextView droit = (TextView) findViewById(R.id.textBoard);
        assert droit != null;
        droit.setText(String.format("Bienvenue sur l'interface de gestion de la %s. Vous en êtes %s.", Balise.balise, Balise.droit));

        createNotification("Voltage trop important", "Le voltage de la balise a dépassé les 4V, veuillez s'il vous plait vérifier l'état de la balise.", "Alerte");

        String[] titles = {"Graphique du voltage de la balise.", "Graphique du signal de la balise."};

        if(Balise.droit.equals("Admin") || Balise.droit.equals("Superadmin") ) {
            // Superadmin/admin surveillence de l'état du voltage de la balise
            setLineChart(R.id.admichart, titles[0], Balise.volts);
        }

        // 1 seul graphique (celui ci-bas) est affiché si technicien (RSSI/temps)
        setLineChart(R.id.techart, titles[1], Balise.rssis);
    }

    public void createNotification(String t, String m, String ticker) {
        Intent notificationIntent = new Intent(this, NotificationView.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                1, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo))
                //.setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setTicker(ticker)
                .setContentTitle(t)
                .setContentText(m);

        // Tablettes sans vibration
                try {
                    builder.setDefaults(Notification.DEFAULT_ALL);
                    Notification n = builder.build();
                    nm.notify(1,n);
                }
                catch (Exception e){
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    Notification n = builder.build();
                    nm.notify(1,n);
                }

    }

    private LineDataSet addEntries(ArrayList<Float> tab){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.clear();
        for(int i=0;i<MAX_POINTS_DISPLAYED;i++) {
            entries.add(new Entry(tab.get(i), i));
        }

        return new LineDataSet(entries, "");
    }

    private ArrayList<String> addLabels(ArrayList<String> tab){
        ArrayList<String> labels = new ArrayList<>();
        labels.clear();
        for(int i=0;i<MAX_POINTS_DISPLAYED;i++) {
            labels.add(tab.get(i));
        }
        return labels;
    }

    private void setLineChart(int id, String title, ArrayList<Float> tab){
        ArrayList<String> lab = new ArrayList<>();
        lab.clear();
        lab = addLabels(Balise.times);

        LineDataSet dataset = new LineDataSet(null, null);
        dataset.clear();
        dataset = addEntries(tab);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData data = new LineData(lab, dataset);

        LineChart chart = (LineChart) findViewById(id);
        assert chart != null;
        chart.setData(data);
        chart.setDescription(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg:
                Intent messages = new Intent(Board.this, Messages.class);
                startActivity(messages);
                finish();
                break;
            case R.id.info:
                Intent info = new Intent(Board.this, Info.class);
                startActivity(info);
                break;
            case R.id.quit:
                Intent intent = new Intent(getApplicationContext(), Inscription.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                break;
        }
    }
}
