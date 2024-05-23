package com.example.demo;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class Main {
    static class Pipeline{
        private static Properties properties;
        private static StanfordCoreNLP stanfordCoreNLP;
        private Pipeline(){}

        static {
            properties = new Properties();
            String propertiesName = "tokenize, ssplit, pos, lemma";
            String filePath = "C:/Users/DungNT/Downloads/english-left3words-distsim.tagger";
            properties.setProperty("annotators", propertiesName);
            properties.setProperty("pos.model",filePath);
        }

        public static StanfordCoreNLP getPipeLine(){
            if (stanfordCoreNLP == null){
                stanfordCoreNLP = new StanfordCoreNLP(properties);
            }
            return stanfordCoreNLP;
        }
    }
    public static void main(String[] args) {
        var pipeline = Pipeline.getPipeLine();
        String text = "Let goes";
        var doc = new CoreDocument(text);
        pipeline.annotate(doc);
        var tokens = doc.tokens();
        for (var i:tokens){
            System.out.println(i.originalText() + ":" + i.lemma());
        }
    }

}