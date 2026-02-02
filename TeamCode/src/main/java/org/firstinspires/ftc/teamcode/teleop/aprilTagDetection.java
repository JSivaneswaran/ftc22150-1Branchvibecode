package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "webcamTesting" , group = "TeleOp")
public class aprilTagDetection extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    double yaw = -1000;
    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagId(20);
        aprilTagWebcam.display(id20);

        if(id20 != null) {
            aprilTagWebcam.getAngle(id20);
        }
        telemetry.addData("yaw", yaw);

    }
}
