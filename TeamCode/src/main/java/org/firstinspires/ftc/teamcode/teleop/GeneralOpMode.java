package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.subsystems.colorSensor;
import org.firstinspires.ftc.teamcode.subsystems.intake;
import org.firstinspires.ftc.teamcode.subsystems.mecanumDrivee;
import org.firstinspires.ftc.teamcode.subsystems.servo;
import org.firstinspires.ftc.teamcode.subsystems.simpleShooter;
import org.firstinspires.ftc.teamcode.subsystems.spindexer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Driver", group = "Teleop")
public class GeneralOpMode extends OpMode {
    private final spindexer spin = new spindexer();
    private final servo all_servo = new servo();
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
    private final colorSensor.DetectedColor[] currentColor = {
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN,
            colorSensor.DetectedColor.UNKNOWN
    };

    private int greenIndexGoal = -1;
    private boolean auton = true;

    @Override
    public void init() {
        all_servo.init(hardwareMap);
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

        updateDrive();

        if(greenIndexGoal != -1) {updateAprilTag();}

        updateIntake();

        updateShooter();

        updateServo();

        spin.stopMotorIfNeeded(currentPosition);

        if(auton && intakeMode) {
            updateSpindexerAutomation();
        }

        // Methods to move spindexer
        updateSpindexer();

        updateAllTelemetry();
    }

    public void updateDrive(){
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        if (Math.abs(forward) < 0.3) {
            forward = 0;
        }else if(forward >= 0.3){
            forward -=0.3;
        }else{
            forward += 0.3;
        }
        if (Math.abs(strafe) < 0.3){
            strafe = 0;
        } else if(strafe >= 0.3){
            strafe -=0.3;
        }else{
            strafe += 0.3;
        }
        if (Math.abs(rotate) < 0.3){
            rotate = 0;
        }else if(rotate >= 0.3){
            rotate -=0.3;
        }else{
            rotate += 0.3;
        }

        drive.fieldOrient(forward, strafe, rotate);

        telemetry.addData("IMU YAW", drive.getYaw());
        if(gamepad1.left_bumper){
            drive.resetIMU();
        }
    }

    public void updateIntake(){
        mainIntake.powerSet(gamepad1.left_trigger);
        if(gamepad1.dpadDownWasPressed()){
            if(!intakeReverse) {
                mainIntake.reverse();
                intakeReverse = true;
            }else{
                mainIntake.proper();
                intakeReverse = false;
            }
        }
    }

    public void updateSpindexerAutomation(){
        updateColor();
        if(intakeMode && currentColor[0] != colorSensor.DetectedColor.UNKNOWN && spin.isItNotBusy(currentPosition)) {
            colorSensor.DetectedColor currcolor = currentColor[0];
            currentColor[0] = currentColor[2];
            currentColor[2] = currentColor[1];
            currentColor[1] = currcolor;
            currentPosition = spin.rotate(1, currentPosition);
            if(currentColor[0] != colorSensor.DetectedColor.UNKNOWN){
                intakeMode = false;
                currentPosition = spin.rotate(colorSensor.numSpin(currentColor, greenIndexGoal), currentPosition);
            }

        }
    }
    public void updateColor(){
        currentColor[0] = colorSensor.getDetectedColor(module[0][0], module[0][1]);
    }

    public void updateShooter(){
        shooter.runShooter(gamepad1.right_trigger);
        if(gamepad1.xWasPressed()) {
            AprilTagDetection id20 = aprilTagWebcam.getTagId(20);
            if(id20 != null) {
                double yaw = aprilTagWebcam.getAngle(id20);
                double bearing = aprilTagWebcam.getBearing(id20);
                double angleChange = 0;
                if (bearing - yaw < 15 && bearing-yaw > -15){
                    angleChange = -yaw;
                }else{
                    if(bearing < -15){
                        angleChange = -15 - bearing;
                    }else if(bearing > 15){
                        angleChange = 15 - bearing;
                    }
                }
                drive.fieldOrient(0, 0, angleChange);
            }
        }
    }
    public void updateSpindexer(){
        if(gamepad2.dpadLeftWasPressed()){
            auton = !auton;
        }
        //spinner testing code
        if(gamepad2.dpadRightWasPressed()||gamepad1.dpadRightWasPressed()){
            currentPosition = spin.resetRotation(currentPosition);
        }

        if(gamepad1.bWasPressed()){
            currentPosition = spin.rotate(1, currentPosition);
        }else if(gamepad1.yWasPressed()){
            // alligns spindexer
            currentPosition = spin.rotate(colorSensor.numSpin(currentColor, greenIndexGoal), currentPosition);
        }else if(gamepad1.aWasPressed()){
            currentPosition = spin.reset();
            intakeMode = true;
        }

        if(gamepad2.rightBumperWasPressed() || gamepad1.rightBumperWasPressed()){
            currentPosition = spin.shoot(currentPosition);
        }
    }

    public void updateAprilTag(){
        aprilTagWebcam.update();
        AprilTagDetection id21 = aprilTagWebcam.getTagId(21);
        AprilTagDetection id22 = aprilTagWebcam.getTagId(22);
        AprilTagDetection id23 = aprilTagWebcam.getTagId(23);

        if(id21 != null){
            greenIndexGoal = 0;
        }else if(id22 != null){
            greenIndexGoal = 1;
        }else if(id23 != null){
            greenIndexGoal = 2;
        }
    }

    public void updateServo(){
        boolean upPressed   = gamepad2.dpad_up;
        boolean downPressed = gamepad2.dpad_down;

        if (upPressed) {
            all_servo.changePosition(1);
        }else if (downPressed) {
            all_servo.changePosition(-1);
        }

    }



    public void updateAllTelemetry(){
        telemetry.addLine("INTAKE");
        telemetry.addData("intake mode", intakeMode);
        telemetry.addLine("--------------------------------------------------------");
        telemetry.addLine("CURRENT COLORS");
        for(int i = 0; i < 3; i++){
            telemetry.addData("current color " + i, currentColor[i]);
        }
        telemetry.addLine("--------------------------------------------------------");
        telemetry.addLine("GREEN INDEX FOR POSITIONING");
        telemetry.addData("green index", greenIndexGoal);
        telemetry.addLine("--------------------------------------------------------");
        telemetry.addLine("SPINDEXER AUTON MODE");
        telemetry.addData("Auton Mode", auton);

        telemetry.addLine("--------------------------------------------------------");
        telemetry.addLine("SERVO POSITIONS");

        telemetry.addData("RS ppos", all_servo.getRightPos());
        telemetry.addData("LS ppos",all_servo.getLeftPos());
    }
}
