package kibu.kuhn.brightness.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect InjectionAspect
{

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectionAspect.class);

    pointcut newInstance() : initialization(public kibu.kuhn.brightness..*.new(..));

    before(Injection anno) : newInstance() && @within(anno) {
        LOGGER.debug("target={}", thisJoinPoint.getTarget());
        Object target = thisJoinPoint.getTarget();
        InjectorSupport.get().injectMembers(target);
    }
}
