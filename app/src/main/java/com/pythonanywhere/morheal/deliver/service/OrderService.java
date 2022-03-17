package com.pythonanywhere.morheal.deliver.service;

import android.content.Context;
import android.util.Log;

import com.pythonanywhere.morheal.deliver.activities.MainActivity;
import com.pythonanywhere.morheal.deliver.models.Order;
import com.pythonanywhere.morheal.deliver.models.address.Address;
import com.pythonanywhere.morheal.deliver.models.address.FlatAddress;
import com.pythonanywhere.morheal.deliver.models.address.OfficeAddress;
import com.pythonanywhere.morheal.deliver.models.address.PrivateBuildAddress;
import com.pythonanywhere.morheal.deliver.models.product.LiquidProduct;
import com.pythonanywhere.morheal.deliver.models.product.Product;
import com.pythonanywhere.morheal.deliver.models.product.SolidMatterProduct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class OrderService {
    private ArrayList<Order> orderList = new ArrayList<>();
    private File fileForSaveData;
    private Context baseContext;

    public OrderService(File file, Context context) {
        fileForSaveData = file;
        baseContext=context;
    }

    public void testFilling() {
        orderList.add(new Order("Замовлення #"+Order.getID(), false, 2020, 23, 1, 23, 55, 0, new PrivateBuildAddress("Київська", "Київ", "Шевченка", "23"),
                new SolidMatterProduct("Динаміки", 2530.3, "SVEN", 20.0, 20.0, 20.0)));
        orderList.add(new Order("Замовлення #"+Order.getID(), true, 2020, 16, 6, 12, 5, 0, new FlatAddress("Одеська", "Одеса", "Інститутська", "3/A", "45б"),
                new LiquidProduct("Пакет молока", 40.4, "Молокія", 1.0)));
        orderList.add(new Order("Замовлення #"+Order.getID(), false, 2020, 16, 6, 12, 5, 0, new OfficeAddress("Львівська", "Львів", "Франка", "43", "1", "3"),
                new SolidMatterProduct("Матеріали", 1050.7, "Atrecket", 100.0, 40.0, 40.0)));
    }

    public void addOrder(Order order) {
        if (findOrderById(order.getOrderID()) != null) {
            order = new Order(order);
        }
        orderList.add(order);
    }

    public Boolean editOrder(Order order) {
        Integer index = orderList.indexOf(findOrderById(order.getOrderID()));
        orderList.remove(index);
        orderList.set(index, order);
        return Boolean.TRUE;
    }

    public Boolean deleteOrder(int id) {
        return orderList.remove(findOrderById(id));
    }

    public Boolean deleteOrder(long id) {
        return orderList.remove(findOrderById(id));
    }


    public ArrayList<Order> getPrepaidedOrders(Boolean type) {
        ArrayList<Order> list = new ArrayList<>();
        for (Order order : orderList) {
            if (type) {
                if (order.getPaided())
                    list.add(order);
            } else {
                if (!order.getPaided())
                    list.add(order);
            }
        }
        return list;
    }

    public ArrayList<Order> getOrderArrayList() {
        return orderList;
    }

    public void setOrderArrayList(ArrayList<Order> list) {
        orderList = new ArrayList<>(list);
    }

    public Order findOrderById(int id) {
        for (Order order : orderList) {
            if (order.getOrderID() == id) return order;
        }
        return null;
    }

    public Order findOrderById(long id) {
        for (Order order : orderList) {
            if (order.getOrderID() == id) return order;
        }
        return null;
    }
    public ArrayList<Order> findOrdersByAddress(Address address) {
        ArrayList<Order> result = new ArrayList<>();
        ArrayList<String> addressFields = ((OfficeAddress)address).getFields();
        String orderAddress;

        for (Order order : orderList) {
            orderAddress = order.getAddress().toString();
            for (String field : addressFields) {
                if (!Objects.equals(field, "")) {
                    if (orderAddress.contains(field) && !result.contains(order)) {
                        result.add(order);
                    } else if (result.contains(order)) {
                        result.remove(order);
                    }
                }
            }
        }

        return result;
    }

    public void loadListFromFile() throws IOException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = baseContext.openFileInput(fileForSaveData.getName());
            objectInputStream = new ObjectInputStream(fileInputStream);
            setOrderArrayList((ArrayList<Order>) objectInputStream.readObject());
            Order.setID(orderList.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            objectInputStream.close();
            if (fileInputStream != null) fileInputStream.close();
        }
    }

    public void saveListToFile() throws IOException {
        FileOutputStream fos = baseContext.openFileOutput(fileForSaveData.getName(), Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(orderList);
        oos.close();
        fos.close();
    }

    public void saveListToFile(ArrayList<Order> arrayList) throws IOException {
        FileOutputStream fos = baseContext.openFileOutput(fileForSaveData.getName(), Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(arrayList);
        oos.close();
        fos.close();
    }

}
