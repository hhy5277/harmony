/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package javax.net.ssl;

import java.io.IOException;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;

import javax.net.SocketFactory;

/**
 * The abstract factory implementation to create {@code SSLSocket}s.
 */
public abstract class SSLSocketFactory extends SocketFactory {
    // FIXME EXPORT CONTROL

    // The default SSL socket factory
    private static SocketFactory defaultSocketFactory;

    private static String defaultName;

    /**
     * Returns the default {@code SSLSocketFactory} instance. The default is
     * defined by the security property {@code 'ssl.SocketFactory.provider'}.
     *
     * @return the default ssl socket factory instance.
     */
    public static synchronized SocketFactory getDefault() {
        if (defaultSocketFactory != null) {
            return defaultSocketFactory;
        }
        if (defaultName == null) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    defaultName = Security.getProperty("ssl.SocketFactory.provider");
                    if (defaultName != null) {
                        ClassLoader cl = Thread.currentThread().getContextClassLoader();
                        if (cl == null) {
                            cl = ClassLoader.getSystemClassLoader();
                        }
                        try {
                            final Class<?> sfc = Class.forName(defaultName, true, cl);
                            defaultSocketFactory = (SocketFactory) sfc.newInstance();
                        } catch (Exception e) {
                        }
                    }
                    return null;
                }
            });
        }

        if (defaultSocketFactory == null) {
            // Try to find in providers
            SSLContext context = DefaultSSLContext.getContext();
            if (context != null) {
                defaultSocketFactory = context.getSocketFactory();
            }
        }
        if (defaultSocketFactory == null) {
            // Use internal implementation
            defaultSocketFactory = new DefaultSSLSocketFactory("No SSLSocketFactory installed");
        }
        return defaultSocketFactory;
    }

    /**
     * Creates a new {@code SSLSocketFactory}.
     */
    public SSLSocketFactory() {
        super();
    }

    /**
     * Returns the names of the cipher suites that are enabled by default.
     *
     * @return the names of the cipher suites that are enabled by default.
     */
    public abstract String[] getDefaultCipherSuites();

    /**
     * Returns the names of the cipher suites that are supported and could be
     * enabled for an SSL connection.
     *
     * @return the names of the cipher suites that are supported.
     */
    public abstract String[] getSupportedCipherSuites();

    /**
     * Creates an {@code SSLSocket} over the specified socket that is connected
     * to the specified host at the specified port.
     *
     * @param s
     *            the socket.
     * @param host
     *            the host.
     * @param port
     *            the port number.
     * @param autoClose
     *            {@code true} if socket {@code s} should be closed when the
     *            created socket is closed, {@code false} if the socket
     *            {@code s} should be left open.
     * @return the creates ssl socket.
     * @throws IOException
     *             if creating the socket fails.
     * @throws java.net.UnknownHostException
     *             if the host is unknown.
     */
    public abstract Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException;
}
