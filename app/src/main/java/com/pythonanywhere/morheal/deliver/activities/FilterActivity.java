package com.pythonanywhere.morheal.deliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pythonanywhere.morheal.deliver.R;
import com.pythonanywhere.morheal.deliver.activities.statics.ActionActivity;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.models.address.Address;
import com.pythonanywhere.morheal.deliver.models.address.OfficeAddress;
import com.pythonanywhere.morheal.deliver.models.address.PrivateBuildAddress;
import com.pythonanywhere.morheal.deliver.service.OrderService;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    public static Address filteredAddress;

    private RadioButton off_radio;
    private RadioButton prepaid_radio;
    private RadioButton with_paid_radio;
    private RadioButton no_paid_radio;
    private RadioButton address_radio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        MainActivity.filterArray = new ArrayList<>(1);

        off_radio = (RadioButton)findViewById(R.id.rb_off);
        prepaid_radio = (RadioButton)findViewById(R.id.rb_prepaid);
        with_paid_radio = (RadioButton)findViewById(R.id.rb_with);
        no_paid_radio = (RadioButton)findViewById(R.id.rb_with_out);
        address_radio = (RadioButton)findViewById(R.id.rb_address);

        prepaid_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                with_paid_radio.setEnabled(isChecked);
                no_paid_radio.setEnabled(isChecked);
            }
        });
        address_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionActivity.ADDRESS);
                intent.putExtra("requestCode", MainActivity.FILTER);
                filteredAddress = new OfficeAddress();
                startActivityForResult(intent, MainActivity.FILTER);
            }
        });
        findViewById(R.id.button_filterApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (off_radio.isChecked()){
            setResult(RESULT_CANCELED);
            finish();
        }else if (prepaid_radio.isChecked()){
            MainActivity.filterArray = MainActivity.orderService.getPrepaidedOrders(with_paid_radio.isChecked());
            setResult(RESULT_OK);
            super.onBackPressed();
        }else{
            MainActivity.filterArray = MainActivity.orderService.findOrdersByAddress(filteredAddress);
            setResult(RESULT_OK);
            super.onBackPressed();
        }
    }
}