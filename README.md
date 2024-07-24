# Valkey-Java
valkey-java is [Valkey](https://github.com/valkey-io/valkey)'s Java client, derived from [Jedis](https://github.com/redis/jedis) fork, dedicated to maintaining simplicity and high performance.


# Getting started
Add the following dependencies to your `pom.xml` file:
```
<dependency>
    <groupId>io.valkey</groupId>
    <artifactId>valkey-java</artifactId>
    <version>5.3.0(coming soon)</version>
</dependency>
```

## Connect to Valkey

```java
public class ValkeyTest {
    // can be static or singleton, thread safety.
    private static io.valkey.JedisPool jedisPool;

    public static void main(String[] args) {
        io.valkey.JedisPoolConfig config = new io.valkey.JedisPoolConfig();
        // It is recommended that you set maxTotal = maxIdle = 2*minIdle for best performance
        config.setMaxTotal(32);
        config.setMaxIdle(32);
        config.setMinIdle(16);
        jedisPool = new io.valkey.JedisPool(config, < host >, <port >, <timeout >, <password >);
        try (io.valkey.Jedis jedis = jedisPool.getResource()) {
            jedis.set("key", "value");
            System.out.println(jedis.get("key"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jedisPool.close(); // when app exit, close the resource.
    }
}
```

## Connect to the Valkey cluster

```java
import java.util.HashSet;
import java.util.Set;

import io.valkey.HostAndPort;

public class ValkeyClusterTest {
    private static final int DEFAULT_TIMEOUT = 2000;
    private static final int DEFAULT_REDIRECTIONS = 5;
    private static io.valkey.JedisCluster jc; // be static or singleton, thread safety.

    public static void main(String[] args) {
        io.valkey.ConnectionPoolConfig config = new io.valkey.ConnectionPoolConfig();
        // It is recommended that you set maxTotal = maxIdle = 2*minIdle for best performance
        // In cluster mode, please note that each business machine will contain up to maxTotal links,
        // and the total number of connections = maxTotal * number of machines
        config.setMaxTotal(32);
        config.setMaxIdle(32);
        config.setMinIdle(16);

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort(host, port));
        jc = new io.valkey.JedisCluster(jedisClusterNode, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_REDIRECTIONS,
            password, null, config);

        jc.set("key", "value"); // Note that there is no need to call jc.close() here, 
        // the connection recycling is actively completed internally.
        System.out.println(jc.get("key"));

        jc.close(); // when app exit, close the resource.
    }
}
```

## Connect using TLS method

```java
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ValkeySSLTest {
    private static SSLSocketFactory createTrustStoreSSLSocketFactory(String jksFile) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("jks");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(jksFile);
            trustStore.load(inputStream, null);
        } finally {
            inputStream.close();
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
        trustManagerFactory.init(trustStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public static void main(String[] args) throws Exception {
        // When you don't have a jks file, just set sslSocketFactory to null.
        final SSLSocketFactory sslSocketFactory = createTrustStoreSSLSocketFactory( < your_jks_file_path >);
        io.valkey.JedisPool jedisPool = new io.valkey.JedisPool(new GenericObjectPoolConfig(), < host >,
            <port >, <timeout >, <password >, 0, true, sslSocketFactory, null, null);

        try (io.valkey.Jedis jedis = pool.getResource()) {
            jedis.set("key", "value");
            System.out.println(jedis.get("key"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        jedisPool.close(); // when app exit, close the resource.
    }
}
```

# Pool Configuration
The following are the common parameters of apache common-pool and their meanings：

| Parameter | Meanings                                                                                                                                                                                                         | Default value | Recommended value              |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|--------------------------------|
|connectionTimeout| Initialize the timeout period for connecting to the cluster, such as the timeout period for reconnecting the cluster at startup and after the TCP connect is disconnected.                                       | 2000          | 5000                           |
|soTimeout| The timeout period for API access. For example, the timeout period for operations such as set and get.                                                                                                           | 2000          | 2000                           |
|maxTotal/maxIdle/minIdle | standalone mode: the connection to redis; cluster mode: The number of connections to a node in the cluster                                                                                                       | 8，8，0         | MaxTotal = MaxIdle = 2*MinIdle |
|blockWhenExhausted| When the resource pool is used up, whether the caller needs to wait or not. If not, an exception with insufficient connection is returned. The following maxWaitMillis takes effect only when the value is true. | true          | true                           |
|maxWaitMillis| The maximum wait time (in milliseconds) of the caller when the resource pool connection is exhausted.                                                                                                            | -1            | depending on your business     |
|testOnBorrow| Whether to check the validity of the connection (send the ping command) when borrowing the connection from the resource pool. The detected invalid connection will be removed.                                   | false         | false                          |
|testOnReturn| Whether to check the validity of the connection (send a ping command) when returning the connection to the resource pool. The detected invalid connection will be removed.                                       | false         | false                          |
|testOnCreate| If you create a new connection when borrowing a connection, we recommend that you disable it if you check whether the connection validity is performed (send a ping command).                                    | false         | false                          |
|testWhileIdle| Whether to check the validity of the connection (send a ping command) when detecting idle connections. If the connection is invalid, it will be closed.                                                          | true          | true                           |
|timeBetweenEvictionRunsMillis| The detection period of idle resources. Unit: milliseconds.                                                                                                                                                      | 30000         | 30000                          |
|minEvictableIdleTimeMillis| The minimum idle time (in milliseconds) of resources in the resource pool. When this value is reached, idle resources are removed. Unit: milliseconds.| 60000         | 60000                          |
|numTestsPerEvictionRun|The number of resources that are detected each time when idle resources are detected.| -1            | -1                             |
|evictionPolicy|Set the evict class, including the elimination algorithm. The default implementation is DefaultEvictionPolicy, which is eliminated according to the idle time.|         DefaultEvictionPolicy      |     DefaultEvictionPolicy                           |
|evictionPolicyClassName|Set the evict class name. The default implementation is DefaultEvictionPolicy, which is eliminated according to the idle time.|      DefaultEvictionPolicy         |         DefaultEvictionPolicy                       |
|evictorShutdownTimeoutMillis|The default waiting time when you exit the evictor.Unit: milliseconds.    |       10000        |                  10000              |
|fairness|When the connection pool is exhausted, multiple threads may block waiting for resources. If the fairness is true, threads can obtain resources in sequence.|        false       |           false                     |
|lifo|When multiple connections are available in the connection pool, a connection is selected based on this value. (Last in, First out)|         true      |        true                        |

# Roadmap
The following is what we plan to complete in the future
1. Support new API for Valkey
2. Support asynchronous
3. Reduce the number of client links in cluster mode
4. Tracing mode can record the access latency of each API.

# Contribution
Contributions are always welcome. If you discover bugs or have new ideas, please open the issue or submit a PR.

# LICENSE
[MIT](LICENSE)