package org.firstinspires.ftc.teamcode.mechanisms;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class simpleShooter {
    private DcMotorEx mainShooter;

    public void init(HardwareMap hardwareMap){
        mainShooter = hardwareMap.get(DcMotorEx.class, "mainShooter");
        mainShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        mainShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void runShooter(double vel){
        if(vel > 0.1){
            mainShooter.setPower(vel);
        }

        else{
            mainShooter.setPower(0.05);
        }
    }

    public void setPosition(int speed) {
        mainShooter.setTargetPosition(speed);
    }
}
