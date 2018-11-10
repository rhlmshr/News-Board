package rahulmishra.app.newsboard.service.net;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import rahulmishra.app.newsboard.NewsBoardApp;
import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.DictResponse;
import rahulmishra.app.newsboard.service.model.NewsRequest;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.service.model.NewsSearchRequest;
import rahulmishra.app.newsboard.util.SourcesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private static DataRepository dataRepository;
    private DataService dataService;
    private DataService dictService;

    private DataRepository() {
        dataService = RetrofitBuilder.getInstance().create(DataService.class);
        dictService = RetrofitBuilder.getDictInstance().create(DataService.class);
    }

    public static DataRepository instance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    public LiveData<NewsResponse> getHeadlines(NewsRequest newsRequest) {
        final MutableLiveData<NewsResponse> returnResponse = new MutableLiveData<>();

        dataService.getHeadlines(newsRequest.getCountry(), newsRequest.getApiKey()).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                returnResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                returnResponse.setValue(null);
                t.printStackTrace();
            }
        });
        return returnResponse;
    }

    public LiveData<NewsResponse> getSearchResults(NewsSearchRequest newsSearchRequest) {
        final MutableLiveData<NewsResponse> returnResponse = new MutableLiveData<>();

        dataService.getSearchResults(newsSearchRequest.getQuery(), newsSearchRequest.getApiKey()).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                returnResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                returnResponse.setValue(null);
                t.printStackTrace();
            }
        });
        return returnResponse;
    }

    public LiveData<DictResponse> getWordMeaning(String query) {
        final MutableLiveData<DictResponse> returnResponse = new MutableLiveData<>();
        String key = NewsBoardApp.instance().getString(R.string.mashape_key);

        dictService.getWordMeaning(query, key).enqueue(new Callback<DictResponse>() {
            @Override
            public void onResponse(Call<DictResponse> call, Response<DictResponse> response) {
                returnResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DictResponse> call, Throwable t) {
                returnResponse.setValue(null);
                t.printStackTrace();
            }
        });
        return returnResponse;
    }

    public LiveData<SourcesResponse> getSources(String apiKey) {
        final MutableLiveData<SourcesResponse> returnResponse = new MutableLiveData<>();

        dataService.getSources(apiKey).enqueue(new Callback<SourcesResponse>() {
            @Override
            public void onResponse(Call<SourcesResponse> call, Response<SourcesResponse> response) {
                returnResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SourcesResponse> call, Throwable t) {
                returnResponse.setValue(null);
                t.printStackTrace();
            }
        });
        return returnResponse;
    }


}
