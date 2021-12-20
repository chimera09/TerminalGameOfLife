package game;

public class FoodResource {
	private int total;
	
	public FoodResource(int total) {
		this.total = total;
	}
	
	public void eat() {
		if(total > 0)
			total --;
	}
	public int availableFood() {
		return total;
	}
}
