package com.example.leonp.okfood.UserAccount.Account.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Comments.CommentThreadActivity;
import com.example.leonp.okfood.UserAccount.Account.Models.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leonp on 4/22/2018.
 */

public class FeedListAdapter extends ArrayAdapter<Post> {
    private static final String TAG = "FeedListAdapter";

    // vars
    private LayoutInflater mInflator;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference reference;
    private Boolean isFavorited = false;

    public FeedListAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        mContext = context;
        reference = FirebaseDatabase.getInstance().getReference();
    }

    static class ViewHolder{
        TextView tvTitle;
        CircleImageView ivPostImage;
        TextView tvComments;
        TextView tvFavorite;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflator.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.ivPostImage = (CircleImageView) convertView.findViewById(R.id.ivPostImage);
            holder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);
            holder.tvFavorite = (TextView) convertView.findViewById(R.id.tvFavorite);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentThreadActivity.class);
                intent.putExtra("post_title", holder.tvTitle.getText());
                mContext.startActivity(intent);
            }
        });

        holder.tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked on favorite text view");
                if (isFavorited) {
                    Log.d(TAG, "onClick: Post was favorited.  Will un-like");
                    isFavorited = false;
                    holder.tvFavorite.setTextColor(mContext.getColor(R.color.Black));
                } else {
                    Log.d(TAG, "onClick: Post wasn't favorited.  Liking it now");
                    holder.tvFavorite.setTextColor(mContext.getColor(R.color.Favorite_Gold));
                    isFavorited = true;
                }

            }
        });

        // set actual values
        try {
            Log.d(TAG, "getView: Trying Universal Image Loader");
            Uri downloadUri = Uri.parse(getItem(position).getImage_path());
            UniversalImageLoader imageLoader = new UniversalImageLoader(mContext);
            ImageLoader.getInstance().init(imageLoader.getConfig());


            UniversalImageLoader.setImage(downloadUri.toString(), holder.ivPostImage);
        } catch (NullPointerException e) {
            Log.e(TAG, "getView: NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "getView: Exception: " + e.getMessage());
        }
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvComments.setText(getItem(position).getNumberOfComments());


        return convertView;
    }
}
