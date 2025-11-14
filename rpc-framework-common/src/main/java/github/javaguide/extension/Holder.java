package github.javaguide.extension;


public class Holder<T> {

    private volatile T obj;

    public T get() {
        return obj;
    }

    public void set(T obj) {
        this.obj = obj;
    }
}
