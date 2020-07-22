package retrofit2;

import kotlin.coroutines.CoroutineContext;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A single invocation of a Retrofit service interface method. This class captures both the method
 * that was called and the arguments to the method.
 *
 *

 Retrofit automatically adds an invocation to each OkHttp request as a tag. You can retrieve
 * the invocation in an OkHttp interceptor for metrics and monitoring.
 *
 *



 * class InvocationLogger implements Interceptor {

 *   @Override public Response intercept(Chain chain) throws IOException {

 *     Request request = chain.request();

 *     Invocation invocation = request.tag(Invocation.class);

 *     if (invocation != null) {

 *       System.out.printf("%s.%s %s%n",

 *           invocation.method().getDeclaringClass().getSimpleName(),

 *           invocation.method().getName(), invocation.arguments());

 *     }

 *     return chain.proceed(request);

 *   }

 * }

 *


 *
 * Note: use caution when examining an invocation's arguments. Although the
 * arguments list is unmodifiable, the arguments themselves may be mutable. They may also be unsafe
 * for concurrent access. For best results declare Retrofit service interfaces using only immutable
 * types for parameters!
 */
public final class Invocation {
  public static Invocation of(Method method, List arguments) {
    return Invocation.of(method, arguments, null);
  }

  public static Invocation of(Method method, List arguments, @Nullable CoroutineContext kotlinCoroutineContext) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(arguments, "arguments == null");
    return new Invocation(method, new ArrayList<>(arguments), kotlinCoroutineContext); // Defensive copy of arguments.
  }

  private final Method method;
  private final List arguments;
  @Nullable private final CoroutineContext kotlinCoroutineContext;

  /** Trusted constructor assumes ownership of {@code arguments}. */
  Invocation(Method method, List arguments, @Nullable CoroutineContext kotlinCoroutineContext) {
    this.method = method;
    this.arguments = Collections.unmodifiableList(arguments);
    this.kotlinCoroutineContext = kotlinCoroutineContext;
  }

  public Method method() {
    return method;
  }

  public List arguments() {
    return arguments;
  }

  @Nullable public CoroutineContext kotlinCoroutineContext() {
    return kotlinCoroutineContext;
  }

  @Override public String toString() {
    return String.format("%s.%s() %s",
            method.getDeclaringClass().getName(), method.getName(), arguments);
  }
}