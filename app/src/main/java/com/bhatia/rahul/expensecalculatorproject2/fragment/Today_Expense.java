package com.bhatia.rahul.expensecalculatorproject2.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhatia.rahul.expensecalculatorproject2.R;
import com.bhatia.rahul.expensecalculatorproject2.adapter.TodaysExpenseListViewAdapter;
import com.bhatia.rahul.expensecalculatorproject2.database.DBhandler;
import com.bhatia.rahul.expensecalculatorproject2.model.Expense;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Today_Expense extends Fragment {
    TextView totalExpenseTextBox;
    private View rootView;
    ListView listView;
    List<Expense> expenses;
    TodaysExpenseListViewAdapter expenseListViewAdapter;
    public Today_Expense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_today__expense, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final DBhandler dBhandler = new DBhandler(getContext());
        expenses = dBhandler.getTodaysExpenses();
        Long totalExpense = 0l;
        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
            Log.d("ID",expense.getId().toString());
            Log.d("Amount",expense.getAmount().toString());
            Log.d("TYPE",expense.getType());
            Log.d("Date",expense.getDate());
        }

        totalExpenseTextBox  = (TextView) rootView.findViewById(R.id.total_expense);
        totalExpenseTextBox.setText(getActivity().getString(R.string.total_expense) + " " + getActivity().getString(R.string.rupee_sym) + totalExpense.toString());


        listView = (ListView) rootView.findViewById(R.id.todays_expenses_list);
        expenseListViewAdapter = new TodaysExpenseListViewAdapter(expenses, getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(expenseListViewAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dBhandler.deleteExpense(expenses.get(i).getId());
                refreshfragment();
                Toast.makeText(getContext(),"Sucssefully Deleted",Toast.LENGTH_SHORT).show();
                return false;

            }
        });


    }
    public void refreshfragment(){

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Today_Expense today_expense = new Today_Expense();
        transaction.replace(R.id.fragment, today_expense);
        transaction.commit();

    }

}
