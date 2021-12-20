package game;

public abstract class Cell implements Runnable {
	protected int T_full;
	protected int T_starve;
	protected int eatCount;
	protected int xLocation;
	protected int yLocation;
	public static int cellNumbers = 1;
	protected int number;
	private boolean alive = true;
	protected final Map map = Map.getInstance();
	private FoodResource food = null;
	private String cellType = this instanceof AsexualCell ? "Asexual cell" : "Sexual cell";

	private Thread thread;

	public Cell(int x, int y) {
		number = cellNumbers;
		cellNumbers++;
		T_full = 0;
		T_starve = 5;
		eatCount = 0;
		xLocation = x;
		yLocation = y;
	}

	public int[] getLocation() {
		int[] location = { xLocation, yLocation };
		return location;
	}

	public void eat() {
		System.out.println(cellType + " " + this.number + " is eating");
		T_full = 5;
		eatCount++;
	}

	public void move(int x, int y) {
		String ret = String.format(cellType + " " + "%d tried moving from %d,%d to %d,%d",number, xLocation, yLocation, x ,y);
		System.out.println(ret);
		Object nextPosition = map.get(x, y);
		Object currentPosition = map.get(xLocation, yLocation);
		if(nextPosition instanceof Free) {
			synchronized (currentPosition) {
				synchronized (nextPosition) {
					map.set(x, y, this);
					map.set(xLocation, yLocation, new Free());
					ret = String.format(cellType + " " + "%d is moving from %d,%d to %d,%d",number, xLocation, yLocation, x ,y);
					System.out.println(ret);
				}
			}
			xLocation = x;
			yLocation = y;
			food = null;
		}
	}

	public void moveRandom() {
		int i = 0;
		int j = 0;
		while (i == 0 && j == 0 ||( xLocation + i < 0 || yLocation + j < 0 || xLocation + i >= 100
				|| yLocation + j >= 100)) {
			i = (int) Math.floor(Math.random() * 5 - 2);
			j = (int) Math.floor(Math.random() * 5 - 2);
		}
		this.move(xLocation + i, yLocation + j);
	}

	public void searchFood() {
		System.out.println(cellType + " " + this.number + " is searching for food");
		Object find;
		int i, j;
		for (i = -2; i <= 2; i++) {
			for (j = -2; j <= 2; j++) {
				find = map.get(xLocation + i, yLocation + j);
				if (find instanceof FoodResource) {
					food = (FoodResource) find;
					synchronized (food) {
						food.eat();
						if (food.availableFood() == 0) {
							map.set(xLocation + i, yLocation + j, new Free());
						}
					}
					this.eat();
					return;
				}
			}
		}
	}

	public abstract void reproduce() throws InterruptedException;

	public void run() {
		while (alive) {
				if (eatCount >= 10)
					try {
						this.reproduce();
					} catch (InterruptedException e) {
						System.out.println("--- " + cellType + " " + this.number + " error on reproducing ---");
						e.printStackTrace();
					}
				if (T_full == 0)
					this.searchFood();
				this.moveRandom();

				if (T_full > 0)
					T_full--;
				else if (T_starve > 0)
					T_starve--;
				else {
					this.kill();
					return;
				}
		}
	}

	protected void launchThread() {
		thread = new Thread(this);
		thread.start();
	}

	protected void kill() {
		System.out.println(cellType + " " + this.number + " died");
		this.alive = false;
		Thread.currentThread().interrupt();
		map.set(xLocation, yLocation, new Free());
	}

}
