package com.road.road1971user.controller.restapi;

import com.road.road1971user.model.NotificationSenderBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAANeglESQ:APA91bEC1TIWJdG3IrK9iEipRnnnUBkPPqsPuVrquXIlSiNmnkUY3yT95r-ARsm_guFiJ6ujFkVuEJQKzMZeFuWPnPaz4Z1Jk-r27VtIFaiFcRXaMryuFhfg2D_AfiLTlC1nOBGue74D"
    })
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSenderBody notificationSender);
}
