package org.firstinspires.ftc.teamcode.trainee;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "AprilTagOpMode", group = "TeleOp")
public class AprilOpMode extends OpMode {

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private int greenIndexGoal = -1;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        AprilTagDetection id21 = aprilTagWebcam.getTagId(21);
        AprilTagDetection id22 = aprilTagWebcam.getTagId(22);
        AprilTagDetection id23 = aprilTagWebcam.getTagId(23);
        aprilTagWebcam.display(id21);
        aprilTagWebcam.display(id22);
        aprilTagWebcam.display(id23);

        if(id21 != null){
            greenIndexGoal = 0;
        }else if(id22 != null){
            greenIndexGoal = 1;
        }else if(id23 != null){
            greenIndexGoal = 2;
        }

        telemetry.addLine();
        telemetry.addData("green index", greenIndexGoal);
    }
}
