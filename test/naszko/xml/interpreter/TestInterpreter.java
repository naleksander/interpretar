package naszko.xml.interpreter;

import java.util.List;
import java.util.Map;

import naszko.xml.interpreter.StringLiteral;
import naszko.xml.interpreter.Expression;
import naszko.xml.interpreter.Function;
import naszko.xml.interpreter.Interpreter;
import naszko.xml.interpreter.Macro;
import naszko.xml.interpreter.Symbol;
import naszko.xml.interpreter.Reader;

import org.junit.Before;
import org.junit.Test;

import static naszko.xml.interpreter.Interpreter.*;

public class TestInterpreter {

	@Before
	public void init() {
		Interpreter.define(new Function("zero") {

			@Override
			public Object invoke(Map<String, Object> attrs, Object... args) {
				return new Integer(args[0].toString()).intValue() == 0;
			}

		}, new Macro("do") {
			@Override
			public Object eval(Map<String, Object> attrs, List<Symbol> args) {
				Object result = null;
				for (Symbol node : args) {
					result = node.getValue();
				}
				return result;
			}
		}, new Function("str") {

			@Override
			public Object invoke(Map<String, Object> attrs, Object... args) {
				return args[0].toString();
			}

		},

		new Macro("if") {
			@Override
			public Object eval(Map<String, Object> attrs, List<Symbol> args) {
				if (truth(args.get(0).getValue())) {
					return args.get(1).getValue();
				} else if (args.size() > 2) {
					return args.get(2).getValue();
				}
				return null;
			}
		}, new Function("add") {
			@Override
			public Object invoke(Map<String, Object> attrs, Object... args) {
				return new Integer(args[0].toString())
						+ new Integer(args[1].toString());
			}
		}, new Macro("koza") {

			@Override
			public Object eval(Map<String, Object> attrs, List<Symbol> arguments) {
				println("In macro ", getName());
				println("Second ", arguments.get(1).getValue());
				return arguments.get(0).getValue();
			}

		});
	}

	@Test
	public void testMapping() {

		Map<String, Object> expression = map(
				"tag",
				"add",
				"attrs",
				map("me", "too"),
				"content",
				list(map("tag", "str", "content", list("13")),
						map("tag", "str", "content", list("22"))));

		Symbol node = Reader.mapToNode(expression);

		println("GOT " + node.getValue());

	}

	@Test
	public void testParsing() {

		Symbol value = Reader.read(resource("Program.xml"));

		println("GOT " + value.getValue());
	}

	@Test
	public void testMacro() {

		println("GOT "
				+ new Expression("koza", map("ala", "bal"), content(
						new Expression("add", content(
								new StringLiteral("1"), new StringLiteral("2"))),
						new StringLiteral("Hello World"))).getValue());
	}

	@Test
	public void testFunction() {

		Interpreter.define(new Function("ala") {

			@Override
			public Object invoke(Map<String, Object> attrs, Object... args) {
				return getName() + " -> " + attrs + " -> " + args[0];
			}

		});

		println("GOT ", new Expression("ala", map("1", "2"),
				content(new StringLiteral("Hello World"))).getValue());
	}
}
