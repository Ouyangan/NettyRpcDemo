package com.hunt.protocol;

import java.io.Serializable;

/**
 * Created by ouyangan on 2017/6/14.
 */
public class RpcResponse  {
    private String requestId;
    private String error;
    private Object result;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RpcResponse{");
        sb.append("requestId='").append(requestId).append('\'');
        sb.append(", error='").append(error).append('\'');
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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

    public boolean isError(){
        return this.error!=null;
    }
}
