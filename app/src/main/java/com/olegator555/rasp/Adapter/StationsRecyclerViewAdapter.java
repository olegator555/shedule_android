package com.olegator555.rasp.Adapter;

import Model.ServerAnswerModel;
import Utils.AppLevelUtilsAndConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.olegator555.rasp.R;
import com.olegator555.rasp.StationSelectionActivityActivity;

import java.util.ArrayList;


public class StationsRecyclerViewAdapter extends RecyclerView.Adapter<StationsRecyclerViewAdapter
        .StationsRecyclerViewHolder> {
    private ArrayList<ServerAnswerModel> model_list;
    private final String pref_key;
    public StationsRecyclerViewAdapter(ArrayList<ServerAnswerModel> model_list, String pref_key) {
        this.model_list = model_list;
        this.pref_key = pref_key;
    }

    @NonNull
    @Override
    public StationsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stations_auto_complete, parent,
                false);
        return new StationsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationsRecyclerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }
    public void filterList(ArrayList<ServerAnswerModel> filteredList) {
        model_list = filteredList;
        notifyDataSetChanged();
    }

    class StationsRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView stationNameTextView;
        private final TextView directionTextView;

        public StationsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            stationNameTextView = itemView.findViewById(R.id.station_name_textview);
            directionTextView = itemView.findViewById(R.id.direction_text_view);
            itemView.setOnClickListener(view -> {
                Gson gson = new Gson();
                String gson_string = gson.toJson(model_list.get(getAdapterPosition()));
                AppLevelUtilsAndConstants.writeToPreferences(itemView.getContext(), pref_key,
                        gson_string, String.class);
                ((StationSelectionActivityActivity)itemView.getContext()).onBackPressed();
            });
        }

        public void bind(int position) {
            stationNameTextView.setText(model_list.get(position).getStation_name());
            directionTextView.setText(model_list.get(position).getDirection());
        }
    }
}
