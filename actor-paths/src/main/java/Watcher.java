import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

public class Watcher extends AbstractActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Watcher.class);

    public Watcher() {
        ActorSelection actorSelection = context().actorSelection("/user/counter");

//        Timeout timeout = new Timeout(5, TimeUnit.MILLISECONDS);
//        AskableActorSelection asker = new AskableActorSelection(actorSelection);
//        Future<Object> future = asker.ask(new Identify(1), timeout);
//        ActorIdentity identity = (ActorIdentity) Await.result(future, timeout.duration());
//        ActorRef ref = identity.getRef();

        actorSelection.tell(new Identify(1), self());

        receive(ReceiveBuilder
                .match(
                        ActorIdentity.class,
                        actorIdentity -> actorIdentity.getRef() != null,
                        actorIdentity -> LOGGER.info("Actor Reference for counter is {}", actorIdentity.getRef()))
                .match(
                        ActorIdentity.class,
                        actorIdentity -> actorIdentity.getRef() == null,
                        actorIdentity -> LOGGER.info("Actor selection for actor doesn't live :( "))
                .build());
    }
}
