package demo;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * -Add Custom pool of connection for DataSource-
 *
 */

public class CustomPoolDataSource extends PGSimpleDataSource {

    private Queue<Connection> connectionQueue;
    private static final int POOL_SIZE = 10;

    @SneakyThrows
    public CustomPoolDataSource(String url, String user, String password) {
        super();
        setURL(url);
        setUser(user);
        setPassword(password);
        connectionQueue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            var physicalConnection = super.getConnection();
            var connectionProxy = new ConnectionProxy(physicalConnection, connectionQueue);
            connectionQueue.add(connectionProxy);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionQueue.poll();
    }


}
