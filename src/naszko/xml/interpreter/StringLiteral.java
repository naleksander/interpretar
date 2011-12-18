package naszko.xml.interpreter;

public class StringLiteral implements Symbol {

	String value;
	
	
	public StringLiteral(String value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Interpreter.str(getValue());
	}

	
}
