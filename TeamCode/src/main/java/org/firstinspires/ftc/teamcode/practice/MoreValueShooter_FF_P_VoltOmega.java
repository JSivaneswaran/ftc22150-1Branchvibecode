package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "NEWShooter_FF_and_P_VoltageOmega", group = "TeleOp")
public class MoreValueShooter_FF_P_VoltOmega extends OpMode {

//    // ===== Gamepad state tracking =====
//    private boolean prevY = false;
//    private boolean prevA = false;
//    private boolean prevB = false;
//    private boolean prevX = false;

    private DcMotorEx shooter;
    private VoltageSensor batteryVoltageSensor;

    // ===== Constants =====
    private static final double TICKS_PER_REV = 28.0;

    // ===== Voltage–Omega Regression Constants =====
    // ω = a * (power * V) + c
    private static final double a = 52.345451;
    private static final double c = 17.025876;

    // Small proportional correction
    //private static double kP = 0.0000008;

    // Test target (rad/s)
    private double omegaTarget = 343.0;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {

//        // ===== Gamepad edge detection =====
//
//// Y: increase omegaTarget by 10
//        if (gamepad1.y && !prevY) {
//            omegaTarget += 10.0;
//        }
//        prevY = gamepad1.y;
//
//// A: decrease omegaTarget by 10
//        if (gamepad1.a && !prevA) {
//            omegaTarget -= 10.0;
//        }
//        prevA = gamepad1.a;
//
//// B: increase kP by 0.001 (1 is WAY too big — explanation below)
//        if (gamepad1.b && !prevB) {
//            kP += 0.001;
//        }
//        prevB = gamepad1.b;
//
//// X: decrease kP by 0.001
//        if (gamepad1.x && !prevX) {
//            kP -= 0.001;
//        }
//        prevX = gamepad1.x;


        // === Battery Voltage ===
        double batteryVoltage = batteryVoltageSensor.getVoltage();

        // === Encoder → angular velocity ===
        double ticksPerSec = shooter.getVelocity();
        double omegaActual = ticksPerSec * (2.0 * Math.PI / TICKS_PER_REV);

        // === Voltage-aware Feedforward ===
        double ffPower = (omegaTarget - c) / (a * batteryVoltage);

        // === Proportional correction ===
        double error = omegaTarget - omegaActual;
        //double pPower = kP * error;

        // === Final command ===
        double power = ffPower; //+ pPower;
        power = Math.max(0.0, Math.min(power, 1.0));

        shooter.setPower(power);

        // === Telemetry ===
        telemetry.addData("Battery Voltage (V)", batteryVoltage);
        telemetry.addData("Target ω (rad/s)", omegaTarget);
        telemetry.addData("Actual ω (rad/s)", omegaActual);
        telemetry.addData("Error (rad/s)", error);
       // telemetry.addData("kP", kP);
        telemetry.addData("FF Power (Voltage)", ffPower);
       // telemetry.addData("P Power", pPower);
        telemetry.addData("Final Power", power);
        telemetry.update();
    }
}
