package com.avintangu.taxiapp.searchres;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avintangu.taxiapp.R;
import com.avintangu.taxiapp.resources.Resources;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class Srchresui {
    private static Context srchuiContext;
    Resources resources;

    public Srchresui(Context context) {
        srchuiContext = context;

        resources = new Resources(srchuiContext);
    }

    /* Get summary */
    public void srchesbackrecdlod(ScrollView scroLayout, double eunica, ProgressBar progbar,
                                  LinearLayout prdcthedcont, ArrayList<String> srchprmsa, TextView trelis) {
        String param = "nada";
        progbar.setIndeterminate(true);
        scroLayout.scrollTo(0, 0);

        new srchRecodsync(scroLayout, eunica, progbar, prdcthedcont, srchprmsa, trelis).execute(param);
    }

    /* Serches backrecd UI */
    public class srchRecodsync extends AsyncTask<String, Integer, String> {
        ScrollView scroLayoutpr;
        double eunicapr;
        ProgressBar progbarpr;
        LinearLayout usrLinear;
        LinearLayout prdcthedcontpr;
        ArrayList<String> prsrchprmsa;
        TextView prtrelis;
        Integer sumrecods = 0;

        public srchRecodsync(ScrollView scroLayout, double eunica, ProgressBar progbar,
                             LinearLayout prdcthedcont, ArrayList<String> srchprmsa, TextView trelis) {
            this.scroLayoutpr = scroLayout;
            this.eunicapr = eunica;
            this.progbarpr = progbar;
            this.prdcthedcontpr = prdcthedcont;
            this.prsrchprmsa = srchprmsa;
            this.prtrelis = trelis;

        }

        protected String doInBackground(String... param) {
            String response = "nada";

            /*Get data*/
            ArrayList<ArrayList<String>> usrVals = resources.searchJson(prsrchprmsa.get(0), prsrchprmsa.get(1),
                    prsrchprmsa.get(2), prsrchprmsa.get(3));
            sumrecods = usrVals.size();

            /* Table container */
            usrLinear = new LinearLayout(srchuiContext);
            usrLinear.setPadding(0, 0, 0, 160);
            usrLinear.removeAllViews();
            usrLinear.setOrientation(LinearLayout.VERTICAL);

            for (int casc = 0; casc < usrVals.size(); casc++) {
                LinearLayout gencells = retCells(usrVals.get(casc), eunicapr);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 0);
                usrLinear.addView(gencells, layoutParams);
            }

            return response;
        }

        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            /* Add to parent */
            scroLayoutpr.removeAllViews();
            scroLayoutpr.addView(usrLinear, new ScrollView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            prtrelis.setText("Total trips (" + sumrecods + ")");

            progbarpr.setIndeterminate(false);


        }

        protected void onProgressUpdate(Integer... array) {
        }
    }

    public LinearLayout retCells(ArrayList<String> valsec, double eunica) {
        /* Table container */
        LinearLayout cellsLinear = new LinearLayout(srchuiContext);
        cellsLinear.setPadding(0, 0, 0, 0);
        cellsLinear.removeAllViews();
        cellsLinear.setOrientation(LinearLayout.VERTICAL);

        FlexboxLayout usrFlexbox = new FlexboxLayout(srchuiContext);
        usrFlexbox.setFlexDirection(FlexDirection.ROW);
        usrFlexbox.setAlignContent(AlignContent.FLEX_START);
        usrFlexbox.setAlignItems(AlignItems.BASELINE);
        usrFlexbox.setFlexWrap(FlexWrap.WRAP);
        usrFlexbox.setPadding(0, 0, 0, 0);
        usrFlexbox.setBackgroundResource(R.drawable.listflexcont);

        /*Start time*/
        TextView datevew = new TextView(srchuiContext);
        String datevewstr = valsec.get(10).replace("\"", "");
        datevew.setText(datevewstr);
        datevew.setAllCaps(false);
        datevew.setGravity(Gravity.LEFT);
        datevew.setTextSize((float) eunica + 3.0f);
        datevew.setBackgroundResource(R.drawable.txtvewlistcomb);
        datevew.setPadding(16, 36, 10, 10);
        datevew.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParams.setFlexBasisPercent(0.70f);
        lfayoutParams.setFlexGrow(1.0f);
        usrFlexbox.addView(datevew, lfayoutParams);

        /*Amount*/
        TextView amount = new TextView(srchuiContext);
        String amountstr = valsec.get(25).replace("\"", "") +
                valsec.get(26).replace("\"", "");
        amount.setText(amountstr);
        amount.setAllCaps(false);
        amount.setGravity(Gravity.RIGHT);
        amount.setTextSize((float) eunica + 3.0f);
        amount.setBackgroundResource(R.drawable.txtvewlistcomb);
        amount.setPadding(16, 36, 10, 10);
        amount.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParamsa = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsa.setFlexBasisPercent(0.30f);
        lfayoutParamsa.setFlexGrow(1.0f);
        usrFlexbox.addView(amount, lfayoutParamsa);

        /*Pick up location*/
        Button picklocat = new Button(srchuiContext);
        String picklocatstr = valsec.get(5).replace("\"", "");
        picklocat.setText(picklocatstr);
        picklocat.setAllCaps(false);
        picklocat.setGravity(Gravity.CENTER_VERTICAL);
        picklocat.setTextSize((float) eunica + 3.0f);
        picklocat.setBackgroundResource(R.drawable.txtvewlistcomb);
        picklocat.setPadding(16, 36, 10, 10);
        picklocat.setTextColor(Color.rgb(77, 77, 77));
        picklocat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gcircle, 0, 0, 0);

        FlexboxLayout.LayoutParams lfayoutParamsb = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsb.setFlexBasisPercent(0.67f);
        lfayoutParamsb.setFlexGrow(1.0f);
        lfayoutParamsb.setMargins(10, 0, 0, 0);
        usrFlexbox.addView(picklocat, lfayoutParamsb);

        /*Rating*/
        RatingBar ratCheck = new RatingBar(srchuiContext, null, android.R.attr.ratingBarStyleSmall);
        String ratCheckstr = valsec.get(14).replace("\"", "");
        ratCheck.setRating(resources.strTointeger(ratCheckstr));
        ratCheck.setIsIndicator(true);
        ratCheck.setStepSize(0.1f);
        ratCheck.setNumStars(5);
        picklocat.setPadding(0, 0, 50, 0);


        FlexboxLayout.LayoutParams lfayoutParamsc = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsc.setFlexBasisPercent(0.23f);
        lfayoutParamsc.setMargins(0, 0, 10, 0);
        usrFlexbox.addView(ratCheck, lfayoutParamsc);

        /*Drop off location*/
        Button droplocat = new Button(srchuiContext);
        String droplocatstr = valsec.get(8).replace("\"", "");
        droplocat.setText(droplocatstr);
        droplocat.setAllCaps(false);
        droplocat.setGravity(Gravity.CENTER_VERTICAL);
        droplocat.setTextSize((float) eunica + 3.0f);
        droplocat.setBackgroundResource(R.drawable.txtvewlistcomb);
        droplocat.setPadding(16, 36, 10, 10);
        droplocat.setTextColor(Color.rgb(77, 77, 77));
        droplocat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rcircle, 0, 0, 0);

        FlexboxLayout.LayoutParams lfayoutParamsd = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsd.setFlexBasisPercent(0.60f);
        lfayoutParamsd.setFlexGrow(1.0f);
        lfayoutParamsd.setMargins(0, 0, 0, 0);
        usrFlexbox.addView(droplocat, lfayoutParamsd);

        /*Direction arrow*/
        Button dirctocx = new Button(srchuiContext);
        dirctocx.setAllCaps(false);
        dirctocx.setGravity(Gravity.CENTER_VERTICAL);
        dirctocx.setTextSize((float) eunica + 3.0f);
        dirctocx.setBackgroundResource(R.drawable.txtvewlistcomb);
        dirctocx.setPadding(16, 36, 10, 10);
        dirctocx.setTextColor(Color.rgb(77, 77, 77));
        dirctocx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.directos, 0);

        FlexboxLayout.LayoutParams lfayoutParamse = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamse.setFlexBasisPercent(0.30f);
        lfayoutParamse.setFlexGrow(1.0f);
        lfayoutParamse.setMargins(0, 0, 10, 0);
        usrFlexbox.addView(dirctocx, lfayoutParamse);

        /*Direction arrow listener*/
        dirctocx.setOnClickListener(v -> {
            Intent intent = new Intent(srchuiContext.getApplicationContext(), Singlerecod.class);

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("singlerecod", valsec);
            intent.putExtras(bundle);

            srchuiContext.startActivity(intent);

        });

        /*Trip status*/
        Button trippad = new Button(srchuiContext);
        trippad.setText("   ");
        trippad.setBackgroundResource(R.drawable.txtvewlistcomb);

        FlexboxLayout.LayoutParams lfayoutPad = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutPad.setFlexBasisPercent(0.5f);
        lfayoutPad.setFlexGrow(1.0f);
        usrFlexbox.addView(trippad, lfayoutPad);

        Button tripstecs = new Button(srchuiContext);
        String tripstecstr = valsec.get(1).replace("\"", "");
        tripstecs.setText(tripstecstr);
        tripstecs.setAllCaps(false);
        tripstecs.setGravity(Gravity.RIGHT);
        tripstecs.setTextSize((float) eunica + 3.0f);
        tripstecs.setBackgroundResource(R.drawable.txtvewlistcomb);
        tripstecs.setPadding(16, 36, 10, 10);
        tripstecs.setTextColor(Color.rgb(77, 77, 77));

        if (tripstecstr.equals("COMPLETED")) {
            tripstecs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.completos, 0);
        } else {
            tripstecs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incompletos, 0);
        }

        FlexboxLayout.LayoutParams lfayoutParamsf = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsf.setFlexBasisPercent(0.4f);
        lfayoutParamsf.setFlexGrow(1.0f);
        lfayoutParamsf.setMargins(0, 0, 10, 0);
        usrFlexbox.addView(tripstecs, lfayoutParamsf);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        cellsLinear.addView(usrFlexbox, layoutParams);

        return cellsLinear;
    }
}
