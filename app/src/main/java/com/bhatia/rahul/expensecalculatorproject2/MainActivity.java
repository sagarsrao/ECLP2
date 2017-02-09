package com.bhatia.rahul.expensecalculatorproject2;

import android.app.Dialog;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.Toast;


import com.bhatia.rahul.expensecalculatorproject2.database.DBhandler;
import com.bhatia.rahul.expensecalculatorproject2.fragment.Month_Expense;
import com.bhatia.rahul.expensecalculatorproject2.fragment.Today_Expense;
import com.bhatia.rahul.expensecalculatorproject2.fragment.Week_Expense;

import com.bhatia.rahul.expensecalculatorproject2.utils.DateUtil;


import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DBhandler dBhandler;
    List<String> itemlist;
    DateUtil dateUtil;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dBhandler = new DBhandler(getApplicationContext());
        dateUtil = new DateUtil();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            addItemList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_camera) {
            fragment = new Today_Expense();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            fragment = new Week_Expense();
        } else if (id == R.id.nav_slideshow) {
            fragment = new Month_Expense();
        }  else if (id == R.id.nav_share) {
            dBhandler.deleteAll();
            Toast.makeText(getApplicationContext(),"Sucssefully Deleted",Toast.LENGTH_SHORT).show();
            fragment = new Today_Expense();
        } else if (id == R.id.nav_send) {
            fragment = new Today_Expense();
        }
        //getting fragment support
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //replace each fragment
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void addItemList(){
// Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.new_category);

        final EditText category = (EditText) dialog.findViewById(R.id.category);
        final Button add_category = (Button) dialog.findViewById(R.id.add_category);

        dialog.show();

        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(category.getText().toString().length() > 0){
                    dBhandler.addExpenseType(category.getText().toString());
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Sucssefully Added",Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    Today_Expense today_expense = new Today_Expense();
                    transaction.replace(R.id.fragment, today_expense);
                    transaction.commit();
                }
                else {
                    category.setError((getApplicationContext().getString(R.string.amount_empty_error)));
                }
            }
        });


    }

    private void  addExpense(){
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.new_expense);
        // Set dialog title
        dialog.setTitle("Add New Expense");
        final Spinner spinner = (Spinner)dialog.findViewById(R.id.expense_type);
        itemlist = dBhandler.getExpenseTypes();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, itemlist);
        spinner.setAdapter(dataAdapter);
        final EditText amount = (EditText) dialog.findViewById(R.id.amount);
        final Button addexpense = (Button) dialog.findViewById(R.id.add_expense);

        dialog.show();

        addexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                                if(amount.getText().toString().length() > 0){
                      dBhandler.addExpense(Integer.parseInt(amount.getText().toString()),spinner.getSelectedItem().toString(),dateUtil.getCurrentDate());

                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Sucssefully Added",Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    Today_Expense today_expense = new Today_Expense();
                    transaction.replace(R.id.fragment, today_expense);
                    transaction.commit();
                                    }
                else {
                    amount.setError((getApplicationContext().getString(R.string.amount_empty_error)));
                }

            }
        });

    }

}
