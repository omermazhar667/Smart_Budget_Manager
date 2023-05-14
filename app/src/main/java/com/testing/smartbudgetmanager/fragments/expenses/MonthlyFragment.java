package com.testing.smartbudgetmanager.fragments.expenses;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testing.smartbudgetmanager.MainActivity;
import com.testing.smartbudgetmanager.R;
import com.testing.smartbudgetmanager.fragments.adapters.MonthlyDataAdapter;
import com.testing.smartbudgetmanager.models.DailyBudgetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MonthlyFragment extends Fragment {
    private Calendar calendar;
    private TextView monthlyCalender;
    private View rootView;
    private DatabaseReference mRef;
    private FirebaseDatabase mDataBase;
    private FirebaseAuth mAuth;
    private MonthlyDataAdapter monthlyDataAdapter;
    private List<DailyBudgetModel> tempList = new ArrayList<>();
    private List<DailyBudgetModel> tempListTwo = new ArrayList<>();
    private List<DailyBudgetModel> incomeList = new ArrayList<>();
    private List<DailyBudgetModel> expenseList = new ArrayList<>();
    private int totalIncome = 0;
    private int totalExpense = 0;
    private TextView monthlyIncome;
    private TextView monthlyExpense;
    private TextView monthlyBalance;
    private RecyclerView monthlyRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_monthly, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialization();
        clickListeners();
    }

    private void initialization() {
        monthlyCalender = rootView.findViewById(R.id.monthlyCalender);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference(currentUser.getUid());
        monthlyBalance = rootView.findViewById(R.id.monthlyBalance);
        monthlyIncome = rootView.findViewById(R.id.monthlyIncome);
        monthlyExpense = rootView.findViewById(R.id.monthlyExpense);
        monthlyDataAdapter = new MonthlyDataAdapter(tempList, tempListTwo);
        monthlyRecyclerView = rootView.findViewById(R.id.monthlyRV);
//        SimpleDateFormat dateFormat = new SimpleDateFormat(" MMMM yyyy");
//
//        // Format the current date
//        String formattedDate = dateFormat.format(calendar.getTime());
//
//        // Set the formatted date to the TextView
//        monthlyCalender.setText(formattedDate);
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        monthlyRecyclerView.setAdapter(monthlyDataAdapter);

        loadData();
    }

    private void clickListeners() {
        monthlyCalender.setOnClickListener(view -> {
            showYearMonthPickerDialog();
        });
    }

    private void loadData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempList.clear();
                tempListTwo.clear();
                incomeList.clear();
                expenseList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the data from the childSnapshot
                    DailyBudgetModel data = childSnapshot.getValue(DailyBudgetModel.class);
                    tempList.add(data);

                }
                for (DailyBudgetModel dailyBudgetModel : tempList) {
                    for (DailyBudgetModel dailyBudgetModel1 : tempListTwo) {
                        if (!dailyBudgetModel1.getDate().equals(dailyBudgetModel.getDate())) {
                            tempListTwo.add(dailyBudgetModel);
                        }

                    }

                }

                for (int i = 0; i <= tempList.size() - 1; i++) {
                    DailyBudgetModel dailyBudgetModel = tempList.get(i);
//                    String originalString = dailyBudgetModel.getDate();
//                    int firstSpaceIndex = originalString.indexOf(" ");
//                    int lastSpaceIndex = originalString.lastIndexOf(" ");
//                    String modifiedString1 = originalString.substring(0, firstSpaceIndex);
//                    String modifiedString2 = originalString.substring(0, lastSpaceIndex);
//                    String finalString =  modifiedString2;
//                    if (dailyBudgetModel.getDate().contains(monthlyCalender.getText().toString())) {
                    if (dailyBudgetModel.getIncome() != null) {
                        if (dailyBudgetModel.getIncome()) {
                            incomeList.add(dailyBudgetModel);
                        } else {
                            expenseList.add(dailyBudgetModel);
                        }
                    }
//                    }
                }
                totalIncome = 0;
                totalExpense = 0;
                monthlyDataAdapter.data = tempList;

                for (int i = 0; i <= incomeList.size() - 1; i++) {
                    DailyBudgetModel item = incomeList.get(i);
                    totalIncome += item.getAmount();
                }
                monthlyIncome.setText("Total Income (Credit) = \nRs" + totalIncome);
                for (int i = 0; i <= expenseList.size() - 1; i++) {
                    DailyBudgetModel item = expenseList.get(i);
                    totalIncome += item.getAmount();
                }
                monthlyExpense.setText("Total Income (Credit) = \nRs" + totalExpense);
                monthlyDataAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void showYearMonthPickerDialog() {
        // Get the current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Create a DatePickerDialog with custom style
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date here
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        // Format the date as "Month Year"
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(calendar.getTime());
                        monthlyCalender.setText(formattedDate);
                        loadData();
                    }
                }, year, month, 1);

        // Set the DatePicker mode to show only year and month
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);

        // Show the calendar picker dialog
        datePickerDialog.show();
    }
}