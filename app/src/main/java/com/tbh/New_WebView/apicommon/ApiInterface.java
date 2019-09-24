package com.tbh.New_WebView.apicommon;


import com.tbh.New_WebView.model.SuccessModel;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(EndApi.OFFER_LIST)
    Call<SuccessModel> offerList();

}
