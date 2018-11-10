package rahulmishra.app.newsboard.service;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import rahulmishra.app.newsboard.NewsBoardApp;
import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.service.model.NewsRequest;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.service.net.DataRepository;
import rahulmishra.app.newsboard.util.GeneralUtils;

public class NotificationJobSchedule extends JobService {
    @Override
    public boolean onStartJob(final JobParameters job) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                NewsRequest newsRequest = new NewsRequest();
                newsRequest.setCountry("in");
                newsRequest.setApiKey(NewsBoardApp.instance().getString(R.string.api_key));

                Log.d("TAG131", "onChanged: ");
                DataRepository.instance().getHeadlines(newsRequest).observeForever(new Observer<NewsResponse>() {
                    @Override
                    public void onChanged(@Nullable NewsResponse newsResponse) {
                        if (newsResponse != null && newsResponse.getStatus().equalsIgnoreCase("ok")) {
                            for (ArticleStructure curr : newsResponse.getArticles()) {
                                final ArticleStructure n = curr;
                                if (n.getTitle() != null && n.getDescription() != null && n.getUrl() != null && n.getUrlToImage() != null) {

                                    Glide.with(getApplicationContext()).asBitmap().load(n.getUrlToImage()).into(new Target<Bitmap>() {
                                        @Override
                                        public void onLoadStarted(@Nullable Drawable placeholder) {

                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            GeneralUtils.scheduleJob(n.getTitle(), n.getDescription(), resource,
                                                    n.getUrl(), NotificationJobSchedule.this);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }

                                        @Override
                                        public void getSize(@NonNull SizeReadyCallback cb) {

                                        }

                                        @Override
                                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                                        }

                                        @Nullable
                                        @Override
                                        public Request getRequest() {
                                            return null;
                                        }

                                        @Override
                                        public void setRequest(@Nullable Request request) {

                                        }

                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onStop() {

                                        }

                                        @Override
                                        public void onDestroy() {

                                        }
                                    });
                                }
                            }
                        }
                    }
                });

                jobFinished(job, true);

            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
