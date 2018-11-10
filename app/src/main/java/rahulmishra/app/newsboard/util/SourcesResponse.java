package rahulmishra.app.newsboard.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rahulmishra.app.newsboard.service.model.SourcesStructure;

public class SourcesResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sources")
    @Expose
    private List<SourcesStructure> sources = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SourcesStructure> getSources() {
        return sources;
    }

    public void setSources(List<SourcesStructure> sources) {
        this.sources = sources;
    }
}
