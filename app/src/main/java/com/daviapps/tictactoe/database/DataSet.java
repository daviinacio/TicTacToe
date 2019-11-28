package com.daviapps.tictactoe.database;

import java.text.SimpleDateFormat;
import java.util.List;

// Created by daviinacio on 26/11/2019.
public abstract class DataSet<T extends Object> {
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy");

    public abstract T insert(T item);
    public abstract T update(T item);

    public abstract boolean delete(T item);

    public boolean deleteById(int id){
        return this.delete(selectById(id));
    }

    public abstract List<T> select(String where, String order);

    public List<T> select(String key, String value, String order){
        return this.select(String.format("%s =  %s", key, value), "");
    }

    public T selectById(int id){
        List<T> result = this.select("id", Integer.toString(id), null);
        return result.size() > 0 ? result.get(0) : null;
    }

    public int count(String where){
        return this.select(where, null).size();
    }
}
