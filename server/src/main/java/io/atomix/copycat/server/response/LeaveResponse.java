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
package io.atomix.copycat.server.response;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.SerializeWith;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.util.BuilderPool;
import io.atomix.copycat.client.error.RaftError;
import io.atomix.copycat.client.response.AbstractResponse;

import java.util.Objects;

/**
 * Protocol leave response.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@SerializeWith(id=271)
public class LeaveResponse extends AbstractResponse<LeaveResponse> {

  private static final BuilderPool<Builder, LeaveResponse> POOL = new BuilderPool<>(Builder::new);

  /**
   * Returns a new leave response builder.
   *
   * @return A new leave response builder.
   */
  public static Builder builder() {
    return POOL.acquire();
  }

  /**
   * Returns an leave response builder for an existing response.
   *
   * @param response The response to build.
   * @return The leave response builder.
   */
  public static Builder builder(LeaveResponse response) {
    return POOL.acquire(response);
  }

  @Override
  public void readObject(BufferInput buffer, Serializer serializer) {
    status = Status.forId(buffer.readByte());
    if (status == Status.OK) {
      error = null;
    } else {
      error = RaftError.forId(buffer.readByte());
    }
  }

  @Override
  public void writeObject(BufferOutput buffer, Serializer serializer) {
    buffer.writeByte(status.id());
    if (status == Status.ERROR) {
      buffer.writeByte(error.id());
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), status);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof LeaveResponse) {
      LeaveResponse response = (LeaveResponse) object;
      return response.status == status;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[status=%s]", getClass().getSimpleName(), status);
  }

  /**
   * Leave response builder.
   */
  public static class Builder extends AbstractResponse.Builder<Builder, LeaveResponse> {

    protected Builder(BuilderPool<Builder, LeaveResponse> pool) {
      super(pool, LeaveResponse::new);
    }

    @Override
    public int hashCode() {
      return Objects.hash(response);
    }

    @Override
    public boolean equals(Object object) {
      return object instanceof Builder && ((Builder) object).response.equals(response);
    }

    @Override
    public String toString() {
      return String.format("%s[response=%s]", getClass().getCanonicalName(), response);
    }

  }

}
