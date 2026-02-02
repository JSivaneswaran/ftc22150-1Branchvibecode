package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
public class mecanumDrivee {
    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    private IMU imu;
    double maxSpeed = 0.5; // adjustable speed for training

    public void init(HardwareMap hardwareMap) {
        // motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft"); // port
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight"); // port
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft"); // port
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight"); // port

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        // ran based on velocity
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // cuts power when not in use so wheels kinda rotate for a bit (good for drifting)
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        imu.initialize(new IMU.Parameters(revOrientation));
    }
    public void fieldOrient(double forward, double strafe, double rotate) {
//        double theta = Math.atan2(forward, strafe); // originally forward, strafe
//        double r = Math.hypot(forward, strafe);

//        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
//
//        double newForward = r * Math.cos(theta); //blue its negative
//        double newStrafe = r * Math.sin(theta); //blue its negative

        double theta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double newStrafe = strafe * Math.cos(-theta) - forward * Math.sin(-theta);
        double newForward = strafe * Math.sin(-theta) + forward * Math.cos(-theta);

        double frontLeftPower = newForward + newStrafe + rotate;
        double frontRightPower = newForward - newStrafe - rotate;
        double backLeftPower = newForward - newStrafe + rotate;
        double backRightPower = newForward + newStrafe - rotate;

        // power normalization (to prevent values going beyond 1.0)
        double max = 1;
        max = Math.max(max, Math.abs(frontLeftPower) * maxSpeed);
        max = Math.max(max, Math.abs(frontRightPower) * maxSpeed);
        max = Math.max(max, Math.abs(backLeftPower) * maxSpeed);
        max = Math.max(max, Math.abs(backRightPower) * maxSpeed);

        frontLeftMotor.setPower((frontLeftPower/max));
        frontRightMotor.setPower((frontRightPower/max));
        backLeftMotor.setPower((backLeftPower/max) * 2.25);
        backRightMotor.setPower((backRightPower/max));
    }
}
