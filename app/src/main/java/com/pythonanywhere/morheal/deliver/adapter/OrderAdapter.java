package com.pythonanywhere.morheal.deliver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.pythonanywhere.morheal.deliver.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pythonanywhere.morheal.deliver.models.Order;


public class OrderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Order> orderArrayList;
    private Boolean listSelecting;

    public OrderAdapter(Context context, ArrayList<Order> list){
        this.context = context;
        this.orderArrayList=list;
        listSelecting =false;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderArrayList.size();
    }

    @Override
    public Order getItem(int position) {
        return orderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderArrayList.get(position).getOrderID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=layoutInflater.inflate(R.layout.order_item, parent, false);
        }


        CheckBox select = (CheckBox)view.findViewById(R.id.chb_select);
        if(listSelecting){
            select.setVisibility(View.VISIBLE);
        }else{
            select.setVisibility(View.GONE);
        }

        Order order = getItem(position);
        ((TextView)view.findViewById(R.id.textView3)).setText("ID: "+String.valueOf(order.getOrderID()));
        ((TextView)view.findViewById(R.id.tv_title)).setText(order.getName());
        ((TextView)view.findViewById(R.id.tv_product)).setText(order.getProduct().toString());
        ((TextView)view.findViewById(R.id.tv_price)).setText(String.format("%.2f %s",order.getProductPrice(), context.getResources().getString(R.string.valuta_uah)));
        ((TextView)view.findViewById(R.id.tv_address)).setText(order.getAddress().toString());

        Date dateTime = order.getOrderDateTime();

        ((TextView)view.findViewById(R.id.tv_date)).setText(new SimpleDateFormat("dd/MM/yyyy").format(dateTime));
        ((TextView)view.findViewById(R.id.tv_time)).setText(new SimpleDateFormat("hh:mm:ss").format(dateTime));
        ((CheckBox)view.findViewById(R.id.cb_prepaided)).setChecked(order.getPaided());


        return view;
    }

    public void enableSelecting(){
        listSelecting=true;
    }

    public void disableSelecting(){
        listSelecting =false;
    }
}
