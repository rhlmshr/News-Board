package rahulmishra.app.newsboard.service.net;

import rahulmishra.app.newsboard.service.model.DictResponse;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.util.SourcesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface DataService {

    @GET("top-headlines?pageSize=50")
    Call<NewsResponse> getHeadlines(@Query("country") String country,
                                    @Query("apiKey") String apiKey);

    @GET("everything?language=en&sortBy=popularity&pageSize=40")
    Call<NewsResponse> getSearchResults(@Query("q") String query,
                                        @Query("apiKey") String apiKey);

    @GET("words/{query}")
    Call<DictResponse> getWordMeaning(@Path("query") String query, @Header("X-Mashape-Key") String dict_key);

    @GET("sources?language=en")
    Call<SourcesResponse> getSources(@Query("apiKey") String apiKey);
}