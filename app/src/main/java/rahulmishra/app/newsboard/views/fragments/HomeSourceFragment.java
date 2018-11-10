package rahulmishra.app.newsboard.views.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.EventQuery;
import rahulmishra.app.newsboard.util.SourcesResponse;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.activities.QueryActivity;
import rahulmishra.app.newsboard.views.adapters.SourceAdapter;

public class HomeSourceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SourceAdapter.OnSourceItemClicked {

    private SourceAdapter sourceAdapter;
    private RecyclerView discoverList;
    private NewsViewModel newsViewModel;
    private SwipeRefreshLayout sourceRefresh;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        sourceAdapter = new SourceAdapter();
        discoverList = view.findViewById(R.id.discover_list);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        sourceRefresh = view.findViewById(R.id.discover_refresh);
        context = inflater.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        discoverList.setLayoutManager(new LinearLayoutManager(getContext()));
        sourceAdapter.setOnSourceItemClicked(this);
        discoverList.setAdapter(sourceAdapter);
        sourceRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        sourceRefresh.setOnRefreshListener(this);

        sourceRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        newsViewModel.getSources().observe(this, new Observer<SourcesResponse>() {
            @Override
            public void onChanged(@Nullable SourcesResponse sourcesResponse) {
                if (sourcesResponse != null && sourcesResponse.getStatus().equalsIgnoreCase("ok")) {
                    sourceAdapter.setmResources(sourcesResponse.getSources());
                }
                sourceRefresh.setRefreshing(false);
            }
        });
    }

    public RelativeLayout.LayoutParams fetchLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // We can add any rule available for RelativeLayout and hence can position accordingly);
        return params;
    }

    @Override
    public void onItemClicked(String query) {
        EventQuery eventQuery = new EventQuery(query);
        EventBus.getDefault().post(eventQuery);

        Intent intent = new Intent(context, QueryActivity.class);
        intent.putExtra("query", query);
        context.startActivity(intent);
    }
}
