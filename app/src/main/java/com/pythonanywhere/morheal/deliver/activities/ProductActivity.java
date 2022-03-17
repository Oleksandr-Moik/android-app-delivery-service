package com.pythonanywhere.morheal.deliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pythonanywhere.morheal.deliver.R;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.models.address.FlatAddress;
import com.pythonanywhere.morheal.deliver.models.address.OfficeAddress;
import com.pythonanywhere.morheal.deliver.models.address.PrivateBuildAddress;
import com.pythonanywhere.morheal.deliver.models.product.LiquidProduct;
import com.pythonanywhere.morheal.deliver.models.product.Product;
import com.pythonanywhere.morheal.deliver.models.product.SolidMatterProduct;
import com.pythonanywhere.morheal.deliver.service.OrderService;

import java.util.ArrayList;


public class ProductActivity extends AppCompatActivity {

    private static final int CHOISER_ACTIVITY = 1;
    private static final int EDITOR_ACTIVITY = 2;
    private static final int WATCHING_ACTIVITY = 3;
    private static final String SIZE_PATTERN = "%.2f-%.2f-%.2f";
    private static int requestCode;
    private int currentActivity;
    private boolean canGoBackToChoiseType;

    private EditText et_name;
    private EditText et_manufacture;
    private EditText et_price;
    private EditText et_size;
    private EditText et_capacity;

    private TableRow size_row;
    private TableRow capacity_row;

    private LinearLayout layout_choiser;
    private LinearLayout layout_editor;

    private Product product;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initComponents();
        setClickListeners();

        TextView activity_title = (TextView) findViewById(R.id.activity_title);

        Intent intent = getIntent();
        order = MainActivity.orderService.findOrderById(intent.getIntExtra("id", -1));
        requestCode = intent.getIntExtra("requestCode", -1);

        switch (requestCode) {
            case MainActivity.CREATE:
                canGoBackToChoiseType = true;
                activity_title.setText(getString(R.string.creating));
                activateLayout(CHOISER_ACTIVITY);
                product = order.getProduct();
                break;
            case MainActivity.CONTEXT_ITEM_EDIT:
                canGoBackToChoiseType = false;
                activity_title.setText(getString(R.string.editing));
                activateLayout(EDITOR_ACTIVITY);
                product = order.getProduct();
                setProductFields();
                break;
        }

    }

    private void initComponents() {
        et_name = (EditText) findViewById(R.id.et_product_name);
        et_manufacture = (EditText) findViewById(R.id.et_manufacture);
        et_price = (EditText) findViewById(R.id.et_price);
        et_size = (EditText) findViewById(R.id.et_size);
        et_capacity = (EditText) findViewById(R.id.et_capacity);

        size_row = (TableRow) findViewById(R.id.size_row);
        capacity_row = (TableRow) findViewById(R.id.capacity_row);

        layout_choiser = (LinearLayout) findViewById(R.id.choise_product_layout);
        layout_editor = (LinearLayout) findViewById(R.id.edit_product_layout);
    }

    private void setClickListeners() {
        findViewById(R.id.btn_address_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validated()) {
                    rebuildProduct();
                    order.setProduct(product);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.not_valideted), Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.btn_product_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_product_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateLayout(EDITOR_ACTIVITY);
            }
        });
        findViewById(R.id.button_product_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.rb_solidMatter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAddressTypedFields(v);
            }
        });
        findViewById(R.id.rb_liqud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAddressTypedFields(v);
            }
        });


    }

    private void setProductFields() {
        if (product != null) {
           et_name .setText(product.getName());
           et_manufacture .setText(product.getManufacture());
           et_price .setText(String.valueOf(product.getPrice()));
            if (product.getClass() == SolidMatterProduct.class) {
                SolidMatterProduct solid = (SolidMatterProduct)product;
                et_size.setText(String.format(SIZE_PATTERN, solid.getWidth(),solid.getHeight(), solid.getLength()));
            } else if (product.getClass() == LiquidProduct.class) {
                et_capacity.setText(String.valueOf(((LiquidProduct) product).getVolume()));
            }
        }
    }

    private void activateAddressTypedFields(View view) {
        size_row.setVisibility(View.GONE);
        capacity_row.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.rb_solidMatter:
                product = new SolidMatterProduct();
                size_row.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_liqud:
                product = new LiquidProduct();
                capacity_row.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void activateLayout(int layout) {
        switch (layout) {
            case CHOISER_ACTIVITY:
                currentActivity = CHOISER_ACTIVITY;
                layout_choiser.setVisibility(View.VISIBLE);
                layout_editor.setVisibility(View.GONE);
                break;
            case EDITOR_ACTIVITY:
                currentActivity = EDITOR_ACTIVITY;
                layout_choiser.setVisibility(View.GONE);
                layout_editor.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean Validated() {
        try {
            if (et_name.getText().toString().length()<3) {
                return false;
            } else if (et_manufacture.getText().toString().length()<3) {
                return false;
            } else if (Double.parseDouble(et_price.getText().toString())<0.0) {
                return false;

            } else if (product.getClass() == LiquidProduct.class) {
                if (Double.parseDouble(et_capacity.getText().toString())<=0.0) {
                    return false;
                }
            } else if (product.getClass() == SolidMatterProduct.class) {
                String size[] = et_size.getText().toString().split("-");
                if (size.length!=3) {
                    return false;
                }else{
                    Double.parseDouble(size[0]);
                    Double.parseDouble(size[1]);
                    Double.parseDouble(size[2]);
                }
            }
            return true;
        }catch (NumberFormatException e) {
            Log.e("all", "Validated: " + e.getStackTrace().toString());
            return false;
        }
    }

    private void rebuildProduct() {
        product.setName(et_name.getText().toString());
        product.setManufacture(et_manufacture.getText().toString());
        product.setPrice(Double.parseDouble(et_price.getText().toString()));

        if (product.getClass() == SolidMatterProduct.class) {
            String size[] = et_size.getText().toString().split("-");
            ((SolidMatterProduct) product).setWidth(Double.parseDouble(size[0]));
            ((SolidMatterProduct) product).setHeight(Double.parseDouble(size[1]));
            ((SolidMatterProduct) product).setLength(Double.parseDouble(size[2]));
        } else if (product.getClass() == LiquidProduct.class) {
            ((LiquidProduct) product).setVolume(Double.parseDouble(et_capacity.getText().toString()));
        }
    }

    @Override
    public void onBackPressed() {
        if (currentActivity == EDITOR_ACTIVITY && canGoBackToChoiseType) {
            activateLayout(CHOISER_ACTIVITY);
        } else if (currentActivity == CHOISER_ACTIVITY) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
