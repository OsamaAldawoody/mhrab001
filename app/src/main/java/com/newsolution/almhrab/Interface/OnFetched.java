package com.newsolution.almhrab.Interface;

import com.newsolution.almhrab.Model.Users;

/**
 * Created by hp on 9/7/2017.
 */

public interface OnFetched {
    void onSuccess(Users success);
    void onFail(String fail);
}
