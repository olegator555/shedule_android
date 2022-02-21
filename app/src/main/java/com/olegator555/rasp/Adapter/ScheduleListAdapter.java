package com.olegator555.rasp.Adapter;

import Model.ScheduleModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olegator555.rasp.R;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>{
    private List<ScheduleModel> modelList;

    public ScheduleListAdapter(List<ScheduleModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ScheduleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suburbans_list_layout, parent,
                 false);
         return new ScheduleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListViewHolder holder, int position) {
        holder.bind(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ScheduleListViewHolder extends RecyclerView.ViewHolder {
        private TextView departureTextView;
        private TextView arrivalTextView;
        private TextView travelTime;

        public ScheduleListViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTextView = itemView.findViewById(R.id.departureTimeTeextView);
            arrivalTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            travelTime = itemView.findViewById(R.id.travelTimeTextView);

        }

        // FIXME: 21/02/2022
        void bind(ScheduleModel element) {
            /*departureTextView.setText(element.getDeparture_station().getStation_name());
            arrivalTextView.setText(element.getArrival_station().getStation_name());
            travelTime.setText(element.getTravel_time());*/
        }
    }

}