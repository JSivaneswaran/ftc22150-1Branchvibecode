package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class servo {

    private Servo leftServo;
    private Servo rightServo;
    double step = 0.02;
    double position = 0;
    double maxPos = 0.62;

    public void init(HardwareMap hardwareMap){
        leftServo = hardwareMap.get(Servo.class, "left_servo");
        rightServo = hardwareMap.get(Servo.class, "right_servo");

        leftServo.setPosition(position);
        rightServo.setPosition(maxPos - position ); //mechanical offset
    }

    public void changePosition(int i){

        position += step * i;

        position = Math.max(0.0, Math.min(maxPos, position));

        //only change position if it has changed (don't constantly run servos)
        if (leftServo.getPosition() != position) {
            leftServo.setPosition(position);
        }
        if (rightServo.getPosition() != position) {
            rightServo.setPosition(maxPos - position);
        }

    }
}
