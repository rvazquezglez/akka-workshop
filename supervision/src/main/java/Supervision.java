import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import java.util.concurrent.TimeUnit;

public class Supervision {
    private static final Logger LOGGER = LoggerFactory.getLogger(Supervision.class);

    private static class Aphrodite extends AbstractActor {
        static class ResumeException extends Exception {
        }

        static class StopException extends Exception {
        }

        static class RestartException extends Exception {
        }

        public Aphrodite() {
            receive(ReceiveBuilder
                    .match(String.class, msg -> {
                        LOGGER.info("Aphrodite throwing {}Exception", msg);
                        switch (msg) {
                            case "Resume":
                                throw new ResumeException();
                            case "Stop":
                                throw new StopException();
                            case "Restart":
                                throw new RestartException();
                            default:
                                throw new Exception();
                        }
                    })
                    .build());
        }

        // Log prestart and poststop hooks here
        // ===
    }

    private static class Hera extends AbstractActor {

        private ActorRef childRef;

        public Hera() {
            receive(ReceiveBuilder
                    .match(
                            Object.class,
                            msg -> {
                                LOGGER.info(String.format("Hera received %s", msg));
                                childRef.tell(msg, self());
                                Thread.sleep(100);
                            })
                    .build());
        }

        // Implement oneForOne strategy here
        // ===

        @Override
        public void preStart() throws Exception {
            // Create Aphrodite Actor
            childRef = context().actorOf(Props.create(Aphrodite.class), "Aphrodite");
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create the 'supervision' actor system
        ActorSystem system = ActorSystem.create("supervision");

        // Create Hera Actor
        ActorRef hera = system.actorOf(Props.create(Hera.class), "hera");

        hera.tell("Resume", ActorRef.noSender());
        Thread.sleep(1000);

        hera.tell("Restart", ActorRef.noSender());
        Thread.sleep(1000);

        ActorSelection actorSelection = system.actorSelection("user/hera/Aphrodite");
        actorSelection.tell("Restart", ActorRef.noSender());

//        hera.tell("Stop", ActorRef.noSender());
//        Thread.sleep(1000);

        system.terminate();
    }
}
