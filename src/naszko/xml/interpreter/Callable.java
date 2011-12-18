package naszko.xml.interpreter;

public abstract class Callable {

	String name;

	public Callable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract Object eval(Expression node);

}
