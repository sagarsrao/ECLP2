package com.bhatia.rahul.expensecalculatorproject2.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.bhatia.rahul.expensecalculatorproject2.R;
import com.bhatia.rahul.expensecalculatorproject2.model.Expense;

import java.util.List;



public class TodaysExpenseListViewAdapter extends ArrayAdapter {
  private final List<Expense> expenses;

  public TodaysExpenseListViewAdapter(List<Expense> expenses, Context context, int resource) {
    super(context, resource);
    this.expenses = expenses;
  }

  @Override
  public int getCount() {
    return expenses.size();
  }

  @Override
  public Object getItem(int position) {
    Expense expense = expenses.get(position);
    return expense.getType() + " - " + " " + getContext().getString(R.string.rupee_sym) +  expense.getAmount()+ " - " +expense.getDate();
  }

  @Override
  public long getItemId(int position) {
    return expenses.get(position).getId();
  }
}
