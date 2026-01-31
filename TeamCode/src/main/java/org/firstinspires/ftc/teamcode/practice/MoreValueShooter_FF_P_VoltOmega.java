package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.mechanisms.shooterTuning;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "NEWShooter_FF_and_P_VoltageOmega", group = "TeleOp")
public class MoreValueShooter_FF_P_VoltOmega extends OpMode {

    shooterTuning shooter = new shooterTuning();

    @Override
    public void init() {
        shooter.init();
    }

    @Override
    public void loop() {

        shooter.example();
    }
}
