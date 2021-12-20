package game;

public class Map {
	private Object [][]matrix = new Object[100][100];
	private final static Map instance = new Map();
	
	public static Map getInstance() {
		return instance;
	}
	
	public void set(int x, int y, Object other) {
		matrix[x][y] = other;
	}
	
	public Object get(int x, int y) {
		if(x >= 0 && y >= 0 && x < 100 & y < 100)
			return matrix[x][y];
		return "error";
	}
	
	public synchronized void setIsBusy(SexualCell male, SexualCell female) {
		if(male.getBusy() == false && female.getBusy() == false) {
			male.setIsBusy(true);
			female.setIsBusy(true);
		}
	}
}
