package com.gaohui.rpc;

import com.gaohui.rpc.client.ClientServiceFactory;
import com.gaohui.rpc.client.DefaultClientServiceFactory;
import com.gaohui.rpc.client.DefaultServiceShop;
import com.gaohui.rpc.client.ServiceShop;
import com.gaohui.rpc.server.ServiceBookTest_;

import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:37
 *
 * @author Bastengao
 */
public class ServiceShopTest_ {

    public static void main(String[] args) {
        ServiceShop serviceShop = new DefaultServiceShop(new DefaultClientServiceFactory(new InetSocketAddress(8888)));

        ServiceBookTest_.HelloService helloService = serviceShop.getService(ServiceBookTest_.HelloService.class);

        String response = helloService.sayHello("gaohui");
        System.out.println(response);

        serviceShop.returnService(helloService);
    }

}
