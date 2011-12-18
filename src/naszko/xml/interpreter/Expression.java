package naszko.xml.interpreter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Expression implements Symbol {

	String name;

	Map<String, Object> attrs;

	List<Symbol> content;

	public Expression(String name) {
		this(name, new TreeMap<String, Object>(), new LinkedList<Symbol>());
	}

	public Expression(String name, Map<String, Object> attrs) {
		this(name, attrs, new LinkedList<Symbol>());
	}

	public Expression(String name, List<Symbol> content) {
		this(name, new TreeMap<String, Object>(), content);
	}

	public Expression(String name, Map<String, Object> attrs, List<Symbol> content) {
		this.name = name;
		this.attrs = attrs;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public List<Symbol> getContent() {
		return content;
	}

	@Override
	public Object getValue() {
		return Interpreter.eval(this);
	}

	@Override
	public String toString() {
		return Interpreter.str(getValue());
	}

}
