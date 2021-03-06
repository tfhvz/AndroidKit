package com.snicesoft.viewbind.base;

public interface IAv {

    void bindAll();

    void bindTo(int id);

    void bind(Object holder);

    int layout();

    void onLoaded();

    void loadNetData();

}