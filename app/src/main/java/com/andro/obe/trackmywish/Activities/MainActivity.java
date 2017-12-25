package com.andro.obe.trackmywish.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.andro.obe.trackmywish.Models.Item;
import com.andro.obe.trackmywish.R;
import com.andro.obe.trackmywish.Models.User;
import com.andro.obe.trackmywish.Utilities.SwipeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private SwipeDetector swipeDetector;

    private static List<Item> myItems = new ArrayList<>();
    ArrayAdapter<Item> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        swipeDetector = new SwipeDetector();
        adapter = new MyListAdapter();
        ListView list = (ListView)findViewById(R.id.itemListView);
        list.setAdapter(adapter);
        registerClickCallback();
        Button addB = (Button)findViewById(R.id.bAddItem);
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addItemDialog();
                Intent intent = new Intent(MainActivity.this,AddItemFlowActivity.class);
                startActivity(intent);
            }
        });
        populateItemList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static List<Item> getItemsList(){
        return myItems;
    }

    private void registerClickCallback() {
        ListView list =(ListView)findViewById(R.id.itemListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        view.findViewById(R.id.sDeleteBtn).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.sDeleteBtn).setVisibility(View.GONE);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                    intent.putExtra("item-position", position);
                    startActivity(intent);
                }
            }
        });

        list.setOnTouchListener(swipeDetector);
    }

    private void populateItemList() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child("users").child(user.
                getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                myItems.clear();
                myItems.addAll(mUser.getItems());
                adapter.notifyDataSetChanged();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserItemList(myItems);
    }

    private void updateUserItemList(List<Item> items){
        mDatabase.child("users").child(mAuth.getCurrentUser().
                getUid()).child("items").setValue(items);
    }

    private class MyListAdapter extends ArrayAdapter<Item> {
        public MyListAdapter(){
            super(MainActivity.this, R.layout.item_view, myItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_view,parent,false);
            //find the current gift
            Item currentItem = myItems.get(position);
            //Fill the view

            //Title
            TextView titleText = (TextView) itemView.findViewById(R.id.itemName);
            titleText.setText(currentItem.getItemName());

            //Distance
            TextView distanceText = (TextView) itemView.findViewById(R.id.itemDistance);
            //distanceText.setText("35");
            return itemView;
        }
    }
}
