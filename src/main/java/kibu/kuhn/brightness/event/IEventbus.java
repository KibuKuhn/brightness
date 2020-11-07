package kibu.kuhn.brightness.event;

public interface IEventbus
{

    void unregister(Object obj);

    void register(Object obj);

    void post(Object event);

}
