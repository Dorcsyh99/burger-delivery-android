package com.example.burgerdelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class MenuListActivity extends AppCompatActivity {
    private static final String LOG_TAG = MenuListActivity.class.getName();
    private static final String PREF_KEY = MenuListActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private SharedPreferences preferences;

    private FirebaseUser user;
    private FirebaseAuth auth;

    public FrameLayout blueCircle;
    public TextView contentTextView;
    public int cartItems = 0;

    private boolean viewRoot = true;

    private RecyclerView recycleView;
    private ArrayList<MenuItem> itemList;
    private MenuItemAdapter adapter;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    MenuItem navSearch;
    MenuItem navCart;
    MenuItem navOptions;
    MenuItem navLogOut;

    private int gridNumber = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i(LOG_TAG, "Bejelentkezett felhasználó");
        } else {
            Log.i(LOG_TAG, "Nem bejelentkezett felhasználó!");
        }

        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("foods");
        queryData();

        adapter = new MenuItemAdapter(this, itemList);

        recycleView.setAdapter(adapter);
    }




    private void queryData(){
        itemList.clear();
        items.orderBy("price", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                MenuItem food = document.toObject(MenuItem.class);
                Log.i("food: ", food.getName());
                food.setId(document.getId());
                itemList.add(food);
            }
            if (itemList.size() == 0){
                queryData();
            }

            adapter.notifyDataSetChanged();
        });
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.burger_navbar, menu);
        /*android.view.MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i(LOG_TAG, s);
                adapter.getFilter().filter(s);
                return false;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item){
        switch (item.getItemId()){
            case R.id.logOut:
                Log.d(LOG_TAG, "Logout clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.search_bar:
                Log.d(LOG_TAG, "Search clicked");
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "Cart clicked");
                return true;
            case R.id.options:
                Log.d(LOG_TAG, "Options clicked");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        final android.view.MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        blueCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(){
        cartItems = (cartItems + 1);
        if(0 < cartItems){
            contentTextView.setText(String.valueOf(cartItems));
        }else{
            contentTextView.setText("");
        }
        blueCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.auth.signOut();

    }


}