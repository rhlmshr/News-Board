package rahulmishra.app.newsboard.views.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.HeadlinesAdapter;

public class HomeFragment extends Fragment implements ViewPager.PageTransformer {

    private HeadlinesAdapter headlinesAdapter;
    private AutoScrollViewPager autoScrollViewPager;
    private TabLayout homeTabLayout;
    private ViewPager homeViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        headlinesAdapter = new HeadlinesAdapter(getActivity());
        autoScrollViewPager = view.findViewById(R.id.top_headlines_list);
        homeTabLayout = view.findViewById(R.id.home_tab);
        homeViewPager = view.findViewById(R.id.home_viewpager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        autoScrollViewPager.setPageTransformer(true, this);
        autoScrollViewPager.setClipToPadding(false);
        autoScrollViewPager.setPadding(20, 20, 20, 20);

        autoScrollViewPager.setAdapter(headlinesAdapter);

        autoScrollViewPager.setInterval(3000);
        autoScrollViewPager.startAutoScroll(5000);

        newsViewModel.getHeadlines().observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                if (newsResponse != null) {
                    Log.d("TAG123", "onChanged: " + newsResponse.getArticles().toString());
                    headlinesAdapter.setmResources(newsResponse.getArticles());
                }
            }
        });

        setupTabs();

    }

    private void setupTabs() {

        homeViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), 3));

        homeTabLayout.setupWithViewPager(homeViewPager);

    }

    @Override
    public void transformPage(@NonNull View view, float v) {
        final float normalizedposition = Math.abs(Math.abs(v) - 1);
        view.setScaleX(normalizedposition / 2 + 0.5f);
        view.setScaleY(normalizedposition / 2 + 0.5f);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new HomeDiscoverFragment();
                case 1:
                    return new HomeSourceFragment();
                case 2:
                    return new HomeLocalFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Discover";
                case 1:
                    return "Source";
                case 2:
                    return "Local";
                default:
                    return "";
            }
        }
    }
}
