package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


public class shooting {
    private DcMotorEx shooter;

    // ===== Physics Constants =====
    private static final double g = 9.81;                 // m/s^2
    private static final double FLYWHEEL_RADIUS = 0.0762; // m (3 in)

    private static final double SHOOTER_HEIGHT = 0.237;   // m
    private static final double GOAL_HEIGHT = 0.9845;     // m
    private static final double HEADSPACE = 0.127;        // m

    private static final double DELTA_Y =
            GOAL_HEIGHT - SHOOTER_HEIGHT + HEADSPACE;

    private static final double IDLE_OMEGA = 200.0;  // starts at 200 rad/sec
    private static final double MAX_OMEGA = 545.0;
    private Telemetry telemetry;

    private static final int TARGET_TAG_ID = 20;
    private static final double INCH_TO_METER = 0.0254;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        shooter = hardwareMap.get(DcMotorEx.class, "mainShooter");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.telemetry = telemetry;
    }

    // Call this every loop to get the desired flywheel omega
    public double getRequiredOmega(AprilTagDetection tag) {

        double rangeMeters = tag.ftcPose.range * INCH_TO_METER;
        double theta = Math.toRadians(tag.ftcPose.elevation);

        if (rangeMeters <= 0.0 || theta <= 0.0) {
            return IDLE_OMEGA;
        }

        // Projectile Physics
        double cosT = Math.cos(theta);
        double tanT = Math.tan(theta);

        double numerator = g * rangeMeters * rangeMeters; // gx^2
        double denominator =
                2.0 * cosT * cosT *
                        (rangeMeters * tanT - DELTA_Y); // ( 2 * cos^2(angle) * (x * tan(angle) - y) )

        if (denominator <= 0.0) {
            return IDLE_OMEGA;
        }

        double v = Math.sqrt(numerator / denominator); // m/s
        double omega = v / FLYWHEEL_RADIUS;             // rad/s

        omega = clamp(omega, IDLE_OMEGA, MAX_OMEGA); // forcing speed to be in between these 2 ranges
        return omega;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void setShooterSpeed(AprilTagDetection tag) {
        shooter.setVelocity(getRequiredOmega(tag));

        telemetry.addData("Target Omega (rad/s)", getRequiredOmega(tag));
        telemetry.addData("Actual Omega (rad/s)",
                shooter.getVelocity());
        telemetry.update();
    }
}