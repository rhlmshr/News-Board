package rahulmishra.app.newsboard.service.net.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rahulmishra.app.newsboard.util.GeneralUtils;

/**
 * Interceptor to cache data and maintain it for four weeks.
 * <p>
 * If the device is offline, stale (at most four weeks old)
 * response is fetched from the cache.
 */

public class OfflineResponseCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!GeneralUtils.isNetworkConnected()) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 2419200)
                    .build();
        }
        return chain.proceed(request);
    }
}
