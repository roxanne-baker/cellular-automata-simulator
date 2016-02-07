import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;

/**
 * @author Sarp Uner
 * 
 * This class processes the XML file required for the configuration of the simulation. 
 *
 */
public class Configuration{
    /**
     * The constant for the XML tag for the root element of the XML file, which should be simulation.
     */
    public static final String SIMULATION = "simulation";
    
    /**
     * The constant for the XML tag of the "name" element.
     */
    public static final String NAME = "name";
    
    /**
     * The constant for the XML tag of the "width" element.
     */
    public static final String WIDTH = "width";
        
    /**
     * The constant for the XML tag of the height element.
     */
    public static final String HEIGHT = "height";
    
    /**
     * The constant for the XML tag of the state element.
     */
    public static final String STATE = "state";
    
    /**
     * The constant for the XML tag of the ProbabilityCatch element.
     */
    public static final String PROBABILITY_CATCH = "ProbabilityCatch";
    
    /**
     * The constant for the XML tag of the PredatorStarve element.
     */
    public static final String PREDATOR_STARVE = "PredatorStarve";
    
    /**
     * The constant for the XML tag of the PredatorBreed element.
     */
    public static final String PREDATOR_BREED = "PredatorBreed";
    
    /**
     * The constant for the XML tag of the PreyBreed element.
     */
    public static final String PREY_BREED = "PreyBreed";
    
    /**
     * The constant for the XML tag of the Threshold element.
     */
    public static final String THRESHOLD = "Threshold";
    
    /**
     * The constant for the XML tag of the GameOfLife element.
     */
    public static final String GAMEOFLIFE = "GameOfLife";
    
    /**
     * The constant for the XML tag of the Segregation element.
     */
    public static final String SEGREGATION = "Segregation";
    
    /**
     * The constant for the XML tag of the PredatorPrey element.
     */
    public static final String PREDATORPREY = "PredatorPrey";
    
    /**
     * The constant for the XML tag of the Fire element.
     */
    public static final String FIRE = "Fire";
    
    /**
     * The ArrayList of states to be returned.
     */
    private ArrayList<String> stateList;
    
    /**
     * The integer width of the grid to be returned.
     */
    private int width;
    
    /**
     * The integer height of the grid to be returned. 
     */
    private int height;
    
    /**
     * The String name of the grid to be returned.
     */
    private String name;
    
    /**
     * The double value that determines the probability of catching fire for Fire simulation.
     */
    private double probabilityCatch;
    
    /**
     * The integer value that determines the rate of starvation of predators.
     */
    private int predatorStarve;
    
    /**
     * The integer value that determines the breeding rate of predators.
     */
    private int predatorBreed;
    
    /**
     * The integer value that determines the breeding rate of preys.
     */
    private int preyBreed;
    
    /**
     * The double value that determines the threshold level for segregation simulations.
     */
    private double threshold;
    
    
    public Configuration(String file){
        try {
            parse(file);
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     * @param String is the name of the file to be parsed. 
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private void parse(String str) throws SAXException, IOException, ParserConfigurationException{
        InputSource file = new InputSource(str);

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(new XMLHandler());
        xmlReader.parse(file);
    }
    
    /**
     * 
     * @return The width (int) of the grid as specified by the XML file chosen.
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * 
     * @return The height (int) of the grid as specified by the XML file chosen.
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * 
     * @return The name (String) of the simulation as specified by the XML file chosen.
     */
    public String getName(){
        return name;
    }
    
    /**
     * 
     * @return The states of the cells as an ArrayList
     */
    public ArrayList<String> getStates(){
        return stateList;
    }
    
    /**
     * 
     * @return The probability of the next cell catching fire for Fire simulation.
     */
    public double getProbabilityCatch(){
        return probabilityCatch;
        
    }
    
    /**
     * 
     * @return The number of turns for which the predator can survive without eating.
     */
    public int getPredatorStarve(){
        return predatorStarve;
    }
    
    /**
     * 
     * @return The rate at which the predators will breed depending on the number of terms they survive.
     */
    public int getPredatorBreed(){
        return predatorBreed;
    }
    
    /**
     * 
     * @return The rate at which the preys will breed depending on the number of terms they survive. - FOR PREDATORPREY SIMULATION
     */
    public int getPreyBreed(){
        return preyBreed;
    }
    
    /**
     * 
     * @return The threshold number of neighboring cells that must be of the same state. - FOR SEGREGATION SIMULATION
     */
    public double getThreshold(){
        return threshold;
    }
    
    
    /**
     * This inner class is the custom ContentHandler for the xmlReader.
     *
     */
    private class XMLHandler extends DefaultHandler{
        /**
         * The StringBuilder object used to collect the data in a tag. This object is reset when the endElement method is called so that it is
         * ready for use for another tag. 
         */
        private StringBuilder str;
        
        @Override 
        public void startDocument(){
           stateList = new ArrayList<String>();
        }
        
        @Override
        public void endDocument(){
            System.out.println(stateList);
            
        }
        
        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts){
            str = new StringBuilder();


        }
        
        @Override
        public void endElement(String namespaceURI, String localName,String qName){
            if(localName.equals(SIMULATION)) return;
            if(localName.equals(STATE)) stateList.add(str.toString());
            if(localName.equals(WIDTH)) width = Integer.parseInt(str.toString());
            if(localName.equals(HEIGHT)) height = Integer.parseInt(str.toString());
            if(localName.equals(NAME)) name = str.toString();
            if(localName.equals(PROBABILITY_CATCH)) probabilityCatch = Double.parseDouble(str.toString());
            if(localName.equals(PREDATOR_STARVE)) predatorStarve = Integer.parseInt(str.toString());
            if(localName.equals(PREDATOR_BREED)) predatorBreed = Integer.parseInt(str.toString());
            if(localName.equals(PREY_BREED)) preyBreed = Integer.parseInt(str.toString());
            if(localName.equals(THRESHOLD)) threshold = Double.parseDouble(str.toString());

            str.setLength(0);
        }
        
        @Override
        public void characters(char[] ch, int start, int length){
            str.append(new String(ch, start, length));
        
        } 
        
    }
}