package com.abreuretto.PlacesHere;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Belisario on 16/08/2014.
 */
    public class CategoriaXmlParser {

        // names of the XML tags
        static final String CATEGORIAS = "categorias";
        static final String CATEGORIA = "categoria";
        static final String ID = "id";
        static final String NOME = "nome";
        static final String CAT = "cat";
        static final String GOOGLE = "google";
        static final String FOURSQUARE = "foursquare";
        static final String NOKIA = "nokia";
        static final String NOKIATIPO = "nokiatipo";
        static final String YELP = "yelp";

        ArrayList<Categoria> categoriaList = null;
        private Categoria currentCategoria = null;
        private boolean done = false;
        private String currentTag = null;

        public ArrayList<Categoria> parse(Context context) {

            XmlPullParser parser = context.getResources().getXml(R.xml.categorias);

            try {

                int eventType = parser.getEventType();

                // Following logic modified from http://www.ibm.com/developerworks/library/x-android/
                // Also look at http://developer.android.com/training/basics/network-ops/xml.html

                while (eventType != XmlPullParser.END_DOCUMENT && !done) {

                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            categoriaList = new ArrayList<Categoria>();
                            break;
                        case XmlPullParser.START_TAG:
                            currentTag = parser.getName();
                            if (currentTag.equalsIgnoreCase(CATEGORIA)) {
                                currentCategoria = new Categoria();
                            } else if (currentCategoria != null) {
                                if (currentTag.equalsIgnoreCase(ID)) {
                                    currentCategoria.setId(Integer.parseInt(parser.nextText()));
                                    // currentCategoria.setId(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(NOME)) {
                                    currentCategoria.setnome(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(CAT)) {
                                    currentCategoria.setcat(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(GOOGLE)) {
                                    currentCategoria.setgoogle(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(FOURSQUARE)) {
                                    currentCategoria.setfoursquare(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(NOKIA)) {
                                    currentCategoria.setnokia(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(NOKIATIPO)) {
                                    currentCategoria.setnokiatipo(parser.nextText());
                                } else if (currentTag.equalsIgnoreCase(YELP)) {
                                    currentCategoria.setyelp(parser.nextText());

                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            currentTag = parser.getName();
                            if (currentTag.equalsIgnoreCase(CATEGORIA) && currentCategoria != null) {
                                categoriaList.add(currentCategoria);
                            } else if (currentTag.equalsIgnoreCase(CATEGORIAS)) {
                                done = true;
                            }
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return categoriaList;

        }


    }
