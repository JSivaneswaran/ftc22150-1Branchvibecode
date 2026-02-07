package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorDemo {

    public NormalizedColorSensor mod2_cs1, mod2_cs2;

    public enum DetectedColor {
        PURPLE,
        GREEN,
        UNKNOWN
    }

    public void init(HardwareMap hardwareMap) {
        mod2_cs1 = hardwareMap.get(NormalizedColorSensor.class, "mod2_cs1");
        mod2_cs2 = hardwareMap.get(NormalizedColorSensor.class, "mod2_cs2");

        mod2_cs1.setGain(30);
        mod2_cs2.setGain(30);
    }

    public DetectedColor detectColor(NormalizedColorSensor sensor, Telemetry telemetry, String lable) {
        NormalizedRGBA colorSensor = sensor.getNormalizedColors();

        if (colorSensor.alpha < 0.02) {
            telemetry.addLine("UNKNOWN");
            return DetectedColor.UNKNOWN;
        }

        double normRed = colorSensor.red / colorSensor.alpha;
        double normGreen = colorSensor.green / colorSensor.alpha;
        double normBlue = colorSensor.blue / colorSensor.alpha;

        telemetry.addData("normRed", normRed);
        telemetry.addData("normGreen", normGreen);
        telemetry.addData("normBlue", normBlue);
        telemetry.addData("alpha", colorSensor.alpha);

// GREEN ball
        if (normGreen > 1.2  && normGreen > normRed && normGreen > normBlue) {
            return DetectedColor.GREEN;
        }

// PURPLE ball (red + blue dominant)
        if (normRed > 0.9 && normBlue > 0.9 && normGreen < 0.9) {
            return DetectedColor.PURPLE;
        }

        return DetectedColor.UNKNOWN;
    }

    // ---------- Public call ----------
    public DetectedColor getDetectedColor(Telemetry telemetry) {
        DetectedColor c1 = detectColor(mod2_cs1, telemetry, "mod2_cs1");
        DetectedColor c2 = detectColor(mod2_cs2, telemetry, "mod2_cs2");

        if (c1 != DetectedColor.UNKNOWN) return c1;
        if (c2 != DetectedColor.UNKNOWN) return c2;

        return DetectedColor.UNKNOWN;
    }

    public static int numSpin(DetectedColor[] colors, int greenIndex) {
        int currentGreenIndex = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == DetectedColor.GREEN) {
                currentGreenIndex = i;
                break;
            }
        }
        return (greenIndex - currentGreenIndex + 3) % 3;
    }
}