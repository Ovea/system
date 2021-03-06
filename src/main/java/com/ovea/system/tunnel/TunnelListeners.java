/**
 * Copyright (C) 2011 Ovea <dev@ovea.com>
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
package com.ovea.system.tunnel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TunnelListeners implements TunnelListener {

    private final List<TunnelListener> listeners = new CopyOnWriteArrayList<TunnelListener>();

    public TunnelListeners(TunnelListener... listeners) {
        this(Arrays.asList(listeners));
    }

    public TunnelListeners(Iterable<? extends TunnelListener> listeners) {
        add(listeners);
    }

    public void add(TunnelListener listener) {
        listeners.add(listener);
    }

    public void add(TunnelListener... listeners) {
        add(Arrays.asList(listeners));
    }

    public void add(Iterable<? extends TunnelListener> listeners) {
        for (TunnelListener listener : listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void onConnect(Tunnel tunnel) {
        for (TunnelListener listener : listeners) {
            listener.onConnect(tunnel);
        }
    }

    @Override
    public void onClose(Tunnel tunnel) {
        for (TunnelListener listener : listeners) {
            listener.onClose(tunnel);
        }
    }

    @Override
    public void onBroken(Tunnel tunnel, BrokenTunnelException e) {
        for (TunnelListener listener : listeners) {
            listener.onBroken(tunnel, e);
        }
    }

    @Override
    public void onInterrupt(Tunnel tunnel) {
        for (TunnelListener listener : listeners) {
            listener.onInterrupt(tunnel);
        }
    }
}
