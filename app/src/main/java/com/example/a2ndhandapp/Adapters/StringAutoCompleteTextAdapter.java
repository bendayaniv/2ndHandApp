package com.example.a2ndhandapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a2ndhandapp.R;

import java.util.ArrayList;
import java.util.List;

public class StringAutoCompleteTextAdapter extends ArrayAdapter<String> {

    private ArrayList<String> stringListFull;

    public StringAutoCompleteTextAdapter(@NonNull Context context, @NonNull List<String> stringList) {
        super(context, 0, stringList);
        stringListFull = new ArrayList<>(stringList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return stringFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.string_list_item, parent, false);
        }

        TextView stringName = convertView.findViewById(R.id.string_view_name);
        String string = getItem(position);
        if (string != null) {
            stringName.setText(getItem(position));
        }

        return convertView;
    }

    private Filter stringFilter = new Filter() {
        /**
         * The filtering operation/logic.
         * @param constraint the constraint used to filter the data
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(stringListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : stringListFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        /**
         * Publish the results of the filtering operation in the user interface.
         * @param constraint the constraint used to filter the data
         * @param results the results of the filtering operation
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList<String>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((String) resultValue).toString();
        }
    };

}
