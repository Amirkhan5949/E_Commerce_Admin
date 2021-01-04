package com.example.e_commerce_admin.model;

public class Order {
    Product Product;
    String ordered_mrp_price;
    String ordered_selling_price;
    String payment_type;
    int quantity;
    long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Order() {
    }

    public com.example.e_commerce_admin.model.Product getProduct() {
        return Product;
    }

    public void setProduct(com.example.e_commerce_admin.model.Product product) {
        Product = product;
    }

    public String getOrdered_mrp_price() {
        return ordered_mrp_price;
    }

    public void setOrdered_mrp_price(String ordered_mrp_price) {
        this.ordered_mrp_price = ordered_mrp_price;
    }

    public String getOrdered_selling_price() {
        return ordered_selling_price;
    }

    public void setOrdered_selling_price(String ordered_selling_price) {
        this.ordered_selling_price = ordered_selling_price;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "Product=" + Product +
                ", ordered_mrp_price='" + ordered_mrp_price + '\'' +
                ", ordered_selling_price='" + ordered_selling_price + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
