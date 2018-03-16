package com.example.bottombar.sample;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN="REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recent_token= FirebaseInstanceId.getInstance().getToken();

    }
}
