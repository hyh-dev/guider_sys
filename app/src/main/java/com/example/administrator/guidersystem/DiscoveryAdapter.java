package com.example.administrator.guidersystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DiscoveryAdapter extends ArrayAdapter<Discovery> {
    private int resourceId;

    public DiscoveryAdapter(Context context, int textViewResourceId, List<Discovery> objects) {
        super(context,textViewResourceId, objects);
        resourceId =textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Discovery discovery=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.discoveryImage=(ImageView) view.findViewById(R.id.discovery_image);
            viewHolder.discoveryName =(TextView) view.findViewById(R.id.discovery_name);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        Glide.with(getContext()).load(discovery.getImageId()).into(viewHolder.discoveryImage);
        viewHolder.discoveryName.setText(discovery.getName());
        return view;
    }
    class ViewHolder{
        ImageView discoveryImage;
        TextView discoveryName;
    }

}
