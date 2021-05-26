package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    private RecyclerView recyclerView;
    private SearchResultAdapter searchAdapter;
    private List<AppModel> appsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        super.onViewCreated(view, savedInstanceState);
        categoriesRadioGroup = v.findViewById(R.id.categories_radio_group);
        mRequestQueue = Volley.newRequestQueue(getActivity());

        //define an array for all the categories
        String[] categoriesArray = {"Education", "Finance", "Business", "Music & Audio", "Health & Fitness", "Productivity", "Shopping", "Tools", "Food & Drink"};
        // create a radio button for each category
        for (String category
                : categoriesArray) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setBackground(getActivity().getDrawable(R.drawable.radio_button_round_grey));
            radioButton.setElevation(3);
            radioButton.setText(category);
            radioButton.setButtonDrawable(null);
            categoriesRadioGroup.addView(radioButton);
            radioButton.setPadding(20, 5, 20, 5);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
            params.setMargins(5, 0, 5, 0);
            radioButton.setLayoutParams(params);
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
        appsList = new ArrayList<>();
        searchAdapter = new SearchResultAdapter(appsList, getActivity());
        recyclerView.setAdapter(searchAdapter);

        String url = "https://asia-southeast2-app-analyzer-cd47b.cloudfunctions.net/recommend_apps";
        //TODO add progress dialog
        HashMap<String, String> params = new HashMap<>();
        params.put("query", query);

        JsonArrayRequest jsonArrayRequest;

        jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                url,
               null,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getActivity()  , "Check 1"  , Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {

                                jsonObject = response.getJSONObject(i);
                                AppModel apps = new AppModel();
                                apps.setName(jsonObject.getString("appName"));
                                apps.setRating(jsonObject.getInt("appRating"));
                                apps.setDescription(jsonObject.getString("appDescription"));

                                appsList.add(apps);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            searchAdapter.notifyDataSetChanged();

                        }

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getActivity()  , "Check 2"  , Toast.LENGTH_SHORT).show();
                         Toast.makeText(getActivity()  , error.getLocalizedMessage()  , Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public byte[] getBody() {
                // TODO Clean code
                JSONObject requestBody = new JSONObject();
                try {
                    query.replaceAll("NaN", String.valueOf(-1));
                    requestBody.put("query", query);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String mRequestBody = requestBody.toString();
                try {
                    return mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return new byte[0];
            }
        };
        // Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonArrayRequest);


}
}

