package com.smileksey.movie2watch;

import com.smileksey.movie2watch.models.kinopoiskmodels.Genre;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.models.kinopoiskmodels.PaginatedResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Component
public class KinopoiskApi {

    private final static String HORROR_FILTERED_URL = "https://api.kinopoisk.dev/v1/movie?selectFields=id&limit=1&name=!null&year=1980-2030&description=!null&rating.imdb=5.5-10.0&genres.name=ужасы";

    private HttpEntity<Void> constructGetRequest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-API-KEY", "4J4GAM8-7PDMX6P-NPRW8Q0-1WSM2VC");

        return new HttpEntity<>(httpHeaders);
    }

    public Movie getRandomMovie() {
        String url = "https://api.kinopoisk.dev/v1/movie/random";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Movie> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), Movie.class);
        Movie movie = response.getBody();
        System.out.println(movie.getId());

        return movie;
    }

    public Movie getRandomMovieByUrl(String url) {
        try {
            int randomPage = getRandomHorrorPage(url);
            String resultUrl = url + "&page=" + randomPage;
            //
            System.out.println(resultUrl);

            PaginatedResponse paginatedResponse = getPaginatedResponse(resultUrl);

            Movie movie = paginatedResponse.getDocs().get(0);
            System.out.println(movie.getId());

            return movie;

        } catch (Exception e) {
            return null;
        }
    }

    public Movie getRandomHorrorMovie() {
        int randomPage = getRandomHorrorPage(HORROR_FILTERED_URL);
        String url =
                "https://api.kinopoisk.dev/v1/movie?selectFields=id name alternativeName year description poster countries genres videos rating&limit=1&name=!null&year=1980-2030&description=!null&rating.imdb=5.5-10.0&genres.name=ужасы&page="
                        + randomPage;

        PaginatedResponse paginatedResponse = getPaginatedResponse(url);

        Movie movie = paginatedResponse.getDocs().get(0);
        System.out.println(movie.getId());

        return movie;
    }

    private int getRandomHorrorPage(String url) {
        PaginatedResponse paginatedResponse = getPaginatedResponse(url);
        int pageCount = paginatedResponse.getPages();

        Random random = new Random();

//        int randomPage = (random.nextInt() * (pageCount - 1)) + 1;
        int randomPage = random.nextInt(pageCount) + 1;

        return randomPage;
    }

    private PaginatedResponse getPaginatedResponse(String url) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PaginatedResponse> paginatedResponse = restTemplate.exchange(url, HttpMethod.GET,
                constructGetRequest(), PaginatedResponse.class);

        return paginatedResponse.getBody();
    }


    public Movie getMovieById(int id) {
        String url = "https://api.kinopoisk.dev/v1/movie/" + id;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Movie> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), Movie.class);
        Movie movie = response.getBody();

        return movie;
    }

    public String getPossibleGenres() {
        List<Genre> possibleGenresList;
        String url = "https://api.kinopoisk.dev/v1/movie/possible-values-by-field?field=genres.name";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Genre>> response = restTemplate.exchange(url, HttpMethod.GET, constructGetRequest(), new ParameterizedTypeReference<List<Genre>>() {
        });

        possibleGenresList = response.getBody();

        StringBuilder builder = new StringBuilder();

        if (possibleGenresList != null && !possibleGenresList.isEmpty())
            possibleGenresList.forEach(genre -> builder.append(genre.getName()).append("\n"));

        return builder.toString();
    }

//    private int getRandomHorrorMovieId(int randomPage) {
//        StringBuilder urlBuilder = new StringBuilder();
//        urlBuilder.append(HORROR_FILTERED_URL)
//                .append("&page=")
//                .append(randomPage);
//
//        PaginatedResponse paginatedResponse = getPaginatedResponse(urlBuilder.toString());
//
//        int id = paginatedResponse.getDocs().get(0).getId();
////        System.out.println(id);
//
//        return id;
//    }





}
