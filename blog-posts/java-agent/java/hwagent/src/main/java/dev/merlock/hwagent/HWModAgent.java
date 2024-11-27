package dev.merlock.hwagent;

import java.lang.instrument.Instrumentation;

public class HWModAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from the PreMain!");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from the AgentMain!");
    }

}
