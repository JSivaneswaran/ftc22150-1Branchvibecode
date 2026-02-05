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
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        IMU.Parameters par = new IMU.Parameters(revOrientation);
        imu.initialize(par);
        resetIMU();
    }

    public void resetIMU(){
        imu.resetYaw();
    }
    public double getYaw(){
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public void robotOrient(double forward, double strafe, double rotate){

        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backLeftPower = forward - strafe + rotate;
        double backRightPower = forward + strafe - rotate;

        double max = 1.0;
        max = Math.max(max, Math.abs(frontLeftPower) * maxSpeed);
        max = Math.max(max, Math.abs(frontRightPower) * maxSpeed);
        max = Math.max(max, Math.abs(backLeftPower) * maxSpeed);
        max = Math.max(max, Math.abs(backRightPower) * maxSpeed);

        //double max = Math.max(Math.abs(newForward) + Math.abs(newStrafe) + Math.abs(rotate), 1.0);

        frontLeftMotor.setPower((frontLeftPower/max));
        frontRightMotor.setPower((frontRightPower /max));
        backLeftMotor.setPower((backLeftPower/max)); //this one
        backRightMotor.setPower((backRightPower/max));
    }
    public void fieldOrient(double forward, double strafe, double rotate) {
//        double theta = Math.atan2(forward, strafe); // originally forward, strafe
//        double r = Math.hypot(strafe, forward);
//
//        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
//
//        double newForward = r * Math.sin(theta); //blue its negative
//        double newStrafe = r * Math.cos(theta); //blue its negative

        double theta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double newStrafe = strafe * Math.cos(-theta) - forward * Math.sin(-theta);
        double newForward = strafe * Math.sin(-theta) + forward * Math.cos(-theta);

//        newForward *= 17.5/16.5;

        robotOrient(newForward, newStrafe, rotate);
    }
}
