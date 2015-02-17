package net.ogify.backend.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    public enum OrderStatus {
        New,
        Running,
        Completed
    }

    public enum OrderNamespace {
        Private,
        Friends,
        FriendsOfFriends,
        All,
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(OrderNamespace namespace) {
        this.namespace = namespace;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    public Date getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Date expireIn) {
        this.expireIn = expireIn;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    Long id;

    User owner;

    User executor;

    OrderStatus status;

    OrderNamespace namespace;

    Double latitude;

    Double longitude;

    String address;

    String reward;

    String description;

    Date createdAt = new Date();

    Date doneAt;

    Date expireIn;

    List<OrderItem> items = new ArrayList<OrderItem>();

    public List<OrderItem> getItems() {
        return items;
    }
}
