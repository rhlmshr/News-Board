package rahulmishra.app.newsboard.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DictResponse {

    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("results")
    @Expose
    private List<DictResultStruct> results = null;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<DictResultStruct> getResults() {
        return results;
    }

    public void setResults(List<DictResultStruct> results) {
        this.results = results;
    }
}
