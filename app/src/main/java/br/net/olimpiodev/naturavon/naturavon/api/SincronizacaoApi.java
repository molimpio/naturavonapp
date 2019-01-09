package br.net.olimpiodev.naturavon.naturavon.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SincronizacaoApi {

    @POST("sincronizacao/sincronizar")
    Call<ResponseBody> sincronizar(@Body RequestBody dados);

}
