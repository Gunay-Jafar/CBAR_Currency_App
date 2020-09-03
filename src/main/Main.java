package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String mezenne;
        String amount;
        do {
            System.out.println("Hansi mezenne :");
            mezenne = br.readLine();
            if (mezenne.equals("-1"))
                break;
            System.out.println("Mebleg");
            amount = br.readLine();

            BigDecimal amountCast = new BigDecimal(amount);

            try {
                File file = new File("src/main/03.09.2020.xml");
                DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("Valute");
                boolean isFound = false;
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node tempNode = nodes.item(i);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) tempNode;
                        if (mezenne.equals(eElement.getAttribute("Code"))) {
                            BigDecimal currency = new BigDecimal(eElement.getElementsByTagName("Value").item(0).getTextContent());
                            BigDecimal result = amountCast.divide(currency, 3, RoundingMode.HALF_UP);
                            System.out.println(result.toString());
                            isFound = true;
                        }
                    }
                }
                if (!isFound) {
                    System.out.println("Sehv mezenne daxil etdiniz!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Devam etmek ucun enter daxil et.Cixmaq ucun -1 daxil et");
        } while (!br.readLine().equals("-1"));


    }
}
