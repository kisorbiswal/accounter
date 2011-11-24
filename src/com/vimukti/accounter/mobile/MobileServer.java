package com.vimukti.accounter.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.vimukti.accounter.main.ServerConfiguration;

public class MobileServer {
	private ServerBootstrap bootstrap;

	public MobileServer() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		bootstrap = new ServerBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				// new SslHandler(getSSLEngine()),
				return Channels.pipeline(
						new MobileDecoder(),
						new MobileChannelHandler(MobileMessageHandler
								.getInstance()));
			}
		});

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
	}

	protected SSLEngine getSSLEngine() {
		try {
			KeyStore ks = KeyStore.getInstance("JKS");

			char[] passphrase = "importkey".toCharArray();

			ks.load(new FileInputStream(ServerConfiguration.getMobileStore()
					+ File.separator + "keystore.ImportKey"), passphrase);

			// ts.load(new FileInputStream(ServerConfiguration.getMobileStore()
			// + File.separator + "keystore"), passphrase);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, passphrase);

			// TrustManagerFactory tmf = TrustManagerFactory
			// .getInstance("SunX509");
			// tmf.init(ts);

			SSLContext sslCtx = SSLContext.getInstance("TLS");

			sslCtx.init(kmf.getKeyManagers(), null, null);

			SSLEngine sslEngine = sslCtx.createSSLEngine();
			sslEngine.setUseClientMode(false);
			sslEngine.setNeedClientAuth(false);
			return sslEngine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void strat() {
		bootstrap.bind(new InetSocketAddress(ServerConfiguration
				.getMobileChatServerPort()));
	}
}
