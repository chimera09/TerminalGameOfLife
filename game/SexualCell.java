package game;

public class SexualCell extends Cell {
    private boolean isBusy = true;
    private SexualCell child;

    public SexualCell(int x, int y) {
        super(x, y);
        System.out.println("Sexual Cell " + this.number + " is created");
    }

    public void setIsBusy(boolean value) {
        isBusy = value;
        System.out.println("Sexual Cell " + this.number + " is set busy");
    }

    public boolean getBusy() {
        return isBusy;
    }

    public void eat() {
        super.eat();
        if (this.eatCount >= 10)
            isBusy = false;
    }

    @Override
    public void reproduce() throws InterruptedException {
        Object find;
        int i, j;
        for (i = -2; i <= 2; i++) {
            for (j = -2; j <= 2; j++) {
                synchronized (map) {
                    find = map.get(xLocation + i, yLocation + j);
                    if (find instanceof SexualCell && find != this) {
                        if (((SexualCell) find).getBusy() == false) {
                            System.out.println("Sexual Cell " + this.number + " is reproducing with sexual cell " + ((SexualCell) find).number);
                            map.setIsBusy(this, (SexualCell) find);
                            int[] location = ((SexualCell) find).getLocation();
                            child = new SexualCell(location[0], location[1]);
                            map.set(location[0], location[1], child);
                            child.launchThread();
                            this.kill();
                            ((Cell) find).kill();
                        }
                    }
                }
            }
        }
    }

}
