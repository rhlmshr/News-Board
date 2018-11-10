package rahulmishra.app.newsboard.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.EventQuery;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView collapsingSearchView;
    private AppBarLayout appBarLayout;
    private TabLayout searchTabs;
    private ViewPager searchViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        collapsingSearchView = view.findViewById(R.id.search_collapse_text);
        appBarLayout = view.findViewById(R.id.search_appbar);
        searchTabs = view.findViewById(R.id.search_tabs);
        searchViewPager = view.findViewById(R.id.search_viewpager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        collapsingSearchView.setOnQueryTextListener(this);
        collapsingSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsingSearchView.setIconified(false);
            }
        });
        searchViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), 2));

        searchTabs.setupWithViewPager(searchViewPager);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        EventQuery eventQuery = new EventQuery(s);
        EventBus.getDefault().post(eventQuery);

        if (s.length() > 0)
            setViewsVisibility(View.VISIBLE);
        else
            setViewsVisibility(View.GONE);

        return true;
    }

    public void setViewsVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            searchTabs.setVisibility(View.VISIBLE);
            searchViewPager.setVisibility(View.VISIBLE);
        } else {
            searchTabs.setVisibility(View.GONE);
            searchViewPager.setVisibility(View.INVISIBLE);
        }
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
                    return new SearchResultFragment();
                case 1:
                    return new SearchArchiveFragment();

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
                    return "Results";
                case 1:
                    return "Archive";

                default:
                    return "";
            }
        }
    }
}
