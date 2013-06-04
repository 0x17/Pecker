package org.andreschnabel.pecker.serialization;

import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Hilfsmethoden für XML-Serialisierung/Deserialisierung.
 */
public class XmlHelpers {

	/**
	 * Nur statische Methoden.
	 */
	private XmlHelpers() {}

	public static Document documentForUrl(String url) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder = dbf.newDocumentBuilder();
		Document doc = dbuilder.parse(url);
		doc.getDocumentElement().normalize();
		return doc;
	}

	public static XPathExpression xPathExprForStr(String expr) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		return xp.compile(expr);
	}

	public static void serializeToXml(Object o, File out) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(o.getClass());
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(o, out);
	}

	public static Object deserializeFromXml(Class<?> clazz, File in) throws Exception {
		if(!in.exists())
			throw new FileNotFoundException(in.getAbsolutePath());
		
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller um = jc.createUnmarshaller();
		return um.unmarshal(in);
	}
}
