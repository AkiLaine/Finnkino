package com.example.finnkino;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Movies {
        private static Movies instance = new Movies();
        private static ArrayList<Movie> movies = new ArrayList<Movie>();
        private static ArrayList<Movie> filteredMovies = new ArrayList<Movie>();
        private static String url = "https://www.finnkino.fi/xml/Schedule/?area=%d&dt=%s";

        public static Movies getInstance() {
            return instance;
        }

        private Movies() {
        }

        public void readAllMovies(Integer theaterId, String date) {
            for (Integer id : MovieTheaters.ALL_IDS) {
                readMovies(id, date);
            }
        }

        public void readMovies(Integer theaterId, String date) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                String movieurl = String.format(url, theaterId, date);
                Document doc = builder.parse(movieurl);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getDocumentElement().getElementsByTagName("Show");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        Integer movieId = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                        String movieName = element.getElementsByTagName("Title").item(0).getTextContent();
                        String startTimeUnformatted = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                        String endTimeUnformatted = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                        String theatre = element.getElementsByTagName("Theatre").item(0).getTextContent();
                        String auditorium = element.getElementsByTagName("TheatreAuditorium").item(0).getTextContent();
                        String picture = element.getElementsByTagName("EventSmallImageLandscape").item(0).getTextContent();
                        if (picture != null) {
                            // Fix to https
                            if (picture.startsWith("http://")) {
                                picture = "https://" + picture.substring(7);
                            }
                            if (movieId != null && movieName != null && theatre != null && auditorium != null && startTimeUnformatted != null && endTimeUnformatted != null) {
                                //formatting dates
                                Date startTime = null;
                                Date endTime = null;
                                try {
                                    startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(startTimeUnformatted);
                                    endTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(endTimeUnformatted);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Movie movie = new Movie(movieId, movieName, theatre, auditorium, startTime, endTime, picture);
                                movies.add(movie);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }


    public List<Movie> filterMovies(SearchParameters parameters) {
        //System.out.println("ID ON: " + parameters.theaterId + " ELOKUVA ON: " + parameters.movieName + " ALOITUSAIKA ON: " + parameters.startTime + " LOPETUSAIKA ON: " + parameters.endTime);
        System.out.println("ELOKUVIA JOITA SUODATETAAN ON: " + movies.size());
        filteredMovies.clear();
        Date startTime = null;
        Date endTime = null;
        try {
        if (!parameters.startTime.isEmpty()) {
            startTime = new SimpleDateFormat("dd.MM.yyyy,HH:mm").parse(parameters.date + "," + parameters.startTime);
        }
        if (!parameters.endTime.isEmpty()) {
            endTime = new SimpleDateFormat("dd.MM.yyyy,HH:mm").parse(parameters.date + "," + parameters.endTime);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Go through every movie
        for (Movie movie : movies) {
            // Check start time
            if (!(startTime == null || startTime.before(movie.getStartTime()))) {}
            // Check end time
            else if (!(endTime == null || endTime.after(movie.getEndTime()))) {}
            // Check movie name
            else if (!parameters.movieName.isEmpty() && !movie.getMovieName().toLowerCase().contains(parameters.movieName.toLowerCase())) {}
            // Add to filtered movies if everything passes
            else {
                filteredMovies.add(movie);
            }
        } System.out.println("Elokuvia jotka vastasivat hakua: " + filteredMovies.size());
        return filteredMovies;
    }

    public void resetMovies() {
        movies.clear();
        filteredMovies.clear();
    }
    public List getMovies() {
            return filteredMovies;
    }

    static class SearchParameters {
        Integer theaterId;
        String date;
        String startTime;
        String endTime;
        String movieName;

        public SearchParameters(Integer theaterId, String date, String startTime, String endTime, String movieName) {
            this.theaterId = theaterId;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.movieName = movieName;
        }
    }
}
