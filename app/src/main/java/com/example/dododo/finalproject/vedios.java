package com.example.dododo.finalproject;

import java.util.List;

/**
 * Created by DoDo DO on 06/10/2015.
 */
public class vedios {
    private int id;
    private List<ResultsEntity> results;

    public void setId(int id) {
        this.id = id;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {


        private String id;
        private String iso_639_1;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public ResultsEntity(String ttt) {
            this.type = ttt;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }
    }
}
