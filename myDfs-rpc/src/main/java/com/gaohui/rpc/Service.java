package com.gaohui.rpc;

/**
 * 表示一个远程服务
 * User: Administrator
 * Date: 11-1-5 Time: 下午2:05
 *
 * @author Bastengao
 */

public class Service {
    private String name;

    public Service(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
