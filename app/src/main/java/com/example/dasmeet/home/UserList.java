package com.example.dasmeet.home;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public int size() {
        return this.userList.size();
    }

    public User get(int i) {
        return this.userList.get(i);
    }
}
