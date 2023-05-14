package com.testing.smartbudgetmanager.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.testing.smartbudgetmanager.R;
import com.testing.smartbudgetmanager.models.DailyBudgetModel;

import java.util.List;

public class DailyIncomeAdapter extends RecyclerView.Adapter<DailyIncomeAdapter.ViewHolder> {
     public List<DailyBudgetModel> data;

    public DailyIncomeAdapter(List<DailyBudgetModel> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyBudgetModel item = data.get(position);
        holder.itemTitle.setText(item.getName());
        holder.itemAmount.setText(Integer.toString(item.getAmount()));
        holder.itemDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemAmount;
        TextView itemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            itemDescription = itemView.findViewById(R.id.description);
        }
    }
}
