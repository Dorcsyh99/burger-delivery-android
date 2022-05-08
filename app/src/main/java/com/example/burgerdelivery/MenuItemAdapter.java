package com.example.burgerdelivery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> implements Filterable {
    private static final String LOG_TAG = MenuItemAdapter.class.getName();

    private ArrayList<MenuItem> itemData;
    private ArrayList<MenuItem> itemDataAll;
    private Context context;
    private int lastPosition = -1;

    MenuItemAdapter(Context context, ArrayList<MenuItem> itemData){
        this.itemData = itemData;
        this.itemDataAll = itemData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem currentItem = itemData.get(position);

        Log.w(LOG_TAG, "adapterben");

        holder.bindTo(currentItem);

        if(holder.getAbsoluteAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.glide_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAbsoluteAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    private Filter menuFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<MenuItem> filterList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = itemDataAll.size();
                results.values = itemDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(MenuItem item : itemDataAll){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return menuFilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemDesc;
        private ImageView itemImage;
        private TextView itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemDesc = itemView.findViewById(R.id.itemDesc);
            itemPrice = itemView.findViewById(R.id.itemPrice);


            itemImage = itemView.findViewById(R.id.itemImage);

            itemView.findViewById(R.id.toCartButtton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "log");

                }
            });
        }

        public void bindTo(MenuItem currentItem) {
            itemName.setText(currentItem.getName());
            itemDesc.setText(currentItem.getDescription());
            itemPrice.setText(Integer.toString(currentItem.getPrice()));

            //Glide.with(context).load(currentItem.getImageRescource()).into(itemImage);
        }
    };
}


