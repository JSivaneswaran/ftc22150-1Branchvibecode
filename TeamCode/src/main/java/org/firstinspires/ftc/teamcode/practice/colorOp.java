package org.firstinspires.ftc.teamcode.practice;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.subsystems.colorSensor;

@TeleOp(name = "ColorOpMode", group = "Teleop")
public class colorOp extends OpMode {

    String[] colorSensorMod2 = {"cs1", "cs2"};
    String[] colorSensorMod1 = {"mod1_cs1", "mod1_cs2"};
    String[] colorSensorMod0 = {"mod0_cs1", "mod0_cs2"};

    colorSensor c = new colorSensor();
    colorSensor c1 = new colorSensor();


    @Override
    public void init() {
        c.init(hardwareMap, colorSensorMod0[0], 162, 195,0.06);
        c1.init(hardwareMap, colorSensorMod0[1], 165, 200, 0.06);
    }

    @Override
    public void loop() {

        //telemetry.addData("color overall", c.getDetectedColor(c , c1,telemetry));

        NormalizedRGBA colors = c.sensor.getNormalizedColors();
        NormalizedRGBA colors2 = c1.sensor.getNormalizedColors();

        float[] hsvValues1 = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues1);
        float[] hsvValues2 = new float[3];
        Color.colorToHSV(colors2.toColor(), hsvValues2);



        telemetry.addData("Module 0 CS", 0);
        telemetry.addData("distance", ((OpticalDistanceSensor)c.sensor).getLightDetected());
        telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues1[0])
                .addData("Saturation", "%.3f", hsvValues1[1])
                .addData("Value", "%.3f", hsvValues1[2]);
        telemetry.addData("Alpha", "%.3f", colors.alpha);
        telemetry.addLine();

        telemetry.addData("Module 0 CS", 1);
        telemetry.addData("distance", ((OpticalDistanceSensor)c1.sensor).getLightDetected());
        telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues2[0])
                .addData("Saturation", "%.3f", hsvValues2[1])
                .addData("Value", "%.3f", hsvValues2[2]);
        telemetry.addData("Alpha", "%.3f", colors2.alpha);
        telemetry.addLine();
        telemetry.addData("color", colorSensor.getDetectedColor(c, c1));
    }
}
