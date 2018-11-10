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
import android.widget.Toast;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.DiscoverAdapter;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;

public class HomeDiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        DiscoverAdapter.OnSaveClickListener {

    private DiscoverAdapter discoverAdapter;
    private RecyclerView discoverList;
    private SwipeRefreshLayout discoverRefresh;
    private NewsViewModel newsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        discoverAdapter = new DiscoverAdapter();
        discoverList = view.findViewById(R.id.discover_list);
        discoverRefresh = view.findViewById(R.id.discover_refresh);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        discoverList.setLayoutManager(layoutManager);
        discoverAdapter.setOnSaveClickListener(this);
        discoverList.setAdapter(discoverAdapter);

        discoverRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        discoverRefresh.setOnRefreshListener(this);

        discoverRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        newsViewModel.getHeadlines().observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                if (newsResponse != null) {
                    discoverAdapter.setmResources(newsResponse.getArticles());
                }
                discoverRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSaveClicked(ArticleStructure articleStructure) {
        newsViewModel.storeArticle(articleStructure, new DbQueryListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Article Archived!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
