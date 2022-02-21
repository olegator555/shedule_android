package com.olegator555.rasp.Adapter;

import Model.ServerAnswerModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.olegator555.rasp.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteStationAdapter extends ArrayAdapter<ServerAnswerModel> {
    private final List<ServerAnswerModel> modelListFull;

    public AutoCompleteStationAdapter(@NonNull Context context, @NonNull List<ServerAnswerModel> modelList) {
        super(context, 0, modelList);
        modelListFull = new ArrayList<>(modelList);

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stations_auto_complete, parent,
                    false);
        TextView station_name = convertView.findViewById(R.id.station_name_textview);
        TextView direction = convertView.findViewById(R.id.direction_text_view);
        ServerAnswerModel model = getItem(position);
        if(model != null){
            station_name.setText(model.getStation_name());
            StringBuilder sb = new StringBuilder(model.getDirection());
            if (!sb.toString().contains("-") && !sb.toString().contains(" ")) {
                sb.append(" направление");
            }
            direction.setText(sb.toString());
        }
        return convertView;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            ArrayList<ServerAnswerModel> suggestions_list = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                suggestions_list.addAll(modelListFull);
            }
            else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for(ServerAnswerModel element : modelListFull){
                    if(element.getStation_name().toLowerCase().startsWith(pattern))
                        suggestions_list.add(element);
                }
            }
            filterResults.values = suggestions_list;
            filterResults.count = suggestions_list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ServerAnswerModel) resultValue).getStation_name();
        }
    };
}
