package com.andro.obe.trackmywish;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    private final String TAG = "AddItemActivity";
    private final int PLACE_PICKER_REQUEST = 1;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Item selectedItem;

    private TextView placeNameText;
    private TextView plaseAddressText;
    private EditText itemNameEditText;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .setCountry("IL")
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        publishUi();
        selectedItem = null;
        setSelectedItem(selectedItem);
    }
    void setSelectedItem(Item item){
        if(item !=null){
            selectedItem = item;
            btnAddItem.setVisibility(View.VISIBLE);
            itemNameEditText.setVisibility(View.VISIBLE);
            placeNameText.setText(item.getmPlace().getPlaceName());
            plaseAddressText.setText(item.getmPlace().getPlaseAddress());
        }
        else{
            placeNameText.setText("");
            plaseAddressText.setText("");
            btnAddItem.setVisibility(View.GONE);
            itemNameEditText.setVisibility(View.GONE);

        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = itemNameEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            itemNameEditText.setError("Required.");
            valid = false;
        } else {
            itemNameEditText.setError(null);
        }
        return valid;
    }
    private void updateUserItemList(List<Item> items){
        mDatabase.child("users").child(mAuth.getCurrentUser().
                getUid()).child("items").setValue(items);
    }
    private void publishUi(){
        placeNameText = (TextView)findViewById(R.id.placeNameText);
        plaseAddressText = (TextView)findViewById(R.id.plaseAddressText);
        itemNameEditText = (EditText)findViewById(R.id.itemNameEditText);

        findViewById(R.id.btnPlacePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(AddItemActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAddItem = (Button)findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    List<Item> items = MainActivity.getItemsList();
                    selectedItem.setItemName(itemNameEditText.getText().toString());
                    items.add(selectedItem);
                    updateUserItemList(items);
                    finish();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                WishPlace wishPlace = new WishPlace(place);
                selectedItem = new Item("",wishPlace);
                setSelectedItem(selectedItem);
            }
        }
    }

}
