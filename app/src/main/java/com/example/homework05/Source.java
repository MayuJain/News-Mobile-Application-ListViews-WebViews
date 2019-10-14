package com.example.homework05;

import java.io.Serializable;

public class Source implements Serializable {
    String id, name;

    @Override
    public String toString() {
        return name;
    }

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
