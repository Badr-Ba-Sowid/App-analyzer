package com.example.appanalyzer;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class BlockedAppsRecyclerAdapter extends RecyclerView.Adapter<BlockedAppsRecyclerAdapter.BlockedAppsViewHolder> {
    private List<AppModel> appsList;
    private Context context;
    private String userID;
    private FirebaseFirestore mDocumentReference;
    private CollectionReference userReferance;
    private RelativeLayout rootLayout;

    public BlockedAppsRecyclerAdapter (List<AppModel> appsList, Context context , String userID , RelativeLayout rootLayout) {
        this.appsList = appsList;
        this.context = context;
        this.userID = userID;
        this.rootLayout = rootLayout;
        mDocumentReference = FirebaseFirestore.getInstance();


    }

    @NonNull
    @Override
    public BlockedAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view  = layoutInflater.inflate(R.layout.view_holder_blocked_apps,parent,false);
        return new BlockedAppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedAppsViewHolder holder, final int position) {
        holder.titleTextView.setText(appsList.get(position).getName());
        String iconURL = appsList.get(position).getIconURL();

        Picasso.get()
                .load(iconURL)
                .transform(new RoundedCornersTransformation(50, 0))
                .into(holder.appIcon);

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context , holder.optionsButton);
                popup.inflate(R.menu.blocked_app_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.unblock_option:
                                removeFromBlockedList(appsList.get(position) , position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });



    }

    private void removeFromBlockedList(AppModel appModel , int adapterPosition) {
        userReferance = mDocumentReference.collection(Config.USERS);
        userReferance.document(userID).collection(Config.BLOCKED_APPS).document(appModel.getAppID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(rootLayout , "The app has been removed from your blocked list" , Snackbar.LENGTH_LONG).show();
                appsList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    class BlockedAppsViewHolder extends RecyclerView.ViewHolder {
        //initializing
        private ImageView appIcon;
        private TextView titleTextView;
        private ImageButton optionsButton;
        public BlockedAppsViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.blocked_app_icon_image_view);
            titleTextView = itemView.findViewById(R.id.blocked_app_name_view);
            optionsButton = itemView.findViewById(R.id.blocked_app_options);
        }

    }
}