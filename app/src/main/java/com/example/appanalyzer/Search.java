package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.RatingBar;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends Fragment {
    View v;
    RadioGroup categoriesRadioGroup;
    SearchView searchView;
    Toolbar toolbar;

    private RequestQueue mRequestQueue;

    private String iconURL;
    private ImageView imageView;

    private RecyclerView recyclerView;
    private SearchResultAdapter searchAdapter;
    private List<AppModel> appsList;
    private CustomProgressBar customProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imageView = v.findViewById(R.id.app_icon_view);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        customProgressBar = new CustomProgressBar(getActivity() , "Searching..." , R.raw.loading);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        super.onViewCreated(view, savedInstanceState);
        categoriesRadioGroup = v.findViewById(R.id.categories_radio_group);
        mRequestQueue = Volley.newRequestQueue(getActivity());

        //define an array for all the categories
        String[] categoriesArray = {"All","Education", "Finance", "Business", "Music & Audio", "Health & Fitness", "Productivity", "Shopping", "Tools", "Food & Drink"};
        // create a radio button for each category
        for (String category
                : categoriesArray) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setBackground(getActivity().getDrawable(R.drawable.radio_button_round_grey));
            radioButton.setMinimumWidth(20);

            radioButton.setElevation(3);
            radioButton.setText(category);
            radioButton.setButtonDrawable(null);
            categoriesRadioGroup.addView(radioButton);
            radioButton.setPadding(20, 5, 20, 5);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
            params.setMargins(5, 0, 5, 0);
            radioButton.setLayoutParams(params);
            if (category.equals("All")){
                radioButton.setChecked(true);
            }
        }

        searchView = getActivity().findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                sendRequest(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }

    void sendRequest(final String query) {
        customProgressBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customProgressBar.show();
        appsList = new ArrayList<>();
        searchAdapter = new SearchResultAdapter(appsList, getActivity());
        recyclerView.setAdapter(searchAdapter);

        String url = "https://asia-southeast2-app-analyzer-cd47b.cloudfunctions.net/recommend_apps";
        //TODO add progress dialog
//        HashMap<String, String> params = new HashMap<>();
//        params.put("query", query);

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            response = response.replaceAll("NaN", "-1" /*Or whatever you need*/);

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AppModel apps = new AppModel();
                                try {
                                    apps.setName(jsonObject.getString("title"));
                                    apps.setDescription(jsonObject.getString("summary"));
                                    apps.setRating(jsonObject.getDouble("score"));
                                    apps.setIconURL(jsonObject.getString("icon"));
                                    apps.setAndroidVersion(jsonObject.getString("androidVersion"));
                                    apps.setVideoImage(jsonObject.getString("videoImage"));
                                    apps.setVideo(jsonObject.getString("video"));
                                    apps.setContentRating(jsonObject.getString("contentRating"));
                                    apps.setInstalls(jsonObject.getString("installs"));
                                    apps.setDeveloper(jsonObject.getString("developer"));
                                    apps.setVersion(jsonObject.getString("version"));
                                    apps.setSize(jsonObject.getString("size"));
                                    apps.setDescription(jsonObject.getString("description"));
                                    apps.setUrl(jsonObject.getString("url"));
                                    apps.setAppID(jsonObject.getString("App Id"));

//                                    //TODO change this from cloud functions
                                    String screenShotsString  = jsonObject.getString("screenShots");
                                    String str[]  =screenShotsString.substring(1, screenShotsString.length() - 1).split(",") ;
                                    List<String> screenShots = new ArrayList<>();
                                    int count  = 0;
                                    for(String x:
                                    str){
                                        if(x.length()>10){
                                            if(count==0){
                                                screenShots.add(x.substring(1, x.length() - 1));
                                            }else{
                                                screenShots.add(x.substring(2, x.length() - 1));
                                            }
                                        }
                                        count++;
                                    }
                                    apps.setScreenShots(screenShots);
                                    appsList.add(apps);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    customProgressBar.dismiss();

                                }
                                customProgressBar.dismiss();
                                searchAdapter.notifyDataSetChanged();

                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                            customProgressBar.dismiss();

                        }
                      //  Toast.makeText(getActivity() , Integer.toString(appsList.size()), Toast.LENGTH_LONG).show();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity() , error.toString() , Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();

            }

        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("query", query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String mRequestBody = jsonBody.toString();
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return super.parseNetworkResponse(response);
            }

        };

        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonArrayRequest);


}
}

