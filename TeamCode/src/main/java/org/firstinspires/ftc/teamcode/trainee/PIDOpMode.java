package org.firstinspires.ftc.teamcode.trainee;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer;
@TeleOp(name = "PID", group = "Teleop")
public class PIDOpMode extends OpMode {
    public DcMotorEx spindexerMotor;

    public double targetTick = 384.5;
    public final double oneRotation = 384.5;

    public double currentTick = 0.0;

    double F = 0.0;
    double P = 0.0;

    double[] stepSize = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};
    int stepCount = 1;

    @Override
    public void init() {
        spindexerMotor = hardwareMap.get(DcMotorEx.class, "mainIntake");
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {
        if(gamepad1.yWasPressed()){
            targetTick = targetTick + oneRotation;
            spindexerMotor.setTargetPosition((int)targetTick);
            spindexerMotor.setTargetPositionTolerance(3);
            spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spindexerMotor.setPower(0.2);
            while(spindexerMotor.isBusy()){

            }
            spindexerMotor.setPower(0);

            telemetry.addData("target tick", (int)targetTick);
            double currTick = spindexerMotor.getCurrentPosition();
            double err = (int)targetTick-currTick;
            telemetry.addData("current tick", (int)currTick);
            telemetry.addData("error", (int) err);
        }

        if(gamepad1.bWasPressed()){
            stepCount = (stepCount+1)%stepSize.length;
        }

        if(gamepad1.dpadLeftWasPressed()){
            P -= stepSize[stepCount];
        }
        if(gamepad1.dpadRightWasPressed()){
            P += stepSize[stepCount];
        }

    }
}
