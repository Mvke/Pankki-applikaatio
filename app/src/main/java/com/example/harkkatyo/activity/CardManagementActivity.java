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
import com.example.harkkatyo.CreditCard;
import com.example.harkkatyo.DatabaseHelper;
import com.example.harkkatyo.R;

import java.util.ArrayList;

public class CardManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private EditText cardname;
    private EditText buylimit;
    private EditText credit;
    private int idnumacc;
    private int idnumcard;
    Context context = null;
    private Spinner spinneraccount;
    private Spinner spinnercard;
    DatabaseHelper databaseHelper;
    ArrayAdapter<BankAccount> adapter;
    ArrayAdapter<CreditCard> adaptercard;
    Account account;
    ArrayList <BankAccount> arrayListAccount;
    ArrayList <CreditCard> arrayListCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_management);
        cardname = findViewById(R.id.editText15);
        buylimit = findViewById(R.id.editText20);
        credit = findViewById(R.id.editText27);
        spinneraccount = findViewById(R.id.spinner3);
        spinnercard = findViewById(R.id.spinner5);
        Account acc = (Account) getIntent().getSerializableExtra("user");
        context = CardManagementActivity.this;
        spinneraccount.setOnItemSelectedListener(this);
        spinnercard.setOnItemSelectedListener(this);
        account = new Account(acc.getBankid(), acc.getUsername(), acc.getPassword(), acc.getFirstname(), acc.getLastname(), acc.getAge());
        databaseHelper = new DatabaseHelper(this);
        arrayListAccount = new ArrayList<>();
        arrayListCard = new ArrayList<>();
        spinnerForAccount();


    }
    //This method gets information from the database and adds bankaccounts to spinner.
    public void spinnerForAccount(){
        Cursor cursor1 = databaseHelper.currentaccountData(account.getUsername());
        while(cursor1.moveToNext()){
            BankAccount bankAccount = new BankAccount(cursor1.getString(2), cursor1.getFloat(3), cursor1.getInt(4), cursor1.getString(1));
            arrayListAccount.add(bankAccount);
        }
        adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayListAccount);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinneraccount.setAdapter(adapter);
    }
    //This method gets information from the database and adds bank cards to spinner.
    public void spinnerForCard(){
        arrayListCard.clear();
        cardname.setText("");
        buylimit.setText("");
        credit.setText("");
        BankAccount bankAccount = arrayListAccount.get(idnumacc);
        Cursor cursor2 = databaseHelper.creditcarddata(bankAccount.getAccountnumber());
        while(cursor2.moveToNext()){
            CreditCard creditCard = new CreditCard(cursor2.getString(1), cursor2.getString(0), bankAccount.getAccountnumber(), cursor2.getInt(3), cursor2.getInt(4));
            arrayListCard.add(creditCard);
        }
        adaptercard = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayListCard);
        adaptercard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercard.setAdapter(adaptercard);

    }



    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //This switch case checks position in each spinner.
        switch(arg0.getId()){
            case R.id.spinner3:
                idnumacc = position;
                //When account is known cards will appear
                spinnerForCard();

                break;
            case R.id.spinner5:
                idnumcard = position;
                texts();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }
    //This method sets texts for the texts boxes
    public void texts() {
        CreditCard creditCard = arrayListCard.get(idnumcard);
        cardname.setText(creditCard.getCardname());
        String limitString = String.valueOf(creditCard.getBuylimit());
        buylimit.setText(limitString);
        String ammountString = String.valueOf(creditCard.getCredit());
        credit.setText(ammountString);
    }

    public void buttonUpdate(View view){
        CreditCard creditCard = arrayListCard.get(idnumcard);
        String cardnamestr = cardname.getText().toString();
        String buylimitstr = buylimit.getText().toString();
        int buylimitint = Integer.parseInt(buylimitstr);
        String creditstr = credit.getText().toString();
        int creditint = Integer.parseInt(creditstr);
        CreditCard crca = new CreditCard(cardnamestr, creditCard.getCardnumber(), creditCard.getAccountnumber(), buylimitint, creditint);
        databaseHelper.updateCreditCard(creditCard.getCardnumber(),creditCard.getAccountnumber(),buylimitint,creditint,cardnamestr);
        arrayListCard.set(idnumcard, crca);
        adaptercard.notifyDataSetChanged();


    }

}
