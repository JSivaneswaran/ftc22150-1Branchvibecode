package org.firstinspires.ftc.teamcode.trainee;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.colorSensor;

@TeleOp(name = "ColorOpMode", group = "Teleop")
public class colorOp extends OpMode {

    String[] colorSensorNames = {"cs1", "cs2"};
    colorSensor c = new colorSensor();

    @Override
    public void init() {
        String deviceName = "cs1";
        c.init(hardwareMap, deviceName);
    }

    @Override
    public void loop() {
        c.getDetectedColor(telemetry);
    }
}
