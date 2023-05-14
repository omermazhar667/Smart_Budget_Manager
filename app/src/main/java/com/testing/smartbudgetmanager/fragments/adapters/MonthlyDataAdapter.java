package com.testing.smartbudgetmanager.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.testing.smartbudgetmanager.R;
import com.testing.smartbudgetmanager.databinding.MonthlyExpensyItemLayoutBinding;
import com.testing.smartbudgetmanager.models.DailyBudgetModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MonthlyDataAdapter extends RecyclerView.Adapter<MonthlyDataAdapter.ViewHolder> {
     public List<DailyBudgetModel> data;
     public HashSet<DailyBudgetModel> data2;


    public MonthlyDataAdapter(List<DailyBudgetModel> data, HashSet<DailyBudgetModel> data2) {
        this.data = data;
        this.data2 = data2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monthly_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyBudgetModel item = data.get(position);
        List<ExpenseData> expense=getExpenses(item.getDate());
        List<ExpenseData> income=getIncome(item.getDate());
        holder.date.setText(item.getDate());
        for (ExpenseData eD:expense ) {
            MonthlyExpensyItemLayoutBinding mE=MonthlyExpensyItemLayoutBinding.inflate(LayoutInflater.from(holder.itemView.getContext()));
            mE.tvPrice.setText(String.valueOf(eD.getAmount()));
            mE.tvTitle.setText(String.valueOf(eD.getTittle()));
            holder.expenseLayout.addView(mE.getRoot());
        }
        for (ExpenseData eD:income ) {
            MonthlyExpensyItemLayoutBinding mE=MonthlyExpensyItemLayoutBinding.inflate(LayoutInflater.from(holder.itemView.getContext()));
            mE.tvPrice.setText(String.valueOf(eD.getAmount()));
            mE.tvTitle.setText(String.valueOf(eD.getTittle()));
            holder.incomeLayout.addView(mE.getRoot());
        }
    }

    private List<ExpenseData> getExpenses(String date){
        List<ExpenseData> expense= new ArrayList<>();
        for(DailyBudgetModel md:data){
            if(!md.getIncome() && md.getDate().equals(date)){
                expense.add(new ExpenseData(md.getName(),md.getAmount()));
            }
        }
        return  expense;
    }
    private List<ExpenseData> getIncome(String date){
        List<ExpenseData> expense= new ArrayList<>();
        for(DailyBudgetModel md:data){
            if(md.getIncome() && md.getDate().equals(date)){
                expense.add(new ExpenseData(md.getName(),md.getAmount()));
            }
        }
        return  expense;
    }
    class ExpenseData{
        String tittle;
        int amount;

        public String getTittle() {
            return tittle;
        }

        public void setTittle(String tittle) {
            this.tittle = tittle;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public ExpenseData() {
        }

        public ExpenseData(String tittle, int amount) {
            this.tittle = tittle;
            this.amount = amount;
        }
    }

    @Override
    public int getItemCount() {
        return data2.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        LinearLayout expenseLayout,incomeLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            expenseLayout = itemView.findViewById(R.id.monthlyExpenseTV);
            incomeLayout = itemView.findViewById(R.id.monthlyIncomeRV);

        }
    }
}
