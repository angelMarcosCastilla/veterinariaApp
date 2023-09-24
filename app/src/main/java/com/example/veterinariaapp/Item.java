package com.example.veterinariaapp;

import androidx.annotation.NonNull;

public class Item {
    private int id;
    private String label;

    public Item(){

    }
    public Item(int id, String label){
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
