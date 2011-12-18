package naszko.xml.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Interpreter {

	static TreeMap<String, Callable> defaultNamespace = new TreeMap<String, Callable>();

	public static void define(Callable... functions) {
		for (Callable function : functions) {
			defaultNamespace.put(function.getName(), function);
		}
	}

	public static Object eval(Expression node) {
		Callable callable = defaultNamespace.get(node.getName());
		if (callable == null) {
			throw new RuntimeException("Unknown " + node.getName());
		}
		return callable.eval(node);
	}

	public static void println(Object... values) {
		StringBuilder builder = new StringBuilder();
		for (Object value : values) {
			String result = str(value);
			if (result != null) {
				builder.append(result);
			}
		}
		System.out.println(builder.toString());
	}

	public static InputStream resource(final String name) {
		return new InputStream() {
			
			InputStream input =  Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(name);
			
			@Override
			public int read() throws IOException {
				int result = input.read();
				if (result == -1) {
					close();
				}
				return result;
			}

			@Override
			public void close() throws IOException {
				if (input != null) {
					input.close();
					input = null;
				}
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				return input.read(b, off, len);
			}
		};
	}

	public static boolean truth(Object value) {
		return (value != null)
				&& (!(value instanceof Boolean) || ((Boolean) value)
						.booleanValue());
	}

	public static String str(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}
	
	public static List<Object> list(Object... nodes) {
		return Arrays.asList(nodes);
	}

	public static List<Symbol> content(Symbol... nodes) {
		return Arrays.asList(nodes);
	}

	public static Map<String, Object> map(Object... values) {
		Map<String, Object> result = new TreeMap<String, Object>();
		for (int i = 0; i < values.length;) {
			result.put(str(values[i]), values[i + 1]);
			i += 2;
		}
		return result;
	}
}
