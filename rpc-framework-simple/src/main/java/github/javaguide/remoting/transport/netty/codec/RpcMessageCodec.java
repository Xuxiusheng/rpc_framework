package github.javaguide.remoting.transport.netty.codec;

import github.javaguide.compress.Compress;
import github.javaguide.enums.CompressTypeEnum;
import github.javaguide.enums.SerializationTypeEnum;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.remoting.constants.RpcConstants;
import github.javaguide.remoting.dto.RpcMessage;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.dto.RpcResponse;
import github.javaguide.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class RpcMessageCodec extends MessageToMessageCodec<ByteBuf, RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, List<Object> list) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        checkMagicNumber(byteBuf);
        checkVersion(byteBuf);

        int fullLength = byteBuf.readInt();

        byte messageType = byteBuf.readByte();
        byte codecType = byteBuf.readByte();
        byte compressType = byteBuf.readByte();
        int requestId = byteBuf.readInt();

        RpcMessage rpcMessage = new RpcMessage();

        rpcMessage.setMessageType(messageType);
        rpcMessage.setCodec(codecType);
        rpcMessage.setCompress(compressType);
        rpcMessage.setRequestId(requestId);

        if(messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            list.add(rpcMessage);
            return;
        }

        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            list.add(rpcMessage);
            return;
        }

        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if(bodyLength > 0) {
            byte[] bodyBytes = new byte[bodyLength];
            byteBuf.readBytes(bodyBytes);

            String compressName = CompressTypeEnum.getName(compressType);
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
            log.debug("before decompress request body size: [{}]", bodyBytes.length);
            bodyBytes = compress.decompress(bodyBytes);
            log.debug("after decompress request body size: [{}]", bodyBytes.length);

            String codecName = SerializationTypeEnum.getName(codecType);
            log.debug("codec name: [{}] ", codecName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);

            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                rpcMessage.setData(rpcRequest);
            } else {
                RpcResponse rpcRequest = serializer.deserialize(bodyBytes, RpcResponse.class);
                rpcMessage.setData(rpcRequest);
            }

        }
        list.add(rpcMessage);
    }

    private void checkMagicNumber(ByteBuf in) {
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for(int i = 0; i < len; i++) {
            if(tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("未知魔数: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("协议版本不匹配" + version);
        }
    }
}
