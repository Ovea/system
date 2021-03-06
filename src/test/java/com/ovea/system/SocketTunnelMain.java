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
package com.ovea.system;

import com.ovea.system.tunnel.BrokenTunnelException;
import com.ovea.system.tunnel.Tunnel;
import com.ovea.system.tunnel.TunnelListener;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SocketTunnelMain {
    public static void main(String[] args) throws IOException {
        // start 2 netcat daemons first before running this class
        Process nc2000 = new ProcessBuilder("C:\\cygwin\\bin\\nc.exe", "-l", "-v", "-p", "2000").start();
        Process nc2222 = new ProcessBuilder("C:\\cygwin\\bin\\nc.exe", "-l", "-v", "-p", "2222").start();

        final Socket socket1 = new Socket("localhost", 2000);
        final Socket socket2 = new Socket("localhost", 2222);
        final Tunnel tunnel = Tunnel.connect(socket1, socket2, new TunnelListener() {
            @Override
            public void onConnect(Tunnel tunnel) {
                System.out.println("onConnect - " + tunnel);
            }

            @Override
            public void onClose(Tunnel tunnel) {
                System.out.println("onClose - " + tunnel);
            }

            @Override
            public void onBroken(Tunnel tunnel, BrokenTunnelException e) {
                System.out.println("onBroken - " + tunnel);
            }

            @Override
            public void onInterrupt(Tunnel tunnel) {
                System.out.println("onInterrupt - " + tunnel);
            }
        });
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    tunnel.interrupt();
                } catch (InterruptedException ignored) {
                }
            }
        }.start();
        try {
            tunnel.await();
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
        } catch (BrokenTunnelException e) {
            e.printStackTrace(System.out);
        }
    }
}
