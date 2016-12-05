

public class DescribedPlace extends Place {
	private static final long serialVersionUID = 1L;
	
	private String description;

	public DescribedPlace(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + description;
	}
}
