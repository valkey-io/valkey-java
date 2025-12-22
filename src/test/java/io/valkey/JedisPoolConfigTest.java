package io.valkey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JedisPoolConfigTest {

    @Test
    public void testDefaultConstructor() {
        JedisPoolConfig config = new JedisPoolConfig();

        // Verify performance defaults
        assertEquals("Default minIdle should be 16", 16, config.getMinIdle());
        assertEquals("Default maxIdle should be 32", 32, config.getMaxIdle());
        assertEquals("Default maxTotal should be 32", 32, config.getMaxTotal());

        // Verify other defaults are still set
        assertTrue("testWhileIdle should be true", config.getTestWhileIdle());
        assertEquals("minEvictableIdleTime should be 60000ms",
                60000, config.getMinEvictableIdleTime().toMillis());
        assertEquals("timeBetweenEvictionRuns should be 30000ms",
                30000, config.getTimeBetweenEvictionRuns().toMillis());
        assertEquals("numTestsPerEvictionRun should be -1",
                -1, config.getNumTestsPerEvictionRun());
    }

    @Test
    public void testOptimizedConstructor() {
        int minIdle = 16;
        JedisPoolConfig config = new JedisPoolConfig(minIdle);

        assertEquals("MinIdle should match input", minIdle, config.getMinIdle());
        assertEquals("MaxIdle should be 2x MinIdle", 32, config.getMaxIdle());
        assertEquals("MaxTotal should be 2x MinIdle", 32, config.getMaxTotal());
    }

    @Test
    public void testCustomMinIdle() {
        JedisPoolConfig config = new JedisPoolConfig(10);

        assertEquals("MinIdle should be 10", 10, config.getMinIdle());
        assertEquals("MaxIdle should be 20", 20, config.getMaxIdle());
        assertEquals("MaxTotal should be 20", 20, config.getMaxTotal());
    }

    @Test
    public void testLargeMinIdle() {
        JedisPoolConfig config = new JedisPoolConfig(50);

        assertEquals("MinIdle should be 50", 50, config.getMinIdle());
        assertEquals("MaxIdle should be 100", 100, config.getMaxIdle());
        assertEquals("MaxTotal should be 100", 100, config.getMaxTotal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroMinIdle() {
        new JedisPoolConfig(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMinIdle() {
        new JedisPoolConfig(-5);
    }

    @Test
    public void testRatioConsistency() {
        // Test the 2x ratio is maintained across various values
        int[] testValues = { 1, 5, 8, 10, 16, 20, 32, 50, 100 };

        for (int minIdle : testValues) {
            JedisPoolConfig config = new JedisPoolConfig(minIdle);
            assertEquals("maxIdle should be 2x minIdle for value " + minIdle,
                    minIdle * 2, config.getMaxIdle());
            assertEquals("maxTotal should be 2x minIdle for value " + minIdle,
                    minIdle * 2, config.getMaxTotal());
            assertEquals("maxIdle should equal maxTotal for value " + minIdle,
                    config.getMaxIdle(), config.getMaxTotal());
        }
    }
}