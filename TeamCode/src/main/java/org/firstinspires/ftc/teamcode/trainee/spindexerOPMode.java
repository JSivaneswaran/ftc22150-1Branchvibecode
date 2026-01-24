package org.firstinspires.ftc.teamcode.trainee;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.mechanisms.colorSensor;
import org.firstinspires.ftc.teamcode.mechanisms.intake;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer;

@TeleOp(name = "Spindexer", group = "Teleop")
public class spindexerOPMode extends OpMode {
    spindexer spin = new spindexer();
    intake mainIntake = new intake();

    colorSensor.DetectedColor[] current = {
        colorSensor.DetectedColor.PURPLE, colorSensor.DetectedColor.GREEN,
        colorSensor.DetectedColor.PURPLE
    };

    colorSensor.DetectedColor[] goal = {
            colorSensor.DetectedColor.GREEN,
            colorSensor.DetectedColor.PURPLE,
            colorSensor.DetectedColor.PURPLE
    };


    @Override
    public void init() {
        spin.init(hardwareMap);
        mainIntake.init(hardwareMap);
    }
    @Override
    public void loop() {

        telemetry.addData("Tick", spin.getTickCount());
        if(gamepad1.right_bumper){
            spin.resetEncoder();
        }
        if(gamepad1.aWasPressed()){
            telemetry.addData("Pressed a", null);
            spin.rotate(0);
        }else if(gamepad1.bWasPressed()){
            telemetry.addData("Pressed b", null);
            spin.rotate(1);
        }else if(gamepad1.xWasPressed()){
            telemetry.addData("Pressed x", null);
            spin.rotate(2);
        }
        if(gamepad1.leftBumperWasPressed()){
            spin.resetRotation();
        }
        if(gamepad1.y){
            spin.rotate(colorSensor.numSpin(current, goal));
        }

        telemetry.addData("Trigger Value", gamepad1.left_trigger);
        mainIntake.powerSet(gamepad1.left_trigger);
    }
}
