package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
public class mecanumDrive {

    private DcMotor frontRightMotor, backRightMotor, frontLeftMotor, backLeftMotor;
    private IMU imu;
    private double maxSpeed = 1.0;

    public void init(HardwareMap hardwareMap) {

        // motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft"); // port 1
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight"); // 2
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft"); // 3
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight"); // 4

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        imu.initialize(new IMU.Parameters(revOrientation));
    }
    public void fieldOrient(double forward, double strafe, double rotate) {
        double heading = imu.getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.RADIANS);

        double newStrafe = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double newForward = strafe * Math.sin(-heading) + forward * Math.cos(-heading);

        double frontLeftPower  = newForward + newStrafe + rotate;
        double frontRightPower = newForward - newStrafe - rotate;
        double backLeftPower   = newForward - newStrafe + rotate;
        double backRightPower  = newForward + newStrafe - rotate;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))
        );

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        frontLeftMotor.setPower(maxSpeed * frontLeftPower);
        frontRightMotor.setPower(maxSpeed * frontRightPower);
        backLeftMotor.setPower(maxSpeed * backLeftPower);
        backRightMotor.setPower(maxSpeed * backRightPower);
    }

    public void resetIMU() {
        imu.resetYaw();
    }

    public void setPower(double power) {
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
    }

    public void stop() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }

}
