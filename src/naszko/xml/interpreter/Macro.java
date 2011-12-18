package naszko.xml.interpreter;

import java.util.List;
import java.util.Map;

public abstract class Macro extends Callable {

	public Macro(String name) {
		super(name);
	}

	@Override
	public Object eval(Expression node) {
		return eval(node.getAttrs(), node.getContent());
	}

	public abstract Object eval(Map<String, Object> attrs, List<Symbol> arguments);

}
