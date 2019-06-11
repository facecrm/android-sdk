package com.ekidpro.facecrmsample.listener;

public interface ViewUIInterface {
    void displayUI(String data);

    void displayError(String s);

    public interface SignInListener {
        void postCreateMember(String name, String email, String phone, int sex);
    }
}
