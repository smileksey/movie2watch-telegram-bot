package com.smileksey.movie2watch.models.kinopoiskmodels;

import java.util.List;

public class PaginatedResponse {
    private List<Movie> docs;
    private int total;
    private int limit;
    private int page;
    private int pages;

    public List<Movie> getDocs() {
        return docs;
    }

    public void setDocs(List<Movie> docs) {
        this.docs = docs;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PaginatedResponse{" +
                "docs=" + docs +
                ", total=" + total +
                ", limit=" + limit +
                ", page=" + page +
                ", pages=" + pages +
                '}';
    }
}
