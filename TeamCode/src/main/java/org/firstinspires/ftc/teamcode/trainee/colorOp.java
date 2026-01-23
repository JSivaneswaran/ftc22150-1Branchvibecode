package org.firstinspires.ftc.teamcode.trainee;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.colorSensor;

public class colorOp extends OpMode {

    colorSensor c = new colorSensor();

    @Override
    public void init() {
        String deviceName = "";
        c.init(hardwareMap, deviceName);
    }

    @Override
    public void loop() {
        telemetry.addData("Color Detected:",c.getDetectedColor());
    }
}
