package naszko.xml.interpreter;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Reader {

	public static Symbol mapToNode(Object obj) {
		if (obj instanceof String) {
			return new StringLiteral(obj.toString());
		} else if (obj instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) obj;
			String tag = map.get("tag").toString();
			@SuppressWarnings("unchecked")
			Map<String, Object> attrs = (Map<String, Object>) map.get("attrs");
			Expression node;
			if (attrs == null) {
				node = new Expression(tag);
			} else {
				node = new Expression(tag, attrs);
			}
			@SuppressWarnings("unchecked")
			List<Object> content = (List<Object>) map.get("content");
			if (content != null) {
				for (Object contentNode : content) {
					node.getContent().add(mapToNode(contentNode));
				}
			}
			return node;
		}
		throw new IllegalArgumentException();

	}

	public static Symbol read(InputStream is) {
		XMLHandler handler = new XMLHandler();
		try {
			SAXParserFactory.newInstance().newSAXParser().parse(is, handler);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return handler.get();
	}

	static private class XMLHandler extends DefaultHandler {

		LinkedList<Expression> stack = new LinkedList<Expression>();
		CharArrayWriter builder = new CharArrayWriter();
		Expression node;
		boolean collect;

		public Symbol get() {
			return node;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (collect) {
				builder.write(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (builder.size() != 0) {
				node.getContent().add(new StringLiteral(builder.toString()));

			}
			if (!stack.isEmpty()) {
				Symbol child = node;
				node = stack.pop();
				node.getContent().add(child);
			}
			collect = false;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			Map<String, Object> attr = new TreeMap<String, Object>();
			for (int i = 0; i < atts.getLength(); ++i) {
				attr.put(atts.getLocalName(i), atts.getValue(i));
			}
			if (node != null) {
				stack.push(node);
			}
			node = new Expression(qName, attr);
			builder.reset();
			collect = true;
		}
	}
}
