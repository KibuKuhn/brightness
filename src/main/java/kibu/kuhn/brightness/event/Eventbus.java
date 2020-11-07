package kibu.kuhn.brightness.event;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class Eventbus implements IEventbus
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Eventbus.class);

    private EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus = new EventBus();
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

    @Override
    public void register(Object obj) {
        eventBus.register(obj);
    }

    @Override
    public void unregister(Object obj) {
        try {
            eventBus.unregister(obj);
        } catch (IllegalArgumentException ex) {
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

}
