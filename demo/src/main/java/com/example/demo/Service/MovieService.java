package com.example.demo.Service;

import com.example.demo.DTOs.MoviesTf;
import com.example.demo.Entities.Movies;
import com.example.demo.Repositories.MovieRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepo movieRepo;

    public Movies getMovie(Long id){
        return movieRepo.findById(id).orElseThrow(NullPointerException::new);
    }

    public int countMovies(String query, String genre){
        return movieRepo.count(query, genre);
    }
    public List<Movies> getMoviesList( int page, int size, String query, String genre){
        return movieRepo.getList(PageRequest.of(page,size), query, genre);
    }

    public List<Movies> highRatingMovies(){
        return movieRepo.getHighRating();
    }

    public List<String> getAllGenres() {
        var allGenres = movieRepo.findAllGenres();
        return allGenres.stream()
                .map(g-> g.split(","))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<MoviesTf> recommendedMovies(Long id) {
        var movie = movieRepo.findById(id).orElseThrow(NullPointerException::new);
        var allMoviesProfile = movieRepo.getMoviesByYear(movie.getReleasedYear());
        var ifIdf = createTfIdf(movie, allMoviesProfile);
        var movieTf = ifIdf.stream().filter((m) -> m.getMovies().getSeriesTitle().equals(movie.getSeriesTitle())).toList().get(0);
        var list = new ArrayList<>(ifIdf.stream().peek((m) -> m.setSimilarity(calculateCosineSimilarity(movieTf, m))).toList());
        //Print matrix
//        StringBuilder res= new StringBuilder();
//        for (var mo : list){
//            for (var el : mo.getScoreList()) res.append(el).append(" ");
//            res.append("\n");
//        }
//        System.out.println(res);
        list.sort(Comparator.comparing(MoviesTf::getSimilarity).reversed());
        list.remove(0);
        return list.stream().limit(10).toList();
    }

    @SneakyThrows
    @Transactional
    public void addMovie(Movies movie) {
        if(movieRepo.findBySeriesTitle(movie.getSeriesTitle()).isPresent()) throw new ClassNotFoundException("Phim đã tồn tại");
        movieRepo.save(movie);
    }

    private List<MoviesTf> createTfIdf(Movies movie, List<Movies> listMovies){
        movie.profileInit();
        var dictionary = movie.getProfile();
        List<MoviesTf> result = new ArrayList<>();
        listMovies.forEach(Movies::profileInit);
        var allWords = listMovies.stream().map(Movies::getProfile).flatMap(Collection::stream).toList();
        for (var word : dictionary){
            log.info("Word: {}", word);
            var idf = calculateIdf(listMovies.size(),
                    (int) listMovies.stream()
                            .filter((m) -> m.checkHaveWord(word)).count());
            for (int m = 0; m < listMovies.size(); m++) {
                var tf = calculateTf(word, listMovies.get(m).getProfile(), allWords);
                if(result.size() <= m) {
                    var movieTf = MoviesTf.builder().movies(listMovies.get(m)).build();
                    movieTf.putScore(tf * idf);
                    result.add(movieTf);
                } else {
                    result.get(m).putScore(tf * idf);
                }
            }
        }
        return result;
    }

    private double calculateTf(String word, List<String> profile, List<String> allWords){
        var count = profile.stream().filter((w) -> w.equals(word)).count();
        var max = allWords.stream().filter((w) -> w.equals(word)).count();
        return (double) count /max;
    }

    private double calculateIdf(int total, int count){
        return Math.log((double) total/count);
    }

    private double calculateCosineSimilarity(MoviesTf movie1, MoviesTf movie2) {
        var a = getAbove(movie1, movie2);
        var b = getUnder(movie1, movie2);
        return a/b;
    }

    private double getAbove(MoviesTf movie1, MoviesTf movie2){
        double result = 0;
        for( int i = 0; i< movie1.getScoreList().size(); i++)
            result += (movie1.getScoreList().get(i) * movie2.getScoreList().get(i));
        return result;
    }

    private double getUnder(MoviesTf movie1, MoviesTf movie2) {
        double x1 = movie1.getScoreList().stream().mapToDouble((score) -> Math.pow(score, 2)).sum();
        double x2 = movie2.getScoreList().stream().mapToDouble((score) -> Math.pow(score, 2)).sum();
        var res = Math.sqrt(x1 * x2);
        if (res != 0) return res;
        return 1;
    }

}
