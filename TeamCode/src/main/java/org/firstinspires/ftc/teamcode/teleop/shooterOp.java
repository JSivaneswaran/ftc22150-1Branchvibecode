package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.practice.shooting;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "ShootingPractice", group = "Teleop")
public class shooterOp extends OpMode {

    //private final simpleShooter shooter = new simpleShooter();
    private final shooting shootingLogic = new shooting();
    private AprilTagWebcam aprilTagWebcam  = new AprilTagWebcam();

    @Override
    public void init() {
        //shooter.init(hardwareMap);
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagId(20);
        AprilTagDetection id24 = aprilTagWebcam.getTagId(24);
        aprilTagWebcam.display(id20);
        aprilTagWebcam.display(id24);

        if(id20 != null && gamepad1.aWasPressed()){
            shootingLogic.setShooterSpeed(id20);
            aprilTagWebcam.stop();
        }else if(id24 != null && gamepad1.aWasPressed()){
            shootingLogic.setShooterSpeed(id24);
            aprilTagWebcam.stop();
        }

    }
}

