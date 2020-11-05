package kibu.kuhn.brightness.event;

public class Event<T>
{

    private T delegate;

    public Event(T delegate) {
        this.delegate = delegate;
    }

    public T getDelegate() {
        return delegate;
    }
}
