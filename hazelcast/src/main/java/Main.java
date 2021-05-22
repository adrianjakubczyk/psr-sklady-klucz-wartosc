import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        final HazelcastInstance client = HazelcastClient.newHazelcastClient();




        Menu menu = new Menu(client);

        menu.selectOperation();

    }
}
