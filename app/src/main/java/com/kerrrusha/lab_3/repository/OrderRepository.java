package com.kerrrusha.lab_3.repository;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kerrrusha.lab_3.domain.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements Repository<Order> {

    private final SQLiteDatabase db;

    public OrderRepository(Context context) {
        db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        db.execSQL("CREATE TABLE IF NOT EXISTS 'order' (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "name TEXT, " +
                "size_weight_cost_option TEXT," +
                "addition_cost_options TEXT)");
    }

    @Override
    public List<Order> findAll() {
        List<Order> result = new ArrayList<>();
        Cursor query = db.rawQuery("SELECT * FROM 'order';", null);

        while(query.moveToNext()) {
            Order order = mapQueryResultToOrder(query);
            result.add(order);
        }

        query.close();
        return result;
    }

    public Order findOne(String args) {
        Order result = new Order();
        Cursor query = db.rawQuery("SELECT * FROM 'order' " + args + ";", null);

        if (query.moveToFirst()) {
            result = mapQueryResultToOrder(query);
        }

        query.close();
        return result;
    }

    private Order mapQueryResultToOrder(Cursor query) {
        Order order = new Order();

        int id = query.getInt(0);
        order.setId(id);

        String createdAt = query.getString(1);
        order.setCreatedAt(createdAt);

        String name = query.getString(2);
        order.setName(name);

        String sizeWeightCostOption = query.getString(3);
        order.setSizeWeightCostOption(sizeWeightCostOption);

        String additionCostOptions = query.getString(4);
        order.setAdditionCostOptions(additionCostOptions);

        return order;
    }

    public Order save(Order order) {
        db.execSQL("INSERT OR IGNORE INTO 'order' VALUES (?, ?, ?, ?, ?);", new Object[] {
                null,
                order.getCreatedAt(),
                order.getName(),
                order.getSizeWeightCostOption(),
                order.getAdditionCostOptions()
        });

        return findOne("ORDER BY id DESC LIMIT 1");
    }

}
