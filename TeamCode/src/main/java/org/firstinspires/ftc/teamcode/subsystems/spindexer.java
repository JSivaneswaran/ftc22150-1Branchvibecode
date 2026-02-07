package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class spindexer {

    private DcMotorEx spindexerMotor;
    private final double ONE_REV_TICKS = 384.5;
    private final int rotateOne = 128;

    public void init(HardwareMap hardwareMap) {
        spindexerMotor = hardwareMap.get(DcMotorEx.class, "mainIntake");
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public int setTarget(int targetPosition, double power, int tolerance) {
        spindexerMotor.setTargetPosition(targetPosition);
        spindexerMotor.setTargetPositionTolerance(tolerance);
        spindexerMotor.setPower(power);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return targetPosition;
    }

    public int rotate(int numRotations, int currentPosition, double power) {
        int newPosition = currentPosition + (int)((numRotations * ONE_REV_TICKS) / 3);
        return setTarget(newPosition, power, 1);
    }

    public int shoot(int currentPosition, double power) {
        int newPosition = currentPosition - (int)((ONE_REV_TICKS) / 3);
        return setTarget(newPosition, power, 1);
    }
    public int reset(double power) {
        return setTarget(0, power, 1);
    }

    public int resetRotation(int currentPosition, double power) {
        int tick = getTickCount() / rotateOne * rotateOne;
        int target = (getTickCount() < currentPosition) ? tick : tick + rotateOne;
        return setTarget(target, power, 1);
    }

    public void stopIfNeeded(int targetPosition, double power) {
        spindexerMotor.setPower(isAtTarget(targetPosition) ? 0 : power);
    }

    public boolean isAtTarget(int targetPosition) { // isBusy
        return Math.abs(getTickCount() - targetPosition) <= 3;
    }

    public void resetEncoder() {
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int getTickCount() {
        return spindexerMotor.getCurrentPosition();
    }

    public void stop() {
        spindexerMotor.setPower(0);
    }
}
