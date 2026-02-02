package org.firstinspires.ftc.teamcode.autos;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.mecanumDrivee;

@Autonomous(name = "AutoCode")
public class autoShoot extends OpMode {
    mecanumDrivee drive = new mecanumDrivee();

    @Override
    public void init() {
        drive.init(hardwareMap);

    }

    @Override
    public void loop() {
    }


}
