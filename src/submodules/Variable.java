package submodules;

import java.io.Serializable;

public class Variable implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Object Value;

	public Variable() {

	}

	public Variable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return Value;
	}

	public void setValue(Object value) {
		Value = value;
	}
	public Variable(String name, Object Value) { //new 
		this.name = name;
		this.Value = Value;
	}

	@Override
	public String toString() {
		if(name==null || Value ==null) return "";
		return "Variable [name=" + name + ", Value=" + Value + "]";
	}
}
