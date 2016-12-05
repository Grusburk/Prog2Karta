
import java.io.Serializable;

public class Position implements Serializable {
	private static final long serialVersionUID = 1L;

	private int x, y;

	@Override
	public int hashCode() {
		return x+y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Position pose = (Position)obj;
		if (x == pose.x && y == pose.y) {
		return true;
	} else {
		return false;
		}
	}
	
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

}
