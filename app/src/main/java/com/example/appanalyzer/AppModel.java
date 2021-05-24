package com.example.appanalyzer;

import java.util.List;

public class AppModel {
        private String name, description;
        private int rating;
        private List<String> apps;
        private String icon;


        public AppModel() {

        }

        public AppModel(String name, String description, int rating, List<String>apps) {
            this.name = name;
            this.description = description;
            this.rating = rating;
            this.apps = apps;
        }

        public String getName() {
            return name;
        }

        public void setName(String date) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) { this.rating = rating; }

        public List<String> getApps() { return apps; }

        public void setApps(List<String> apps) { this.apps = apps; }

}


