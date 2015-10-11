/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.copycat.client.request;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.util.Assert;
import io.atomix.catalyst.util.BuilderPool;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract request implementation.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public abstract class AbstractRequest<T extends Request<T>> implements Request<T> {

  @Override
  public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
  }

  @Override
  public void readObject(BufferInput<?> buffer, Serializer serializer) {
  }

  /**
   * Abstract request builder.
   *
   * @param <T> The builder type.
   * @param <U> The request type.
   */
  protected static abstract class Builder<T extends Builder<T, U>, U extends AbstractRequest<U>> extends Request.Builder<T, U> {
    private final Supplier<U> factory;
    protected U request;

    /**
     * @throws NullPointerException if {@code pool} or {@code factory} are null
     */
    protected Builder(BuilderPool<T, U> pool, Supplier<U> factory) {
      super(pool);
      this.factory = Assert.notNull(factory, "factory");
    }

    @Override
    protected void reset() {
      request = factory.get();
    }

    @Override
    protected void reset(U request) {
      this.request = Assert.notNull(request, "request");
    }

    @Override
    public U build() {
      close();
      return request;
    }

    @Override
    public int hashCode() {
      return Objects.hash(request);
    }

    @Override
    public boolean equals(Object object) {
      return getClass().isAssignableFrom(object.getClass()) && ((Builder) object).request.equals(request);
    }

    @Override
    public String toString() {
      return String.format("%s[request=%s]", getClass().getCanonicalName(), request);
    }
  }

}
