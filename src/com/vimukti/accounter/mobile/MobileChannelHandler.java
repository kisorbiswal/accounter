package com.vimukti.accounter.mobile;

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
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		String message = (String) e.getMessage();
		String networkId = (String) ctx.getAttachment();

		String messageReceived = messageHandler.messageReceived(networkId,
				message, AdaptorType.MOBILE,
				AccounterChatServer.NETWORK_TYPE_MOBILE);

		Channel channel = e.getChannel();

		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(messageReceived,
				Charset.forName("UTF-8"));
		channel.write(buffer);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ctx.setAttachment(SecureUtils.createID(16));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		String networkId = (String) ctx.getAttachment();
		messageHandler.logout(networkId);
	}
}

class MobileDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel arg1,
			ChannelBuffer buff) throws Exception {
		String string = buff.toString(Charset.forName("UTF-8"));
		buff.readerIndex(buff.writerIndex());
		return string;
	}
}