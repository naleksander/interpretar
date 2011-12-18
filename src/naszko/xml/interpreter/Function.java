package naszko.xml.interpreter;

import java.util.List;
import java.util.Map;

public abstract class Function extends Callable {

	public Function(String name) {
		super(name);
	}

	@Override
	public Object eval(Expression node) {
		List<Symbol> arguments = node.getContent();
		Object[] args = new Object[arguments.size()];
		int index = 0;
		for (Symbol argument : arguments) {
			args[index++] = argument.getValue();
		}
		return invoke(node.getAttrs(), args);
	}

	public abstract Object invoke(Map<String, Object> attrs, Object... args);

}
