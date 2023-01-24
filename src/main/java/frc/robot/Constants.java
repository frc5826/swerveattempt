// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.DriveSubsystem;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static Joystick joystick = new Joystick(0);

    public static final XboxController xbox = new XboxController(1);

    public static final Trigger autobalance = new Trigger(() -> joystick.getRawButton(11));

    public static final Trigger zeroGyro = new Trigger(() -> joystick.getRawButtonPressed(7));

    //public static CANSparkMax turn = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    //public static CANSparkMax drive = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);

    public static double xboxMin = 0.15;

    public static double joystickMin = 0.15;

    public static double turnSpeed = 12;
    public static double driveSpeed = 6;

    public static double turnDeadBand = Math.PI / 180;

}
