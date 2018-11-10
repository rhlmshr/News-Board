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

import java.util.List;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.FavoritesAdapter;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        FavoritesAdapter.OnSaveClickListener {

    private FavoritesAdapter discoverAdapter;
    private RecyclerView discoverList;
    private SwipeRefreshLayout discoverRefresh;
    private NewsViewModel newsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        discoverAdapter = new FavoritesAdapter();
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
        newsViewModel.getAllArticles().observe(this, new Observer<List<ArticleStructure>>() {
            @Override
            public void onChanged(@Nullable List<ArticleStructure> articleStructures) {
                if (articleStructures != null) {
                    discoverAdapter.setmResources(articleStructures);
                }
                discoverRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRemoveClicked(ArticleStructure articleStructure) {
        newsViewModel.removeArticle(articleStructure, new DbQueryListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Article Removed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
