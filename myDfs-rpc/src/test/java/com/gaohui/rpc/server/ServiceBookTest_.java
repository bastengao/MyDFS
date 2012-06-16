package com.gaohui.rpc.server;

import com.gaohui.rpc.client.ServiceShop;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:46
 *
 * @author Bastengao
 */
public class ServiceBookTest_ {

    public static void main(String[] args) {
        ServiceBook serviceBook = new DefaultServiceBook(new InetSocketAddress(8888));

        serviceBook.register(HelloService.class, new HelloService() {
            @Override
            public String sayHello(String s) {
                return "hello," + s;
            }
        });
    }



    public static interface HelloService {
        public String sayHello(String s);
    }

}
