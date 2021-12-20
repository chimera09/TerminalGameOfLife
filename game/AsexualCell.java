package game;

public class AsexualCell extends Cell {
	
	public AsexualCell(int x, int y) {
		super(x, y);
		System.out.println("Asexual Cell " + this.number + " is created");
	}

	@Override
	public void reproduce() throws InterruptedException {
		System.out.println("Asexual Cell " + this.number + " is reproducing");
		Object find;
		int i, j;
		int children = 0;
		AsexualCell child;
		for (i = -2; i <= 2; i++)
			for (j = -2; j <= 2; j++) {
				find = map.get(xLocation + i, yLocation + j);
				if (find instanceof Free) {
					synchronized (find) {
						{
							children++;
							child =  new AsexualCell(xLocation + i, yLocation + j);
							map.set(xLocation + i, yLocation + j, child);
						}
					}
					child.launchThread();
				}
				if (children == 2) {
					break;
				}
		}
		if (children == 0) {
			throw new IllegalStateException("Not enough space for both children");
		}
		this.kill();
	}

}
