package com.example.networkedcrossword;

import org.json.JSONObject;

import java.util.HashMap;

public class Data {
    // variables
    private String data;

    // constructor
    public Data() {
    }

    // getters and setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addData(String data) {
        this.data += data;
    }

    public void prepDataForSend() {
        this.data = this.data + "\n";
    }

    // turn object into json string
    public String toJson(String arg) {
        HashMap<String, String> map = new HashMap<>();
        // player:word
        String key = arg.split(":")[0];
        String value = arg.split(":")[1];
        map.put(key, value);

        JSONObject json = new JSONObject(map);
        System.out.println("JSON: " + json.toString());
        return json.toString();

    }

}
