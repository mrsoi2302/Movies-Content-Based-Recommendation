package com.example.demo.DTOs;

import com.example.demo.Entities.Movies;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class MoviesTf {
    private Movies movies;
    @JsonIgnore
    private List<Double> scoreList;
    private Double similarity;
    private String seriesTitle;


    public void putScore ( double value){
        if (this.scoreList == null)
            this.scoreList = new ArrayList<>();
        this.scoreList.add(value);
    }

}
