import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter extends AbstractActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);

    static class Inc {
        private final int num;

        Inc(int num) {
            this.num = num;
        }
    }

    static class Dec {
        private final int num;

        Dec(int num) {
            this.num = num;
        }
    }

    private int count = 0;

    public Counter() {
        receive(ReceiveBuilder
                .match(Inc.class, msg -> increment(msg))
                .match(Dec.class, msg -> decrement(msg))
                .build()
        );
    }

    private int decrement(Dec msg) {
        LOGGER.info("{} decrementing counter {}", self(), msg.num);
        return count -= msg.num;
    }

    private int increment(Inc msg) {
        LOGGER.info("{} incrementing counter {}", self(), msg.num);
        return count += msg.num;
    }
}
