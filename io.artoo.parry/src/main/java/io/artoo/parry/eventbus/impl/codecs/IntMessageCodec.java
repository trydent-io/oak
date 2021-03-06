/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.artoo.parry.eventbus.impl.codecs;


import io.artoo.parry.buffer.Buffer;
import io.artoo.parry.eventbus.MessageCodec;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class IntMessageCodec implements MessageCodec<Integer, Integer> {

  @Override
  public void encodeToWire(Buffer buffer, Integer i) {
    buffer.appendInt(i);
  }

  @Override
  public Integer decodeFromWire(int pos, Buffer buffer) {
    return buffer.getInt(pos);
  }

  @Override
  public Integer transform(Integer i) {
    // Integers are immutable so just return it
    return i;
  }

  @Override
  public String name() {
    return "int";
  }

  @Override
  public byte systemCodecID() {
    return 5;
  }
}
