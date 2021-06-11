package com.example.veggieportz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class cart extends AppCompatActivity implements View.OnClickListener {

    TextView item_total, total_selected, amount;
    int amnt = 0;
    ListView listView;
    ArrayList<Integer> items;
    int[] quantity;
    String[] selectedVeg, VegAry;
    int[] PriAry, selectedPri;
    Integer[] ImgAry = {
            R.drawable.onion, R.drawable.tomato,
            R.drawable.potato, R.drawable.cabbage,
            R.drawable.peas, R.drawable.coliflower,
            R.drawable.ladyfingure, R.drawable.bottlegourd,
            R.drawable.carrot, R.drawable.brinjal,
            R.drawable.garlic, R.drawable.ginger,
            R.drawable.capsicum, R.drawable.chilli,
            R.drawable.palak, R.drawable.lemon,
            R.drawable.methi, R.drawable.beetroot,
            R.drawable.redcapsi, R.drawable.yellowcapsi,
            R.drawable.broccoli
    };
    Integer[] selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        items = getIntent().getIntegerArrayListExtra("Items");
        VegAry = getResources().getStringArray(R.array.vegitables);
        PriAry = getResources().getIntArray(R.array.price);
        total_selected = findViewById(R.id.total_selected);
        amount = findViewById(R.id.amount);
        total_selected.setText(items.size() + "Items");
        selectedVeg = new String[items.size()];
        selectedPri = new int[items.size()];
        selectedImg = new Integer[items.size()];
        quantity = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            selectedVeg[i] = VegAry[items.get(i)];
            selectedPri[i] = PriAry[items.get(i)];
            selectedImg[i] = ImgAry[items.get(i)];
            quantity[i] = 1;
            amnt = amnt + selectedPri[i] * quantity[i];
        }
        amount.setText(amnt + " :: Rupees");
        listView = findViewById(R.id.listview);
        cartAdapter cartAdapter = new cartAdapter(this, selectedVeg, selectedPri, selectedImg);
        listView.setAdapter(cartAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.pay) {
            item_total = findViewById((Integer) v.getTag());
            int i = (int) v.getTag();
            switch (v.getId()) {
                case R.id.increment:
                    if (quantity[i] >= 1 && quantity[i] < 5) {
                        quantity[i]++;
                        amnt = amnt + selectedPri[i];
                    }
                    item_total.setText(String.valueOf(quantity[i]) + "Kg");
                    break;
                case R.id.decrement:
                    if (quantity[i] > 1) {
                        quantity[i]--;
                        amnt = amnt - selectedPri[i];
                    }
                    item_total.setText(String.valueOf(quantity[i]) + "Kg");
                    break;
            }
            amount.setText(amnt + " :: Rupees");
        }else{
            Intent intent = new Intent(cart.this,payment_sucess.class);
            startActivity(intent);
            finish();
        }
    }
}