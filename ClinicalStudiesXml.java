// package com.zetcode;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ClinicalStudiesXml {

    public static void main(String argv[]) throws SAXException,
            IOException, ParserConfigurationException {
        
        String country="not set";
        String nct_id="not set";
        String brief_title="not set";
        String study_type="not set";
        String condition="not set";

        try{
        
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
            System.out.println("Connected!");
        
            File folder = new File(argv[0]);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
            

                if (file.isFile()) {
                // System.out.println(file.getName());
                
                    File xmlFile = new File(argv[0]+"/"+file.getName());
                    
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = factory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);

                    doc.getDocumentElement().normalize();


                    //<INSERTS>
                    String query = " INSERT INTO countries (UID, TITLE, COUNTRY, TYPE)"
                            + " values (?, ?, ?, ?)";
                    PreparedStatement preparedStmt_countries = con.prepareStatement(query);

                    query = " INSERT INTO conditions (UID, COND)"
                            + " values (?, ?)";

                    PreparedStatement preparedStmt_conditions = con.prepareStatement(query);


                    NodeList nList = doc.getElementsByTagName("clinical_study");

                    for (int i = 0; i < nList.getLength(); i++) {

                        Node nNode = nList.item(i);

                        Element elem = (Element) nNode;

                        NodeList itemList = elem.getElementsByTagName("id_info");
            
                        for (int j = 0; j < itemList.getLength(); j++) 
                        {
                            Node itemNode = itemList.item(j);
                            Element eElement = (Element) itemNode;
                            Element cElement =  (Element) eElement.getElementsByTagName("nct_id").item(0);
                            nct_id = cElement.getTextContent();
                        }

                        NodeList countryList = elem.getElementsByTagName("location_countries");
            
                        for (int j = 0; j < countryList.getLength(); j++) 
                        {
                            Node itemNode = countryList.item(j);
                            Element eElement = (Element) itemNode;
                            Element cElement =  (Element) eElement.getElementsByTagName("country").item(0);
                            country = cElement.getTextContent();
                        }

                
                    

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {


                        

                            Node node1 = elem.getElementsByTagName("brief_title").item(0);
                            brief_title = node1.getTextContent();

                            NodeList Conditions = elem.getElementsByTagName("condition");
                            
                            for (int k = 0; k < Conditions.getLength(); k++) {
                                condition = Conditions.item(k).getTextContent();
                                preparedStmt_conditions.setString (1, nct_id);
                                preparedStmt_conditions.setString (2, condition);
                                preparedStmt_conditions.execute();
                            }

                            Node node3 = elem.getElementsByTagName("study_type").item(0);
                            study_type = node3.getTextContent();

                        
                            

                            
                            preparedStmt_countries.setString (1, nct_id);
                            preparedStmt_countries.setString (2, brief_title);
                            preparedStmt_countries.setString (3, country);
                            preparedStmt_countries.setString (4, study_type);
                            preparedStmt_countries.execute();
                            
                        }

                   
                    

                   
                }
            }
        
        }
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Exception " + e);
        }
    }

    
}