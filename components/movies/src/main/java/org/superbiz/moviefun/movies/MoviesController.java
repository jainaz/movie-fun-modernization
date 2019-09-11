package org.superbiz.moviefun.movies;

import org.springframework.web.bind.annotation.*;
import org.superbiz.moviefun.movies.api.MovieIntf;
import org.superbiz.moviefun.movies.api.MoviesControllerIntf;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController implements MoviesControllerIntf {

    private MoviesRepository moviesRepository;

    public MoviesController(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    @PostMapping
    public void addMovie(@RequestBody MovieIntf movie) {
        moviesRepository.addMovie(movie);
    }

    @Override
    @DeleteMapping("/{movieId}")
    public void deleteMovieId(@PathVariable Long movieId) {
        moviesRepository.deleteMovieId(movieId);
    }

    @Override
    @GetMapping("/count")
    public int count(
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "key", required = false) String key
    ) {
        if (field != null && key != null) {
            return moviesRepository.count(field, key);
        } else {
            return moviesRepository.countAll();
        }
    }

    @Override
    @GetMapping
    public List<MovieIntf> find(
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "start", required = false) Integer start,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        if (field != null && key != null) {
            return moviesRepository.findRange(field, key, start, pageSize);
        } else if (start != null && pageSize != null) {
            return moviesRepository.findAll(start, pageSize);
        } else {
            return moviesRepository.getMovies();
        }
    }
}
