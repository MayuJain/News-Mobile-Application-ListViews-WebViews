package com.example.homework05;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titletextview);
            viewHolder.urlImageView = (ImageView) convertView.findViewById(R.id.urlImageView);
            viewHolder.authortextView = (TextView) convertView.findViewById(R.id.authortextView);
            viewHolder.datetextView = (TextView) convertView.findViewById(R.id.datetextView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTextView.setText(news.Title);
        Log.d("demo",news.urlToImage);
        if(!news.urlToImage.isEmpty()) {
            Picasso.get().load(news.urlToImage).into(viewHolder.urlImageView);
        }else{
            viewHolder.urlImageView.setImageResource(R.drawable.download);
        }
        viewHolder.authortextView.setText(news.author);
        viewHolder.datetextView.setText(news.publishedAt);

        return convertView;

    }

    private static class ViewHolder{
        TextView titleTextView;
        ImageView urlImageView;
        TextView authortextView;
        TextView datetextView;
    }
}


