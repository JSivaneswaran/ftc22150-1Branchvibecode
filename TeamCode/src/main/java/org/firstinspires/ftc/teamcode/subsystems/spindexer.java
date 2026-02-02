package org.firstinspires.ftc.teamcode.subsystems;

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
        spindexerMotor.setTargetPositionTolerance(1);
        spindexerMotor.setPower(0.2);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        return newPosition;
    }

    public int reset(){
        spindexerMotor.setTargetPosition(0);
        spindexerMotor.setPower(0.1);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return 0;
    }
    public int resetRotation(int currentPos){
        int tick = getTickCount()/128 * 128;
        int unstuckPos = 0;
        if(getTickCount() < currentPos){
            unstuckPos = tick;
        }else{
            unstuckPos = tick + 128;
        }
        spindexerMotor.setTargetPosition(unstuckPos);
        //spindexerMotor.setPower(0.1);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return unstuckPos;
    }

    public void stopMotorIfNeeded(int currentPosition){
        if(isItNotBusy(currentPosition)){
            spindexerMotor.setPower(0);
        }
    }

    public boolean isItNotBusy(int currentPosition){
        int currentTick = getTickCount();
        return currentPosition + 3 > currentTick && currentPosition - 3 < currentTick;
    }

    public int shoot(int currentPosition){
        int newPosition =  currentPosition-(int) (one_rev);
        spindexerMotor.setTargetPosition(newPosition);
        spindexerMotor.setTargetPositionTolerance(3);
        spindexerMotor.setPower(0.2);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        return newPosition;
    }
    public void resetEncoder(){
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getTickCount() {
        return spindexerMotor.getCurrentPosition();
    }

    public void setPosition(int speed) {
        spindexerMotor.setTargetPosition(speed);
    }
}