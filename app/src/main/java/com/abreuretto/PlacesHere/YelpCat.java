package com.abreuretto.PlacesHere;

/**
 * Created by Belisario on 22/08/2014.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YelpCat {

    private List<Categorium> categoria = new ArrayList<Categorium>();
    private String alias;
    private String title;
    private List<Category> category = new ArrayList<Category>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Categorium> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<Categorium> categoria) {
        this.categoria = categoria;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}