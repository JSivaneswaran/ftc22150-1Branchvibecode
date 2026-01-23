package org.firstinspires.ftc.teamcode.trainee;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer;

@TeleOp(name = "Spindexer", group = "Teleop")
public class spindexerBeta extends OpMode {
    spindexer spin = new spindexer();


    @Override
    public void init() {
        spin.init(hardwareMap,telemetry);
    }
    @Override
    public void loop() {

        telemetry.addData("Tick", spin.getTickCount());
        if(gamepad1.right_bumper){
            spin.resetEncoder();
        }
        if(gamepad1.a){
            spin.rotate(0);
        }else if(gamepad1.b){
            spin.rotate(1);
        }else if(gamepad1.x){
            spin.rotate(2);
        }
    }
}
