
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javax.swing.JComponent;

public class Triangel extends JComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean marked ,folded;
	private Color color;
	private Place place;
	Polygon threecorner = new Polygon(
            new int[]{0, 20, 10},
            new int[]{0, 0, 20},3);
	
	public Place getPlace() {
		return place;
	}
	
	public Triangel(Place place) {
		super();
		this.place = place;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void setMarked(boolean marked) {
		this.marked = marked;
		repaint();
	}
	
	public boolean isFolded() {
		return folded;
	}
	
	public void setFolded(boolean folded) {
		this.folded = folded;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    if (folded == false){
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setColor(color);
	        g2d.fill(threecorner);
	        setSize(20,20);
	        if (marked == true) {
	        	g.drawRect(0, 0, 19, 19);
	        }
	    } else {
	        int x = 0;
	        int y = 10;
	        FontMetrics fontMetrics = g.getFontMetrics();
	        Rectangle2D blackBorder = fontMetrics.getStringBounds(place.toString(), g);
	
	        g.setColor(Color.BLACK);
	        g.fillRect(x,y - fontMetrics.getAscent(),(int) blackBorder.getWidth(),(int) blackBorder.getHeight());
	        g.setColor(Color.WHITE);
	        g.drawString(place.toString(), x, y);
	    	setSize(300, 15);
		}
	}

	public void setColor(Color colo){
		color = colo;
	}

}