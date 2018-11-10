package rahulmishra.app.newsboard.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.List;

import rahulmishra.app.newsboard.NewsBoardApp;
import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.database.LocalDataRepository;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.service.model.DictResponse;
import rahulmishra.app.newsboard.service.model.NewsRequest;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.service.model.NewsSearchRequest;
import rahulmishra.app.newsboard.service.net.DataRepository;
import rahulmishra.app.newsboard.util.SourcesResponse;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;

public class NewsViewModel extends ViewModel {

    private DataRepository dataRepository;
    private LocalDataRepository localDataRepository;

    public NewsViewModel() {
        dataRepository = DataRepository.instance();
        localDataRepository = LocalDataRepository.instance();
    }

    public LiveData<NewsResponse> getHeadlines() {

        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setApiKey(NewsBoardApp.instance().getString(R.string.api_key));
        newsRequest.setCountry("in");

        final MutableLiveData<NewsResponse> headlinesResponse = new MutableLiveData<>();
        dataRepository.getHeadlines(newsRequest).observeForever(new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                headlinesResponse.setValue(newsResponse);
            }
        });
        return headlinesResponse;
    }

    public LiveData<NewsResponse> getSearchResult(String query) {

        NewsSearchRequest newsRequest = new NewsSearchRequest();
        newsRequest.setApiKey(NewsBoardApp.instance().getString(R.string.api_key));
        newsRequest.setQuery(query);

        final MutableLiveData<NewsResponse> headlinesResponse = new MutableLiveData<>();
        dataRepository.getSearchResults(newsRequest).observeForever(new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                headlinesResponse.setValue(newsResponse);
            }
        });
        return headlinesResponse;
    }

    public void storeArticle(ArticleStructure articleStructure, DbQueryListener dbQueryListener) {
        localDataRepository.storeArticle(articleStructure, dbQueryListener);
    }

    public LiveData<List<ArticleStructure>> getAllArticles() {
        return localDataRepository.getAllArticles();
    }

    public LiveData<DictResponse> getWordMeaning(String query) {

        final MutableLiveData<DictResponse> headlinesResponse = new MutableLiveData<>();

        dataRepository.getWordMeaning(query).observeForever(new Observer<DictResponse>() {
            @Override
            public void onChanged(@Nullable DictResponse newsResponse) {
                headlinesResponse.setValue(newsResponse);
            }
        });
        return headlinesResponse;
    }

    public void removeArticle(ArticleStructure articleStructure, DbQueryListener dbQueryListener) {
        localDataRepository.removeArticle(articleStructure, dbQueryListener);
    }

    public LiveData<SourcesResponse> getSources() {
        final MutableLiveData<SourcesResponse> headlinesResponse = new MutableLiveData<>();
        dataRepository.getSources(NewsBoardApp.instance().getString(R.string.api_key))
                .observeForever(new Observer<SourcesResponse>() {
                    @Override
                    public void onChanged(@Nullable SourcesResponse newsResponse) {
                        headlinesResponse.setValue(newsResponse);
                    }
                });
        return headlinesResponse;
    }
}
