package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    public NormalizedColorSensor sensor;

    public enum DetectedColor{
        PURPLE,
        GREEN,
        UNKNOWN
    }

    private int maxGreen;
    private int minPurple;
    private double blank_dist;

    public void init(HardwareMap hardwareMap, String device, int maxGreen, int minPurple, double blank_dist) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, device);
        if (sensor instanceof SwitchableLight) {
            ((SwitchableLight) sensor).enableLight(false);
        }
        sensor.setGain(30);

        this.maxGreen = maxGreen;
        this.minPurple = minPurple;
        this.blank_dist = blank_dist;
    }

    // Detects the color
    public static DetectedColor getDetectedColor(colorSensor cs1, colorSensor cs2, Telemetry telemetry){

        NormalizedRGBA colors = cs1.sensor.getNormalizedColors();
        NormalizedRGBA colors2 = cs2.sensor.getNormalizedColors();

        float[] hsvValues1 = new float[3];
        float[] hsvValues2 = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues1);
        Color.colorToHSV(colors2.toColor(), hsvValues2);

        double distance = Math.min(((OpticalDistanceSensor)cs1.sensor).getLightDetected(),
                ((OpticalDistanceSensor) cs2.sensor).getLightDetected());

        DetectedColor color = DetectedColor.UNKNOWN;
        if(distance < Math.max(cs1.blank_dist, cs2.blank_dist)){
            return color;
        }
        // telemetry.addData("Overall Distance", distance);
        if(hsvValues1[0] > cs1.minPurple || hsvValues2[0] > cs2.minPurple){
            color = DetectedColor.PURPLE;
        } else if (hsvValues1[0] < cs1.maxGreen || hsvValues2[0] < cs2.maxGreen) {
            color = DetectedColor.GREEN;
        }

        // figures out the color bv
        return color;
    }

    // an alogrithm to determine order of modules to shoot, 0 being next to intake and bellow shooter
    public static int numSpin(DetectedColor[] colors, int greenIndex){
        /**
         * Returns an array of size 3 that determines what modules should the bot rotate
         * in order to execute the command effeciently. It optimizes the path the bot should take.
         *
         * @param colors The current colors in each module from module 0,1,2 in that order
         * @param greenIndex The index that the green ball should be in
         * @return an array of int size 3 that determines order of shooting
         *
         * */
        int currentGreenIndex = -1;
        int unknownIndex = -1;

        for(int i = 0; i < 3; i++){
           if (colors[i] == DetectedColor.GREEN){
               currentGreenIndex = i;
           }else if(colors[i] == DetectedColor.UNKNOWN){
               unknownIndex = i;
           }
        }

        if(currentGreenIndex == -1 && unknownIndex != -1){
            currentGreenIndex = unknownIndex;
        }else{
            return 0;
        }

        return (greenIndex-currentGreenIndex + 3)%3;
    }
}
