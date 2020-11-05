package kibu.kuhn.brightness.event;

public interface IEventbus
{
    static IEventbus get() {
        return Eventbus.get();
    }

    void unregister(Object obj);

    void register(Object obj);

    void post(Object event);

}
