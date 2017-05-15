package com.andro.obe.trackmywish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        EditText titleText = (EditText) findViewById(R.id.editNameText);
        TextView distanceText = (TextView) findViewById(R.id.itemDistance);

        int position = getIntent().getIntExtra("item-position",-1);
        if (position != -1){
            final Item currentItem = MainActivity.getItemsList().get(position);
            //Title
            titleText.setText(currentItem.getItemName());

            //Distance
            distanceText.setText(Double.toString(currentItem.getLatitude()));

            Button savrB = (Button)findViewById(R.id.saveBtt);
            savrB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.getItemsList().remove(currentItem);
                    finish();
                }
            });
        }
    }
}
