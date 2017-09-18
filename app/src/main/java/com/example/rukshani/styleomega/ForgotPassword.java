package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ForgotPassword extends Navigation{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        onCreateMethod();
    }

    public void submitEmail(View view) throws IOException {
        Intent intent = new Intent(this, Login.class);
        EditText eMail = (EditText) findViewById(R.id.email);
        String email = eMail.getText().toString();

        DBHelper db = new DBHelper(this);
        String sql ="SELECT * FROM users WHERE Email='"+email+"'";
        Cursor rs = db.getData(sql);
        if (rs.moveToFirst()){
            do{
                String messageBody="Dear "+rs.getString(2) +",\nYour Username and Password is: \n" + "Username = "+
                        rs.getString(0) + "\nPassword = " + rs.getString(1)+"\nRegards,\nStyle Omega";
                EmailSender emailSender = new EmailSender(this,messageBody,"Style Omega",rs.getString(6));
                emailSender.execute();
                Toast.makeText(this,"The authentication details are sent to your email",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }while(rs.moveToNext());

        }else if(email.equals("")){
            eMail.setError("Email is required");
        }else if(!(email.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}"))) {
            eMail.setError("Email Format is invalid");
        }
        else{
            eMail.setError("Email does not exist, Try another Email address!");
        }
        rs.close();
    }
}
