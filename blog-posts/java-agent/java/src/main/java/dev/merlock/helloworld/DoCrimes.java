package dev.merlock.helloworld;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DoCrimes {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ClassLoader willACrimeBeCommited = new CrimeClassloader();

        Class<?> output = willACrimeBeCommited.loadClass("this.isnt.a.real.name");

        Method helloWorld = output.getMethod("main", args.getClass());

        String[] arguments = new String[]{""};
        helloWorld.invoke(null, new Object[]{arguments});
    }
}
