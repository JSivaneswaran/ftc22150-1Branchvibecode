package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class servo {

    private Servo leftServo;
    private Servo rightServo;
    double step = 0.02;
    double position = 1;
    public double maxPos = 0.64;
    double minPos = 0.4; // 1-0.6

    public void init(HardwareMap hardwareMap){
        leftServo = hardwareMap.get(Servo.class, "left_servo");
        rightServo = hardwareMap.get(Servo.class, "right_servo");

        rightServo.setDirection(Servo.Direction.REVERSE);

      //  leftServo.setPosition(1.0);
      //  rightServo.setPosition(1.0);//mechanical offset
    }

    public void changePosition(int i){

        position += step * i;

        if(i == -1){
            position = maxPos;
        }else if (i == 1){
            position = 1.0;
        }

        //position = Math.max(minPos, Math.min(maxPos, position));

        //only change position if it has changed (don't constantly run servos)
        if (leftServo.getPosition() != position || rightServo.getPosition() != position) {
            leftServo.setPosition(position);
            rightServo.setPosition(position);
        }

    }

    public double getRightPos(){
        return rightServo.getPosition();
    }
    public double getLeftPos(){
        return leftServo.getPosition();
    }

}