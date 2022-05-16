package com.abreuretto.PlacesHere;

/**
 * Created by Belisario on 17/08/2014.
 */
public class CatTipo {

    Integer _ID;
    String _nome;

    // constructor
    public CatTipo() {

    }

    // constructor with parameters
    public CatTipo(Integer ID, String nome) {
        this._ID = ID;
        this._nome = nome;

    }

    // All set methods

    public void setId(Integer ID) {
        this._ID = ID;
    }

    public void setnome(String nome) {
        this._nome = nome;
    }




    // All get methods

    public Integer getId() {
        return this._ID;
    }

    public String getnome() {
        return this._nome;
    }






}
