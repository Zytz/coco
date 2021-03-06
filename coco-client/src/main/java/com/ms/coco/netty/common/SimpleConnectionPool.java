/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ms.coco.netty.common;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.ms.coco.exception.RpcFrameworkException;
import com.ms.coco.exception.RpcNettyConnectLessException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;

/**
 * 
 * TODO: DOCUMENT ME!
 * 
 * @author netboy
 */
public class SimpleConnectionPool extends SimpleChannelPool implements ConnectionPool {
	private static final Logger logger = LoggerFactory.getLogger(SimpleConnectionPool.class);

    protected static final int DEFAULT_CONNECT_TIMEOUT = 20000;
	protected final SocketAddress socketAddress;
	protected final String host;
	protected final int port;
    private Map<Integer, Channel> okChannels = Maps.newConcurrentMap();
    protected int connectTimeout = 20000; // default 2s
	protected ConnectionPoolContext poolContext;

	public SimpleConnectionPool(Bootstrap bootstrap, SocketAddress remoteAddress) {
		this(bootstrap, HandlerConfig.DEFAULT_CONFIG, remoteAddress, DEFAULT_CONNECT_TIMEOUT);
	}

	public SimpleConnectionPool(Bootstrap bootstrap, HandlerConfig handlerConfig, SocketAddress remoteAddress) {
		this(bootstrap, handlerConfig, remoteAddress, DEFAULT_CONNECT_TIMEOUT);
	}

	public SimpleConnectionPool(Bootstrap bootstrap, HandlerConfig handlerConfig, SocketAddress remoteAddress, int connectTimeout) {
		super(bootstrap, new RpcClientChannelPoolHandler(handlerConfig, remoteAddress));
		this.connectTimeout = connectTimeout;
		this.socketAddress = remoteAddress;
		if (remoteAddress instanceof InetSocketAddress) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
			host = inetSocketAddress.getAddress().getHostAddress();
			port = inetSocketAddress.getPort();
		} else if (remoteAddress instanceof LocalAddress) {
			LocalAddress localAddress = (LocalAddress) remoteAddress;
			int myPort = -1;
			try {
				myPort = Integer.parseInt(localAddress.id());
			} catch (NumberFormatException e) {
                throw new RpcFrameworkException(localAddress.id() + " port parse error", e);
			}

			host = "local";
			port = myPort;
		} else {
            throw new RpcFrameworkException(
					"SocketAddress must be '" + InetSocketAddress.class.getName() + "' or '" + LocalAddress.class.getName() + "' (sub) class");
		}

		poolContext = new ConnectionPoolContext(handlerConfig.getResponsePromiseContainer());
	}

	@Override
	protected ChannelFuture connectChannel(Bootstrap bs) {
		bs.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
		return bs.connect(socketAddress);
	}

	public Channel acquireConnect() throws RpcNettyConnectLessException {
        // if (okChannels.size() > 0) {
        // return okChannels.get(0);
        // }
        Future<Channel> future = acquire();
		// see https://netty.io/4.0/api/io/netty/channel/ChannelFuture.html
		// use bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
		// so await without timeout
		future.awaitUninterruptibly();

		assert future.isDone();

		if (future.isCancelled()) {
			// Connection attempt cancelled by user
			throw new RpcNettyConnectLessException("connection cancelled tcp=" + socketAddress);
		} else if (!future.isSuccess()) {
			throw new RpcNettyConnectLessException("connect tcp=" + socketAddress + " fail within " + connectTimeout + "ms time!", future.cause());
		} else {
			// Connection established successfully
			Channel channel = future.getNow();

			if (logger.isDebugEnabled()) {
				logger.debug("acquire connect success channel={}", channel);
			}

			assert channel != null;

			if (channel == null) {
				throw new RpcNettyConnectLessException("connect tcp=" + socketAddress + " fail within " + connectTimeout + "ms time, future.getNow return null!");
			}
            // okChannels.put(channel.hashCode(), channel);
			return channel;
		}
	}

	@Override
	public void releaseConnect(Channel channel) {
        // okChannels.remove(channel.hashCode());
		Future<Void> future = release(channel);

		//TODO  wait future done ?
	}

	@Override
	public ConnectionPoolContext poolContext() {
		return poolContext;
	}

	@Override
	public String channelHost() {
		return host;
	}

	@Override
	public int channelPort() {
		return port;
	}

	public void setConnectTimeout(int connectTimeout) {
		if (connectTimeout < 10) {
			throw new IllegalArgumentException("connectTimeout can't < 10ms");
		}
		this.connectTimeout = connectTimeout;
	}

}
