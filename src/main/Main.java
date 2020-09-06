package main;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Tarix daxil et :");
        String userDate = br.readLine();
        if (!userDate.matches("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
            System.out.println("Yanlis vaxt daxil etdiniz!");
            return;
        }
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
//        Date date = new Date();
//        String todaysDate = formatter.format(date);

        URL url = new URL("https://www.cbar.az/currencies/" + userDate + ".xml");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuffer content = new StringBuffer();
        while ((inputline = in.readLine()) != null) {
            content.append(inputline);
        }
        in.close();
        con.disconnect();


        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/res/newData.xml")));
        writer.write(content.toString());

        writer.close();


        String mezenne;
        String amount;
        do {
            System.out.println("Hansi mezenne :");
            mezenne = br.readLine().toUpperCase();
            if (mezenne.equals("-1"))
                break;
            System.out.println("AZN Mebleg daxil edin :");
            amount = br.readLine();
            boolean isChech = false;
            for (int i = 0; i < amount.length(); i++) {
                if (amount.charAt(i) >= '0' && amount.charAt(i) <= '9')
                    isChech = true;
            }
            if (!isChech) {
                System.out.println("Yanlis mebleg daxil etdiniz!");
                break;
            }
            BigDecimal amountCast = new BigDecimal(amount);
            try {
                File file = new File(System.getProperty("user.dir") + "/res/newData.xml");
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
