package kibu.kuhn.brightness.domain;

public class DisplayUnit {

	private String name;
	private int value;

	public DisplayUnit(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public DisplayUnit(String name) {
		this(name, 100);
	}

	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
}
