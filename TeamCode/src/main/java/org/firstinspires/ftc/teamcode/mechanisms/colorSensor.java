package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import java.util.ArrayList;

public class colorSensor {
    NormalizedColorSensor colorSensor;

    public enum DetectedColor{
        PURPLE,
        GREEN,
        UNKNOWN
    }

    public void init(HardwareMap hardwareMap, String device){
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, device);
    }

    // Detects the color
    public DetectedColor getDetectedColor(){
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        double normRed, normGreen, normBlue;
        normRed = colors.red/colors.alpha;
        normGreen = colors.green/colors.alpha;
        normBlue = colors.blue/colors.alpha;

        // figures out the color

        return DetectedColor.UNKNOWN;
    }

    // an alogrithm to determine order of modules to shoot, 0 being next to intake and bellow shooter
    public int[] moduleAlgorithm(DetectedColor[] colors, DetectedColor[] goal){
        /**
         * Returns an array of size 3 that determines what modules should the bot rotate
         * in order to execute the command effeciently. It optimizes the path the bot should take.
         *
         * @param colors The current colors in each module from module 0,1,2 in that order
         * @param goal The order in which the balls should shoot
         * @return an array of int size 3 that determines order of shooting
         *
         * */
        // creates an array list to easliy search inside during the foor loop
        ArrayList<Integer> rotations = new ArrayList<>();

        // a last index to ensure we are always checking the current index at the shooter
        int lastindex = 1;

        // for loops to iterate between the goal and colors array
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) {
                // checkindex finds out what is the current index we just checked, and checks from
                // that index so the path is minimized
                int checkindex = (lastindex + j+2)%3;

                // if the the colors contain the goal color and
                // that index is not in rotations, then put that index into rotations
                if (colors[checkindex] == goal[i] && !rotations.contains(checkindex)) {
                    lastindex = checkindex;
                    rotations.add(checkindex);
                    break; //breaks to reduce time
                }
            }

            // if the rotations did not update in size, then we do not have the right balls so
            // so returns null
            if(rotations.size() != i + 1){
                return null;
            }
        }

        // a streamer that changes Integers to ints and then an array list into an array
        return rotations.stream().mapToInt(i -> i).toArray();
    }

    public int[] rotations(int[] modulePath){
        /**
         * Simple math to detemrine how many rotations is needed given a module path
         * that the shooter needs to take to shoot all balls in order
         * */
        int[] rotations = new int[3];

        rotations[0] = modulePath[0];
        rotations[1] = (modulePath[1] - modulePath[0] + 4)%3;
        rotations[2] = (modulePath[2] - modulePath[1] + 4)%3;

        return rotations;
    }
}
