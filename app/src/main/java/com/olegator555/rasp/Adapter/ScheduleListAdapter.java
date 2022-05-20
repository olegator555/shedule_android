package com.olegator555.rasp.Adapter;

import Model.ScheduleModel;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.olegator555.rasp.ActivityScheduleElementSelected;
import com.olegator555.rasp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>{
    private List<ScheduleModel> modelList;
    private int currentHour;
    private int currentMinutes;
    private int offsetIndex;
    private long estimated;
    private Date currentTime;
    public ScheduleListAdapter(List<ScheduleModel> modelsList) {
        modelList = new ArrayList<>(modelsList);
    }

    @NonNull
    @Override
    public ScheduleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suburbans_list_layout, parent,
                 false);
         currentTime = new Date();
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
        private final TextView departureTextView;
        private final TextView arrivalTextView;
        private final TextView travelTime;
        private final TextView departurePlatform;
        private final TextView arrivalPlatform;
        private final TextView typeTitle;
        private final TextView estimatedTime;
        private final SimpleDateFormat simpleDateFormat;

        public ScheduleListViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTextView = itemView.findViewById(R.id.departureTimeTeextView);
            arrivalTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            travelTime = itemView.findViewById(R.id.travelTimeTextView);
            departurePlatform = itemView.findViewById(R.id.departurePlatformTextView);
            arrivalPlatform = itemView.findViewById(R.id.arrivalPlatformTextView);
            typeTitle = itemView.findViewById(R.id.typeTitleTextView);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            itemView.setOnClickListener(view -> {
                Gson gson = new Gson();
                String serializedElement = gson.toJson(modelList.get(getAdapterPosition()));
                Intent intent = new Intent(itemView.getContext(), ActivityScheduleElementSelected.class);
                intent.putExtra("SelectedElement", serializedElement);
                view.getContext().startActivity(intent);
            });

        }

        void bind(ScheduleModel element) {
            try {
                arrivalTextView.setText(simpleDateFormat.format(element.getArrival()));
            } catch (NullPointerException e) {
                arrivalTextView.setText("Нет данных");
            }
            try {
                departureTextView.setText(simpleDateFormat.format(element.getDeparture()));
            } catch (NullPointerException e) {
                departureTextView.setText("Нет данных");
            }
            travelTime.setText(String.valueOf(element.getDuration()));
            departurePlatform.setText(element.getDeparture_platform());
            arrivalPlatform.setText(element.getArrival_platform());
            typeTitle.setText(element.getType_title());
            estimated = (element.getDeparture().getTime() - currentTime.getTime())/60000;
            if(estimated>0) {
                if (estimated<60)
                    estimatedTime.setText(String.format("Через %d минут", estimated));
                else {
                    estimatedTime.setText(String.format("Через %d час %d минут", estimated/60, estimated%60));
                }
            }
            else
                estimatedTime.setText("Ушла");
        }
    }

}
