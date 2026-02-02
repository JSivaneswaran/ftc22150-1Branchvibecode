package org.firstinspires.ftc.teamcode.practice;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class shooterTuning {

// ===== Gamepad state tracking =====


    private DcMotorEx shooter;
    private VoltageSensor batteryVoltageSensor;

    // ===== Constants =====
    private static final double TICKS_PER_REV = 28.0;

    // ===== Voltage–Omega Regression Constants ====
    private static final double a = 52.345451;
    private static final double c = 17.025876;

    // Small proportional correction
    //private static double kP = 0.0000008;

    // Test target (rad/s)
    private double omegaTarget = 343.0;

    public void init() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void example() {
        // === Battery Voltage ===
        double batteryVoltage = batteryVoltageSensor.getVoltage();

        // === Encoder → angular velocity ===
        double ticksPerSec = shooter.getVelocity();
        double omegaActual = ticksPerSec * (2.0 * Math.PI / TICKS_PER_REV);

        // === Voltage-aware Feedforward ===
        double ffPower = (omegaTarget - c) / (a * batteryVoltage);

        // === Proportional correction ===
        //double pPower = kP * error;

        // === Final command ===
        double power = ffPower; //+ pPower;
        power = Math.max(0.0, Math.min(power, 1.0));

        shooter.setPower(power);
    }
}
