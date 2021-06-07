package com.example.appanalyzer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.fajaragungpramana.dotsindicator.DotsIndicator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AppViewPage extends AppCompatActivity  {
    private ImageView iconImageView;
    private RatingBar appRatingBar;
    private TextView appTitleTextView , downloadsTextView, requiresAndroidTextView,
            sizeTextView , currentVersionTextView , contentRatingTextView ,
            developerTextView , descriptionTextView;
    private ViewPagerAdapterAppView viewPagerAdapterAppView;
    private ViewPager viewPager;
    private DotsIndicator dotsIndicator;
    private Toolbar toolbar;
    private MotionLayout motionLayout;
    private Button readMoreButton , openInPlayStore , installButton;
    private boolean informationLayoutUp = true;
    private ImageButton appOptions;
    private CollectionReference userReferance;
    private FirebaseFirestore mDocRef;
    private FirebaseAuth mAuth;

    private AppModel app;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view_page);
        mDocRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userReferance = mDocRef.collection("Users");

        iconImageView = findViewById(R.id.app_image);
        appRatingBar = findViewById(R.id.app_rating_bar);
        appTitleTextView = findViewById(R.id.app_title_place_holder);
        downloadsTextView = findViewById(R.id.downloads_text_view_place_holder);
        requiresAndroidTextView = findViewById(R.id.requires_android_text_view_place_holder);
        sizeTextView = findViewById(R.id.size_text_view_place_holder);
        currentVersionTextView = findViewById(R.id.current_version_text_view_place_holder);
        contentRatingTextView = findViewById(R.id.content_rating_text_view_place_holder);
        currentVersionTextView = findViewById(R.id.current_version_text_view_place_holder);
        developerTextView = findViewById(R.id.developers_text_view_place_holder);
        viewPager = findViewById(R.id.app_view_pager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        toolbar = findViewById(R.id.toolbar_app_view_page);
//        toolbar.findViewById(R.id.logo_image_button).setVisibility(View.GONE);
        descriptionTextView = findViewById(R.id.description_text_view_place_holder);
        motionLayout = findViewById(R.id.motion_layout_app_view_page);
        readMoreButton = findViewById(R.id.read_more_button);
        appOptions = findViewById(R.id.app_options_app_view_page);
        openInPlayStore = findViewById(R.id.open_playstore_button);
        installButton = findViewById(R.id.install_buttom);



        app = getIntent().getParcelableExtra("APP");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        toolbar.setTitle(app.getName());
        String iconURL = app.getIconURL() ;
        Picasso.get()
                .load(iconURL)
                .transform(new RoundedCornersTransformation(50, 0))
                .into(iconImageView);
        appTitleTextView.setText(app.getName());
        downloadsTextView.setText(app.getInstalls());
        requiresAndroidTextView.setText(app.getAndroidVersion());
        sizeTextView.setText(app.getSize());
        currentVersionTextView.setText(app.getVersion());
        contentRatingTextView.setText(app.getContentRating());
        developerTextView.setText(app.getDeveloper());
        descriptionTextView.setText(app.getDescription());


        double rating = app.getRating();
        appRatingBar.setRating((float) rating);

        if(app.getScreenShots() !=null) {
            viewPagerAdapterAppView = new ViewPagerAdapterAppView(getApplicationContext(), app.getScreenShots());
            viewPager.setAdapter(viewPagerAdapterAppView);
            dotsIndicator.setViewPager(viewPager);

        }else{
            Toast.makeText(getApplicationContext() , "null" , Toast.LENGTH_LONG).show();
        }

        motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
                if(informationLayoutUp) {
                    readMoreButton.setText("Read less");
                    informationLayoutUp = false;
                }else{
                    readMoreButton.setText("Read more");
                    informationLayoutUp= true;
                }
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        appOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopup(view);
            }
        });

        openInPlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app.getUrl())));
                } catch (android.content.ActivityNotFoundException anfe) {

                }
            }
        });



    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.app_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.hide_option:
                        hideApplication();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }




    private void hideApplication() {
        String userId = mAuth.getCurrentUser().getUid();
        HashMap<String , Boolean> map = new HashMap<>();
        map.put(app.getAppID() , true);
        userReferance.document(userId).collection("blockedApps" ).document(app.getAppID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(motionLayout , "The app has been blocked" , Snackbar.LENGTH_SHORT).show();

            }
        });



    }


}
    class ViewPagerAdapterAppView extends PagerAdapter {
    Context context;
    private List<String> imageUrls;
    ViewPagerAdapterAppView(Context context , List<String> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                .centerInside()
                .into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }





}
