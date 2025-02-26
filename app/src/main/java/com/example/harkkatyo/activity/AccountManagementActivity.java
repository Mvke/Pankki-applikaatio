package com.example.harkkatyo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.harkkatyo.Account;
import com.example.harkkatyo.BankAccount;
import com.example.harkkatyo.DatabaseHelper;
import com.example.harkkatyo.R;

import java.util.ArrayList;

public class AccountManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList <BankAccount> arrayList;
    private Spinner spinner;
    private EditText accountname;
    private EditText limit;
    private EditText ammount;
    private int idnum;
    Context context = null;
    Account account;
    DatabaseHelper databaseHelper;
    ArrayAdapter<BankAccount> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        arrayList = new ArrayList<>();
        accountname = findViewById(R.id.editText14);
        limit = findViewById(R.id.editText26);
        spinner = findViewById(R.id.spinner2);
        ammount = findViewById(R.id.editText23);
        context = AccountManagementActivity.this;
        spinner.setOnItemSelectedListener(this);
        Account acc = (Account) getIntent().getSerializableExtra("user");
        account = new Account(acc.getBankid(), acc.getUsername(), acc.getPassword(), acc.getFirstname(), acc.getLastname(), acc.getAge());
        databaseHelper = new DatabaseHelper(this);
        spinneri();
        texts();

    }
    //This method sets texts for the texts boxes
    public void texts() {
        BankAccount bA = arrayList.get(idnum);
        accountname.setText(bA.getAccountname());
        String limitString = String.valueOf(bA.getPaylimit());
        limit.setText(limitString);
        String ammountString = String.valueOf(bA.getAmmount());
        ammount.setText(ammountString);
    }



    //This method gets information from the database and adds bankaccounts to spinner.
    public void spinneri(){
        Cursor cursor1 = databaseHelper.currentaccountData(account.getUsername());
        while(cursor1.moveToNext()){
            System.out.println(cursor1.getInt(4));
            BankAccount bankAccount = new BankAccount(cursor1.getString(2), cursor1.getFloat(3), cursor1.getInt(4), cursor1.getString(1));
            arrayList.add(bankAccount);
        }

        adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        idnum = position;
        texts();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }

    //This method works when buttons is pressed. It updates the information to database and to the spinner.
    public void buttonUpdate(View v){
        if(accountname.getText().toString().isEmpty()){

        }
        else if(limit.getText().toString().isEmpty()){

        }

        else if(ammount.getText().toString().isEmpty()){
            ammount.setError("Insert ammount (Insert 0 if you dont want add money))");
        }
        else{
            BankAccount bA = arrayList.get(idnum);
            String paylimit = limit.getText().toString();
            int paylimitint = Integer.parseInt(paylimit);
            String ammountstring = ammount.getText().toString();
            float ammountotal = bA.getAmmount() + Float.parseFloat(ammountstring);
            BankAccount bankAccount = new BankAccount(accountname.getText().toString(),ammountotal,paylimitint, bA.getAccountnumber());
            databaseHelper.updateCurrentAccount(account.getUsername(), accountname.getText().toString(), ammountotal,paylimitint, bankAccount.getAccountnumber());
            arrayList.set(idnum, bankAccount);
            adapter.notifyDataSetChanged();
            texts();

        }
    }


}







