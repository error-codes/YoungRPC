package io.young.rpc.protocol.response;

import io.young.rpc.protocol.base.BaseMessage;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/23 14:26
 * @descritpion YoungResponse
 */
public class YoungResponse extends BaseMessage {

    @Serial
    private static final long serialVersionUID = -6722961400231673348L;

    private String error;

    private Object result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
