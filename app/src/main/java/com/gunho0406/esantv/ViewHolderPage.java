package com.gunho0406.esantv;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView imageView;

    DataPage data;

    ViewHolderPage(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgpage);
        context = itemView.getContext();
    }

    public void onBind(DataPage data){
        this.data = data;

        Glide.with(context)
                .load(data.getBitmap())
                .centerCrop()
                .override(1080,1080)
                .into(imageView);
    }
}
