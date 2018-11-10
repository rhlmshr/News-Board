package rahulmishra.app.newsboard.service.model;

public class NewsSearchRequest {
    private String apiKey;
    private String query;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
