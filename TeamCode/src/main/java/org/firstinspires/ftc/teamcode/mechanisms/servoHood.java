package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Servo Testing")
public class servoHood extends LinearOpMode {
    private Servo leftServo;
    private Servo rightServo;
    double step = 0.02;
    double position = 0;
    double maxPos = 0.62;

    @Override
    public void runOpMode() {

        leftServo = hardwareMap.get(Servo.class, "left_servo");
        rightServo = hardwareMap.get(Servo.class, "right_servo");

        leftServo.setPosition(position);
        rightServo.setPosition(0.8 - position ); //mechanical offset

        waitForStart();

        while (opModeIsActive()) {

            boolean upPressed   = gamepad2.dpad_up;
            boolean downPressed = gamepad2.dpad_down;

            if (upPressed) {
                position += step;
            }
            if (downPressed) {
                position -= step;
            }

            // set max and mins
            //0.8 because at 1.0 it exceeds contact point
            position = Math.max(0.0, Math.min(maxPos, position));

            //only change position if it has changed (don't constantly run servos)
            if (leftServo.getPosition() != position) {
                leftServo.setPosition(position);
            }
           if (rightServo.getPosition() != position) {
             rightServo.setPosition(maxPos - position);
            }

           //print out positions for testing
            telemetry.addData("LPOS", leftServo.getPosition());
            telemetry.addData("RPOS",rightServo.getPosition());
            telemetry.update();

            //small delay, reduces chance of breaking components in testing
            sleep(10);
        }
    }
}