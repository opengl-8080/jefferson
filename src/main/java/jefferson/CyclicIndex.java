package jefferson;

public class CyclicIndex {
    private int index;
    private final int max;

    public CyclicIndex(int max) {
        this.max = max;
    }
    
    public void add(int delta) {
        this.index += delta;
        int tmp = this.index % this.max;
        if (tmp < 0) {
            tmp = this.max + tmp;
        }
        this.index = tmp;
    }

    public void increment() {
        this.index++;
        if (max <= this.index) {
            this.index = 0;
        }
    }
    
    public void decrement() {
        this.index--;
        if (this.index < 0) {
            this.index = max - 1;
        }
    }
    
    public int value() {
        return this.index;
    }
    
    public CyclicIndex copy() {
        CyclicIndex cyclicIndex = new CyclicIndex(this.max);
        cyclicIndex.index = this.index;
        return cyclicIndex;
    }
}
