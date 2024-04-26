package com.example.demo.Repositories;

import com.example.demo.Entities.Movies;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MovieRepo extends JpaRepository<Movies, Long> {

    @Query("""
            SELECT m from Movies m
            WHERE m.seriesTitle LIKE :query
            AND (m.genre like :genre OR :genre = '')
            ORDER BY m.releasedYear DESC
            """)
    List<Movies> getList(Pageable pageable, String query, String genre);

    @Query("SELECT m from Movies m ORDER BY m.imdbRating desc limit 10")
    List<Movies> getHighRating();

    @Query("SELECT m from Movies m where m.seriesTitle=:seriesTitle")
    Optional<Movies> findBySeriesTitle(String seriesTitle);

    @Query("SELECT m from Movies m where m.releasedYear between :year -20 and :year + 20")
    List<Movies> getMoviesByYear(int year);

    @Query("SELECT distinct m.genre from Movies m ORDER BY m.genre ASC")
    List<String> findAllGenres();

    @Query("""
            SELECT count(m) from Movies m
            WHERE m.seriesTitle LIKE :query
            AND (m.genre like :genre OR :genre = '')
            """)
    Integer count(String query, String genre);
}
