package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.subsystems.colorSensor;
import org.firstinspires.ftc.teamcode.subsystems.intake;
import org.firstinspires.ftc.teamcode.subsystems.mecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.simpleShooter;
import org.firstinspires.ftc.teamcode.subsystems.spindexer;
import org.firstinspires.ftc.teamcode.subsystems.servo;
import org.firstinspires.ftc.teamcode.teleop.GeneralOpMode;

@Autonomous(name = "farAuto", group = "Autonomous")
public class farAuto extends OpMode {

    private AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private simpleShooter shooter = new simpleShooter();
    private spindexer spin = new spindexer();
    private intake mainIntake = new intake();
    private mecanumDrive drive = new mecanumDrive();
    private servo hood = new servo();

    private colorSensor.DetectedColor[] currentColor = new colorSensor.DetectedColor[3];

    private int greenIndexGoal = -1;
    private int currentPosition = 0;
    private boolean sorted = false;
    private boolean shotFirst = false;
    private boolean shotSecond = false;
    private boolean shotThird = false;
    private boolean autoMove = true;

    @Override
    public void init() {
        telemetry.update();

        shooter.init(hardwareMap);
        spin.init(hardwareMap);
        mainIntake.init(hardwareMap);
        hood.init((hardwareMap));
        aprilTagWebcam.init(hardwareMap, telemetry);
        drive.init(hardwareMap);

        for(int i = 0; i < 3; i++) {
            currentColor[i] = colorSensor.DetectedColor.UNKNOWN;
        }

        aprilTagWebcam.setCameraOn();
    }

    @Override
    public void loop() {

        if (hood.getLeftPos() != hood.maxPos){
            hood.changePosition(1);
        }

        if (autoMove) {
            for (int i = 0; i < 100; i++) {
                drive.setPower(0.5);
                drive.fieldOrient(0.2, 0.2, 0);
            }
            autoMove = !autoMove;
        }

        if (!autoMove) {
            drive.stop();
            drive.fieldOrient(0,0,0);
        }
        aprilTagWebcam.AutoAlign(true);

        /*

        if (greenIndexGoal == -1) {
            aprilTagWebcam.update();

            if (aprilTagWebcam.getTagId(21) != null) {
                greenIndexGoal = 0;
            } else if (aprilTagWebcam.getTagId(22) != null) {
                greenIndexGoal = 1;
            } else if (aprilTagWebcam.getTagId(23) != null) {
                greenIndexGoal = 2;
            }

            if (greenIndexGoal != -1) {
                telemetry.addData("Green Index", greenIndexGoal);
                aprilTagWebcam.setCameraOff();
            }
            telemetry.update();
            return;
        }



        // values needa be tuned green and purple to effectively detect those colors
        if (!sorted) {
            currentPosition = spin.rotate(greenIndexGoal, currentPosition, 0.3);
            sorted = true;
            telemetry.update();
            return;
        }



        try{
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("sleeping failed");
        }

        // Shoot all 3 balls one by one, delay required???
        if (!shotFirst) {
            shooter.runShooter(1.0); // max speed
            currentPosition = spin.shoot(currentPosition, 0.3);
            shotFirst = true;
            return;
        }

        if (!shotSecond) {
            currentPosition = spin.rotate(1, currentPosition, 0.3);
            currentPosition = spin.shoot(currentPosition, 0.3);
            shotSecond = true;
            return;
        }

        if (!shotThird) {
            currentPosition = spin.rotate(1, currentPosition, 0.3);
            currentPosition = spin.shoot(currentPosition, 0.3);
            shotThird = true;
            return;
        }

        shooter.runShooter(0);
        telemetry.update();

*/

    }
}
