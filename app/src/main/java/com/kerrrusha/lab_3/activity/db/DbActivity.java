package com.kerrrusha.lab_3.activity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kerrrusha.lab_3.R;
import com.kerrrusha.lab_3.activity.main.MainActivity;
import com.kerrrusha.lab_3.domain.entity.Order;
import com.kerrrusha.lab_3.repository.OrderRepository;

import java.util.List;

public class DbActivity extends AppCompatActivity {

    TableLayout orderTable;
    OrderRepository orderRepository;
    TextView emptyDbMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        initialize();

        fillOrderTable();

        Button goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(v -> openMainActivity());
    }

    private void initialize() {
        orderRepository = new OrderRepository(getBaseContext());
        orderTable = findViewById(R.id.orderTable);
        emptyDbMessage = findViewById(R.id.emptyDbMessage);
    }

    private void fillOrderTable() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            emptyDbMessage.setVisibility(View.VISIBLE);
            return;
        }
        emptyDbMessage.setVisibility(View.INVISIBLE);
        for (Order order : orders) {
            TableRow row = new TableRow(this);

            row.addView(createCell(order.getId()));
            row.addView(createCell(order.getCreatedAt()));
            row.addView(createCell(order.getName()));
            row.addView(createCell(order.getSizeWeightCostOption()));
            row.addView(createCell(order.getAdditionCostOptions()));

            orderTable.addView(row);
        }
    }

    private TextView createCell(int value) {
        return createCell("" + value);
    }

    private TextView createCell(String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setTextColor(Color.BLACK);
        cell.setWidth(100);
        return cell;
    }

    private void openMainActivity() {
        Intent intent = new Intent(DbActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
