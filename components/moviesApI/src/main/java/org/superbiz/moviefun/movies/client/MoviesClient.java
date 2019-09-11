package org.superbiz.moviefun.movies.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;
import org.superbiz.moviefun.movies.api.MovieIntf;
import org.superbiz.moviefun.movies.api.MoviesControllerIntf;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class MoviesClient implements MoviesControllerIntf {

    private String moviesUrl;
    private RestOperations restOperations;

    private static ParameterizedTypeReference<List<MovieIntf>> movieListType = new ParameterizedTypeReference<List<MovieIntf>>() {
    };

    public MoviesClient(String moviesUrl, RestOperations restOperations) {
        this.moviesUrl = moviesUrl;
        this.restOperations = restOperations;
    }

    @Override
    public void addMovie(MovieIntf movie) {
        restOperations.postForEntity(moviesUrl, movie, MovieInfo.class);
    }



    public void deleteMovieId(Long movieId) {
        restOperations.delete(moviesUrl + "/" + movieId);
    }

    public int countAll() {
        return restOperations.getForObject(moviesUrl + "/count", Integer.class);
    }


    public int count(String field, String key) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl + "/count")
            .queryParam("field", field)
            .queryParam("key", key);

        return restOperations.getForObject(builder.toUriString(), Integer.class);
    }




    public List<MovieIntf> findAll(int start, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
            .queryParam("start", start)
            .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    @Override
    public List<MovieIntf> find(String field, String key, Integer start, Integer pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
            .queryParam("field", field)
            .queryParam("key", key)
            .queryParam("start", start)
            .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    public List<MovieIntf> getMovies() {
        return restOperations.exchange(moviesUrl, GET, null, movieListType).getBody();
    }
}
