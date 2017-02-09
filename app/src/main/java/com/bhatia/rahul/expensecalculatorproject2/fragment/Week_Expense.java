package com.bhatia.rahul.expensecalculatorproject2.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Week_Expense extends Fragment {

    TextView totalExpenseTextBox;
    private View rootView;
    ListView listView;
    public Week_Expense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_week__expense, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DBhandler dBhandler = new DBhandler(getContext());
        List<Expense> expenses = dBhandler.getCurrentWeeksExpenses();
        Long totalExpense = 0l;
        for (Expense expense : expenses)
            totalExpense += expense.getAmount();

        Toast.makeText(getContext(),totalExpense.toString(),Toast.LENGTH_LONG).show();

        totalExpenseTextBox  = (TextView) rootView.findViewById(R.id.current_week_expense);
        totalExpenseTextBox.setText(getActivity().getString(R.string.current_week_expenses) + " " + getActivity().getString(R.string.rupee_sym) + totalExpense.toString());

        listView = (ListView) rootView.findViewById(R.id.current_week_expenses_list);
        listView.setAdapter(new TodaysExpenseListViewAdapter(expenses, getActivity(), android.R.layout.simple_list_item_1));
    }
}
