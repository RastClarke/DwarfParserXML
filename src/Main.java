import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.util.*;


public class Main {
   static int iteration;
    static List<String> alForm;
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, InterruptedException {


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("C:\\dwarf\\dwarf\\bin\\out.xml");


        Element element = document.getDocumentElement();

        NodeList nodeList = element.getChildNodes();
        int size =0;
        size=getSize(nodeList, size);


        alForm= new ArrayList<>();



        String[][] combinations = new String[size][15];

     iteration =0;
        combinations = getCombinations(nodeList,combinations);



        Set<String> hsForm = new HashSet<>();
        hsForm.addAll(alForm);
        alForm.clear();
        alForm.addAll(hsForm);



        Map<String, Integer> combinationsMap = new HashMap<String, Integer>();
        for (int i=0; i<size; i++){
            for (int j=0; j<15; j++){
                if(combinations[i][j]!=null)
                if (combinationsMap.containsKey(combinations[i][j])) {
                    combinationsMap.put(combinations[i][j], combinationsMap.get(combinations[i][j])+1 );
                } else {
                    combinationsMap.put(combinations[i][j], 1);
                }
            }

        }



        for (int i=0; i<alForm.size(); i++) {
            String[] parts = alForm.get(i).split("DELIMITER", 2);
            if (combinationsMap.containsKey(parts[0])&& !parts[1].equals("none")) {
                combinationsMap.put(alForm.get(i), combinationsMap.remove(parts[0]));

            }
        }


        combinationsMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(System.out::println);





    }



    static int getSize(NodeList nodeList ,int size){

        for (int i=0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node instanceof Element)
                if (node.getNodeName().equals("Text") || node.getNodeName().equals("Sentence") || node.getNodeName().equals("WordList")){
                    size=getSize(node.getChildNodes(), size);
                }
            if (node.getNodeName().equals("Word") && ((Element) node).getAttribute("POS").equals("1")){
                size++;
            }
        }

        return size;
    }






    static String[][] getCombinations(NodeList nodeList , String[][] combinations){


        for (int i=0; i< nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element)
                if (node.getNodeName().equals("Text") || node.getNodeName().equals("Sentence") || node.getNodeName().equals("WordList")) {
                    combinations = getCombinations(node.getChildNodes(), combinations);


                }
            if (node.getNodeName().equals("Word") && ((Element) node).getAttribute("POS").equals("1")) {

                List<String> al = new ArrayList<>();



                for (int ii = 4; ii > 0; ii--)
                    for (int j = ii; j > 0; j--)
                    {
                        String termPhrase=getCombination(ii - j, j, (Element) node);
                        String[] parts = termPhrase.split("DELIMITER", 2);
                        al.add(parts[0]);
                        alForm.add(termPhrase);


        }

                for (int ii=0;ii<4;ii++) {
                    String termPhrase=getCombination(ii, 0, (Element) node);
                    String[] parts = termPhrase.split("DELIMITER", 2);
                    al.add(parts[0]);
                    alForm.add(termPhrase);
                }

                Set<String> hs = new HashSet<>();
                hs.addAll(al);
                al.clear();
                al.addAll(hs);



                for (int iter=0; iter<al.size(); iter++){
                    combinations[iteration][iter]=al.get(iter);
                }
                iteration++;


            }
        }

        return combinations;
    }



    public static String getCombination(int left, int right, Element element){
        NodeList nodeList = element.getChildNodes();
        String form = "none", norm = "";
        for (int i=0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node instanceof Element && node.getTextContent().equals("25")){
               // System.out.println(getPrevElementStr(element,left,"Form")+ element.getAttribute("Form") + " " +getNextElementStr(element, right, "Form"));
                form= getPrevElementStr(element,left,"Form")+ element.getAttribute("Form") + " " +getNextElementStr(element, right, "Form");
                //System.out.println(form);
            }

        }
norm= getPrevElementStr(element,left,"Norm")+ element.getAttribute("Norm") + " " +getNextElementStr(element, right, "Norm");
        String newStr= norm+"DELIMITER"+form;
        return newStr;
    }


    public static String getPrevElementStr(Element el, int left, String attributeValue) {
        Node nd = el.getPreviousSibling();

        String form ="";
        while (nd != null && left!=0) {
            if (nd.getNodeType() == Node.ELEMENT_NODE) {

                if(combinationFilter(nd)){
                    left--;
                    form = ((Element) nd).getAttribute(attributeValue).concat(" "+form);
                }
                else  left=0;
            }
            nd = nd.getPreviousSibling();
        }
        return form;
    }


    public static String getNextElementStr(Element el, int right,String attributeValue) {
        Node nd = el.getNextSibling();

        String form ="";
        while (nd != null && right!=0) {
            if (nd.getNodeType() == Node.ELEMENT_NODE) {

                if(combinationFilter(nd)){
                    right--;

                            form = form.concat(((Element) nd).getAttribute(attributeValue)+ " ");

                }
                else  right=0;

            }
            nd = nd.getNextSibling();
        }
        return form;
    }



    public static boolean combinationFilter(Node nd){
        if(((Element) nd).getAttribute("Norm").equals(".")
                ||
                ((Element) nd).getAttribute("Norm").equals(",")
                ||
                ((Element) nd).getAttribute("Norm").equals("(")
                ||
                ((Element) nd).getAttribute("Norm").equals(")")
                ||
                ((Element) nd).getAttribute("Norm").equals(":")
                ||
                ((Element) nd).getAttribute("Norm").equals("-")
                ||
                ((Element) nd).getAttribute("POS").equals("11")
                ||
                ((Element) nd).getAttribute("POS").equals("10")
                ||
                ((Element) nd).getAttribute("POS").equals("17")
                ||
                ((Element) nd).getAttribute("POS").equals("16")
                ||
                ((Element) nd).getAttribute("POS").equals("16")
                ||
                ((Element) nd).getAttribute("POS").equals("12")
                ||
                ((Element) nd).getAttribute("POS").equals("15")
                ||
                ((Element) nd).getAttribute("POS").equals("2")
                ||
                ((Element) nd).getAttribute("POS").equals("6")
                ) return  false;
                return true;

    }


}
