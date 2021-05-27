package com.example.appanalyzer;

import java.util.List;

public class AppModel {
        private String name, description;
        private double rating;
        private List<String> apps;
        private String iconURL;


        public AppModel() {

        }

        public AppModel(String name, String description, String iconURL, int rating, List<String>apps) {
            this.name = name;
            this.description = description;
            this.rating = rating;
            this.apps = apps;
            this.iconURL = iconURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) { this.name = name; }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) { this.rating = rating; }

        public List<String> getApps() { return apps; }

        public void setApps(List<String> apps) { this.apps = apps; }

        public String getIconURL (){return iconURL;}

        public void setIconURL(String iconURL) {this.iconURL = iconURL;}

}


