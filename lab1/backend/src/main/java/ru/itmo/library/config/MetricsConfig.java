package ru.itmo.library.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    private final MeterRegistry registry;

    public MetricsConfig(MeterRegistry registry) {
        this.registry = registry;
    }

    private final LogbackMetrics logbackMetrics = new LogbackMetrics();
    private final JvmGcMetrics jvmGcMetrics = new JvmGcMetrics();

    @PostConstruct
    public void init() {
        logbackMetrics.bindTo(registry);
        jvmGcMetrics.bindTo(registry);
    }

    @PreDestroy
    public void cleanup() {
        logbackMetrics.close();
        jvmGcMetrics.close();
    }

    @PostConstruct
    public void bindMetrics() {
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);

        new JvmThreadMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);
        new FileDescriptorMetrics().bindTo(registry);
        new DiskSpaceMetrics(new java.io.File(".")).bindTo(registry);
    }
}
