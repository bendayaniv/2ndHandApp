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

public class CategoryAutoCompleteTextAdapter extends ArrayAdapter<String> {

    private ArrayList<String> categoryListFull;

    public CategoryAutoCompleteTextAdapter(@NonNull Context context, @NonNull List<String> categoryList) {
        super(context, 0, categoryList);
        categoryListFull = new ArrayList<>(categoryList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        TextView categoryName = convertView.findViewById(R.id.category_view_name);
        String category = getItem(position);
        if (category != null) {
            categoryName.setText(getItem(position));
        }

        return convertView;
    }

    private Filter categoryFilter = new Filter() {
        /**
         * The filtering operation/logic.
         * @param constraint the constraint used to filter the data
         * @return
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(categoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : categoryListFull) {
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
