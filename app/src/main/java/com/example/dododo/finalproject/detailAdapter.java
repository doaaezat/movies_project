package com.example.dododo.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.Inflater;

/**
 * Created by DoDo DO on 06/10/2015.
 */
public class detailAdapter extends BaseAdapter {

    private static final int reviewType = 0;
    private static final int trailerType = 1;
    Context context;
    List objects;

    public detailAdapter(Context context) {
        this.context = context;
        objects = new ArrayList<>();

    }

    public void addItem(List item) {
        objects.addAll(item);
    }

    //
//    public void addSectionHeaderItem(final String item) {
//        header_item .add(item);
//        sectionHeader.add(header_item.size() - 1);
//        notifyDataSetChanged();
//    }
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof review.ResultsEntity)
            return reviewType;
        else
            return trailerType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (rowType) {
                case reviewType:
                    convertView = inflater.inflate(R.layout.review, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.author);
                    holder.release = (TextView) convertView.findViewById(R.id.review);
                    break;
                case trailerType:
                    convertView = inflater.inflate(R.layout.trailer, null);
                    holder.title = (TextView) convertView.findViewById(R.id.trailer);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (rowType) {
            case reviewType:
                review.ResultsEntity review1 = (review.ResultsEntity) getItem(position);
                holder.title.setText(review1.getAuthor());
                holder.release.setText(review1.getContent());
                break;
            case trailerType:
                vedios.ResultsEntity vedios = (vedios.ResultsEntity) getItem(position);
                holder.title.setText(vedios.getName());
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView title;
        TextView release;
    }
}


