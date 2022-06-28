package com.example.hibarking.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                     "Content-Type:application/json",
                    "Authorization:key=AAAAnHSERIU:APA91bE_kZnjsGP7Ie_DLqRZcx9hP4-lc7CvzIrLUmsVlmsU96zfftJR0gO1-Aq9Iwd4l4WwnTEMBlBhh5Zc-H-Swkkz6hzYHQYlifepW6R2kRnJduYQQqRNFKRdbSb1Fzu59aIFIofV" // Your server key refer to video for finding your server key
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

