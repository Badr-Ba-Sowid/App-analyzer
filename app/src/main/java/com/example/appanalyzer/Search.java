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
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends Fragment {
    View v;
    RadioGroup categoriesRadioGroup;
    SearchView searchView;
    Toolbar toolbar;
    private RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoriesRadioGroup = v.findViewById(R.id.categories_radio_group);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //define an array for all the categories
        String[] categoriesArray = {"Education" , "Finance" ,"Bussiness" , "Music & Audio"  , "Health & Fitness" , "Productivity" , "Shopping" , "Tools" , "Food & Drink"};
        // create a radio button for each category
        for(String category
        : categoriesArray){
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setBackground(getActivity().getDrawable(R.drawable.radio_button_round_grey));
            radioButton.setElevation(3);
            radioButton.setText(category);
            radioButton.setButtonDrawable(null);
            categoriesRadioGroup.addView(radioButton);
            radioButton.setPadding(20,5,20,5);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
            params.setMargins(5,0,5,0);
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
    void sendRequest(final String query){
        String url = "https://asia-southeast2-app-analyzer-cd47b.cloudfunctions.net/recommend_apps";
        //TODO add progress dialog
        HashMap<String, String> params = new HashMap<>();
        params.put("query", query );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity() ,response.toString()  , Toast.LENGTH_SHORT).show();

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getActivity()  , error.getLocalizedMessage()  , Toast.LENGTH_SHORT).show();

                    }

                });

// Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonObjectRequest);    }
}