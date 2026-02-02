package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {
    private DcMotorEx intakeMotor;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "mainOrbitor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public void powerSet(double triggerOutput) {
        //intakeMotor.setVelocity(1000);
        intakeMotor.setPower(1 * triggerOutput);
    }

    public void reverse(){
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void proper(){
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

}
