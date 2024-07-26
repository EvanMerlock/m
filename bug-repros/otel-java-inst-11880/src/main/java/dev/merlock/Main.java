package dev.merlock;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.kafkaclients.v2_6.KafkaTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        setupOtel();

        var prodThreads = createProducerThreads(1);
        var consThreads = createConsumerThreads(5);

        prodThreads.forEach(Thread::start);

        Thread.sleep(Duration.of(1, ChronoUnit.MINUTES));

        consThreads.forEach(Thread::start);
    }

    public static void setupOtel() {
        OpenTelemetrySdk sdk = AutoConfiguredOpenTelemetrySdk.builder().setResultAsGlobal().build().getOpenTelemetrySdk();
    }

    public static List<Thread> createProducerThreads(int n) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            threads.add(createProdThread());
        }

        return threads;
    }

    public static Thread createProdThread() {
        return new Thread(() -> {
            var kp = createProducer();
            produceMessages("example-topic", kp);
            kp.close();
        });
    }

    public static List<Thread> createConsumerThreads(int n) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            threads.add(createConsThread());
        }
        return threads;
    }

    public static Thread createConsThread() {
        return new Thread(() -> {
            var kc = createConsumer();
            consumeMessages("example-topic", kc);
            kc.close();
        });
    }

    public static KafkaProducer<String, String> createProducer() {
        Properties config = new Properties();
        try {
            config.put("client.id", InetAddress.getLocalHost().getHostName() + UUID.randomUUID());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        config.put("bootstrap.servers", "127.0.0.1:9092");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        var metricsProps = KafkaTelemetry.create(GlobalOpenTelemetry.get()).metricConfigProperties();

        config.putAll(metricsProps);

        return new KafkaProducer<>(config);
    }

    public static KafkaConsumer<String, String> createConsumer() {
        Properties config = new Properties();
        try {
            config.put("client.id", InetAddress.getLocalHost().getHostName() + UUID.randomUUID());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        config.put("group.id", "foobar" + UUID.randomUUID());
        config.put("bootstrap.servers", "127.0.0.1:9092");

        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        var metricsProps = KafkaTelemetry.create(GlobalOpenTelemetry.get()).metricConfigProperties();

        config.putAll(metricsProps);

        return new KafkaConsumer<>(config);
    }

    public static void produceMessages(String topic, KafkaProducer<String, String> producer) {
        var i = 0;
        var startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 300_000) {
            producer.send(new ProducerRecord<>(
                    topic, "I am an example message! " + i
            ));
            i++;

            try {
                Thread.sleep(Duration.of(1, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void consumeMessages(String topic, KafkaConsumer<String, String> consumer) {
        int i = 0;
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(1000, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("KM Recv: " + record.value());
            }
            consumer.commitSync();
            i++;
            if (shouldTerminate(i)) {
                break;
            }
        }
    }

    public static boolean shouldTerminate(int i) {
        var randomizer = new Random();
        var pick = randomizer.nextInt(1_000);

        return i > pick;
    }

}
