package com.example.panerorenn9541.mycontactapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editName;
    EditText editNumber;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editNumber = findViewById(R.id.editText_number);
        editAddress = findViewById(R.id.editText_address);

        myDB = new DatabaseHelper( this);
        Log.d("MyContactApp", "MainActivity: instantiated Databasehelper");
    }

    public void addData(View view)
    {
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDB.insertData(editName.getText().toString(), editNumber.getText().toString(), editAddress.getText().toString());

        if(isInserted == true) {
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view)
    {
        Cursor res = myDB.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor" + res.getCount());
        if(res.getCount() == 0)
        {
            showMessage("Error", "No data found in database");
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            //append res column 0, 1, 2, 3 to the buffer
            buffer.append(res.getString(0));
            buffer.append(" ");
            buffer.append(res.getString(1));
            buffer.append(" ");
            buffer.append(res.getString(2));
            buffer.append(" ");
            buffer.append(res.getString(3));
            buffer.append("\n");
        }
        Log.d("MyContactApp", "MainActivity: viewData: assembled stringbuffer");
        showMessage("data", buffer.toString());
    }

    private void showMessage(String title, String message)
    {
        Log.d("MyContactApp" ,"MainActivity: showMessage: building alert dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public static final String EXTRA_MESSAGE = "com.example.panerorenn9541.mycontactapp.MESSAGE";
    public void SearchRecord(View view)
    {
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Intent intent = new Intent(this, SearchActivity.class);
        Cursor res = myDB.getAllData();
        Log.d("MyContactApp", "MainActivity: searchRecord: received cursor" + res.getCount());
        if(res.getCount() == 0)
        {
            showMessage("Error", "No data found in database");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            if (res.getString(1).equals(editName.getText().toString()))
            {
                buffer.append("Contact: " + res.getString(0) + "\n" + "Name: " + res.getString(1) + "\n" + "Address: " + res.getString(3) + "\n" + "Phone Number: " + res.getString(2) + "\n\n");
            }

        }

        if(buffer.length() == 0)
        {
            buffer.append("Nobody with the name " + editName.getText().toString() + "\nfound in the database");
        }
        intent.putExtra(EXTRA_MESSAGE, buffer.toString());
        startActivity(intent);

    }


}