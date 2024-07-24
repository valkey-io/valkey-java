package io.valkey.commands.jedis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import io.valkey.BinaryJedisShardedPubSub;
import io.valkey.Connection;
import io.valkey.Jedis;
import io.valkey.JedisShardedPubSub;
import io.valkey.util.JedisClusterCRC16;
import io.valkey.util.SafeEncoder;
import org.junit.Assert;
import org.junit.Test;

public class ClusterShardedPublishSubscribeCommandsTest extends ClusterJedisCommandsTestBase {

  private void publishOne(final String channel, final String message) {
    Thread t = new Thread(() -> cluster.spublish(channel, message));
    t.start();
  }

  @Test
  public void subscribe() throws InterruptedException {
    cluster.ssubscribe(new JedisShardedPubSub() {
      @Override public void onSMessage(String channel, String message) {
        assertEquals("foo", channel);
        assertEquals("exit", message);
        sunsubscribe();
      }

      @Override public void onSSubscribe(String channel, int subscribedChannels) {
        assertEquals("foo", channel);
        assertEquals(1, subscribedChannels);

        // now that I'm subscribed... publish
        publishOne("foo", "exit");
      }

      @Override public void onSUnsubscribe(String channel, int subscribedChannels) {
        assertEquals("foo", channel);
        assertEquals(0, subscribedChannels);
      }
    }, "foo");
  }

  @Test
  public void subscribeMany() {
    cluster.ssubscribe(new JedisShardedPubSub() {
      @Override public void onSMessage(String channel, String message) {
        sunsubscribe(channel);
      }

      @Override public void onSSubscribe(String channel, int subscribedChannels) {
        publishOne(channel, "exit");
      }

    }, "{foo}", "{foo}bar");
  }

  @Test
  public void pubSubChannels() {
    cluster.ssubscribe(new JedisShardedPubSub() {
      private int count = 0;

      @Override public void onSSubscribe(String channel, int subscribedChannels) {
        count++;
        // All channels are subscribed
        if (count == 3) {
          try (Connection conn = cluster.getConnectionFromSlot(JedisClusterCRC16.getSlot("testchan"));
               Jedis jedis = new Jedis(conn)) {
            assertThat(jedis.pubsubShardChannels(),
                hasItems("{testchan}1", "{testchan}2", "{testchan}3"));
          }
          sunsubscribe();
        }
      }
    }, "{testchan}1", "{testchan}2", "{testchan}3");
  }

  @Test
  public void pubSubChannelsWithPattern() {
    cluster.ssubscribe(new JedisShardedPubSub() {
      private int count = 0;

      @Override public void onSSubscribe(String channel, int subscribedChannels) {
        count++;
        // All channels are subscribed
        if (count == 3) {
          try (Connection conn = cluster.getConnectionFromSlot(JedisClusterCRC16.getSlot("testchan"));
              Jedis otherJedis = new Jedis(conn)) {
            assertThat(otherJedis.pubsubShardChannels("*testchan*"),
                hasItems("{testchan}1", "{testchan}2", "{testchan}3"));
          }
          sunsubscribe();
        }
      }
    }, "{testchan}1", "{testchan}2", "{testchan}3");
  }

  @Test
  public void pubSubNumSub() {
    final Map<String, Long> expectedNumSub = new HashMap<>();
    expectedNumSub.put("{testchannel}1", 1L);
    expectedNumSub.put("{testchannel}2", 1L);

    cluster.ssubscribe(new JedisShardedPubSub() {
      private int count = 0;

      @Override public void onSSubscribe(String channel, int subscribedChannels) {
        count++;
        if (count == 2) {
          try (Connection conn = cluster.getConnectionFromSlot(JedisClusterCRC16.getSlot("testchannel"));
              Jedis otherJedis = new Jedis(conn)) {
            Map<String, Long> numSub = otherJedis.pubsubShardNumSub("{testchannel}1", "{testchannel}2");
            assertEquals(expectedNumSub, numSub);
          }
          sunsubscribe();
        }
      }
    }, "{testchannel}1", "{testchannel}2");
  }

  @Test
  public void binarySubscribe() {
    cluster.ssubscribe(new BinaryJedisShardedPubSub() {
      @Override public void onSMessage(byte[] channel, byte[] message) {
        Assert.assertArrayEquals(SafeEncoder.encode("foo"), channel);
        assertArrayEquals(SafeEncoder.encode("exit"), message);
        sunsubscribe();
      }

      @Override public void onSSubscribe(byte[] channel, int subscribedChannels) {
        assertArrayEquals(SafeEncoder.encode("foo"), channel);
        assertEquals(1, subscribedChannels);
        publishOne(SafeEncoder.encode(channel), "exit");
      }

      @Override public void onSUnsubscribe(byte[] channel, int subscribedChannels) {
        assertArrayEquals(SafeEncoder.encode("foo"), channel);
        assertEquals(0, subscribedChannels);
      }
    }, SafeEncoder.encode("foo"));
  }

  @Test
  public void binarySubscribeMany() {
    cluster.ssubscribe(new BinaryJedisShardedPubSub() {
      @Override public void onSMessage(byte[] channel, byte[] message) {
        sunsubscribe(channel);
      }

      @Override public void onSSubscribe(byte[] channel, int subscribedChannels) {
        publishOne(SafeEncoder.encode(channel), "exit");
      }
    }, SafeEncoder.encode("{foo}"), SafeEncoder.encode("{foo}bar"));
  }

}
