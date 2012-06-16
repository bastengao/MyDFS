package com.gaohui.myDfs.leaderServer;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午9:39
 *
 * @author Basten Gao
 */
public class ConfigTest {
    @Test
    public void test() throws IOException {
        Config config = new Config();
        assertEquals(8888, config.getRpcPort());
    }

    @Test
    public void test2() throws IOException {
        Config config = new Config("config.properties");
        assertEquals(8888, config.getRpcPort());
    }

    @Test(expected = NullPointerException.class)
    public void test3() throws IOException {
        Config config = new Config("notExists.properties");
        assertEquals(8888, config.getRpcPort());
    }
}
