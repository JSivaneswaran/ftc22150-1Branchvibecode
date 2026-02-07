package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.SwitchableLight;

public class colorSensor {

    public NormalizedColorSensor sensor;

    public enum DetectedColor {
        PURPLE,
        GREEN,
        UNKNOWN
    }

    private int maxGreen;
    private int minPurple;
    private double blankDist;

    public void init(HardwareMap hardwareMap, String device, int maxGreen, int minPurple, double blankDist) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, device);
        if (sensor instanceof SwitchableLight) ((SwitchableLight)sensor).enableLight(false);
        sensor.setGain(30);

        this.maxGreen = maxGreen;
        this.minPurple = minPurple;
        this.blankDist = blankDist;
    }

    private float getHue() {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(colors.toColor(), hsv);
        return hsv[0];
    }

    private double getDistance() {
        return ((OpticalDistanceSensor) sensor).getLightDetected();
    }

    public static DetectedColor getDetectedColor(colorSensor cs1, colorSensor cs2) {
        double distance = Math.min(cs1.getDistance(), cs2.getDistance());
        if (distance < Math.max(cs1.blankDist, cs2.blankDist)) return DetectedColor.UNKNOWN;

        float hue1 = cs1.getHue();
        float hue2 = cs2.getHue();

        if (hue1 > cs1.minPurple || hue2 > cs2.minPurple) {
            return DetectedColor.PURPLE;
        }
        if (hue1 < cs1.maxGreen || hue2 < cs2.maxGreen){
            return DetectedColor.GREEN;
        }

        return DetectedColor.UNKNOWN;
    }

    
    public static int numSpin(DetectedColor[] colors, int greenIndex){
        int currentGreenIndex = 0;
        for(int i = 0; i < colors.length; i++){
            if(colors[i] == DetectedColor.GREEN){
                currentGreenIndex = i;
                break; // exit early once found
            }
        }
        return (greenIndex - currentGreenIndex + 3) % 3;
    }
}