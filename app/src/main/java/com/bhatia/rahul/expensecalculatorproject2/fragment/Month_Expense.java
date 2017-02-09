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
import com.bhatia.rahul.expensecalculatorproject2.database.DBhandler;
import com.bhatia.rahul.expensecalculatorproject2.model.Expense;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Month_Expense extends Fragment {
    TextView totalExpenseTextBox;
    private View rootView;


    public Month_Expense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_month__expense, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DBhandler dBhandler = new DBhandler(getContext());
        List<Expense> expenses = dBhandler.getExpensesForCurrentMonthGroupByCategory();

        Long totalExpense = 0l;
        for (Expense expense : expenses)
            totalExpense += expense.getAmount();

        totalExpenseTextBox = (TextView) getActivity().findViewById(R.id.current_months_total_expense);
        totalExpenseTextBox.setText(getString(R.string.total_expense) + " " + getString(R.string.rupee_sym) + totalExpense);


        plotGraph(expenses);
    }

    public void plotGraph(List<Expense> expenses) {
        List<Bar> points = new ArrayList<>();

        for (Expense expense : expenses){
            Bar bar = new Bar();
            bar.setColor(getActivity().getResources().getColor(R.color.light_blue));
            bar.setName(expense.getType());
            bar.setValue(expense.getAmount());
            points.add(bar);
        }

        BarGraph graph = (BarGraph)getActivity().findViewById(R.id.graph);
        graph.setBars((ArrayList<Bar>) points);
    }
}
