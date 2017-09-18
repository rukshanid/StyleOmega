package com.example.rukshani.styleomega;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Rukshani on 09/08/2017.
 */

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


protected void onCreateMethod() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

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
    getMenuInflater().inflate(R.menu.home_page, menu);
    MenuItem searchItem = menu.findItem(R.id.search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setQueryHint("Search Style Omega...");
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
        @Override
        public boolean onQueryTextSubmit(String query) {
           Intent intent =new Intent(getApplicationContext(),SearchResults.class);
            intent.putExtra("SEARCH",query);
            startActivity(intent);
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    });
    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this,HomePage.class);
            startActivity(intent);
        } else if (id == R.id.nav_women) {
            Intent intent = new Intent(this,Catergories_Women.class);
            startActivity(intent);
        } else if (id == R.id.nav_men) {
            Intent intent = new Intent(this,catergories_men.class);
            startActivity(intent);
        } else if (id == R.id.nav_kids) {
            Intent intent = new Intent(this,catergories_kids.class);
            startActivity(intent);
        } else if (id == R.id.nav_myaccount) {
            SessionManager session = new SessionManager(this);
            String username = session.getusername();
            if(username==null || username==""){
                Intent intent =new Intent(this,Login.class);
                startActivity(intent);
            }else{
                Intent intent =new Intent(this,MyAccount.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_contactus) {
            Intent intent =new Intent(this,ContactUs.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToShoppingCart(View view){
        Intent intent = new Intent(this,ShoppingCart.class);
        startActivity(intent);
    }

    public void goToMyAccount(View view){
        SessionManager session = new SessionManager(this);
        String username = session.getusername();

        if(username=="" || username==null){
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), MyAccount.class);
            startActivity(intent);
        }
    }

    public static final String VALUE = "com.example.rukshani.value";
    public static final String CATEGORY= "com.example.rukshani.category";

public void goToDisplayItems(View view){
    Intent intent =new Intent(this,DisplayItems.class);
    String value=""; String category="";
    if(view.getId()==R.id.womenSkirts){
        value="Skirts";
        category="Women";
    }
    if(view.getId()==R.id.womenbags){
    value="Bags";
        category="Women";
    }
    if(view.getId()==R.id.womenBlouses){
    value="Tops";
        category="Women";
    }
        if(view.getId()==R.id.womenDress){
        value="Dresses";
            category="Women";
    }
        if(view.getId()==R.id.womenJeans){
        value="Jeans";
            category="Women";
    }
        if(view.getId()==R.id.womenAcc){
        value="Accessories";
            category="Women";
    }
        if(view.getId()==R.id.womenshoes){
        value="Shoes";
            category="Women";
    }
        if(view.getId()==R.id.womenTee){
        value="T-shirts";
            category="Women";
    }
    if(view.getId()==R.id.boys){
        value="Boys";
        category="Kids";
    }
    if(view.getId()==R.id.girls){
        value="Girls";
        category="Kids";
    }
    if(view.getId()==R.id.school){
        value="SchoolSupplies";
        category="Kids";
    }
    if(view.getId()==R.id.menShirts){
        value="Shirts";
        category="Men";
    }
    if(view.getId()==R.id.formalWear){
        value="FormalWear";
        category="Men";
    }
    if(view.getId()==R.id.menpants){
        value="Pants";
        category="Men";
    }
    if(view.getId()==R.id.menAccessory){
        value="Accessories";
        category="Men";
    }
    if(view.getId()==R.id.menshoes){
        value="Shoes";
        category="Men";
    }
    if(view.getId()==R.id.menTeeShirt){
        value="T-shirts";
        category="Men";
    }
        intent.putExtra(VALUE,value);
        intent.putExtra(CATEGORY,category);
    startActivity(intent);

}

    public void goToCategory(View view){
        Intent intent=null;
        if(view.getId()==R.id.womenHomepage){
            intent = new Intent(getApplicationContext(),Catergories_Women.class);
        }
        if(view.getId()==R.id.girlsHomePage || view.getId()==R.id.boysHomepage ){
            intent = new Intent(getApplicationContext(),catergories_kids.class);
        }
        if(view.getId()==R.id.menHomepage){
            intent = new Intent(getApplicationContext(),catergories_men.class);
        }

        startActivity(intent);

    }

}
