package com.example.demo.Entities;

import com.example.demo.DTOs.MoviesTf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String seriesTitle;
    private Integer releasedYear;
    private String genre;
    private double imdbRating;
    private String director;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private String posterLink;
    private String overview;

    @Transient
    @JsonIgnore
    private List<String> profile;

    public List<String> overviewSplitting(){
        return Arrays.asList(
                        Arrays
                                .stream(overview.toLowerCase()
                                        .replaceAll("[\\p{Punct}]","")
                                        .split(" "))
                                .filter((w) -> w.length() > 3)
                                .toArray(String[]::new));
    }

    public void profileInit(){
        this.profile = overviewSplitting();
        List<String> temp = new ArrayList<>(this.profile);
        temp.addAll(Stream.of(
                director,
                star1,
                star2,
                star3,
                star4)
                .toList());
        temp.addAll(Arrays.
                stream(genre.split(","))
                .map(String::trim)
                .toList());
        this.profile = temp;
    }

    public boolean checkHaveWord(String word){
        return profile.contains(word);
    }

    public int countWord(String word){
        return (int) profile.stream().filter((w) -> w.equals(word)).count();
    }
}
