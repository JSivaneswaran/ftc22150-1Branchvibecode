package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.mechanisms.colorSensor;
import org.firstinspires.ftc.teamcode.mechanisms.intake;
import org.firstinspires.ftc.teamcode.mechanisms.mecanumDrivee;
import org.firstinspires.ftc.teamcode.mechanisms.simpleShooter;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Driver", group = "Teleop")
public class GeneralOpMode extends OpMode {
    private final spindexer spin = new spindexer();
    private boolean blue = false;
    private int currentPosition = 0;
    private final intake mainIntake = new intake();
    private boolean intakeReverse = false;
    private final simpleShooter shooter = new simpleShooter();
    private boolean intakeMode = true;

    private final mecanumDrivee drive = new mecanumDrivee();
    double forward, strafe, rotate;

    private final AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    private final colorSensor[][] module = {
            {new colorSensor(), new colorSensor()},
            {new colorSensor(), new colorSensor()},
            {new colorSensor(), new colorSensor()}
    };
    private final String[][] colorSensorModules = {
            {"mod0_cs1", "mod0_cs2"},
            {"mod1_cs1", "mod1_cs2"},
            {"cs1", "cs2"}
    };
    private final int[][] greenMax = {
            {162, 165},
            {160, 160},
            {170, 165}
    };
    private final int[][] purpleMin = {
            {195, 200},
            {210, 195},
            {220, 220}
    };

    private final double[][] blankDistance = {
            {0.06, 0.06},
            {0.09, 0.06},
            {0.04, 0.095}
    };
    private final colorSensor.DetectedColor[] current = {
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN
    };

    private int greenIndexGoal = -1;

    @Override
    public void init() {
        drive.init(hardwareMap);
        spin.init(hardwareMap);
        mainIntake.init(hardwareMap);
        aprilTagWebcam.init(hardwareMap, telemetry);
        shooter.init(hardwareMap);

        for(int j = 0; j < 3; j++){
            for(int i = 0; i < 2; i++) {
                module[j][i].init(hardwareMap, colorSensorModules[j][i], greenMax[j][i], purpleMin[j][i], blankDistance[j][i]);
            }
        }

    }
    @Override
    public void loop() {

        telemetry.addData("Tick", spin.getTickCount());
        telemetry.addData("Supposed to be Tick", currentPosition);
        telemetry.addData("Error", currentPosition-spin.getTickCount());

        updateDrive();

        updateAprilTag();

        updateIntake();

        updateColor();

        updateShooter();
        // updates spindexeder automation based on color
        updateSpindexerAutomation();

        // Methods to move spindexer
        updateSpindexer();
    }

    public void updateDrive(){
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

//        if (Math.abs(forward) < 0.1) forward = 0;
//        if (Math.abs(strafe) < 0.1) strafe = 0;
//        if (Math.abs(rotate) < 0.1) rotate = 0;

        if(gamepad1.dpadUpWasPressed()){
            blue = !blue;
        }
        telemetry.addData("Alliance", blue);
        telemetry.addLine();
        if(blue){
            drive.fieldOrient(forward, strafe, rotate);
        }else{
            drive.fieldOrient(-forward, -strafe, rotate);
        }

    }

    public void updateIntake(){
        mainIntake.powerSet(gamepad1.left_trigger);
        if(gamepad1.dpadDownWasPressed()){
            if(intakeReverse == false) {
                mainIntake.reverse();
                intakeReverse = true;
            }else{
                mainIntake.proper();
                intakeReverse = false;
            }
        }
    }

    public void updateSpindexerAutomation(){
//        telemetry.addData("intake mode", intakeMode);
//        if(intakeMode && current[0] != colorSensor.DetectedColor.UNKNOWN) {
//            int checkUknown = -1;
//            for(int i = 2; i>0; i--){
//                if(current[i] == colorSensor.DetectedColor.UNKNOWN){
//                    checkUknown = i;
//                    break;
//                }
//            }
//            if(checkUknown == 2) {
//                currentPosition = spin.rotate(1, currentPosition);
//            }else if(checkUknown == 1){
//                currentPosition = spin.rotate(2, currentPosition);
//            }else{
//                updateColor();
//                currentPosition = spin.rotate(colorSensor.numSpin(current, greenIndexGoal), currentPosition);
//                intakeMode = false;
//            }
//        }

        if(gamepad2.rightBumperWasPressed()){
            currentPosition = spin.rotate(1, currentPosition);
        }
    }
    public void updateColor(){
        for(int i = 0; i < 3; i++){
            current[i] = colorSensor.getDetectedColor(module[i][0], module[i][1], telemetry);
        }

        for(int i = 0; i < 3; i++){
            telemetry.addData("current color " + i, current[i]);
        }
    }

    public void updateShooter(){
        shooter.runShooter(gamepad1.right_trigger);
    }
    public void updateSpindexer(){
        //spinner testing code
        if(gamepad1.dpadRightWasPressed()){
            spin.resetEncoder();
        }

        if(gamepad1.aWasPressed()){
            currentPosition = spin.rotate(0, currentPosition);
        }else if(gamepad1.bWasPressed()){
            currentPosition = spin.rotate(1, currentPosition);
        }else if(gamepad1.xWasPressed()){
            currentPosition = spin.rotate(2, currentPosition);
        }

        if(gamepad1.rightBumperWasPressed()){
            spin.shoot();
            spin.resetRotation();
            currentPosition = 0;
            intakeMode = true;
        }
    }

    public void updateAprilTag(){
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
        telemetry.addData("green index:", greenIndexGoal);
    }
}
