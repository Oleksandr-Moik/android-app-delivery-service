package com.pythonanywhere.morheal.deliver.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pythonanywhere.morheal.deliver.R;
import com.pythonanywhere.morheal.deliver.activities.statics.ActionActivity;
import com.pythonanywhere.morheal.deliver.adapter.OrderAdapter;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.service.OrderService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int CREATE = 1;
    public static final int FILTER = 10;
    public static final int CONTEXT_ITEM_EDIT = 2;
    public static final int CONTEXT_ITEM_LOOK = 3;
    public static final int CONTEXT_ITEM_DELETE = 4;

    public static ArrayList<Order> filterArray;
    public static OrderService orderService;

    private FloatingActionButton fab_create;
    private FloatingActionButton fab_save;
    private FloatingActionButton fab_delete;
    private FloatingActionButton fab_filter;
    private FloatingActionButton fab_tools;
    private ListView listView;

    private ArrayList<Order> selectedOrders;
    private Boolean listSelecting;
    private long selectedItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        fab_create = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_filter = (FloatingActionButton) findViewById(R.id.fab_filter);
        fab_tools = (FloatingActionButton) findViewById(R.id.fab_tools);

        listView = (ListView) findViewById(R.id.list_view);

        listSelecting = false;
        selectedOrders = new ArrayList<>(1);

        orderService = new OrderService(new File("orders.bin"), getBaseContext());

        try {
            orderService.loadListFromFile();
            Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.list_loaded), Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fab_tools.setRotation(0);
        ShowButtons();

        registerForContextMenu(listView);

        refreshList(orderService.getOrderArrayList());
        setClickListeners();
    }

    private void setClickListeners() {
        fab_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getRotation() == 0) {
                    ShowButtons();
                } else {
                    HideButtons();
                }
            }
        });

        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons();
                Intent intent = new Intent(ActionActivity.ORDER);
                intent.putExtra("requestCode", CREATE);
                try{
                    orderService.saveListToFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                startActivityForResult(intent, CREATE);
            }
        });
        fab_create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), getString(R.string.create), Toast.LENGTH_SHORT).show();
                orderService.testFilling();
                refreshList(orderService.getOrderArrayList());
                return true;
            }
        });
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons();
                try {
                    if (listSelecting) {
                        listSelecting=false;
                        orderService.saveListToFile(selectedOrders);
                        Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.list_selected_seved), Snackbar.LENGTH_SHORT).show();
                    } else {
                        orderService.saveListToFile();
                        Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.list_saved), Snackbar.LENGTH_SHORT).show();
                    }
                    refreshList(orderService.getOrderArrayList());
                } catch (IOException e) {
                    e.printStackTrace();
+                }
            }
        });
        fab_save.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HideButtons();
                listSelecting = true;
                Toast.makeText(v.getContext(), getString(R.string.save), Toast.LENGTH_SHORT).show();
                refreshList(orderService.getOrderArrayList());
                return true;
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons();
                if (listSelecting) {
                    listSelecting = false;
                    for (Order order : selectedOrders) {
                        orderService.deleteOrder(order.getOrderID());
                    }
                    if (selectedOrders.size() >= 1) {
                        Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.deleted_selected_items), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.cancel), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        orderService.getOrderArrayList().addAll(selectedOrders);
                                        selectedOrders.clear();
                                    }
                                })
                                .show();
                    }
                } else {
                    listSelecting = true;
                    selectedOrders = new ArrayList<>(1);
                }
                refreshList(orderService.getOrderArrayList());
            }
        });
        fab_delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), getString(R.string.deleting), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons();
                Intent intent = new Intent(ActionActivity.FILTER);
                startActivityForResult(intent, FILTER);
                // filter logic in void onActivityResult
            }
        });
        fab_filter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), getString(R.string.filter), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                HideButtons();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listSelecting) {
                    Order order = orderService.findOrderById(id);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.chb_select);
                    if (selectedOrders.contains(order)) {
                        selectedOrders.remove(order);
                        checkBox.setChecked(false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            view.setBackgroundColor(getColor(R.color.unselected));
                        }
                    } else {
                        selectedOrders.add(order);
                        checkBox.setChecked(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            view.setBackgroundColor(getColor(R.color.selected));
                        }
                    }
                }else{
                    listView.showContextMenuForChild(view);
                    selectedItemId = id;
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemId = id;
                return false;
            }
        });
    }

    private void refreshList(ArrayList<Order> list) {
        OrderAdapter orderAdapter = new OrderAdapter(getBaseContext(), list);
        if (listSelecting) {
            orderAdapter.enableSelecting();
        } else {
            orderAdapter.disableSelecting();
        }
        listView.setAdapter(orderAdapter);
    }

    private void ShowButtons() {
        if (fab_tools.getRotation() != 180) {
            if (!listSelecting) {
                fab_create.setVisibility(View.VISIBLE);
                fab_filter.setVisibility(View.VISIBLE);
            }
            fab_delete.setVisibility(View.VISIBLE);
            fab_save.setVisibility(View.VISIBLE);
            fab_tools.setRotation(180);
        }
    }
    private void HideButtons() {
        if (fab_tools.getRotation() != 0) {
            fab_create.setVisibility(View.GONE);
            fab_save.setVisibility(View.GONE);
            fab_delete.setVisibility(View.GONE);
            fab_filter.setVisibility(View.GONE);
            fab_tools.setRotation(0);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String selectedWord = ((TextView) info.targetView.findViewById(R.id.tv_title)).getText().toString();

        menu.setHeaderTitle(selectedWord);
        menu.add(0, CONTEXT_ITEM_EDIT, 0, R.string.edit);
        menu.add(0, CONTEXT_ITEM_LOOK, 0, R.string.watch_order);
        menu.add(0, CONTEXT_ITEM_DELETE, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_ITEM_EDIT:
                try {
                    orderService.saveListToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(
                        new Intent(ActionActivity.ORDER)
                            .putExtra("id", selectedItemId)
                            .putExtra("requestCode", CONTEXT_ITEM_EDIT),
                        CONTEXT_ITEM_EDIT);
                return false;
            case CONTEXT_ITEM_LOOK:
                startActivityForResult(
                        new Intent(ActionActivity.ORDER)
                                .putExtra("id", selectedItemId)
                                .putExtra("requestCode", CONTEXT_ITEM_LOOK),
                        CONTEXT_ITEM_LOOK);
                return false;
            case CONTEXT_ITEM_DELETE:
                orderService.deleteOrder(selectedItemId);
                refreshList(orderService.getOrderArrayList());
                return false;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.added), Snackbar.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(findViewById(R.id.coordinator_main), getString(R.string.canceled), Snackbar.LENGTH_LONG).show();
                }
                refreshList(orderService.getOrderArrayList());
                break;
            case FILTER:
                if (resultCode == RESULT_OK) {
                    refreshList(filterArray);
                } else if (resultCode == RESULT_CANCELED) {
                    refreshList(orderService.getOrderArrayList());
                }
                break;
            case CONTEXT_ITEM_EDIT:
                if (resultCode == RESULT_CANCELED) {
                    try {
                        orderService.loadListFromFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                refreshList(orderService.getOrderArrayList());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(listSelecting){
            HideButtons();
            listSelecting = false;
            refreshList(orderService.getOrderArrayList());
        }else{
            super.onBackPressed();
        }
    }


}

