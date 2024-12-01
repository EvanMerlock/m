package dev.merlock.hwagent.mod2;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class HWInterceptor {

    public static void interceptor(@Origin Method m) {
        System.out.println("Hello from " + m.getName() + "!");
    }

}
