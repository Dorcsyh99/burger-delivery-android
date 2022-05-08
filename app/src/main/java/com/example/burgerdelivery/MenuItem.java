package com.example.burgerdelivery;

public class MenuItem {

    private String id;
    private String name;
    private String description;
    private String category;
    private int price;

    public MenuItem(String name, String desc, int price, String category) {
        this.name = name;
        this.description = desc;
        this.price = price;
        this.category = category;
    }

    public MenuItem(){}


    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
