package com.example.finnkino;

import android.content.Context;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MovieTheaters {
    private static List<Theater> theaters = new ArrayList<Theater>();
    private static MovieTheaters instance = new MovieTheaters();
    public static Integer[] ALL_IDS = new Integer[]{1014, 1015, 1016, 1017, 1041, 1018, 1019, 1021, 1022};

    public static MovieTheaters getInstance() {
        return instance;
    }

    private MovieTheaters() {}

    public void readXML () {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String url = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Integer id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    String name = element.getElementsByTagName("Name").item(0).getTextContent();
                    if (id != null && name != null) {
                        Theater theater = new Theater(id, name);
                        theaters.add(theater);
                    }
                }
            }
        } catch(IOException e){
                e.printStackTrace();
        } catch(SAXException e){
                e.printStackTrace();
        } catch(ParserConfigurationException e){
                e.printStackTrace();
        }
    }

    public String[] getNames() {
        String[] names = new String[theaters.size()];
        for (int i = 0; i < theaters.size(); i++) {
                names[i] = theaters.get(i).getName();
        }
        return names;
    }

    public Integer getId(String name) {
        for (Theater theater : theaters) {
            if (theater.getName().equals(name)) {
                return theater.getId();
            }
        }
        return null;
    }
}
