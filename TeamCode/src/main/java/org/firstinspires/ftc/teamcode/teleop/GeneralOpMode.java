package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.subsystems.colorSensor;
import org.firstinspires.ftc.teamcode.subsystems.intake;
import org.firstinspires.ftc.teamcode.subsystems.mecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.servo;
import org.firstinspires.ftc.teamcode.subsystems.simpleShooter;
import org.firstinspires.ftc.teamcode.subsystems.spindexer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Driver", group = "Teleop")
public class GeneralOpMode extends OpMode {
    private final mecanumDrive drive = new mecanumDrive();
    private final spindexer spin = new spindexer();
    private final intake mainIntake = new intake();
    private final servo servo = new servo();
    private final simpleShooter shooter = new simpleShooter();
    private final AprilTagWebcam webcam = new AprilTagWebcam();


    // ----------
    private final int[] aprilTagIDs = {21, 22, 23};
    private boolean aprilTagClosed = false;
    private double power = 0.3;
    private int currentPosition = 0;
    private boolean intake = true; // true = proper, false = reverse
    private boolean intakeMode = true; // reverse intak


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
    private final colorSensor.DetectedColor[] currentColor = {
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN
    };

    private int greenIndexGoal = -1;
    private boolean auton = true;

    @Override
    public void init() {
        servo.init(hardwareMap);
        drive.init(hardwareMap);
        spin.init(hardwareMap);
        mainIntake.init(hardwareMap);
        webcam.init(hardwareMap, telemetry);
        shooter.init(hardwareMap);

        webcam.setCameraOn();

        for (int j = 0; j < 3; j++){
            for(int i = 0; i < 2; i++) {
                module[j][i].init(hardwareMap, colorSensorModules[j][i], greenMax[j][i], purpleMin[j][i], blankDistance[j][i]);
            }
        }

    }

    @Override
    public void loop() {

        updateDrive();

        if (!aprilTagClosed) {
            updateAprilTag();
        }

        updateIntake();

        updateShooter();

        updateServo();

        spin.stopIfNeeded(currentPosition, power);

        if (auton && intakeMode) {
            updateSpindexerAutomation();
        }

        updateSpindexer();

        updateAllTelemetry();

        if(gamepad2.aWasPressed()){
            shooter.setPosition(1);
        }else if(gamepad2.xWasPressed()){
            shooter.setPosition(-1);
        }
    }

    public void updateDrive(){
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        if (Math.abs(forward) < 0.3) {
            forward = 0;
        }else if(forward >= 0.3){
            forward -=0.3;
        }else{
            forward += 0.3;
        }
        if (Math.abs(strafe) < 0.3){
            strafe = 0;
        } else if (strafe >= 0.3){
            strafe -=0.3;
        } else {
            strafe += 0.3;
        }
        if (Math.abs(rotate) < 0.3){
            rotate = 0;
        } else if (rotate >= 0.3){
            rotate -=0.3;
        } else{
            rotate += 0.3;
        }

        drive.fieldOrient(forward, strafe, rotate);

        if (gamepad1.left_bumper){
            drive.resetIMU();
        }
    }

    public void updateIntake() {
        if (gamepad1.right_bumper) {
            intake = !intake;
        }

        double power = gamepad1.left_trigger;
        if (power > 0) {
            if (intake) mainIntake.proper(power);
            else mainIntake.reverse(power);
        } else {
            mainIntake.stop();
        }
    }

    public void updateSpindexerAutomation() {
        updateColor();

        if (currentColor[0] != colorSensor.DetectedColor.UNKNOWN && spin.isAtTarget(currentPosition)) {

            if (currentColor[2] != colorSensor.DetectedColor.UNKNOWN) {
                intakeMode = false;
                int rotations = colorSensor.numSpin(currentColor, greenIndexGoal);
                currentPosition = spin.rotate(rotations, currentPosition, 0.3);
            } else {
                colorSensor.DetectedColor temp = currentColor[0];
                currentColor[0] = currentColor[2];
                currentColor[2] = currentColor[1];
                currentColor[1] = temp;

                currentPosition = spin.rotate(1, currentPosition, 0.3);
            }
        }
    }

