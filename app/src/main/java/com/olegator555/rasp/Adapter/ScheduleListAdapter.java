package com.olegator555.rasp.Adapter;

import Model.ScheduleModel;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olegator555.rasp.ActivityScheduleElementSelected;
import com.olegator555.rasp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>{
    private List<ScheduleModel> modelList;
    private boolean isListFilled = false;
    private int currentHour;
    private int currentMinutes;
    private int offsetIndex;
    private int estimated;

    public ScheduleListAdapter(List<ScheduleModel> modelsList) {
        modelList = new ArrayList<>(modelsList);
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

    @SuppressLint("NotifyDataSetChanged")
    public int updateModelList(@NonNull ArrayList<ScheduleModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
        isListFilled = true;
        currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        currentMinutes = Calendar.getInstance().get(Calendar.MINUTE);
        for (ScheduleModel element:modelList
             ) {
            if(element.getDeparture_hour()==currentHour)
                if(element.getDeparture_minute()<currentMinutes) {
                    offsetIndex++;
                }
            if(element.getDeparture_hour()<currentHour)
                offsetIndex++;
        }
        Log.d("Cal hour", String.valueOf(currentHour));
        Log.d("Cal minute", String.valueOf(currentMinutes));
        return offsetIndex;
    }


    class ScheduleListViewHolder extends RecyclerView.ViewHolder {
        private TextView departureTextView;
        private TextView arrivalTextView;
        private TextView travelTime;
        private TextView departurePlatform;
        private TextView arrivalPlatform;
        private TextView typeTitle;
        private TextView estimatedTime;

        public ScheduleListViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTextView = itemView.findViewById(R.id.departureTimeTeextView);
            arrivalTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            travelTime = itemView.findViewById(R.id.travelTimeTextView);
            departurePlatform = itemView.findViewById(R.id.departurePlatformTextView);
            arrivalPlatform = itemView.findViewById(R.id.arrivalPlatformTextView);
            typeTitle = itemView.findViewById(R.id.typeTitleTextView);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), ActivityScheduleElementSelected.class);
                intent.putExtra("SelectedElement", modelList.get(getAdapterPosition()));
                view.getContext().startActivity(intent);
            });

        }

        void bind(ScheduleModel element) {
            departureTextView.setText(element.getDeparture());
            arrivalTextView.setText(element.getArrival());
            travelTime.setText(String.valueOf(element.getDuration()));
            departurePlatform.setText(element.getDeparture_platform());
            arrivalPlatform.setText(element.getArrival_platform());
            typeTitle.setText(element.getType_title());
            estimatedTime.setText(String.valueOf(estimated));
        }
    }

}
