package io.github.sercasti.tracing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interceptor annotation that can be used to add the timing (runtime) of a
 * method to the server timing that is send back to the client is a response.
 * Such timing can be shown in the Chrome dev tools, for example. See
 * {@link io.github.sercasti.tracing.core.Tracing} got more details.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Traceable {
}