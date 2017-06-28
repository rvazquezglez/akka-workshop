import akka.actor.AbstractFSMWithStash;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSM {

    private static final Logger LOGGER = LoggerFactory.getLogger(FSM.class);

    //FSM State
    enum DbState {
        CONNECTED, DISCONNECTED
    }

    //FSM data
    interface Data {
    }

    static class EmptyData implements Data {

    }

    enum DBoperation {
        CREATE, UPDATE, READ, DELETE
    }

    static class Connect {
    }

    static class Disconnect {
    }

    static class Operation {
        private final DBoperation operation;
        private final User user;

        Operation(DBoperation operation, User user) {
            this.operation = operation;
            this.user = user;
        }
    }

    static class User {
        private final String email;
        private final String username;

        User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    static class UserStorageFSM extends AbstractFSMWithStash<DbState, Data> {


        {
            // 1. define start with

            // ===

            // 2. define states

            // ===

            // 3. initialize the actor

            // ===
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("Hotswap-FSM");

        ActorRef userStorage = system.actorOf(Props.create(UserStorageFSM.class), "userStorage-fsm");

        userStorage.tell(new Connect(), ActorRef.noSender());

        userStorage.tell(new Operation(DBoperation.CREATE, new User("Admin", "admin@domain.com")), ActorRef.noSender());

        userStorage.tell(new Disconnect(), ActorRef.noSender());

        Thread.sleep(100);

        system.terminate();
    }
}
