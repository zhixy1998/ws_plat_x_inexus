package com.persee.workstation.demo.model;

/**
 * 前端 sendMessage 时 data 字段的承载结构
 * <p>
 * 例：
 * <pre>
 * { "operation": "conciseZeroCalibration",
 *   "data": {
 *     "deviceSerialNumber": "35-F1701-01-0001",
 *     "taskId": "d57c9943422da2b6672603df23f89f65",
 *     "methodId": "22b82658696c8708729ecb4e9e9549d3"
 *   }
 * }
 * </pre>
 */
public class OperationData {

    private String deviceSerialNumber;
    private String taskId;
    private String methodId;

    public OperationData() {
    }

    public OperationData(String deviceSerialNumber, String taskId, String methodId) {
        this.deviceSerialNumber = deviceSerialNumber;
        this.taskId = taskId;
        this.methodId = methodId;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    @Override
    public String toString() {
        return "OperationData{deviceSerialNumber='" + deviceSerialNumber +
                "', taskId='" + taskId + "', methodId='" + methodId + "'}";
    }
}
