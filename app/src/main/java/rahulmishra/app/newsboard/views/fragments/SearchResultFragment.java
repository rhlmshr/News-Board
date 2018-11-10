package rahulmishra.app.newsboard.views.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.EventQuery;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.ResultAdapter;

public class SearchResultFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ResultAdapter resultAdapter;
    private SwipeRefreshLayout searchRefresh;
    private RecyclerView resultList;
    private NewsViewModel newsViewModel;
    private String query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        resultAdapter = new ResultAdapter();
        searchRefresh = view.findViewById(R.id.discover_refresh);
        resultList = view.findViewById(R.id.discover_list);
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        resultList.setLayoutManager(layoutManager);
        resultList.setAdapter(resultAdapter);

        searchRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        searchRefresh.setOnRefreshListener(this);

        searchRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        newsViewModel.getSearchResult(query).observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                if (newsResponse != null) {
                    resultAdapter.setmResources(newsResponse.getArticles());
                }
                searchRefresh.setRefreshing(false);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventQuery query) {
        searchRefresh.setRefreshing(true);
        this.query = query.getQuery();
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
