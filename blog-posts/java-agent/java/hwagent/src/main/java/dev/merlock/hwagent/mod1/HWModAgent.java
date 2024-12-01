package dev.merlock.hwagent.mod1;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class HWModAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from the PreMain!");

        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(
                        (builder, type, cl, module, pd) ->
                            builder
                                    .method(ElementMatchers.any())
                                    .intercept(MethodDelegation.to(HWInterceptor.class))
                )
                .installOn(inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from the AgentMain!");

        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(
                        (builder, type, cl, module, pd) ->
                                builder
                                        .method(ElementMatchers.any())
                                        .intercept(MethodDelegation.to(HWInterceptor.class))
                )
                .installOn(inst);
    }

}
