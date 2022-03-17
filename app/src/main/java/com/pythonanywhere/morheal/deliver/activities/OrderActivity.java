package com.pythonanywhere.morheal.deliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pythonanywhere.morheal.deliver.R;
import com.pythonanywhere.morheal.deliver.activities.statics.ActionActivity;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class OrderActivity extends AppCompatActivity {

    private static SimpleDateFormat DATE_PATTERN;
    private static SimpleDateFormat TIME_PATTERN;
    private static int requestCode;

    private OrderService orderService;
    private Order order;

    private EditText et_name;
    private EditText et_date;
    private EditText et_time;

    private TextView tv_address;
    private TextView tv_product;
    private TextView tv_totalPrice;

    private Switch sw_prepaided;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        requestCode = getIntent().getIntExtra("requestCode", -1);
        TextView activity_title = (TextView) findViewById(R.id.activity_title);

        DATE_PATTERN = new SimpleDateFormat("dd/MM/yyyy");
        TIME_PATTERN = new SimpleDateFormat("hh:mm:ss");

        orderService = MainActivity.orderService;

        et_name = (EditText) findViewById(R.id.et_order_name);
        et_date = (EditText) findViewById(R.id.et_order_date);
        et_time = (EditText) findViewById(R.id.et_order_time);

        tv_address = (TextView) findViewById(R.id.tv_order_address);
        tv_product = (TextView) findViewById(R.id.tv_order_product);
        tv_totalPrice = (TextView) findViewById(R.id.tv_order_totalPrice);

        sw_prepaided = (Switch) findViewById(R.id.sw_paided);

        setButtonsClick();

        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setTextIsSelectable(true);
        et_time.setInputType(InputType.TYPE_NULL);
        et_time.setTextIsSelectable(true);

        if (savedInstanceState != null) {
            et_name.setText(savedInstanceState.getString("name"));
            et_date.setText(savedInstanceState.getString("date"));
            et_time.setText(savedInstanceState.getString("time"));
        }
        switch (requestCode) {
            case MainActivity.CREATE:
                activity_title.setText(R.string.creating);
                order = new Order();
                order.setName(getString(R.string.order) + " #" + order.getOrderID());
                orderService.addOrder(order);
                Date currentTime = Calendar.getInstance().getTime();
                order.setOrderDateTime(currentTime);
                break;
            case MainActivity.CONTEXT_ITEM_EDIT:
                activity_title.setText(R.string.editing);
                ((Button)findViewById(R.id.btn_order_createOrder)).setText(getString(R.string.save));
                order = orderService.findOrderById(getIntent().getLongExtra("id", -1));
                break;
            case MainActivity.CONTEXT_ITEM_LOOK:
                activity_title.setText(R.string.watching);
                disableEditing();
                order = orderService.findOrderById(getIntent().getLongExtra("id", -1));
                break;
            default:
                finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("name", et_name.getText().toString());
        outState.putString("date", et_date.getText().toString());
        outState.putString("time", et_time.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void setButtonsClick() {
        findViewById(R.id.btn_order_editAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionActivity.ADDRESS);
                intent.putExtra("id", order.getOrderID());
                intent.putExtra("requestCode", requestCode);
                startActivityForResult(intent, requestCode);
            }
        });

        findViewById(R.id.btn_order_addProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                intent.putExtra("id", order.getOrderID());
                intent.putExtra("requestCode", requestCode);
                startActivityForResult(intent,requestCode);
            }
        });

        findViewById(R.id.btn_order_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btn_order_createOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderClick(v);
            }
        });
    }

    private void disableEditing() {
        et_name.setInputType(InputType.TYPE_NULL);
        et_name.setTextIsSelectable(true);
        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setTextIsSelectable(true);
        et_time.setInputType(InputType.TYPE_NULL);
        et_time.setTextIsSelectable(true);

        findViewById(R.id.btn_order_editAddress).setVisibility(View.GONE);
        findViewById(R.id.btn_order_createOrder).setVisibility(View.GONE);
        findViewById(R.id.btn_order_addProduct).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MainActivity.CREATE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getBaseContext(), getString(R.string.added), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), getString(R.string.canceled), Toast.LENGTH_LONG).show();
                }
                break;
            case MainActivity.CONTEXT_ITEM_EDIT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getBaseContext(), getString(R.string.edited), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), getString(R.string.canceled), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrderFields();
    }

    @Override
    public void onBackPressed() {
        if (requestCode == MainActivity.CREATE) {
            orderService.deleteOrder(order.getOrderID());
        }
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    private void createOrderClick(View v) {
        RebuildOrder();
        orderService.editOrder(order);
        setResult(RESULT_OK);
        finish();
    }

    private void setOrderFields() {
        et_name.setText(order.getName());
        Date date = order.getOrderDateTime();
        et_date.setText(DATE_PATTERN.format(date));
        et_time.setText(TIME_PATTERN.format(date));
        tv_address.setText(order.getAddress().toString());
        tv_totalPrice.setText(String.format("%.2f %s", order.getProductPrice(), getString(R.string.valuta_uah)));
        tv_product.setText(String.valueOf(order.getProduct()));
        sw_prepaided.setChecked(order.getPaided());
    }

    private void RebuildOrder() {
        order.setName(et_name.getText().toString());
        order.setPaided(sw_prepaided.isChecked());
    }
}