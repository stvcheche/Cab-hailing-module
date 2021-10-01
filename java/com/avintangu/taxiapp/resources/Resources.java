package com.avintangu.taxiapp.resources;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;

import androidx.constraintlayout.widget.Guideline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Resources {

    private static Context rContext;
    Jsonresource jsonresource;

    public Resources(Context context) {
        rContext = context;
        jsonresource = new Jsonresource(rContext);

    }

    /* Search Json object */
    public ArrayList<ArrayList<String>> searchJson(String keyword, String tripstate, String distance, String time) {

        ArrayList<ArrayList<String>> retVal = new ArrayList<>();
        JSONObject nwobj;
        String jsonStr = jsonresource.retJson();

        try {
            nwobj = new JSONObject(jsonStr);
            JSONArray trips = nwobj.getJSONArray("trips");

            for (int obj = 0; obj < trips.length(); obj++) {
                JSONObject innerObj = new JSONObject(trips.get(obj).toString());
                ArrayList<String> srchdeterm = new ArrayList<>();

                ArrayList<String> valecs = new ArrayList<>();
                valecs.add("\"" + innerObj.get("id").toString() + "\"");
                valecs.add("\"" + innerObj.get("status").toString() + "\"");
                valecs.add("\"" + innerObj.get("request_date").toString() + "\"");
                valecs.add("\"" + innerObj.get("pickup_lat").toString() + "\"");
                valecs.add("\"" + innerObj.get("pickup_lng").toString() + "\"");
                valecs.add("\"" + innerObj.get("pickup_location").toString() + "\"");
                valecs.add("\"" + innerObj.get("dropoff_lat").toString() + "\"");
                valecs.add("\"" + innerObj.get("dropoff_lng").toString() + "\"");
                valecs.add("\"" + innerObj.get("dropoff_location").toString() + "\"");
                valecs.add("\"" + innerObj.get("pickup_date").toString() + "\"");
                valecs.add("\"" + innerObj.get("dropoff_date").toString() + "\"");
                valecs.add("\"" + innerObj.get("type").toString() + "\"");
                valecs.add("\"" + innerObj.get("driver_id").toString() + "\"");
                valecs.add("\"" + innerObj.get("driver_name").toString() + "\"");
                valecs.add("\"" + innerObj.get("driver_rating").toString() + "\"");
                valecs.add("\"" + innerObj.get("driver_pic").toString() + "\"");
                valecs.add("\"" + innerObj.get("car_make").toString() + "\"");
                valecs.add("\"" + innerObj.get("car_model").toString() + "\"");
                valecs.add("\"" + innerObj.get("car_number").toString() + "\"");
                valecs.add("\"" + innerObj.get("car_year").toString() + "\"");
                valecs.add("\"" + innerObj.get("car_pic").toString() + "\"");
                valecs.add("\"" + innerObj.get("duration").toString() + "\"");
                valecs.add("\"" + innerObj.get("duration_unit").toString() + "\"");
                valecs.add("\"" + innerObj.get("distance").toString() + "\"");
                valecs.add("\"" + innerObj.get("distance_unit").toString() + "\"");
                valecs.add("\"" + innerObj.get("cost").toString() + "\"");
                valecs.add("\"" + innerObj.get("cost_unit").toString() + "\"");

                if (!keyword.equals("nada")) {
                    srchdeterm.add(srchKeyword(valecs, keyword));

                }
                if (!tripstate.equals("All")) {
                    srchdeterm.add(srchTripstate(valecs, tripstate));

                }
                if (!distance.equals("Any")) {
                    srchdeterm.add(srchDistance(valecs, distance));

                }
                if (!time.equals("Any")) {
                    srchdeterm.add(srchTime(valecs, time));

                }

                if (srchdeterm.isEmpty()) {
                    retVal.add(valecs);

                } else if (!srchdeterm.contains("nada")) {
                    retVal.add(valecs);

                }

            }


        } catch (JSONException ex) {

        }

        return retVal;

    }

    /*Search keyword*/
    public String srchKeyword(ArrayList<String> valecs, String prkeyword) {
        String retval = "nada";
        String keyword = prkeyword.toLowerCase();

        String pickup_location = valecs.get(5).toLowerCase().replace("\"", "");
        String dropoff_location = valecs.get(8).toLowerCase().replace("\"", "");
        String type = valecs.get(11).toLowerCase().replace("\"", "");
        String driver_name = valecs.get(13).toLowerCase().replace("\"", "");
        String car_make = valecs.get(16).toLowerCase().replace("\"", "");
        String car_model = valecs.get(17).toLowerCase().replace("\"", "");
        String car_number = valecs.get(18).toLowerCase().replace("\"", "");

        if (pickup_location.contains(keyword) || dropoff_location.contains(keyword)
                || type.contains(keyword) || driver_name.contains(keyword)
                || car_make.contains(keyword) || car_model.contains(keyword)
                || car_number.contains(keyword)) {
            retval = "cool";

        }
        return retval;
    }

    /*Search trip state*/
    public String srchTripstate(ArrayList<String> valecs, String tripstate) {
        String retval = "nada";
        String testStr = "COMPLETED";

        String tripstateval = valecs.get(1).replace("\"", "");

        if (tripstate.equals(testStr)) {
            if (tripstateval.equals(testStr)) {
                retval = "cool";
            }

        } else {
            retval = "cool";
        }
        return retval;
    }

    /*Search distance*/
    public String srchDistance(ArrayList<String> valecs, String distance) throws JSONException {
        String retval = "nada";
        String distanceval = valecs.get(23).replace("\"", "");

        switch (distance) {

            case "Under 3km": {
                if (strTodouble(distanceval) < 3.0) {
                    retval = "cool";
                }

                break;
            }

            case "3 to 8km": {
                if (strTodouble(distanceval) >= 3.0
                        && strTodouble(distanceval) < 8.0) {
                    retval = "cool";
                }

                break;
            }

            case "8 to 15km": {
                if (strTodouble(distanceval) >= 8.0
                        && strTodouble(distanceval) <= 15.0) {
                    retval = "cool";
                }

                break;
            }

            case "More than 15km": {
                if (strTodouble(distanceval) > 15.0) {
                    retval = "cool";
                }

                break;
            }
            default:
                break;
        }

        return retval;
    }

    /*Search time*/
    public String srchTime(ArrayList<String> valecs, String time) throws JSONException {
        String retval = "nada";
        String timeval = valecs.get(21).replace("\"", "");

        switch (time) {

            case "Under 5min": {
                if (strTointeger(timeval) < 5) {
                    retval = "cool";
                }

                break;
            }

            case "5 to 10min": {
                if (strTointeger(timeval) >= 5
                        && strTodouble(timeval) < 10) {
                    retval = "cool";
                }

                break;
            }

            case "10 to 20min": {
                if (strTointeger(timeval) >= 10
                        && strTodouble(timeval) <= 20) {
                    retval = "cool";
                }

                break;
            }

            case "More than 20min": {
                if (strTointeger(timeval) > 20) {
                    retval = "cool";
                }

                break;
            }
            default:
                break;
        }

        return retval;
    }

    /*String to double*/
    public Double strTodouble(String strnum) {
        Double retfl;

        try {
            retfl = Double.valueOf(strnum);
        } catch (NumberFormatException ex) {
            retfl = 0.0;

        }

        return retfl;

    }

    /*String to integer*/
    public Integer strTointeger(String strnum) {
        Integer retfl;

        try {
            retfl = Integer.valueOf(strnum);
        } catch (NumberFormatException ex) {
            retfl = 0;

        }

        return retfl;

    }

    /*Get font sizes*/
    public double getFontex(Display display, Point point) {
        display.getSize(point);
        int x = point.x;
        int y = point.y;
        return (x + y) / (int) Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0)) * 10.0;
    }

    /* Adjust dimensions */
    public void adjsDimens(Guideline guidelina) {
        float constimgWidtha = 0f;

        if (rContext.getResources().getConfiguration().orientation == 2) {
            constimgWidtha = 100f;

        } else if (rContext.getResources().getConfiguration().orientation == 1) {
            constimgWidtha = 100f;

        }

        /* Calculated constraints */
        float newimgWidtha = constimgWidtha / 100f;

        guidelina.setGuidelinePercent(newimgWidtha);

    }

    /*Generate unique ids*/
    public static int generateViewId() {
        AtomicInteger sNextGeneratedId = new AtomicInteger(1);

        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                int result = sNextGeneratedId.get();

                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

}
