package com.cloud.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class TracingContextResolver implements HandlerMethodArgumentResolver {

    private final Tracer tracer;
    private final ObjectMapper objectMapper;
    private final String SPAN_CTX = "span_ctx";

    public TracingContextResolver(Tracer tracer, ObjectMapper objectMapper) {
        this.tracer = tracer;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Span.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        Tracer.SpanBuilder childSpan = buildChildSpanFor(parameter);
        checkAndSetParentSpanContext(message.getHeaders(), childSpan);
        Span span = childSpan.start();
        tracer.activateSpan(span);
        setTags(span);
        return span;
    }

    private void setTags(Span span) {
    }

    private Tracer.SpanBuilder buildChildSpanFor(MethodParameter parameter) {
        String className = Optional.ofNullable(parameter.getMethod())
                .map(Method::getDeclaringClass)
                .map(Class::getName)
                .orElse("ClassNameNotFound");
        String methodName = Optional.ofNullable(parameter.getMethod())
                .map(Method::getName)
                .orElse("MethodNameNotFound");
        String invoker = String.format("%s.%s", className, methodName);
        return tracer.buildSpan(invoker).ignoreActiveSpan();
    }

    private void checkAndSetParentSpanContext(MessageHeaders messageHeaders,
                                              Tracer.SpanBuilder childSpan) throws Exception {
        Object spanContextHeader = messageHeaders.get(SPAN_CTX);
        Optional<String> parentSpanContextName = Optional.ofNullable(spanContextHeader).map(Object::toString);
        if (parentSpanContextName.isPresent()) {
            addParentReference(childSpan, parentSpanContextName.get());
        }
    }

    private void addParentReference(Tracer.SpanBuilder childSpan, String parentSpanContextName) throws Exception {
        String decodedParentContext = URLDecoder.decode(parentSpanContextName, StandardCharsets.UTF_8);
        Map<String, String> spanContextMap = objectMapper.readValue(decodedParentContext, new TypeReference<>() {
        });
        SpanContext parentSpanContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(spanContextMap));
        if (Objects.nonNull(parentSpanContext)) {
            childSpan.asChildOf(parentSpanContext);
        }
    }
}
