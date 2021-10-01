package com.avintangu.taxiapp.searchres;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avintangu.taxiapp.R;
import com.avintangu.taxiapp.resources.Resources;

import java.util.ArrayList;

public class Searchres extends AppCompatActivity implements View.OnClickListener {
    double sreunica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchres);

        Resources resources = new Resources(this);

        /* Declarations */
        sreunica = resources.getFontex(getWindowManager().getDefaultDisplay(), new Point());
        ((TextView) findViewById(R.id.srchestitle_text)).setTextSize((float) sreunica + 6f);

        /* Get parameter */
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> srchprms = bundle.getStringArrayList("srchparams");

        /* Adjust dimensions */
        resources.adjsDimens(findViewById(R.id.srchesactopconsa));

        /* Set action listeners */
        findViewById(R.id.srchesBck).setOnClickListener(this);

        if (srchprms.size() > 0) {
            ((TextView) findViewById(R.id.srcheskey)).setText(srchprms.get(0));
            ((TextView) findViewById(R.id.srcheswitch)).setText(srchprms.get(1));
            ((TextView) findViewById(R.id.srchesdist)).setText(srchprms.get(2));
            ((TextView) findViewById(R.id.srchestime)).setText(srchprms.get(3));
        }

        /* Load Serches summary */
        srchLodsumary();

    }

    public void srchLodsumary() {
        Srchresui srchresui= new Srchresui(this);
        ArrayList<String> srchprmsa = new ArrayList<>();

        srchprmsa.add( ((TextView) findViewById(R.id.srcheskey)).getText().toString());
        srchprmsa.add( ((TextView) findViewById(R.id.srcheswitch)).getText().toString());
        srchprmsa.add( ((TextView) findViewById(R.id.srchesdist)).getText().toString());
        srchprmsa.add( ((TextView) findViewById(R.id.srchestime)).getText().toString());

        ProgressBar progbar = findViewById(R.id.srchesprogress);
        ScrollView scroLayout = findViewById(R.id.srchesscrollview);
        LinearLayout prdcthedcont= findViewById(R.id.srcheshedcont);
        TextView trelics= findViewById(R.id.srchestitle_text);

        srchresui.srchesbackrecdlod( scroLayout, sreunica,  progbar, prdcthedcont, srchprmsa,
                trelics);

    }

    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.srchesBck: {
                finish();
                break;
            }

            default:
                break;
        }
    }
}