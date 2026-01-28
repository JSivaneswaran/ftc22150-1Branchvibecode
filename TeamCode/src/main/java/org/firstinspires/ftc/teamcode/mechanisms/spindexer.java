package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class spindexer {
    private DcMotorEx spindexerMotor;
    private double one_rev = 384.5;

    public void init(HardwareMap hardwareMap){
        spindexerMotor = hardwareMap.get(DcMotorEx.class, "mainIntake");
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public int rotate(int numRotations, int currentPosition){
        int newPosition = (int) (numRotations * one_rev)/3 + currentPosition;
        spindexerMotor.setTargetPosition(newPosition);
        spindexerMotor.setTargetPositionTolerance(3);
        spindexerMotor.setPower(0.2);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(spindexerMotor.isBusy()){
            //stall time
        }
        spindexerMotor.setPower(0);

        return newPosition;
    }

    public void resetRotation(){
        spindexerMotor.setTargetPosition(0);
        spindexerMotor.setPower(0.3);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(spindexerMotor.isBusy()){
            //stall time
        }
        spindexerMotor.setPower(0);
    }

    public void shoot(){
        spindexerMotor.setTargetPosition(-128);
        spindexerMotor.setPower(0.2);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(spindexerMotor.isBusy()){
            //stall time
        }

        spindexerMotor.setPower(0);
    }
    public void resetEncoder(){
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getTickCount() {
        return spindexerMotor.getCurrentPosition();
    }
}