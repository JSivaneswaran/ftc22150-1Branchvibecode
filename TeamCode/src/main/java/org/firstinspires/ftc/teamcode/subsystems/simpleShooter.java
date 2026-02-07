package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class simpleShooter {
    private DcMotorEx mainShooter;

    private final double F = 5.0;
    private final double P = 10.1;
    private final double MAX_VELOCITY = 4500; // assuming ticks per second but doesnt make sense
    private boolean running = false;

    public void init(HardwareMap hardwareMap) {
        mainShooter = hardwareMap.get(DcMotorEx.class, "mainShooter");
        mainShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        mainShooter.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        mainShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidf);
    }

    public void runShooter(double vel) {
        if (vel < 0.1) {
            if (running) {
                mainShooter.setVelocity(0);
                running = false;
            }
        } else {
            mainShooter.setVelocity(MAX_VELOCITY * vel);
            running = true;
        }
    }

    public void setPosition(int targetPosition) {
        mainShooter.setTargetPosition(targetPosition);
    }

    public void stop() {
        mainShooter.setPower(0);
    }
}
