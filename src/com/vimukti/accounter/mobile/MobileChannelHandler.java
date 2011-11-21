package com.vimukti.accounter.mobile;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.utils.SecureUtils;

public class MobileChannelHandler extends SimpleChannelHandler {

	private MobileMessageHandler messageHandler;

	public MobileChannelHandler(MobileMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) throws Exception {
		String message = (String) e.getMessage();
		String networkId = (String) ctx.getAttachment();
		MobileChannelContext context = new MobileChannelContext(networkId,
				message, AdaptorType.MOBILE,
				AccounterChatServer.NETWORK_TYPE_MOBILE) {

			@Override
			public void send(String string) {
				if (string.isEmpty()) {
					System.out.println();
				}
				Channel channel = e.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(string.length() + 4);
				buffer.putInt(string.length());
				buffer.put(string.getBytes());
				channel.write(ChannelBuffers.wrappedBuffer(buffer));
			}

			@Override
			public void changeNetworkId(String networkId) {
				ctx.setAttachment(networkId);
			}
		};
		messageHandler.putMessage(context);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ctx.setAttachment(SecureUtils.createID(16));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		// String networkId = (String) ctx.getAttachment();
		// messageHandler.logout(networkId);
	}
}

class MobileDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel arg1,
			ChannelBuffer buff) throws Exception {
		ByteBuffer allocate = ByteBuffer.allocate(4);
		buff.readBytes(allocate);
		String string = buff.toString(Charset.forName("UTF-8"));
		System.out.println(string);
		buff.readerIndex(buff.writerIndex());
		return string;
	}
}