package io.young.rpc.protocol;

import io.young.rpc.protocol.header.YoungHeader;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2024/12/23 18:47
 * @descritpion YoungProtocol 协议类
 */
public class YoungProtocol<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -946538215304334848L;

    /**
     * 消息头
     */
    private YoungHeader header;

    /**
     * 消息体
     */
    private T body;

    public YoungHeader getHeader() {
        return header;
    }

    public void setHeader(YoungHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
