package github.javaguide.extension;


public class Holder {

    private volatile Object obj;

    public Object get() {
        return obj;
    }

    public void set(Object obj) {
        this.obj = obj;
    }
}
