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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testing.smartbudgetmanager.R;
import com.testing.smartbudgetmanager.fragments.adapters.DailyExpenseAdapter;
import com.testing.smartbudgetmanager.fragments.adapters.DailyIncomeAdapter;
import com.testing.smartbudgetmanager.models.DailyBudgetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DailyFragment extends Fragment {
    private View rootView;
    private FloatingActionButton addBtn;
    private List<DailyBudgetModel> incomeList = new ArrayList<>();
    private List<DailyBudgetModel> expenseList = new ArrayList<>();
    private List<DailyBudgetModel> tempList = new ArrayList<>();
    private DailyIncomeAdapter dailyIncomeAdapter;
    private DailyExpenseAdapter dailyExpenseAdapter;
    private RecyclerView incomeRV;
    private RecyclerView expenseRV;
    private int tabPosition = 0;
    private int totalIncome = 0;
    private TextView totalIncomeTV;
    private int totalExpense = 0;
    private TextView totalExpenseTV;
    private TextView balanceTV;
    private Calendar calendar;
    private int currentDay = 1;
    private DatabaseReference mRef;
    private FirebaseDatabase mDataBase;
    private FirebaseAuth mAuth;
    private TextView calenderDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialization();
        clickListeners();
    }

    private void initialization() {
        addBtn = rootView.findViewById(R.id.floatingActionButton);
        totalIncomeTV = rootView.findViewById(R.id.totalIncomeTV);
        totalExpenseTV = rootView.findViewById(R.id.totalExpenseTV);
        calenderDate = rootView.findViewById(R.id.calenderDate);
        balanceTV = rootView.findViewById(R.id.balanceTV);
        calendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference(currentUser.getUid());

// Add tabs with values
        // Create the adapter and pass the user list
        dailyIncomeAdapter = new DailyIncomeAdapter(incomeList);
        dailyExpenseAdapter = new DailyExpenseAdapter(expenseList);

        incomeRV = rootView.findViewById(R.id.incomeRV);
        expenseRV = rootView.findViewById(R.id.expenseRV);
        // Set the layout manager and adapter for the RecyclerView
        incomeRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        expenseRV.setLayoutManager(new LinearLayoutManager(requireContext()));

        incomeRV.setAdapter(dailyIncomeAdapter);
        expenseRV.setAdapter(dailyExpenseAdapter);


        // Set the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy EEEE");

        // Format the current date
        String formattedDate = dateFormat.format(calendar.getTime());

        // Set the formatted date to the TextView
        calenderDate.setText(formattedDate);

        loadData();


    }

    private void clickListeners() {
        // Add a listener for tab selection events
        addBtn.setOnClickListener(view ->
                showBottomSheetDialog());

        calenderDate.setOnClickListener(view -> {
            showDatePicker();
        });
    }

    private void loadData(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempList.clear();
                incomeList.clear();
                expenseList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the data from the childSnapshot
                    DailyBudgetModel data = childSnapshot.getValue(DailyBudgetModel.class);
                    tempList.add(data);
                }

                for (int i = 0; i <= tempList.size()-1; i++) {
                    DailyBudgetModel dailyBudgetModel = tempList.get(i);
                    Log.d("**TAG", "onDataChange: "+dailyBudgetModel.getDate());
                    if (Objects.equals(dailyBudgetModel.getDate(), calenderDate.getText().toString())) {
                        if (dailyBudgetModel.getIncome() != null) {
                            if (dailyBudgetModel.getIncome()) {
                                incomeList.add(dailyBudgetModel);
                            } else {
                                expenseList.add(dailyBudgetModel);
                            }
                        }
                    }
                }



                totalIncome = 0;
                totalExpense = 0;
                dailyIncomeAdapter.data = incomeList;
                dailyExpenseAdapter.data = expenseList;

                for (int i = 0; i <= incomeList.size() - 1; i++) {
                    DailyBudgetModel item = incomeList.get(i);
                    totalIncome += item.getAmount();
                }
                totalIncomeTV.setText("RS " + totalIncome + ".00");

                for (int i = 0; i <= expenseList.size() - 1; i++) {
                    DailyBudgetModel item = expenseList.get(i);
                    totalExpense += item.getAmount();
                }
                totalExpenseTV.setText("RS " + totalExpense + ".00");
                String balance = String.valueOf(totalIncome-totalExpense);

                balanceTV.setText("Balance "+ balance);

                dailyIncomeAdapter.notifyDataSetChanged();
                dailyExpenseAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

        TabLayout tabLayout = bottomSheetDialog.findViewById(R.id.dataTabLayout);
        ImageView done = bottomSheetDialog.findViewById(R.id.done);
        TextView title = bottomSheetDialog.findViewById(R.id.itemName);
        TextView amount = bottomSheetDialog.findViewById(R.id.itemAmountBS);
        TextView description = bottomSheetDialog.findViewById(R.id.descriptionET);

        tabLayout.addTab(tabLayout.newTab().setText("Income (Credit)"));
        tabLayout.addTab(tabLayout.newTab().setText("Expense (Debit)"));

        done.setOnClickListener(view -> {
            String key = mRef.push().getKey();
            mRef.child(key).child("name").setValue(title.getText().toString());
            mRef.child(key).child("amount").setValue(Integer.parseInt(amount.getText().toString()));
            mRef.child(key).child("description").setValue(description.getText().toString());
            mRef.child(key).child("key").setValue(key);
            mRef.child(key).child("date").setValue(calenderDate.getText().toString());
            if (tabPosition == 0) {
                mRef.child(key).child("income").setValue(true);
            } else {
                mRef.child(key).child("income").setValue(false);
            }

            bottomSheetDialog.dismiss();
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                int position = tab.getPosition();
                tabPosition = position;
                // Perform actions based on the selected tab position
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection
            }
        });

        bottomSheetDialog.show();
    }

    private void showDatePicker() {
        // Get the current date values from the Calendar instance
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the initial date to the current values
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update the Calendar instance with the selected date
                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Format the selected date as desired
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy EEEE", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());

                    // Set the selected date in the EditText field
                    calenderDate.setText(formattedDate);
                    loadData();
                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void changeDateProgrammatically() {
        // Get the updated date from the Calendar instance
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Log.d("**TAG", "changeDateProgrammatically:" + currentDay);

        // Perform further operations with the updated date
        // ...
    }

}