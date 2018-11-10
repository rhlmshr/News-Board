package rahulmishra.app.newsboard.service.model;

public class EventQuery {
    private String query;

    public EventQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
