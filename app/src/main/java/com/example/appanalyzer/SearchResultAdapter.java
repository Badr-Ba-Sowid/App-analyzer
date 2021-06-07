package com.example.appanalyzer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<AppModel> appsList;
    private Context context;

    public SearchResultAdapter (List<AppModel> appsList, Context context) {
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
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, final int position) {
        holder.appName.setText(appsList.get(position).getName());

        //set app description
        holder.appDescription.setText(appsList.get(position).getDescription());

        // set the rating
        DecimalFormat df2 = new DecimalFormat("#.#");
        holder.appRating.setText(String.valueOf(df2.format(appsList.get(position).getRating())));


         Float appRating = (float) (appsList.get(position).getRating());
         holder.starRatings.setRating(appRating);

            String iconURL = appsList.get(position).getIconURL();

            Picasso.get()
                    .load(iconURL)
                    .transform(new RoundedCornersTransformation(50, 0))
                    .into(holder.appIconPicture);
            holder.appIconPicture.setPadding(3,3,3,3);


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
    ImageButton shareButton, favouriteButton, optionsButton;
    Button PlayStoreButton;
    RatingBar starRatings;


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
        starRatings = itemView.findViewById(R.id.app_rating_stars);

        layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , AppViewPage.class);
                intent.putExtra("APP" , appsList.get(getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }
}
}