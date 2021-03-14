package entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 潘聪
 * @description
 * @date 2021/3/14 20:54
 */
@Data
public class CPU {

    private String cpuId;
    private double temperature;
    private Date captureTime;

    public CPU() {
    }

    public CPU(String cpuId, double temperature, Date captureTime) {
        this.cpuId = cpuId;
        this.temperature = temperature;
        this.captureTime = captureTime;
    }
}
