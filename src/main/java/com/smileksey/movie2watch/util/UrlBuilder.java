package com.smileksey.movie2watch.util;


public class UrlBuilder {
    public static String buildUrl(String genre, String year, String rating) {
        StringBuilder url = new StringBuilder("https://api.kinopoisk.dev/v1.3/movie?selectFields=id name alternativeName year description poster countries genres videos rating isSeries&limit=1&name=!null&description=!null");
        url.append("&genres.name=")
                .append(genre)
                .append("&year=")
                .append(year)
                .append("&rating.imdb=")
                .append(rating)
                .append("-10.0");

        if (!genre.equals("короткометражка"))
            url.append("&genres.name=!короткометражка");

        return url.toString();
    }

    public static String getKinopoiskUrl(int id) {
        return "https://www.kinopoisk.ru/film/" + id;
    }
}
