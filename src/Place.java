
import java.awt.Color;
import java.io.Serializable;


public class Place implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected Color color;
	public Place(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
