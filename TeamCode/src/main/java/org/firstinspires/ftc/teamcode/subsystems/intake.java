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
    }

    public void reverse(double trigger){
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setPower(trigger);
    }
    public void proper(double trigger){
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setPower(trigger);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

}
