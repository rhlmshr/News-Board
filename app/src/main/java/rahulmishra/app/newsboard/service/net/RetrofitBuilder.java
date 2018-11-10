package rahulmishra.app.newsboard.service.net;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rahulmishra.app.newsboard.BuildConfig;
import rahulmishra.app.newsboard.NewsBoardApp;
import rahulmishra.app.newsboard.service.net.interceptors.OfflineResponseCacheInterceptor;
import rahulmishra.app.newsboard.service.net.interceptors.ResponseCacheInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit retrofit = null;
    private static Retrofit dictRetrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
            httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
            httpClient.cache(new Cache(new File(NewsBoardApp.instance().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            httpClient.connectTimeout(60, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit;
    }

    public static Retrofit getDictInstance() {
        if (dictRetrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.DICT_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            builder.client(client);
            retrofit = builder.build();
        }
        return retrofit;
    }
}