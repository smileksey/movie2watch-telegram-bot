package com.smileksey.movie2watch;

import com.smileksey.movie2watch.models.kinopoiskmodels.Genre;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.models.kinopoiskmodels.PaginatedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Component
public class KinopoiskApi {

    @Value("${api.headername}")
    private String apiHeaderName;

    @Value("${api.token}")
    private String apiToken;

    private HttpEntity<Void> constructGetRequest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(apiHeaderName, apiToken);

        return new HttpEntity<>(httpHeaders);
    }

    public Movie getRandomMovie() {
        String url = "https://api.kinopoisk.dev/v1.3/movie/random";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Movie> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), Movie.class);
        Movie movie = response.getBody();

        return movie;
    }

    public Movie getRandomMovieByUrl(String url) {
        try {
            int randomPage = getRandomPage(url);
            String resultUrl = url + "&page=" + randomPage;

            PaginatedResponse paginatedResponse = getPaginatedResponse(resultUrl);

            Movie movie = paginatedResponse.getDocs().get(0);

            return movie;

        } catch (Exception e) {
            return null;
        }
    }

    private int getRandomPage(String url) {
        PaginatedResponse paginatedResponse = getPaginatedResponse(url);
        int pageCount = paginatedResponse.getPages();

        Random random = new Random();

        return random.nextInt(pageCount) + 1;
    }

    private PaginatedResponse getPaginatedResponse(String url) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PaginatedResponse> paginatedResponse = restTemplate.exchange(url, HttpMethod.GET,
                constructGetRequest(), PaginatedResponse.class);

        return paginatedResponse.getBody();
    }

    public Movie getMovieById(int id) {
        String url = "https://api.kinopoisk.dev/v1.3/movie/" + id;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Movie> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), Movie.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String getPossibleGenres() {
        List<Genre> possibleGenresList;
        String url = "https://api.kinopoisk.dev/v1/movie/possible-values-by-field?field=genres.name";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Genre>> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), new ParameterizedTypeReference<List<Genre>>() {});

        possibleGenresList = response.getBody();

        StringBuilder builder = new StringBuilder();

        if (possibleGenresList != null && !possibleGenresList.isEmpty())
            possibleGenresList.forEach(genre -> builder.append(genre.getName()).append("\n"));

        return builder.toString();
    }


}
