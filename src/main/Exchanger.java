package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Exchanger {

    private boolean isDateValid(String userDate) {
        if (!userDate.matches("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
            System.out.println("Yanlis vaxt daxil etdiniz!");
            return false;
        }
        return true;
    }

    private String askInputFromUser(String message) {
        Scanner br = new Scanner(System.in);
        System.out.println(message);
        return br.nextLine();
    }

    private StringBuilder getCurrencyDataWithDate(String date) throws Exception {
        URL url = new URL("https://www.cbar.az/currencies/" + date + ".xml");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return content;
    }

    private boolean fileExists(String date){
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        return file.exists();
    }

    private void createAndFillFileIfNotExist(String date, StringBuilder content) throws Exception {
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        if (!file.exists()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());

            writer.close();

            System.out.println("Fayl ugurla endirildi.");
        } else {
            System.out.println("Fayl artiq movcuddur.");
        }
    }

    private boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) throws Exception {
        BigDecimal amountCast = new BigDecimal(amount);
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
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
        return isFound;
    }

    void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                if (askInputFromUser("Devam etmek ucun enter daxil et. Cixmaq ucun -1 daxil et").equals("-1"))
                    return;

                System.out.println("****MENU****");
                System.out.println("1.Fayli Endir");
                System.out.println("2.Mezennenin daxil edilmesi");

                if (scanner.nextLine().equals("1")) {
                    String userDate = askInputFromUser("Tarix daxil et:");
                    boolean isDateValid = isDateValid(userDate);
                    if (!isDateValid)
                        continue;
                    StringBuilder content = getCurrencyDataWithDate(userDate);
                    createAndFillFileIfNotExist(userDate, content);
                    continue;

                } else {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    String dateCur = formatter.format(date);
                    System.out.println(dateCur + " " + "gunluk tarixinin valyutasina baxmaq ucun 1 daxil edin,Tarix daxil etmek isdeyirsizse 2 daxil edin");
                    if (scanner.nextLine().equals("2")) {

                        String userDate = askInputFromUser("Tarix daxil et:");
                        boolean isDateValid = isDateValid(userDate);
                        if (!isDateValid)
                            continue;

                        if (!fileExists(userDate)){
                            String input = askInputFromUser("Fayl tapilmadi. Yükləyib davam etmək üçün 1, çıxış etmək üçün 2 daxil edin.");
                            if (input.equals("1")){
                                StringBuilder cont = getCurrencyDataWithDate(userDate);
                                createAndFillFileIfNotExist(userDate, cont);
                            }else if (input.equals("2")) {
                                return;
                            }
                        }

                        String mezenne = askInputFromUser("Mezenneni daxil edin:").toUpperCase();
                        String amount = askInputFromUser("AZN Mebleg daxil edin:");

                        if (!amount.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")) {
                            System.out.println("Yanlis mebleg daxil etdiniz!");
                            continue;
                        }

                        readAndCalculateExchangeValue(amount, userDate, mezenne);

                    } else {
                        String mezenne = askInputFromUser("Mezenneni daxil edin:").toUpperCase();
                        String amount = askInputFromUser("AZN Mebleg daxil edin:");

                        if (!amount.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")) {
                            System.out.println("Yanlis mebleg daxil etdiniz!");
                            continue;
                        }
                        readAndCalculateExchangeValue(amount, dateCur, mezenne);

                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}