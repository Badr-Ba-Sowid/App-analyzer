package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Set;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<AppModel> appsList;
    private Context context;

    public SearchResultAdapter (List<AppModel> appsList, Context context,String userId, Boolean isFromfav) {
        this.appsList = appsList;
        this.context = context;

    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view  = layoutInflater.inflate(R.layout.view_apps,parent,false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        //set app description
        holder.appDescription.setText(appsList.get(position).getDescription());

        // set the rating
        holder.appRating.setText(appsList.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
    //initializing
    ImageView appIconPicture;
    TextView appName, appDescription, appRating;
    RelativeLayout layoutCard;
    ImageButton shareButton, favouriteButton, optionsButton, PlayStoreButton;


    public SearchResultViewHolder(@NonNull View itemView) {
        super(itemView);

        appIconPicture = itemView.findViewById(R.id.app_icon_view);
        appName = itemView.findViewById(R.id.app_name_view);
        appDescription = itemView.findViewById(R.id.app_description_view);
        appRating = itemView.findViewById(R.id.rating_play_store);
        layoutCard = itemView.findViewById(R.id.layout_app_search_results);
        shareButton = itemView.findViewById(R.id.share_app_button);
        favouriteButton = itemView.findViewById(R.id.favorite_button);
        optionsButton = itemView.findViewById(R.id.app_options);
        PlayStoreButton = itemView.findViewById(R.id.play_store_app_button);

    }
}
}