package com.kerrrusha.lab_3.domain.entity;

import androidx.annotation.NonNull;

import java.time.Instant;

public class Order {

    public static final Order EMPTY_ORDER = new Order();

    private int id;
    private String createdAt;
    private String name;
    private String sizeWeightCostOption;
    private String additionCostOptions;

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt.toString();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSizeWeightCostOption(String sizeWeightCostOption) {
        this.sizeWeightCostOption = sizeWeightCostOption;
    }

    public void setAdditionCostOptions(String additionCostOptions) {
        this.additionCostOptions = additionCostOptions;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getSizeWeightCostOption() {
        return sizeWeightCostOption;
    }

    public String getAdditionCostOptions() {
        return additionCostOptions;
    }

    @NonNull
    @Override
    public String toString() {
        final String SEPARATOR = ";";
        return createdAt + SEPARATOR +
               name + SEPARATOR +
               sizeWeightCostOption + SEPARATOR +
               additionCostOptions;
    }

}
