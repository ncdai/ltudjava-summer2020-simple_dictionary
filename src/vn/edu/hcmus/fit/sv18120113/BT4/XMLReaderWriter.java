package vn.edu.hcmus.fit.sv18120113.BT4;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class XMLReaderWriter {
    public static Map<String, Vocabulary> readDictionaryFromXML(String fileName) {
        try {
            Map<String, Vocabulary> dictionary = new HashMap<>();

            File xmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("record");

            for (int i = 0; i < list.getLength(); ++i) {
                Node item = list.item(i);

                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) item;

                    String word = eElement.getElementsByTagName("word")
                            .item(0)
                            .getTextContent();

                    String meaning = eElement.getElementsByTagName("meaning")
                            .item(0)
                            .getTextContent();

                    Vocabulary newVocabulary = new Vocabulary(word, meaning);
                    dictionary.put(Utils.slugify(word), newVocabulary);
                }
            }

            return dictionary;

        } catch (Exception e) {
            return null;
        }
    }

    public static boolean writeDictionaryToXML(String fileName, Map<String, Vocabulary> dictionary) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Dictionary Element
            Element root = document.createElement("dictionary");
            document.appendChild(root);

            Set<String> keys = dictionary.keySet();
            for (String key : keys) {
                // Record Element
                Element record = document.createElement("record");
                root.appendChild(record);

                Vocabulary vocabulary = dictionary.get(key);
                String word = vocabulary.getWord();
                String meaning = vocabulary.getMeaning();

                // Word Element
                Element wordElement = document.createElement("word");
                wordElement.appendChild(document.createTextNode(word));
                record.appendChild(wordElement);

                // Meaning Element
                Element meaningElement = document.createElement("meaning");
                meaningElement.appendChild(document.createTextNode(meaning));
                record.appendChild(meaningElement);
            }

            // Create the XML file
            // Transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));

            transformer.transform(domSource, streamResult);

            return true;

        } catch (ParserConfigurationException | TransformerException e) {
            return false;
        }
    }
}
