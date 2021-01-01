package com.gunho0406.esantv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private Context context;
    ArrayList<String> bitmaplist = new ArrayList<>();

    ViewPagerAdapter(Context context, ArrayList<String> data) {
        this.bitmaplist = data;
        this.context = context;
    }

    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewPagerAdapter.ViewHolder holder, int position) {
        Log.e("bitmap",bitmaplist.get(position));
            Glide.with(context)
                    .load(bitmaplist.get(position))
                    .centerCrop()
                    .override(1080,1080)
                    .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return bitmaplist.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title, user, date, subject;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgpage);
        }
    }

}