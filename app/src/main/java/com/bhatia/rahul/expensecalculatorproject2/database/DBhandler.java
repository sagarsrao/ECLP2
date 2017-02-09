package com.bhatia.rahul.expensecalculatorproject2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.bhatia.rahul.expensecalculatorproject2.model.Expense;
import com.bhatia.rahul.expensecalculatorproject2.model.ExpenseType;
import com.bhatia.rahul.expensecalculatorproject2.table.ExpenseTable;
import com.bhatia.rahul.expensecalculatorproject2.table.ExpenseTypeTable;


import java.util.ArrayList;
import java.util.List;

import static com.bhatia.rahul.expensecalculatorproject2.utils.DateUtil.currentMonthOfYear;

import static com.bhatia.rahul.expensecalculatorproject2.utils.DateUtil.getCurrentDate;
import static com.bhatia.rahul.expensecalculatorproject2.utils.DateUtil.getCurrentWeeksDates;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Created by Rahul on 1/23/2017.
 */

public class DBhandler extends SQLiteOpenHelper implements BaseColumns {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExpenseDatabase";
    public static final String TABLE_NAME = "expenses";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String DATE = "date";


    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ExpenseTable.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(ExpenseTypeTable.CREATE_TABLE_QUERY);
        seedExpenseTypes();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void deleteExpense(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, "_id = ?", new String[] { String.valueOf(id) });

    }

    public void addExpense(int amount, String type, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AMOUNT, amount);
        values.put(TYPE, type);
        values.put(DATE, date);

        database.insert(TABLE_NAME, null, values);

    }
    public List<String> getExpenseTypes() {
        ArrayList<String> expenseTypes = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTypeTable.SELECT_ALL, null);

        if(isCursorPopulated(cursor)){
            do {
                String type = cursor.getString(cursor.getColumnIndex(ExpenseTypeTable.TYPE));
                expenseTypes.add(type);
            } while(cursor.moveToNext());
        }

        return expenseTypes;
    }
    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ExpenseTypeTable.TABLE_NAME, "", new String[]{});
        database.delete(ExpenseTable.TABLE_NAME, "", new String[]{});
        seedExpenseTypes();
        database.close();
    }

    public List<Expense> getExpensesForCurrentMonthGroupByCategory() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getExpenseForCurrentMonth(currentMonthOfYear()), null);
        return buildExpenses(cursor);
    }

    public void addExpenseType(String type) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.TYPE, type);

        database.insert(ExpenseTypeTable.TABLE_NAME, null, values);
    }
    public List<Expense> getCurrentWeeksExpenses() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getConsolidatedExpensesForDates(getCurrentWeeksDates()), null);
        return buildExpenses(cursor);
    }

    public List<Expense> getTodaysExpenses() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getExpensesForDate(getCurrentDate()), null);

        return buildExpenses(cursor);
    }

    private boolean isCursorPopulated(Cursor cursor) {
        return cursor != null && cursor.moveToFirst();
    }

    private List<Expense> buildExpenses(Cursor cursor) {
        List<Expense> expenses = new ArrayList<>();
        if(isCursorPopulated(cursor)){
            do {
                String type = cursor.getString(cursor.getColumnIndex(TYPE));
                String amount = cursor.getString(cursor.getColumnIndex(AMOUNT));
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                String id = cursor.getString(cursor.getColumnIndex(_ID));

                Expense expense = id == null ? new Expense(parseLong(amount), type, date) : new Expense(parseInt(id), parseLong(amount), type, date);
                expenses.add(expense);
            } while(cursor.moveToNext());
        }

        return expenses;
    }

    private void seedExpenseTypes() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<ExpenseType> expenseTypes = ExpenseTypeTable.seedData();
        for (ExpenseType expenseType : expenseTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ExpenseTypeTable.TYPE, expenseType.getType());

            database.insert(ExpenseTypeTable.TABLE_NAME, null, contentValues);
        }
    }
}

