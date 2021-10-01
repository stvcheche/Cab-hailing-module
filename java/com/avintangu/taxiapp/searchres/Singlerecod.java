package com.avintangu.taxiapp.searchres;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.avintangu.taxiapp.R;
import com.avintangu.taxiapp.resources.Resources;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class Singlerecod extends FragmentActivity implements OnMapReadyCallback,  View.OnClickListener {

    private GoogleMap mMap;
    LatLng firstLocat;
    LatLng secondLocat;
    double singunica;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlerecod);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         resources = new Resources(this);

        /* Declarations */
        singunica = resources.getFontex(getWindowManager().getDefaultDisplay(), new Point());
        ((TextView) findViewById(R.id.singtitle_text)).setTextSize((float) singunica + 6f);

        /* Get parameter */
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> singrecod = bundle.getStringArrayList("singlerecod");
        LinearLayout singrecdlayout = retCells(singrecod, singunica);

        LinearLayout singrecdparent = findViewById(R.id.singrecdparent);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        singrecdparent.addView(singrecdlayout, layoutParams);

        firstLocat = new LatLng(resources.strTodouble(singrecod.get(3).replace("\"", "")),
                resources.strTodouble(singrecod.get(4).replace("\"", "")));
        secondLocat = new LatLng(resources.strTodouble(singrecod.get(6).replace("\"", "")),
                resources.strTodouble(singrecod.get(7).replace("\"", "")));

        /* Set action listeners */
        findViewById(R.id.singBck).setOnClickListener(this);
    }

    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.singBck: {
                finish();

                break;
            }

            default:
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.addMarker(new MarkerOptions().position(firstLocat).title("Marker in Barcelona"));
        mMap.addMarker(new MarkerOptions().position(secondLocat).title("Marker in Madrid"));

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDsMPOpkCGlRBt8GT38YZll9cyFcbp6Ox8")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocat, 11));
    }


    /*Get trip details*/
    public LinearLayout retCells(ArrayList<String> valsec, double eunica) {
        /* Table container */
        LinearLayout cellsLinear = new LinearLayout(this);
        cellsLinear.setPadding(0, 0, 0, 0);
        cellsLinear.removeAllViews();
        cellsLinear.setOrientation(LinearLayout.VERTICAL);

        /*First row*/
        FlexboxLayout usrFlexbox = new FlexboxLayout(this);
        usrFlexbox.setFlexDirection(FlexDirection.ROW);
        usrFlexbox.setAlignContent(AlignContent.FLEX_START);
        usrFlexbox.setAlignItems(AlignItems.BASELINE);
        usrFlexbox.setFlexWrap(FlexWrap.WRAP);
        usrFlexbox.setPadding(0, 0, 0, 0);
        usrFlexbox.setBackgroundResource(R.drawable.listflexcont);

        /*Request date*/
        TextView datevew = new TextView(this);
        String datevewstr = valsec.get(2).replace("\"", "");
        datevew.setText(datevewstr);
        datevew.setAllCaps(false);
        datevew.setGravity(Gravity.LEFT);
        datevew.setTextSize((float) eunica + 3.0f);
        datevew.setBackgroundResource(R.drawable.txtvewlistcomb);
        datevew.setPadding(16, 10, 10, 10);
        datevew.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParams.setFlexBasisPercent(0.70f);
        lfayoutParams.setFlexGrow(1.0f);
        usrFlexbox.addView(datevew, lfayoutParams);

        /*Amount*/
        TextView amount = new TextView(this);
        String amountstr = valsec.get(25).replace("\"", "") +
                valsec.get(26).replace("\"", "");
        amount.setText(amountstr);
        amount.setAllCaps(false);
        amount.setGravity(Gravity.RIGHT);
        amount.setTextSize((float) eunica + 3.0f);
        amount.setBackgroundResource(R.drawable.txtvewlistcomb);
        amount.setPadding(16, 10, 10, 10);
        amount.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParamsa = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsa.setFlexBasisPercent(0.30f);
        lfayoutParamsa.setFlexGrow(1.0f);
        usrFlexbox.addView(amount, lfayoutParamsa);

        /*Second row*/
        FlexboxLayout usrFlexboxa = new FlexboxLayout(this);
        usrFlexboxa.setFlexDirection(FlexDirection.ROW);
        usrFlexboxa.setAlignContent(AlignContent.FLEX_START);
        usrFlexboxa.setAlignItems(AlignItems.BASELINE);
        usrFlexboxa.setFlexWrap(FlexWrap.WRAP);
        usrFlexboxa.setPadding(0, 0, 0, 0);
        usrFlexboxa.setBackgroundResource(R.drawable.listflexcont);

        /*Pick up location*/
        Button picklocat = new Button(this);
        String picklocatstr = valsec.get(5).replace("\"", "");
        picklocat.setText(picklocatstr);
        picklocat.setAllCaps(false);
        picklocat.setGravity(Gravity.CENTER_VERTICAL);
        picklocat.setTextSize((float) eunica + 3.0f);
        picklocat.setBackgroundResource(R.drawable.txtvewlistcomb);
        picklocat.setPadding(16, 10, 10, 10);
        picklocat.setTextColor(Color.rgb(77, 77, 77));
        picklocat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gcircle, 0, 0, 0);

        FlexboxLayout.LayoutParams lfayoutParamsb = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsb.setFlexBasisPercent(0.67f);
        lfayoutParamsb.setFlexGrow(1.0f);
        lfayoutParamsb.setMargins(10, 0, 0, 0);
        usrFlexboxa.addView(picklocat, lfayoutParamsb);

        /*Start time*/
        TextView startime = new TextView(this);
        String startimestr = valsec.get(9).replace("\"", "").substring(11,19);
        startime.setText(startimestr);
        startime.setAllCaps(false);
        startime.setGravity(Gravity.RIGHT);
        startime.setTextSize((float) eunica + 3.0f);
        startime.setBackgroundResource(R.drawable.txtvewlistcomb);
        startime.setPadding(16, 10, 10, 10);
        startime.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParamsc = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsc.setFlexBasisPercent(0.30f);
        lfayoutParamsc.setFlexGrow(1.0f);
        usrFlexboxa.addView(startime, lfayoutParamsc);

        /*Drop off location*/
        Button droplocat = new Button(this);
        String droplocatstr = valsec.get(8).replace("\"", "");
        droplocat.setText(droplocatstr);
        droplocat.setAllCaps(false);
        droplocat.setGravity(Gravity.CENTER_VERTICAL);
        droplocat.setTextSize((float) eunica + 3.0f);
        droplocat.setBackgroundResource(R.drawable.txtvewlistcomb);
        droplocat.setPadding(16, 10, 10, 10);
        droplocat.setTextColor(Color.rgb(77, 77, 77));
        droplocat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rcircle, 0, 0, 0);

        FlexboxLayout.LayoutParams lfayoutParamsd = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsd.setFlexBasisPercent(0.60f);
        lfayoutParamsd.setFlexGrow(1.0f);
        lfayoutParamsd.setMargins(0, 0, 0, 0);
        usrFlexboxa.addView(droplocat, lfayoutParamsd);

        /*End time*/
        TextView endtime = new TextView(this);
        String endtimestr = valsec.get(10).replace("\"", "").substring(11,19);
        endtime.setText(endtimestr);
        endtime.setAllCaps(false);
        endtime.setGravity(Gravity.RIGHT);
        endtime.setTextSize((float) eunica + 3.0f);
        endtime.setBackgroundResource(R.drawable.txtvewlistcomb);
        endtime.setPadding(16, 10, 10, 10);
        endtime.setTextColor(Color.rgb(77, 77, 77));

        FlexboxLayout.LayoutParams lfayoutParamse = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamse.setFlexBasisPercent(0.30f);
        lfayoutParamse.setFlexGrow(1.0f);
        usrFlexboxa.addView(endtime, lfayoutParamse);

        /*Third row*/
        FlexboxLayout usrFlexboxb = new FlexboxLayout(this);
        usrFlexboxb.setFlexDirection(FlexDirection.ROW);
        usrFlexboxb.setAlignContent(AlignContent.SPACE_AROUND);
        usrFlexboxb.setAlignItems(AlignItems.BASELINE);
        usrFlexboxb.setFlexWrap(FlexWrap.WRAP);
        usrFlexboxb.setJustifyContent(JustifyContent.CENTER);
        usrFlexboxb.setPadding(0, 0, 0, 0);

        /*Vehicle type*/
        /*Inner rowA*/
        FlexboxLayout usrFlexboxbinn = new FlexboxLayout(this);
        usrFlexboxbinn.setFlexDirection(FlexDirection.ROW);
        usrFlexboxbinn.setAlignContent(AlignContent.FLEX_START);
        usrFlexboxbinn.setAlignItems(AlignItems.BASELINE);
        usrFlexboxbinn.setFlexWrap(FlexWrap.WRAP);
        usrFlexboxbinn.setPadding(0, 0, 0, 0);

        Button vtype = new Button(this);
        String vtypestr = valsec.get(16).replace("\"", "");
        vtype.setText(vtypestr);
        vtype.setAllCaps(false);
        vtype.setGravity(Gravity.CENTER);
        vtype.setTextSize((float) eunica + 3.0f);
        vtype.setBackgroundResource(R.drawable.txtvewlistcomb);
        vtype.setPadding(0, 10, 10, 10);
        vtype.setTextColor(Color.rgb(77, 77, 77));
        vtype.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.car, 0, 0);

        FlexboxLayout.LayoutParams lfayoutParamsf = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsf.setFlexBasisPercent(0.95f);
        usrFlexboxbinn.addView(vtype, lfayoutParamsf);

        FlexboxLayout.LayoutParams lfayoutParamsfa = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsfa.setFlexBasisPercent(0.2f);
        usrFlexboxb.addView(usrFlexboxbinn, lfayoutParamsfa);

        /*Inner row*/
        FlexboxLayout usrFlexboxbin = new FlexboxLayout(this);
        usrFlexboxbin.setFlexDirection(FlexDirection.ROW);
        usrFlexboxbin.setAlignContent(AlignContent.FLEX_START);
        usrFlexboxbin.setAlignItems(AlignItems.BASELINE);
        usrFlexboxbin.setFlexWrap(FlexWrap.WRAP);
        usrFlexboxbin.setPadding(0, 0, 0, 0);

        /*End time to sub total*/
        ArrayList<String> invalecs = new ArrayList<>();
        invalecs.add("Distance");
        invalecs.add(valsec.get(23).replace("\"", ""));
        invalecs.add("Duration");
        invalecs.add(valsec.get(21).replace("\"", ""));
        invalecs.add("Sub-total");
        invalecs.add(valsec.get(25).replace("\"", ""));

        for(int h=0; h<6; h++) {
            TextView distance = new TextView(this);
            distance.setText(invalecs.get(h));
            distance.setAllCaps(false);
            distance.setGravity(Gravity.RIGHT);
            distance.setTextSize((float) eunica + 3.0f);
            distance.setBackgroundResource(R.drawable.txtvewlistcomb);
            distance.setPadding(16, 10, 10, 10);
            distance.setTextColor(Color.rgb(77, 77, 77));

            FlexboxLayout.LayoutParams lfayoutParamsg = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            lfayoutParamsg.setFlexBasisPercent(0.45f);
            lfayoutParamsg.setFlexGrow(1.0f);
            usrFlexboxbin.addView(distance, lfayoutParamsg);
        }

        FlexboxLayout.LayoutParams lfayoutParamsh = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsh.setFlexBasisPercent(0.5f);
        lfayoutParamsh.setMargins(0, 0, 0, 0);
        usrFlexboxb.addView(usrFlexboxbin, lfayoutParamsh);

        /*Inner rowA*/
        FlexboxLayout usrFlexboxbina = new FlexboxLayout(this);
        usrFlexboxbina.setFlexDirection(FlexDirection.ROW);
        usrFlexboxbina.setAlignContent(AlignContent.FLEX_START);
        usrFlexboxbina.setAlignItems(AlignItems.BASELINE);
        usrFlexboxbina.setFlexWrap(FlexWrap.WRAP);
        usrFlexboxbina.setPadding(0, 0, 0, 0);

        /*Driver pic*/
        Button driverPic = new Button(this);
        String driverPicstr = valsec.get(13).replace("\"", "");
        driverPic.setText(driverPicstr);
        driverPic.setAllCaps(false);
        driverPic.setGravity(Gravity.CENTER);
        driverPic.setTextSize((float) eunica + 3.0f);
        driverPic.setBackgroundResource(R.drawable.txtvewlistcomb);
        driverPic.setPadding(16, 10, 10, 10);
        driverPic.setTextColor(Color.rgb(77, 77, 77));
        driverPic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.face);

        FlexboxLayout.LayoutParams lfayoutParamsj = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsj.setFlexBasisPercent(0.95f);
        lfayoutParamsj.setMargins(0, 0, 0, 0);
        usrFlexboxbina.addView(driverPic, lfayoutParamsj);

        /*Rating*/
        RatingBar ratCheck = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
        String ratCheckstr = valsec.get(14).replace("\"", "");
        ratCheck.setRating(resources.strTointeger(ratCheckstr));
        ratCheck.setIsIndicator(true);
        ratCheck.setStepSize(0.1f);
        ratCheck.setNumStars(5);
        picklocat.setPadding(0, 0, 10, 0);


        FlexboxLayout.LayoutParams lfayoutParamsk = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsk.setFlexBasisPercent(0.95f);
        lfayoutParamsk.setMargins(0, 0, 10, 0);
        usrFlexboxbina.addView(ratCheck, lfayoutParamsk);

        FlexboxLayout.LayoutParams lfayoutParamsi = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lfayoutParamsi.setFlexBasisPercent(0.2f);
        lfayoutParamsi.setMargins(0, 0, 0, 0);
        usrFlexboxb.addView(usrFlexboxbina, lfayoutParamsi);

        /*Add rows*/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(7, 0, 7, 5);
        cellsLinear.addView(usrFlexbox, layoutParams);

        LinearLayout.LayoutParams layoutParamsa = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsa.setMargins(7, 0, 7, 5);
        cellsLinear.addView(usrFlexboxa, layoutParamsa);

        LinearLayout.LayoutParams layoutParamsb = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsb.setMargins(7, 0, 7, 5);
        cellsLinear.addView(usrFlexboxb, layoutParamsb);


        return cellsLinear;
    }
}