package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
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

    @Transient
    private static Properties properties;
    static {
        properties = new Properties();
        String propertiesName = "tokenize, ssplit, pos, lemma";
        String filePath = "C:/Users/DungNT/Downloads/english-left3words-distsim.tagger";
        properties.setProperty("annotators", propertiesName);
        properties.setProperty("pos.model",filePath);

    }
    /**
     * <B> Xử lý mô tả của phim:</B>
     *  <li> Bỏ mạo từ bất định như a, an , the</li>
     *  <li> Lemmat hóa</li>
     *  <li> Loại bỏ dấu câu</li>
     *  <li> Split thành array</li>
     */
    public List<String> overviewSplitting(){

        var pipeline = new StanfordCoreNLP(properties);
        var text = this.overview;
        var doc = new CoreDocument(text);
        pipeline.annotate(doc);
        var tokens = doc.tokens();
        StringBuilder lemmatizedSentence = new StringBuilder();
        for ( var i:tokens){
            lemmatizedSentence.append(i.lemma()).append(" ");
        }
        return Arrays.asList(
                Arrays
                                .stream(lemmatizedSentence.toString()
                                        .replaceAll("\\p{Punct}","")
                                        .split(" "))
                                .filter((w) -> w.length() > 3)
                                .toArray(String[]::new));
    }

    /**
     * <B>Trả về đặc trưng của phim</B>
     */
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
