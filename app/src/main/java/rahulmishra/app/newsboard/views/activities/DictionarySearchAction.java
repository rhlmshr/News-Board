package rahulmishra.app.newsboard.views.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.DictResponse;
import rahulmishra.app.newsboard.service.model.DictResultStruct;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;

public class DictionarySearchAction extends AppCompatActivity {

    private NewsViewModel newsViewModel;
    private TextView dictWord;
    private ProgressBar dictProgress;
    private ListView dictResult;
    private ArrayAdapter<SpannableString> arrayAdapter;
    private List<SpannableString> meaningsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search_action);

        String search = null;

        if (Intent.ACTION_PROCESS_TEXT.equals(getIntent().getAction())) {
            search = getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT);

            if (search == null) {
                search = getIntent()
                        .getStringExtra(Intent.EXTRA_PROCESS_TEXT_READONLY);
            }
        }

        init();

        dictProgress.setVisibility(View.VISIBLE);

        newsViewModel.getWordMeaning(search).observe(this, new Observer<DictResponse>() {
            @Override
            public void onChanged(@Nullable DictResponse dictResponse) {
                if (dictResponse != null && dictResponse.getResults() != null) {
                    dictWord.setText(dictResponse.getWord());
                    for (DictResultStruct d : dictResponse.getResults()) {
                        String value = d.getPartOfSpeech().charAt(0)
                                + ". " + d.getDefinition();
                        SpannableString string = new SpannableString(value);

                        string.setSpan(new StyleSpan(Typeface.ITALIC), 0, 2, 0);

                        meaningsList.add(string);
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    dictWord.setText("Word not found. Try another!");
                }
                dictProgress.setVisibility(View.GONE);
            }
        });

        arrayAdapter = new ArrayAdapter<>(DictionarySearchAction.this, android.R.layout.simple_list_item_1, meaningsList);
        dictResult.setAdapter(arrayAdapter);

    }

    private void init() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        dictWord = findViewById(R.id.dict_word);
        dictProgress = findViewById(R.id.dict_progress_bar);
        dictResult = findViewById(R.id.dict_results);
        meaningsList = new ArrayList<>();

    }

    @Override
    public void onStart() {
        super.onStart();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        if (getWindow() != null) {
            getWindow().setLayout(width, height);
//            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }
}
