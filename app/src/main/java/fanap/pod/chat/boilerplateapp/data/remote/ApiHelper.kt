package fanap.pod.chat.boilerplateapp.data.remote

import fanap.pod.chat.boilerplateapp.data.remote.models.HandShakeRes
import fanap.pod.chat.boilerplateapp.data.remote.models.LoginResult
import fanap.pod.chat.boilerplateapp.data.remote.models.SSoTokenResult
import io.reactivex.Observable
import retrofit2.http.*

interface ApiHelper {

    @POST("otp/handshake")
    fun handShake(
        @Query("deviceName") deviceName: String,
        @Query("deviceOs") deviceOs: String,
        @Query("deviceOsVersion") deviceOsVersion: String,
        @Query("deviceType") deviceType: String,
        @Query("deviceUID") deviceUID: String
    ): Observable<HandShakeRes?>

    @POST("otp/authorize")
    fun login(
        @Query("identity") identity: String,
        @Header("keyId") keyId: String
    ): Observable<LoginResult?>


    @POST("otp/verify")
    fun verifyNumber(
        @Header("keyId") keyId: String,
        @Query("identity") number: String,
        @Query("otp") verifyCode: String
    ): Observable<SSoTokenResult?>

    @POST("accessToken/")
    fun getOTPToken(@Query("code") code: String): Observable<SSoTokenResult?>


    @GET("refresh")
    fun  refreshToken(
        @Query("refreshToken") refreshToken: String
    ): Observable<SSoTokenResult?>
}