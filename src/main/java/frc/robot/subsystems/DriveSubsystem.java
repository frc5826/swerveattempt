// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class DriveSubsystem extends SubsystemBase
{

    public SwerveModule frontLeftModule;
    public SwerveModule frontRightModule;
    public SwerveModule backLeftModule;
    public SwerveModule backRightModule;

    public SwerveDriveKinematics kinematics;

    public AHRS gyro = new AHRS(SPI.Port.kMXP);

    public final SwerveDriveOdometry odometry = new SwerveDriveOdometry(kinematics, Rotation2d.fromDegrees(gyro.getFusedHeading()));


    /** Creates a new ExampleSubsystem. */
    public DriveSubsystem() {

        gyro.reset();

        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("Drivetrain");
        frontLeftModule = Mk4SwerveModuleHelper.createNeo(
                shuffleboardTab.getLayout("Front Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(0, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 2, 1, 51, -Math.toRadians(230.13)); //front left
        frontRightModule = Mk4SwerveModuleHelper.createNeo(
                shuffleboardTab.getLayout("Front Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(1, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 4, 3, 52, -Math.toRadians(267.81)); //front right
        backLeftModule = Mk4SwerveModuleHelper.createNeo(
                shuffleboardTab.getLayout("Back Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(2, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 8, 7, 50, -Math.toRadians(205.76-180)); //back left
        backRightModule = Mk4SwerveModuleHelper.createNeo(
                shuffleboardTab.getLayout("Back Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(3, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 6, 5, 49, -Math.toRadians(216.86-180)); //back right

        //TODO wheel locations
        Translation2d frontLeftWheel = new Translation2d(0.1905, 0.2413); // 15/19
        Translation2d frontRightWheel = new Translation2d(0.1905, -0.2413);
        Translation2d backLeftWheel = new Translation2d(-0.1905, 0.2413);
        Translation2d backRightWheel = new Translation2d(-0.1905, -0.2413);


        kinematics = new SwerveDriveKinematics(frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel);

        shuffleboardTab.addNumber("Gyroscope Angle", () -> gyro.getAngle());
        //odometry.getPoseMeters().getRotation().getDegrees()

    }

    public void drive(ChassisSpeeds speeds) {

        SwerveModuleState[] moduleStates;

        moduleStates = kinematics.toSwerveModuleStates(speeds);

        SwerveModuleState frontLeft = moduleStates[0];
        SwerveModuleState frontRight = moduleStates[1];
        SwerveModuleState backLeft = moduleStates[2];
        SwerveModuleState backRight = moduleStates[3];

        //set motor speeds
        frontLeftModule.set(frontLeft.speedMetersPerSecond, frontLeft.angle.getRadians()); // frontLeft.angle.getDegrees()
        frontRightModule.set(frontRight.speedMetersPerSecond, frontRight.angle.getRadians());
        backLeftModule.set(backLeft.speedMetersPerSecond, backLeft.angle.getRadians());
        backRightModule.set(backRight.speedMetersPerSecond, backRight.angle.getRadians());
    }
    
    
    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }
    
    
    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}
