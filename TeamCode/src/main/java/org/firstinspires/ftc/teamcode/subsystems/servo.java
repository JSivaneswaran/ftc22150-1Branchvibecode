package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class servo {

    private Servo leftServo;
    private Servo rightServo;
    double step = 0.02;
    double positionLeft = 0;
    double positionRight = 0;
    double max = 0.5;
    double maxPosLeft = 0;
    double minPos = 0;
    double maxPosRight = 0;

    public void init(HardwareMap hardwareMap){
        leftServo = hardwareMap.get(Servo.class, "left_servo");
        rightServo = hardwareMap.get(Servo.class, "right_servo");

        rightServo.setDirection(Servo.Direction.REVERSE);
        //leftServo.setPosition(leftServo.getPosition());
        //rightServo.setPosition(rightServo.getPosition()); //mechanical offset

        positionLeft = leftServo.getPosition();
        positionRight = rightServo.getPosition();
        rightServo.setPosition(positionLeft);

        maxPosLeft = Math.max(max + positionLeft,1);
        minPos = positionLeft;
    }

    public void changePosition(int i){

        positionLeft += step * i;

        positionLeft = Math.max(minPos, Math.min(maxPosLeft, positionLeft));

        //only change position if it has changed (don't constantly run servos)
//        if (leftServo.getPosition() != positionLeft) {
            leftServo.setPosition(positionLeft);
            rightServo.setPosition(positionLeft);
//        }
//        if (rightServo.getPosition() != positionLeft) {
//            rightServo.setPosition(positionLeft);
//        }

    }
}
