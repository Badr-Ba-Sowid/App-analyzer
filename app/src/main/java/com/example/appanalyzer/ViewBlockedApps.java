package com.example.appanalyzer;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.factor.bouncy.BouncyRecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBlockedApps extends AppCompatActivity {
    private Toolbar toolbar;
    private BouncyRecyclerView recyclerView;
    private BlockedAppsRecyclerAdapter blockedAppsRecyclerAdapter;
    private String userID;
    private FirebaseFirestore mDocumentReference;
    private CollectionReference userReferance;
    List<AppModel> appsList;
    private RequestQueue mRequestQueue;
    private RelativeLayout rootLayout;
    private CustomProgressBar customProgressBar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blocked_apps);
        recyclerView = findViewById(R.id.blocked_apps_recycler_view);
        toolbar = findViewById(R.id.view_blocked_apps_toolbar);
        rootLayout = findViewById(R.id.view_blocked_apps_root_layout);
        customProgressBar = new CustomProgressBar(getApplicationContext() , "Loading" , R.raw.loading);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        toolbar.setTitle("Blocked Apps");

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mDocumentReference = FirebaseFirestore.getInstance();

        getBlockedApps(userID);



    }

    void getBlockedApps(String userID){
        customProgressBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customProgressBar.show();
        userReferance = mDocumentReference.collection(Config.USERS);
         userReferance.document(userID).collection(Config.BLOCKED_APPS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                HashMap<String , Object> blockedApps = new HashMap<>();
                QuerySnapshot querySnapshots = task.getResult();
                for (QueryDocumentSnapshot queryDocumentSnapshot
                :querySnapshots){
                    blockedApps.putAll(queryDocumentSnapshot.getData());
                }
                getAppDetails(blockedApps , userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 customProgressBar.dismiss();
             }
         });

    }

    private void getAppDetails(HashMap<String, Object> blockedApps , String userID) {
        appsList = new ArrayList<>();
        blockedAppsRecyclerAdapter = new BlockedAppsRecyclerAdapter(appsList, ViewBlockedApps.this , userID , rootLayout);
        recyclerView.setAdapter(blockedAppsRecyclerAdapter);

        JSONObject jsonObject = new JSONObject();
        JSONObject objects = new JSONObject();
        for (Map.Entry<String,Object> entry :
        blockedApps.entrySet()){
            try {
                objects.put(entry.getKey() , entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        try {
            jsonObject.put(Config.BLOCKED_APP_LIST , objects);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,
                Config.RETRIVE_APPS_URL,
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
                                    apps.setIconURL(jsonObject.getString("icon"));
                                    apps.setAppID(jsonObject.getString("App Id"));
                                    appsList.add(apps);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                customProgressBar.dismiss();
                                blockedAppsRecyclerAdapter.notifyDataSetChanged();
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
                Toast.makeText(getApplicationContext() , error.toString() , Toast.LENGTH_SHORT).show();
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

                final String mRequestBody = jsonObject.toString();
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



//        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(Request.Method.POST, Config.RETRIVE_APPS_URL, jsonObject
//                , new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    JSONArray jsonArray = new JSONArray(response.getJSONArray("apps"));
//
//                    for (int i = 0; i < response.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        AppModel apps = new AppModel();
//                        try {
//                            apps.setName(jsonObject.getString("title"));
//                            apps.setIconURL(jsonObject.getString("icon"));
//                            appsList.add(apps);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        blockedAppsRecyclerAdapter.notifyDataSetChanged();
//
//                    }
//
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext() , "json error", Toast.LENGTH_LONG).show();
//
//                }
//                  Toast.makeText(getApplicationContext() , Integer.toString(appsList.size()), Toast.LENGTH_LONG).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                  Toast.makeText(getApplicationContext() , error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }){
//
//                @Override
//                public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//
//
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response.statusCode);
//                    // can get more details such as response.headers
//                }
//                return super.parseNetworkResponse(response);
//            }
//        };

        mRequestQueue.add(jsonArrayRequest);

    }





}
