import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws UnknownHostException {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }
}
