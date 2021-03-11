package com.example.finnkino;

public class Theater {
    private String name;
    private Integer id;

    public Theater (Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
