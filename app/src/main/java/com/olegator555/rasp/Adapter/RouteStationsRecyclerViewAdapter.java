package com.olegator555.rasp.Adapter;

import Model.StationInRouteModel;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olegator555.rasp.R;
import com.olegator555.rasp.StationInfoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RouteStationsRecyclerViewAdapter extends
        RecyclerView.Adapter<RouteStationsRecyclerViewAdapter.RouteStationsViewHolder> {
    private final ArrayList<StationInRouteModel> stationInRouteModels;

    public RouteStationsRecyclerViewAdapter(ArrayList<StationInRouteModel> stationInRouteModels) {
        this.stationInRouteModels = stationInRouteModels;
    }

    @NonNull
    @Override
    public RouteStationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RouteStationsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_in_route_list_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RouteStationsViewHolder holder, int position) {
        holder.bind(stationInRouteModels.get(position));
    }

    @Override
    public int getItemCount() {
        return stationInRouteModels.size();
    }

    class RouteStationsViewHolder extends RecyclerView.ViewHolder {
        private TextView stationNameTextView;
        private TextView arrivalToStationTextView;
        private TextView departureFromStationTextView;
        private SimpleDateFormat simpleDateFormat;
        public RouteStationsViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            stationNameTextView = itemView.findViewById(R.id.stationName);
            arrivalToStationTextView = itemView.findViewById(R.id.arrivalAtStationTimeTextView);
            departureFromStationTextView = itemView.findViewById(R.id.departureFromStationTextView);
            itemView.setOnClickListener((view) -> {
                Intent intent = new Intent(itemView.getContext(), StationInfoActivity.class);
                intent.putExtra("StationName", stationInRouteModels.get(getAdapterPosition()).getStation_title());
                itemView.getContext().startActivity(intent);
            });
        }
        void bind(StationInRouteModel model) {
            stationNameTextView.setText(model.getStation_title());
            try {
                arrivalToStationTextView.setText(simpleDateFormat.format(model.getArrival()));
            } catch (NullPointerException e) {
                arrivalToStationTextView.setText("Нет данных");
            }
            try {
                departureFromStationTextView.setText(simpleDateFormat.format(model.getDeparture()));
            } catch (NullPointerException e) {
                departureFromStationTextView.setText("Нет данных");
            }
        }
    }

}
