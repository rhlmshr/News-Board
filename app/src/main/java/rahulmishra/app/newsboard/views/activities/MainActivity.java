package rahulmishra.app.newsboard.views.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.NotificationJobSchedule;
import rahulmishra.app.newsboard.util.NoSwipePager;
import rahulmishra.app.newsboard.views.adapters.BottomAdapter;
import rahulmishra.app.newsboard.views.fragments.FavoriteFragment;
import rahulmishra.app.newsboard.views.fragments.HomeFragment;
import rahulmishra.app.newsboard.views.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private NoSwipePager mainViewPager;
    private AHBottomNavigation bottomNavigation;
    private BottomAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setupBottomNav();
        setupViewPager();
        setJobs();
    }

    private void init() {
        mainViewPager = findViewById(R.id.main_viewpager);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNav() {
        AHBottomNavigationItem homeItem =
                new AHBottomNavigationItem("Home",
                        R.drawable.home);

        AHBottomNavigationItem searchItem =
                new AHBottomNavigationItem("Search",
                        R.drawable.search);

        AHBottomNavigationItem favsItem =
                new AHBottomNavigationItem("Favorites",
                        R.drawable.favorites);

        bottomNavigation.addItem(homeItem);
        bottomNavigation.addItem(searchItem);
        bottomNavigation.addItem(favsItem);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#b5dbea"));
        bottomNavigation.setBehaviorTranslationEnabled(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected)
                    mainViewPager.setCurrentItem(position);
                return true;
            }
        });
    }

    private void setupViewPager() {
        mainViewPager.setPagingEnabled(false);
        pagerAdapter = new BottomAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(new HomeFragment());
        pagerAdapter.addFragments(new SearchFragment());
        pagerAdapter.addFragments(new FavoriteFragment());

        mainViewPager.setAdapter(pagerAdapter);
    }

    private void setJobs() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job appInfoJob = dispatcher.newJobBuilder()
//                .setService(NotificationJobSchedule.class)
//                .setTag("notification_service")
//                .setTrigger(Trigger.NOW)
//                .setReplaceCurrent(true)
//                .build();

        Job appInfoJob = dispatcher.newJobBuilder()
                .setService(NotificationJobSchedule.class)
                .setTag("notification_service")
                .setTrigger(Trigger.executionWindow(60 * 60 * 24, 60 * 60 * 24 + 60))
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(false)
                .build();

        dispatcher.mustSchedule(appInfoJob);

    }
}
