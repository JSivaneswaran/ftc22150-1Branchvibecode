package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "GettingColorValues")
public class colorTestOp extends OpMode {
    private colorDemo color = new colorDemo();

    @Override
    public void init() {
        color.init(hardwareMap);
    }

    @Override
    public void loop() {
        color.getDetectedColor(telemetry);
    }
}
