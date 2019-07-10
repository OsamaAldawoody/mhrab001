package com.newsolution.almhrab.Interface;

import com.newsolution.almhrab.Model.Users;


public interface OnFetched {
    void onSuccess(Users success);
    void onFail(String fail);
}
