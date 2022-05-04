import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String PATH = "C:/Users/MGK9/IdeaProjects/work5.2/data/";

    public static void main(String[] args) {
        String pathToXml = PATH + "data.xml";
        String pathToJson2 = PATH + "data.json";
        List<Employee> listXml = parseXML(pathToXml);
        String json2 = listToJson(listXml);
        writeString(json2, pathToJson2);
    }

    private static List<Employee> parseXML(String xml) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(xml));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                employees.add(new Employee(
                        Long.parseLong(getTextContent("id", element)),
                        getTextContent("firstName", element),
                        getTextContent("lastName", element),
                        getTextContent("country", element),
                        Integer.parseInt(getTextContent("age", element))
                ));
            }
        }
        return employees;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTextContent(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

}
