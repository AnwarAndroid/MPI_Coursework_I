package com.example.ireda;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btn_register, _btn_login, bbtn;
    EditText _name, _email, _password, _phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openHelper = new DatabaseHelper(this);

        _btn_register = (Button) findViewById(R.id.btn_register);
        _btn_login = (Button) findViewById(R.id.btn_login);
        _name = (EditText) findViewById(R.id.name);
        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.password);
        _phone = (EditText) findViewById(R.id.phone);


        _btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });

        _btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    db = openHelper.getWritableDatabase();
                    String Rname = _name.getText().toString();
                    String Remail = _email.getText().toString();
                    String Rpassword = _password.getText().toString();
                    String Rphone = _phone.getText().toString();
                    insertdata(Rname, Remail, Rpassword, Rphone);
                    Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_LONG).show();

                }
            }

            public boolean validate(){
                if(_name.getText().toString().trim().length() <=0){
                    Toast.makeText(MainActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    return true;
                }else if (_email.getText().toString().trim().length() <=0){
                    Toast.makeText(MainActivity.this, "Please Enter email", Toast.LENGTH_SHORT).show();
                    return true;
                }else if (_password.getText().toString().trim().length() <=0){
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (_phone.getText().toString().trim().length() <=0){
                    Toast.makeText(MainActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

    }
    public void insertdata(String Rname, String Remail, String Rpassword, String Rphone){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, Rname);
        contentValues.put(DatabaseHelper.COL_3, Remail);
        contentValues.put(DatabaseHelper.COL_4, Rpassword);
        contentValues.put(DatabaseHelper.COL_5, Rphone);
        long id = db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);

    }
}
