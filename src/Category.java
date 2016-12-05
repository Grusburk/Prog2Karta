
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;


public class Category implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Color color;
	private ArrayList<Triangel> triangel = new ArrayList<Triangel>();
	
	public Category(String name, Color color) {
		super();
		this.name = name;
		this.color = color;
	}
	
	public ArrayList<Triangel> getTriangel() {
		return triangel;
	}
	
	public void setTriangel(ArrayList<Triangel> triangel) {
		this.triangel = triangel;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
