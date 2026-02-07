package org.firstinspires.ftc.teamcode.subsystems;

import android.annotation.SuppressLint;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagWebcam {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    private List<AprilTagDetection> tagsDetected = new ArrayList<>();

    private Telemetry telemetry;

    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES).build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
        visionPortal.setProcessorEnabled(aprilTagProcessor, true);
    }

    public void setCameraOn(){
        visionPortal.setProcessorEnabled(aprilTagProcessor, true);
    }

    public void setCameraOff(){
        visionPortal.setProcessorEnabled(aprilTagProcessor, false);
    }
    public void update(){
        tagsDetected = aprilTagProcessor.getDetections();
    }

    public AprilTagDetection getTagId(int id){
        for(AprilTagDetection i : tagsDetected){
            if(i.id == id){
                return i;
            }
        }

        return null;
    }

    public void stop(){
        if(visionPortal != null){
            visionPortal.close();
        }
    }

    public double[] AutoAlign(boolean blueSide){

            AprilTagDetection id20 = getTagId(20);
            AprilTagDetection id24 = getTagId(24);

            double rotate = 0.0;
            double velocity, servo;

            double x,y;
            if (id20 != null && blueSide ) {
                x = getX(id20);
                y = getY(id20);}
            else if(id24 != null && !blueSide){
                x = getX(id24);
                y = getY(id24);
            }

            if (x + 10 > 5) {
                rotate = 0.5;
            } else if (x + 10 < -5) {
                rotate = -0.5;
            }
            if (y > 200){
                velocity = 4500;
                servo = 1;
            }else if(y > 150){
                velocity = 2500;
                servo = -1;
            }else{
                velocity = 1800;
                servo = -1;
            }

            double[] returns = {rotate, velocity, servo};
            return returns;
    }
    public double getAngle(AprilTagDetection detectedId){
        if(detectedId == null){
            return 0.0;
        }

        return detectedId.ftcPose.yaw;
    }

    public double getX(AprilTagDetection detectedId){
        if(detectedId == null){
            return 0.0;
        }

        return detectedId.ftcPose.x;
    }
    public double getY(AprilTagDetection detectionId){
        if(detectionId == null){
            return 0;
        }

        return detectionId.ftcPose.y;
    }

    public List<AprilTagDetection> getTagsDetected() {
        return tagsDetected;
    }

    @SuppressLint("DefaultLocale")
    public void display(AprilTagDetection detectedId){
        if(detectedId == null){
            return;
        }

        if (detectedId.metadata != null) {
            telemetry.addLine(String.format("\n==== (ID %d) %s", detectedId.id, detectedId.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (cm)", detectedId.ftcPose.x, detectedId.ftcPose.y, detectedId.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectedId.ftcPose.pitch, detectedId.ftcPose.roll, detectedId.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (cm, deg, deg)", detectedId.ftcPose.range, detectedId.ftcPose.bearing, detectedId.ftcPose.elevation));
        } else {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectedId.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectedId.center.x, detectedId.center.y));
            }
    }
}
