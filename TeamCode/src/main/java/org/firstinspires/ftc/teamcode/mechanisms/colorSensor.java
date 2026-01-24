package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class colorSensor {
    NormalizedColorSensor sensor;

    public enum DetectedColor{
        PURPLE,
        GREEN,
        UNKNOWN
    }

    public void init(HardwareMap hardwareMap, String device) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, device);
    }

    // Detects the color
    public DetectedColor getDetectedColor(Telemetry telemetry){

        NormalizedRGBA colors = sensor.getNormalizedColors();
        telemetry.addData("Light Detected", ((OpticalDistanceSensor) sensor).getLightDetected());
        telemetry.addData("red", colors.red/colors.alpha);
        telemetry.addData("blue", colors.blue/colors.alpha);
        telemetry.addData("green", colors.green/colors.alpha);

        // figures out the color
        return DetectedColor.UNKNOWN;
    }

    // an alogrithm to determine order of modules to shoot, 0 being next to intake and bellow shooter
    public static int numSpin(DetectedColor[] colors, DetectedColor[] goal){
        /**
         * Returns an array of size 3 that determines what modules should the bot rotate
         * in order to execute the command effeciently. It optimizes the path the bot should take.
         *
         * @param colors The current colors in each module from module 0,1,2 in that order
         * @param goal The order in which the balls should shoot
         * @return an array of int size 3 that determines order of shooting
         *
         * */
        int currentGreenIndex = -1;
        int greenIndex = 0;

        for(int i = 0; i < 3; i++){
           if (colors[i] == DetectedColor.GREEN){
               currentGreenIndex = i;
           }
           if(goal[i] == DetectedColor.GREEN){
               greenIndex = i;
           }
        }
        if(currentGreenIndex == -1){
            return 0;
        }
        return (greenIndex-currentGreenIndex + 3)%3;
    }
}
