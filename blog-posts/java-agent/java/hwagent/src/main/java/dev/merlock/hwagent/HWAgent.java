package dev.merlock.hwagent;

import java.lang.instrument.Instrumentation;

public class HWAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from the Agent!");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {

    }

}
