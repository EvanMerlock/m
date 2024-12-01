package dev.merlock.hwagent.mod1;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class HWInterceptor {

    public static void interceptor() {
        System.out.println("Hello from an interceptor!");
    }

}
