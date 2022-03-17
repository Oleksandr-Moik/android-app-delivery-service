package com.pythonanywhere.morheal.deliver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pythonanywhere.morheal.deliver.R;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.models.address.Address;
import com.pythonanywhere.morheal.deliver.models.address.FlatAddress;
import com.pythonanywhere.morheal.deliver.models.address.OfficeAddress;
import com.pythonanywhere.morheal.deliver.models.address.PrivateBuildAddress;
import com.pythonanywhere.morheal.deliver.service.OrderService;


public class AddressActivity extends AppCompatActivity {

    private static final int CHOISER_ACTIVITY = 1;
    private static final int EDITOR_ACTIVITY = 2;
    private static int requestCode;
    private int currentActivity;
    private boolean canGoBackToChoiseType;

    private EditText et_state;
    private EditText et_city;
    private EditText et_street;
    private EditText et_buildNum;
    private EditText et_floor;
    private EditText et_number;

    private TableRow floor_row;
    private TableRow number_row;

    private LinearLayout layout_choiser;
    private LinearLayout layout_editor;

    private Address address;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

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
                address = order.getAddress();
                break;
            case MainActivity.CONTEXT_ITEM_EDIT:
                canGoBackToChoiseType = false;
                activity_title.setText(getString(R.string.editing));
                activateLayout(EDITOR_ACTIVITY);
                address = order.getAddress();
                setAddressFields();
                break;
            case MainActivity.FILTER:
                canGoBackToChoiseType = false;
                activity_title.setText(getString(R.string.filter_address_info));
                activateLayout(EDITOR_ACTIVITY);
                activateAddressTypedFields(findViewById(R.id.rb_office));
                address = FilterActivity.filteredAddress;
        }

    }

    private void initComponents() {
        et_state = (EditText) findViewById(R.id.et_state);
        et_city = (EditText) findViewById(R.id.et_city);
        et_street = (EditText) findViewById(R.id.et_street);
        et_buildNum = (EditText) findViewById(R.id.et_buildNum);
        et_floor = (EditText) findViewById(R.id.et_floor);
        et_number = (EditText) findViewById(R.id.et_number);

        floor_row = (TableRow) findViewById(R.id.floor_row);
        number_row = (TableRow) findViewById(R.id.number_row);

        layout_choiser = (LinearLayout) findViewById(R.id.choise_address_layout);
        layout_editor = (LinearLayout) findViewById(R.id.edit_address_layout);
    }

    private void setClickListeners() {
        findViewById(R.id.btn_address_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validated()) {
                    rebuildAddress();
                    if (requestCode != MainActivity.FILTER) {
                        order.setAddress(address);
                    } else {
                        FilterActivity.filteredAddress = address;
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.not_valideted), Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.btn_address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_address_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateLayout(EDITOR_ACTIVITY);
            }
        });
        findViewById(R.id.button_address_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.rb_flat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAddressTypedFields(v);
            }
        });
        findViewById(R.id.rb_office).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAddressTypedFields(v);
            }
        });
        findViewById(R.id.rb_privateBuild).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAddressTypedFields(v);
            }
        });

    }

    private void setAddressFields() {
        if (address != null) {
            et_state.setText(address.getState());
            et_city.setText(address.getCity());
            et_street.setText(address.getStreet());
            et_buildNum.setText(address.getBuildNum());
            if (address.getClass() == FlatAddress.class) {
                et_number.setText(((FlatAddress) address).getFlatNum());
            } else if (address.getClass() == OfficeAddress.class) {
                et_number.setText(((OfficeAddress) address).getNumber());
                et_floor.setText(((OfficeAddress) address).getFloor());
            } else if (address.getClass() == PrivateBuildAddress.class) {

            }
        }
    }

    private void activateAddressTypedFields(View view) {
        floor_row.setVisibility(View.GONE);
        number_row.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.rb_flat:
                address = new FlatAddress();
                number_row.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_office:
                address = new OfficeAddress();
                floor_row.setVisibility(View.VISIBLE);
                number_row.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_privateBuild:
                address = new PrivateBuildAddress();
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
        if (requestCode != MainActivity.FILTER) {
            if (et_state.getText().toString().length() < 4) {
                return false;
            } else if (et_city.getText().toString().length() < 4) {
                return false;
            } else if (et_street.getText().toString().length() < 1) {
                return false;
            } else if (et_buildNum.getText().toString().length() < 1) {
                return false;
            } else if (address.getClass() == FlatAddress.class) {
                if (et_number.getText().toString().length() < 1) {
                    return false;
                }
            } else if (address.getClass() == OfficeAddress.class) {
                if (et_number.getText().toString().length() < 1) {
                    return false;
                } else if (et_floor.getText().toString().length() < 1) {
                    return false;
                }
            } else if (address.getClass() == PrivateBuildAddress.class) {
                return true;
            }
        }
        return true;
    }

    private void rebuildAddress() {
        address.setState(et_state.getText().toString());
        address.setCity(et_city.getText().toString());
        address.setStreet(et_street.getText().toString());
        address.setBuildNum(et_buildNum.getText().toString());

        if (address.getClass() == FlatAddress.class) {
            ((FlatAddress) address).setFlatNum(et_number.getText().toString());
        } else if (address.getClass() == OfficeAddress.class) {
            ((OfficeAddress) address).setNumber(et_number.getText().toString());
            ((OfficeAddress) address).setFloor(et_floor.getText().toString());
        } else if (address.getClass() == PrivateBuildAddress.class) {
            // are no different fields
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