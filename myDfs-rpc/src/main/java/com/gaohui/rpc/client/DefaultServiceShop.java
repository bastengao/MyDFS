package com.gaohui.rpc.client;

import com.gaohui.rpc.Closable;

/**
 * User: Administrator
 * Date: 11-1-8 Time: 下午10:33
 *
 * @author Basten Gao
 */
public class DefaultServiceShop implements ServiceShop {
    private ClientServiceFactory clientServiceFactory;

    public DefaultServiceShop(ClientServiceFactory clientServiceFactory) {
        this.clientServiceFactory = clientServiceFactory;
    }

    @Override
    public <T> T getService(Class<T> t) {
        return clientServiceFactory.createClientService(t, t.getName());
    }

    @Override
    public <T> T getService(Class<T> t, String serviceName) {
        return clientServiceFactory.createClientService(t, t.getName());
    }

    @Override
    public Object getService(String serviceName) {
        return clientServiceFactory.createClientService(null, serviceName);
    }

    @Override
    public void returnService(Object clientService) {
        Closable closable = (Closable) clientService;
        closable.close();
    }
}
