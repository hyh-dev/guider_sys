package com.example.administrator.guidersystem.discoveryPage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.guidersystem.R;

import java.util.List;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    private Context mContext;
    private List<Discovery> mDiscoveryItem;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView discoveryImage;
        TextView discoveryName;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            discoveryImage=(ImageView) view.findViewById(R.id.discovery_image);
            discoveryName =(TextView) view.findViewById(R.id.discovery_name);
        }
    }
    public DiscoveryAdapter(List<Discovery> discoveryList){
        mDiscoveryItem=discoveryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext==null){
            mContext=viewGroup.getContext();
        }
        View view=LayoutInflater.from(mContext).inflate(R.layout.discovery_item,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Discovery discovery=mDiscoveryItem.get(position);
                Intent intent=new Intent(mContext,DiscoveryContentActivity.class);
                intent.putExtra(DiscoveryContentActivity.PLANT_NAME,discovery.getName());
                intent.putExtra(DiscoveryContentActivity.PLANT_IMAGE_URL,discovery.getImageId());
                intent.putExtra(DiscoveryContentActivity.PLANT_AREA,discovery.getArea());
                intent.putExtra(DiscoveryContentActivity.PLANT_ENGNAME,discovery.getEngName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Discovery discovery=mDiscoveryItem.get(position);
        holder.discoveryName.setText(discovery.getName());
        Glide.with(mContext).load(discovery.getImageId()).into(holder.discoveryImage);

    }

    @Override
    public int getItemCount() {
        return mDiscoveryItem.size();
    }
}
