package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Login extends Navigation {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onCreateMethod();
    }

    public void login(View view) throws IOException {
        Intent intent = new Intent(this, MyAccount.class);
        EditText username = (EditText) findViewById(R.id.username);
        String un = username.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        String pwd = password.getText().toString();
        DBHelper db = new DBHelper(this);
        String sql ="SELECT * FROM users WHERE Username='"+un+"' AND Pasword='"+pwd+"'";
        Cursor rs = db.getData(sql);
        if (rs.moveToFirst()){
            do{
                SessionManager session = new SessionManager(this);
                session.setusername(un);
                startActivity(intent);
            }while(rs.moveToNext());
        }else if(un.equals("")){
            username.setError("Username is required");
        }
        else if(pwd.equals("")){
           password.setError("Password is required");
        }
        else{
            Toast.makeText(this,"Incorrect Authentication Details",Toast.LENGTH_LONG).show();
        }
        rs.close();
    }

    public void goToRegister(View view){
        Intent intent  = new Intent(this,Register.class);
        startActivity(intent);
    }
    public void goToForgotPassword(View view){
        Intent intent  = new Intent(this,ForgotPassword.class);
        startActivity(intent);
    }
}
