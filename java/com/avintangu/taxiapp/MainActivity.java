package com.avintangu.taxiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.avintangu.taxiapp.resources.Resources;
import com.avintangu.taxiapp.searchres.Searchres;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    double eunica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = new Resources(this);
        eunica = resources.getFontex(getWindowManager().getDefaultDisplay(), new Point());
        ((TextView) findViewById(R.id.srchinput)).setTextSize((float) eunica + 12f);

        int[] btnidec = {R.id.serchbtn, R.id.exitbtn};

        Button[] Btnarry = new Button[btnidec.length];
        for (int j = 0; j < btnidec.length; ++j) {
            (Btnarry[j] = findViewById(btnidec[j])).setTextSize((float) eunica + 4.5f);

        }

        /*Set display font sizes*/
        int[] edtxtidec = {R.id.edtxtasn};

        EditText[] Edtxtarry = new EditText[edtxtidec.length];
        for (int k = 0; k < edtxtidec.length; ++k) {
            (Edtxtarry[k] = findViewById(edtxtidec[k])).setTextSize((float) eunica + 4.5f);
        }

        /* Set up constraints */
        float[] percy = {0.05f, 0.90f};
        schGuides(percy);

        /* Set on actions listener */
        findViewById(R.id.serchbtn).setOnClickListener(this);
        findViewById(R.id.exitbtn).setOnClickListener(this);

    }

    /*Buttons action listeners*/
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.serchbtn: {
                /*Search keyword*/
                String srchkey = "";
                String prsrchkey = ((EditText) findViewById(R.id.edtxtasn)).getText().toString();

                if (prsrchkey.equals("")) {
                    srchkey = "nada";
                } else {
                    srchkey = prsrchkey;
                }

                /*Switch status*/
                String switchstr = "";
                Switch stateswitch = findViewById(R.id.stateswitch);
                Boolean switchstate = stateswitch.isChecked();

                if (switchstate == true) {
                    switchstr = this.getResources().getText(R.string.taxiappStrq).toString();

                } else if (switchstate == false) {
                    switchstr = this.getResources().getText(R.string.taxiappStrr).toString();

                }

                /*Distance radio buttons*/
                String diststr = "";
                int[] distradidec = {R.id.distrad, R.id.distrada, R.id.distradb, R.id.distradc, R.id.distradd};
                RadioButton distrad[] = new RadioButton[distradidec.length];

                for (int u = 0; u < distradidec.length; u++)
                    if ((distrad[u] = findViewById(distradidec[u])).isChecked()) {
                        diststr = (distrad[u] = findViewById(distradidec[u])).getText().toString();
                    }

                /*Time radio buttons*/
                String timestr = "";
                int[] timeradidec = {R.id.timerad, R.id.timerada, R.id.timeradb, R.id.timeradc, R.id.timeradd};
                RadioButton timerad[] = new RadioButton[distradidec.length];

                for (int u = 0; u < distradidec.length; u++)
                    if ((timerad[u] = findViewById(timeradidec[u])).isChecked()) {
                        timestr = (timerad[u] = findViewById(timeradidec[u])).getText().toString();
                    }

                /*Start search activity*/
                Intent intent = new Intent(this.getApplicationContext(), Searchres.class);
                ArrayList<String> srchParams =new ArrayList<>();
                srchParams.add(srchkey);
                srchParams.add(switchstr);
                srchParams.add(diststr);
                srchParams.add(timestr);

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("srchparams", srchParams);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            }

            case R.id.exitbtn: {
                finish();

                break;
            }

            default:
                break;
        }
    }

    /* User assign set guidelines */
    public void schGuides(float[] percy) {
        int[] guideidec = {R.id.usasstopcons, R.id.assusbottomconst};

        Guideline[] Guideidec = new Guideline[guideidec.length];

        for (int recs = 0; recs < guideidec.length; recs++) {
            (Guideidec[recs] = findViewById(guideidec[recs])).setGuidelinePercent(percy[recs]);

        }
    }
}