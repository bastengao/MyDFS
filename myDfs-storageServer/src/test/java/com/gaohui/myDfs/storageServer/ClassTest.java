package com.gaohui.myDfs.storageServer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Administrator
 * Date: 11-2-10 Time: 下午10:54
 *
 * @author Basten Gao
 */
public class ClassTest {
    @Test
    public void test() {
        Class listClass = List.class;
        Class arrayListClass = ArrayList.class;
        System.out.println(listClass.isAssignableFrom(arrayListClass));

    }
}
