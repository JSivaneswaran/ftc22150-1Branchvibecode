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
    private boolean aprilTagClosed = false;
    private double power = 0.3;
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

        aprilTagWebcam.setCameraOn();

        for(int j = 0; j < 3; j++){
            for(int i = 0; i < 2; i++) {
                module[j][i].init(hardwareMap, colorSensorModules[j][i], greenMax[j][i], purpleMin[j][i], blankDistance[j][i]);
            }
        }

    }
    @Override
    public void loop() {

        updateDrive();

        if(!aprilTagClosed) {updateAprilTag();}

        updateIntake();

        updateShooter();

        updateServo();

        spin.stopMotorIfNeeded(currentPosition, power);

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
        if(currentColor[0] != colorSensor.DetectedColor.UNKNOWN && spin.isItNotBusy(currentPosition)) {
            if(currentColor[2] != colorSensor.DetectedColor.UNKNOWN){
                intakeMode = false;
                currentPosition = spin.rotate(colorSensor.numSpin(currentColor, greenIndexGoal), currentPosition);
            }else{
                colorSensor.DetectedColor currcolor = currentColor[0];
                currentColor[0] = currentColor[2];
                currentColor[2] = currentColor[1];
                currentColor[1] = currcolor;
                currentPosition = spin.rotate(1, currentPosition);
            }
            power = 0.3;
        }
    }
    public void updateColor(){
        currentColor[0] = colorSensor.getDetectedColor(module[0][0], module[0][1]);

        if(gamepad1.leftStickButtonWasPressed()){
            currentColor[1] = colorSensor.getDetectedColor(module[1][0], module[1][1]);
            currentColor[2] = colorSensor.getDetectedColor(module[2][0], module[2][1]);
        }
    }

    public void updateShooter(){
        shooter.runShooter(gamepad1.right_trigger);
        if(gamepad1.x) {
            aprilTagWebcam.setCameraOn();
            aprilTagClosed = false;
            AprilTagDetection id20 = aprilTagWebcam.getTagId(20);
            if(id20 != null) {
                double x = aprilTagWebcam.getX(id20);
                double y  = aprilTagWebcam.getY(id20);

                if(x + 10 > 5 ){
                    drive.fieldOrient(0, 0, 0.2);
                }else if(x + 10 < -5){
                    drive.fieldOrient(0,0, -0.2);
                }
            }
        }else{
            aprilTagWebcam.setCameraOff();
            aprilTagClosed = true;
        }
    }
    public void updateSpindexer(){
        if(gamepad2.dpadLeftWasPressed()){
            auton = !auton;
        }
        //spinner testing code
        if(gamepad2.dpadRightWasPressed()||gamepad1.dpadRightWasPressed()){
            currentPosition = spin.resetRotation(currentPosition);
            power = 0.2;
        }

        if(gamepad1.bWasPressed()){
            currentPosition = spin.rotate(1, currentPosition);
            power = 0.2;
        }else if(gamepad1.yWasPressed()){
            // alligns spindexer
            currentPosition = spin.rotate(colorSensor.numSpin(currentColor, greenIndexGoal), currentPosition);
            power = 0.2;
        }else if(gamepad1.aWasPressed()){
            currentPosition = spin.reset();
            power = 0.5;
            intakeMode = true;
        }

        if(gamepad2.rightBumperWasPressed() || gamepad1.rightBumperWasPressed()){
            currentPosition = spin.shoot(currentPosition);
            power = 0.2;
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
            aprilTagWebcam.setCameraOff();
            aprilTagClosed = true;
        }else if(id22 != null){
            greenIndexGoal = 1;
            aprilTagWebcam.setCameraOff();
            aprilTagClosed = true;
        }else if(id23 != null){
            greenIndexGoal = 2;
            aprilTagWebcam.setCameraOff();
            aprilTagClosed = true;
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
        telemetry.addData("is it busy", spin.isItNotBusy(currentPosition));
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
    }
}
