package com.example.demo.Controller;

import com.example.demo.DTOs.MoviesTf;
import com.example.demo.Entities.Movies;
import com.example.demo.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/search")
    public List<Movies> getMoviesList(@RequestParam(name = "page", defaultValue = "1") int page,@RequestParam(name= "limit", defaultValue = "10") int limit, @RequestParam(name = "query", defaultValue = "") String query, @RequestParam(name = "genre", defaultValue = "") String genre){
        query = "%" + query + "%";
        genre = "%" + genre + "%";
        return movieService.getMoviesList(page,limit,query,genre);
    }

    @GetMapping("/count")
    public int countMovies(@RequestParam(name = "query", defaultValue = "") String query, @RequestParam(name = "genre", defaultValue = "") String genre){
        query = "%" + query + "%";
        genre = "%" + genre + "%";
        return movieService.countMovies(query,genre);
    }
    
    @GetMapping("/genres")
    public List<String> getAllGenres(){
        return movieService.getAllGenres();
    }

    @GetMapping("/{movieId}")
    public Movies getMovie(@PathVariable Long movieId){
        return movieService.getMovie(movieId);
    }

    @GetMapping("/{movieId}/recommend")
    public List<MoviesTf> getRecommend(@PathVariable Long movieId){
        return movieService.recommendedMovies(movieId);
    }

    @GetMapping("/high_rating")
    public List<Movies> getHighRatingMovies(){
        return movieService.highRatingMovies();
    }

    @PostMapping("/add")
    public void createMovie(Movies movie){ movieService.addMovie(movie); }
}
