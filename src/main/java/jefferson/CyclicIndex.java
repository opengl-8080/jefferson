package jefferson;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

public class CyclicIndex {
    private ReadOnlyIntegerWrapper index = new ReadOnlyIntegerWrapper(0);
    private final int max;

    public CyclicIndex(int max) {
        this.max = max;
    }
    
    public void set(int index) {
        int _index = index;
        int tmp = _index % this.max;
        if (tmp < 0) {
            tmp = this.max + tmp;
        }
        _index = tmp;
        this.index.set(_index);
    }
    
    public void add(int delta) {
        int _index = this.index.get() + delta;
        int tmp = _index % this.max;
        if (tmp < 0) {
            tmp = this.max + tmp;
        }
        _index = tmp;
        this.index.set(_index);
    }

    public void increment() {
        int _index = this.index.get() + 1;
        if (max <= _index) {
            _index = 0;
        }
        this.index.set(_index);
    }
    
    public void decrement() {
        int _index = this.index.get() - 1;
        if (_index < 0) {
            _index = max - 1;
        }
        this.index.set(_index);
    }
    
    public int value() {
        return this.index.get();
    }
    
    public CyclicIndex copy() {
        CyclicIndex cyclicIndex = new CyclicIndex(this.max);
        cyclicIndex.index = this.index;
        return cyclicIndex;
    }
    
    public ReadOnlyIntegerProperty indexProperty() {
        return this.index.getReadOnlyProperty();
    }
}
