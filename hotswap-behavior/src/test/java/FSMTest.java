import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.Test;

import static org.junit.Assert.*;

public class FSMTest {

    @Test
    public void testUserStorage() throws Exception {
        ActorSystem system = ActorSystem.create("userStorageTest");

        new JavaTestKit(system) {{
            ActorRef userStorage = system.actorOf(Props.create(FSM.UserStorageFSM.class), "userStorage-fsm");

            userStorage.tell(new FSM.Connect(), getRef());

//            expectMsgEquals(FSM.DbState.CONNECTED);

            userStorage.tell(new FSM.Operation(FSM.DBoperation.CREATE, new FSM.User("Admin", "admin@domain.com")), ActorRef.noSender());

            userStorage.tell(new FSM.Disconnect(), getRef());

//            expectMsgEquals(FSM.DbState.DISCONNECTED);

            system.stop(userStorage);
        }};

    }

    @Test
    public void testUserStorageStashing() throws Exception {

    }
}