    public void updateColor() { // update all 3 rather than 1
        for (int i = 0; i < 3; i++) {
            currentColor[i] = colorSensor.getDetectedColor(module[i][0], module[i][1]);
        }
    }

//    public void updateColor(){
//        currentColor[0] = colorSensor.getDetectedColor(module[0][0], module[0][1]);
//        if (gamepad1.leftStickButtonWasPressed()){
//            for(int i = 1; i <= 2; i++){
//                currentColor[i] = colorSensor.getDetectedColor(module[i][0], module[i][1]);
//            }
//        }
//    }

    public void updateShooter() {
        if (gamepad1.right_trigger > 0.1) {
            shooter.runShooter(gamepad1.right_trigger);

            if (spin.isAtTarget(currentPosition)) {
                currentPosition = spin.shoot(currentPosition, 0.2);
            }
        } else {
            shooter.runShooter(0);
        }

        if (gamepad1.x) {
            webcam.setCameraOn();
            aprilTagClosed = false;

            AprilTagDetection id20 = webcam.getTagId(20);
            if (id20 != null) {
                double x = webcam.getX(id20);
                double y = webcam.getY(id20);

                if (x + 10 > 5) {
                    drive.fieldOrient(0, 0, 0.2);
                } else if (x + 10 < -5) {
                    drive.fieldOrient(0, 0, -0.2);
                }
            }
        } else {
            webcam.setCameraOff();
            aprilTagClosed = true;
        }
    }

    public void updateSpindexer() {
        if (gamepad2.dpadLeftWasPressed()) {
            auton = !auton;
        }

        if (gamepad2.dpadRightWasPressed() || gamepad1.dpadRightWasPressed()) {
            currentPosition = spin.resetRotation(currentPosition, 0.2);
        }

        if (gamepad1.bWasPressed()) {
            currentPosition = spin.rotate(1, currentPosition, 0.2);
        }

        else if (gamepad1.yWasPressed()) {
            int rotations = colorSensor.numSpin(currentColor, greenIndexGoal);
            currentPosition = spin.rotate(rotations, currentPosition, 0.2);
        }

        else if (gamepad1.aWasPressed()) {
            currentPosition = spin.reset(0.5);
            intakeMode = true;
        }

        if (gamepad2.rightBumperWasPressed() || gamepad1.rightBumperWasPressed()) {
            currentPosition = spin.shoot(currentPosition, 0.2);
        }
    }

    public void updateAprilTag() {
        webcam.update();
        for (int i = 0; i < aprilTagIDs.length; i++){ // used the array length itself to get the greenIndex
            AprilTagDetection tag = webcam.getTagId(aprilTagIDs[i]);
            webcam.display(tag);
            if(tag != null){
                greenIndexGoal = i;
                webcam.setCameraOff();
                aprilTagClosed = true;
                break;
            }
        }
    }

    public void updateServo(){
        if (gamepad2.dpad_up) servo.changePosition(1);
        else if (gamepad2.dpad_down) servo.changePosition(-1);
    }

    public void updateAllTelemetry(){
        telemetry.addData("Spindexer Busy", spin.isAtTarget(currentPosition));
        telemetry.addData("Intake Mode", intakeMode);
        telemetry.addData("Green Index Goal", greenIndexGoal);
        telemetry.addData("Auton Mode", auton);

        telemetry.addLine("Current Colors:");
        for(int i = 0; i < currentColor.length; i++){
            telemetry.addData("Color " + i, currentColor[i]);
        }

        telemetry.addLine("Servo Positions:");
        telemetry.addData("Right Servo", servo.getRightPos());
        telemetry.addData("Left Servo", servo.getLeftPos());
    }

    @Override
    public void stop() {
        drive.stop();
        webcam.stop();
        spin.stop();
        mainIntake.stop();
        shooter.stop();
    }

}


/*
Gamepad control (just so I dont forget)

leftstick_y = forward
leftstick_x = strafe
rightstick_x = rotate
left_bumper = resetting imu
right_bumper = change intake direction
left_trigger = intake
right_trigger = shoot and rotate balls at the same time
 */