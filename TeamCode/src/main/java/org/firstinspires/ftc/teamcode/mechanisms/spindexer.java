package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class spindexer {
    private DcMotor spindexerMotor;
    private final int[] tickCounts = {0,128,256,384,512};
    private int currentModule = 0;

    public void init(HardwareMap hardwareMap){
        spindexerMotor = hardwareMap.get(DcMotor.class, "mainIntake");
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void rotate(int numRotations){
        int newPosition = tickCounts[currentModule+numRotations];
        //leftover = getTickCount() + module * tickCount + leftover - newPosition;
        spindexerMotor.setTargetPosition(newPosition);
        spindexerMotor.setPower(0.2);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(spindexerMotor.isBusy()){
            //stall time
        }
        spindexerMotor.setPower(0);
        currentModule++;
    }

    public void resetRotation(){
        spindexerMotor.setTargetPosition(0);
        spindexerMotor.setPower(0.3);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(spindexerMotor.isBusy()){
            //stall time
        }

        if(spindexerMotor.getCurrentPosition() != 0){
            spindexerMotor.setTargetPosition(0);
            spindexerMotor.setPower(0.1);
            spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        while(spindexerMotor.isBusy()){
            //stall time
        }
        currentModule = 0;
        spindexerMotor.setPower(0);
    }
    public void resetEncoder(){
        currentModule = 0;
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getTickCount() {
        return spindexerMotor.getCurrentPosition();
    }
}