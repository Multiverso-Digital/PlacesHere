package com.abreuretto.PlacesHere;

/**
 * Created by Belisario on 16/08/2014.
 */


    public class Categoria {
        Integer _empID;
        String _nome;
        String _cat;
        String _google;
        String _foursquare;
        String _nokia;
        String _nokiatipo;
        String _yelp;

        // constructor
        public Categoria() {

        }

        // constructor with parameters
        public Categoria(Integer empID, String nome, String cat, String google, String foursquare, String nokia, String nokiatipo, String yelp) {
            this._empID = empID;
            this._nome = nome;
            this._cat = cat;
            this._google = google;
            this._foursquare = foursquare;
            this._nokia = nokia;
            this._nokiatipo = nokiatipo;
            this._yelp = yelp;

        }

        // All set methods

        public void setId(Integer empID) {
            this._empID = empID;
        }

        public void setnome(String nome) {
            this._nome = nome;
        }

        public void setcat(String cat) {
            this._cat = cat;
        }

        public void setgoogle(String google) {
            this._google = google;
        }

        public void setfoursquare(String foursquare) {
            this._foursquare = foursquare;
        }

        public void setnokia(String nokia) {
            this._nokia = nokia;
        }

        public void setnokiatipo(String nokiatipo) {
            this._nokiatipo = nokiatipo;
        }

        public void setyelp(String yelp) {
            this._yelp = yelp;
        }



        // All get methods

        public Integer getId() {
            return this._empID;
        }

        public String getnome() {
            return this._nome;
        }

        public String getcat() {
            return this._cat;
        }

        public String getgoogle() {
            return this._google;
        }

        public String getfoursquare() {
            return this._foursquare;
        }

        public String getnokia() {
            return this._nokia;
        }

        public String getnokiatipo() {
            return this._nokiatipo;
        }

        public String getyelp() {
            return this._yelp;
        }


        }





