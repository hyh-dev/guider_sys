package com.example.administrator.guidersystem.homePage;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.administrator.guidersystem.R;
        import com.example.administrator.guidersystem.homePage.Icon;

        import java.util.List;

public class IconAdapter extends ArrayAdapter<Icon> {
    private int resourceId;

    public IconAdapter(Context context, int textViewResourceId, List<Icon> objects) {
        super(context,textViewResourceId, objects);
        resourceId =textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
       Icon icon=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.iconImage=(ImageView)view.findViewById(R.id.img_icon);
            viewHolder.iconName=(TextView)view.findViewById(R.id.txt_icon);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.iconImage.setImageResource(icon.getiId());
        viewHolder.iconName.setText(icon.getiName());
        return view;
    }
    class ViewHolder{
        ImageView iconImage;
        TextView iconName;
    }
}
