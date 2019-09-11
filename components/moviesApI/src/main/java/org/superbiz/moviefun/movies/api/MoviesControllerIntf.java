package org.superbiz.moviefun.movies.api;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MoviesControllerIntf {
    @PostMapping
    void addMovie(@RequestBody MovieIntf movie);

    @DeleteMapping("/{movieId}")
    void deleteMovieId(@PathVariable Long movieId);

    @GetMapping("/count")
    int count(
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "key", required = false) String key
    );

    @GetMapping
    List<MovieIntf> find(
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "start", required = false) Integer start,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    );
}
