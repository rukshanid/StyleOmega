package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Register extends Navigation{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onCreateMethod();
    }
    public void register(View view) throws IOException {
        Intent intent = new Intent(this, MyAccount.class);
        EditText username = (EditText) findViewById(R.id.username);
        String un = username.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        String pwd = password.getText().toString();
        EditText firstname = (EditText) findViewById(R.id.firstName);
        String fn = firstname.getText().toString();

        EditText lastname = (EditText) findViewById(R.id.lastName);
        String ln = lastname.getText().toString();

        EditText contactNum= (EditText) findViewById(R.id.contactNumber);
        String cn = contactNum.getText().toString();
        EditText address= (EditText) findViewById(R.id.address);
        String add = address.getText().toString();
        EditText email = (EditText) findViewById(R.id.email);
        String eMail = email.getText().toString();
        EditText reenter = (EditText) findViewById(R.id.password2);
        String pwd2 = reenter.getText().toString();

        DBHelper db = new DBHelper(this);
        String sql ="SELECT * FROM users WHERE Username='"+un+"'";
        String sql1 ="SELECT * FROM users WHERE Email='"+eMail+"'";
        Cursor rs = db.getData(sql);
        Cursor rs1 = db.getData(sql1);
        if (rs.moveToFirst()){
            do{
                username.setError("Username already exists");
            }while(rs.moveToNext());
        }else if(rs1.moveToFirst()){
            do{
                email.setError("Email already exists");
            }while(rs.moveToNext());
        }
        else if(un.equals("")){
            username.setError("Username is required");
        }else if(pwd.equals("")) {
            password.setError("Password is required");
        }else if(pwd2.equals("")){
            reenter.setError("Re enter the password");
        }else if(fn.equals("")) {
            firstname.setError("First name is required");
        } else if(ln.equals("")) {
            lastname.setError("Last name is required");
        }else if(eMail.equals("")){
            email.setError("Email is required");
        }else if(add.equals("")) {
            address.setError("Address is required");
        }else if(!pwd2.equals(pwd)){
            reenter.setError("Password does not match");
        }else if(!(eMail.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}"))) {
            email.setError("Email Format is invalid");        }
        else
         {
            String query2 ="INSERT INTO users (Username,Pasword,FirstName,LastName,ContactNumber,Address,Email) \n" +
                    "VALUES ('"+un+"','"+pwd+"','"+fn+"','"+ln+"','"+cn+"','"+add+"','"+eMail+"')";
            db.executeSQL(query2);
             EmailSender emailSender = new EmailSender(this,"Dear " + fn +",\n\nYou have successfully registered with Style Omega. \n" + "\n Thank you,"
                     + "\nRegards, "+"\nStyle Omega","Style Omega - Registration",eMail);
             emailSender.execute();
            Toast.makeText(this,"Successfully Registered with Style Omega!",Toast.LENGTH_LONG).show();

            Intent intent1 = new Intent(this,Login.class);
            startActivity(intent1);

        }
        rs.close();


    }
}
