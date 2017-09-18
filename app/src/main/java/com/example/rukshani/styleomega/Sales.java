package com.example.rukshani.styleomega;

import android.database.Cursor;

import java.sql.SQLException;

/**
 * Created by Rukshani on 22/08/2017.
 */

public class Sales {


    public String generateSalesID(DBHelper db) {
     String qry = "SELECT SalesID FROM sales";
     Cursor rs = db.getData(qry);
        if (rs.getCount()==0){
            String cid = "S1000";
            rs.close();
            return cid;
        }
        else{
            String query= "SELECT substr(SalesID,2,4)+1 AS SID FROM sales ORDER BY SalesID DESC LIMIT 1";
            Cursor rs1 = db.getData(query);
            if (rs1.moveToFirst()) {
                do {
                    String cid = "S" + rs1.getString(rs1.getColumnIndex("SID"));
                    return cid;

                } while (rs1.moveToNext());
            }
            rs.close();
            String cid = "S" + rs1.getString(rs1.getColumnIndex("SID"));
            rs1.close();
            return cid;
        }
    }

    public void updateStock(String itemID,String Size,int qty,DBHelper db) throws SQLException {

        String query1 = "UPDATE item_stock SET Quantity= Quantity - '"+qty+"' WHERE ItemID='"+itemID+"' " +
                "AND Size='"+Size+"'";
        db.executeSQL(query1);

    }



    public void newSale(String un,String salesID,double total,String itemid,String size,int qty,DBHelper db)
            throws SQLException{

     String query = "INSERT INTO sales(`SalesID`,`Username`,`Total`,`ItemID`,`Size`, `Quantity`) "
             + "VALUES('"+salesID+"','"+un+"','"+total+"','"+itemid+"','"+size+"','"+qty+"')";
     db.executeSQL(query);

   }

}
