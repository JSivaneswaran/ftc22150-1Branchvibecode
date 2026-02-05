package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.intake;
import org.firstinspires.ftc.teamcode.subsystems.mecanumDrivee;

@TeleOp(name = "TestDrive", group = "Teleop")
public class mechanum extends OpMode {

    private final mecanumDrivee drive = new mecanumDrivee();
    private final intake inting = new intake();
    double forward, strafe, rotate;

    private boolean blue = true;
    @Override
    public void init() {

        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        if (Math.abs(forward) < 0.1) forward = 0;
        if (Math.abs(strafe) < 0.1) strafe = 0;
        if (Math.abs(rotate) < 0.1) rotate = 0;

        if(gamepad1.dpadUpWasPressed()){
            blue = !blue;
        }
        telemetry.addData("Alliance", blue);
        telemetry.addLine();
        drive.robotOrient(forward, strafe, rotate);
        //inting.powerSet(gamepad1.left_trigger);

    }
}
