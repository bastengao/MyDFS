package com.gaohui.rpc.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例服务提供者。此实现在查找相同的服务时总是返回相同的服务对象
 * User: Administrator
 * Date: 11-1-16 Time: 下午6:06
 *
 * @author Basten Gao
 */
public class SingletonServiceProvider implements ServiceProvider {
    private Map<String, Object> services = new HashMap<String, Object>();

    public void register(String servcieName, Object service) {
        services.put(servcieName, service);
    }

    @Override
    public boolean hasService(String serviceName) {
        return services.containsKey(serviceName);
    }

    @Override
    public Object getService(String name) {
        return services.get(name);
    }
}
