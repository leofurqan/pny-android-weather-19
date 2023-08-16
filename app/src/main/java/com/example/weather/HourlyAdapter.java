package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder>{

    Context context;
    private ArrayList<HourlyData> weatherData;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txtTime, txtTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTime = itemView.findViewById(R.id.txt_time);
            txtTemp = itemView.findViewById(R.id.txt_temp);
            img = itemView.findViewById(R.id.img);
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public TextView getTxtTemp() {
            return txtTemp;
        }
    }

    public HourlyAdapter(Context c, ArrayList<HourlyData> weather) {
        this.context = c;
        this.weatherData = weather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyData data = weatherData.get(position);
        holder.getTxtTime().setText(data.getTime());
        holder.getTxtTemp().setText(data.getTemp());
        Glide.with(context).load(data.getIcon()).into(holder.getImg());
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }
}
