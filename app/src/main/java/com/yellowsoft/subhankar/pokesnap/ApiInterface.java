package com.yellowsoft.subhankar.pokesnap;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by subhankar on 7/17/2016.
 */
public interface ApiInterface {
    @GET("posts/all")
    Call<List<Post>> getNewposts(@Query("token") String token);
    //Call<FeedResult> getNewposts(@Query("token") String token);

    @GET("posts/all")
    Call<List<Post>> getMoreNewposts(@Query("lastid") String id,
                                     @Query("token") String token);

    @GET("posts/top")
    Call<List<Post>> getTopposts(@Query("token") String token);
    //Call<FeedResult> getNewposts(@Query("token") String token);

    @GET("posts/top")
    Call<List<Post>> getMoreTopposts(@Query("lastid") String id,
                                     @Query("token") String token);

    @GET("posts/all/{username}")
    Call<List<Post>> getUserPosts(@Path("username") String user);

    @GET("posts/all/{username}")
    Call<List<Post>> getMoreUserPosts(@Path("username") String user);

    @FormUrlEncoded
    @POST("authenticate")
    Call<AuthResult> authenticate(@Field("name") String name,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("users/add")
    Call<AuthResult> register(@Field("name") String name,
                              @Field("password") String password,
                              @Field("team") String team);

    @Multipart
    @POST("posts/add")
    Call<ResponseBody> upload(@Part("userName") RequestBody name,
                              @Part("team") RequestBody team,
                              @Part MultipartBody.Part file,
                              @Part("postBody") RequestBody postBody,
                              @Part("token") RequestBody token);

    @FormUrlEncoded
    @POST("posts/like")
    Call<Result> like(@Field("post_id") String id,
                      @Field("userName") String user);
}
