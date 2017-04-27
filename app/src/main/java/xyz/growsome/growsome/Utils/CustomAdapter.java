package xyz.growsome.growsome.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
//import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import xyz.growsome.growsome.R;

/**
 * Created by aang on 4/24/17.
 */

public class CustomAdapter extends ArrayAdapter<ItemData> {

    Context mContext;
    private ArrayList<ItemData> dataSet;


    public CustomAdapter(ArrayList<ItemData> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtMonto;
        ImageView imgCat;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        // Get the data item for this position
        ItemData itemData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtMonto = (TextView) convertView.findViewById(R.id.monto);
            viewHolder.imgCat = (ImageView) convertView.findViewById(R.id.category_img);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


            viewHolder.txtName.setText(itemData.getName());
            viewHolder.txtMonto.setText(itemData.getMonto());
            viewHolder.imgCat.setBackgroundColor(Color.parseColor(itemData.getCatColor()));


        // Return the completed view to render on screen
        return convertView;
    }
}

