package com.example.rukshani.styleomega;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccount extends Navigation{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        onCreateMethod();
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        TextView name = (TextView)findViewById(R.id.name);
        name.setText("Welcome "+username+",");

    }

    public void logout(View view){
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        session.removeUsername(username);
        Intent intent = new Intent(this,Login.class);
        Toast.makeText(this,"You have logged out of Style Omega",Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public void goToModifyAccount(View view){
        Intent intent = new Intent(this,ModifyAccount.class);
        startActivity(intent);
    }

    public void goToInbox(View view){
        Intent intent = new Intent(this,Inbox.class);
        startActivity(intent);
    }

    public void goToShoppingCart(View view){
        Intent intent = new Intent(this,ShoppingCart.class);
        startActivity(intent);
    }

    public void gotToResetPassword(View view){
        Intent intent = new Intent(this,ResetPassword.class);
        startActivity(intent);
    }
}
