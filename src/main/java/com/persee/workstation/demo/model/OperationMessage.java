package com.persee.workstation.demo.model;

/**
 * 前端 sendMessage 发送的消息体
 * <p>
 * 例：
 * <pre>
 * {
 *   "operation": "conciseZeroCalibration",
 *   "data": { ...OperationData... }
 * }
 * </pre>
 */
public class OperationMessage {

    private String operation;
    private OperationData data;

    public OperationMessage() {
    }

    public OperationMessage(String operation, OperationData data) {
        this.operation = operation;
        this.data = data;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public OperationData getData() {
        return data;
    }

    public void setData(OperationData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OperationMessage{operation='" + operation + "', data=" + data + '}';
    }
}
