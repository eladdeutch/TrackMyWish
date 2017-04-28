package com.andro.obe.trackmywish;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static List<Item> myItems = new ArrayList<>();
    ArrayAdapter<Item> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new MyListAdapter();
        ListView list = (ListView)findViewById(R.id.itemListView);
        list.setAdapter(adapter);
        populateItemList();
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
                Intent intent = new Intent(MainActivity.this,ItemActivity.class);
                intent.putExtra("item-position",position);
                startActivity(intent);
            }
        });
    }

    private void populateItemList() {
        myItems.add(new Item("Drill",20));
        myItems.add(new Item("Nozel",5));
        myItems.add(new Item("Flowers",55));
        myItems.add(new Item("Car",14));

    }

    private void addItemDialog()
    {
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

                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int randomNum = rand.nextInt(100) + 1;
                myItems.add(new Item(input.getText().toString(),randomNum));
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
            distanceText.setText(Double.toString(currentItem.getDistance()));
            return itemView;
        }
    }
}
