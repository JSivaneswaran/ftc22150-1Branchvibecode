package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class simpleShooter {
    private DcMotorEx mainShooter;

    double F = 5.0;
    double P = 10.1;

    public void init(HardwareMap hardwareMap){
        mainShooter = hardwareMap.get(DcMotorEx.class, "mainShooter");
        mainShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        mainShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        mainShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
    }


    public void runShooter(double velocity){

            mainShooter.setVelocity(velocity);
    }

    public void setPosition(int speed) {
        mainShooter.setTargetPosition(speed);
    }

    public void stop() {
        mainShooter.setPower(0);
    }
}
