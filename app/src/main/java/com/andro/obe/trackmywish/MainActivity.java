package com.andro.obe.trackmywish;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private static List<Item> myItems = new ArrayList<>();
    ArrayAdapter<Item> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        adapter = new MyListAdapter();
        ListView list = (ListView)findViewById(R.id.itemListView);
        list.setAdapter(adapter);
        registerClickCallback();
        Button addB = (Button)findViewById(R.id.bAddItem);
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateItemList();
    }

    public static List<Item> getItemsList(){
        return myItems;
    }

    private void registerClickCallback() {
        ListView list =(ListView)findViewById(R.id.itemListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ItemActivity.class);
                intent.putExtra("item-position",position);
                startActivity(intent);
            }
        });
    }

    private void populateItemList() {
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child("users").child(user.
                getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                myItems.clear();
                myItems.addAll(mUser.getItems());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addItemDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Random rand = new Random();
                Item newItem = new Item(input.getText().toString(),rand.nextInt(100) + 1,rand.nextInt(100) + 1);
                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                myItems.add(newItem);
                //saveItemToFire(newItem);
                updateUserItemList(myItems);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
            distanceText.setText(Double.toString(currentItem.getLatitude()));
            return itemView;
        }
    }
}
