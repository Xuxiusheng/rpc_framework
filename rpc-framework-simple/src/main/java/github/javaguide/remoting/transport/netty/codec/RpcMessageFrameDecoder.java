package github.javaguide.remoting.transport.netty.codec;

import github.javaguide.remoting.constants.RpcConstants;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RpcMessageFrameDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageFrameDecoder() {
        super(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }
}
