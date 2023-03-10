// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.swervedrivespecialties.swervelib.ctre.CanCoderFactoryBuilder;
import frc.robot.Constants;
import frc.robot.swerve.ModuleBuilder;


public class DriveSubsystem extends SubsystemBase
{

    public SwerveModule frontLeftModule;
    public SwerveModule frontRightModule;
    public SwerveModule backLeftModule;
    public SwerveModule backRightModule;

    public SwerveDriveKinematics kinematics;

    public AHRS gyro = new AHRS(SPI.Port.kMXP);

    private double gyroPitch;
    private double gyroRoll;

    public final SwerveDriveOdometry odometry;
    public double fusedHeadingOffset;


    /** Creates a new ExampleSubsystem. */
    public DriveSubsystem() {

        gyro.reset();
        gyroPitch = gyro.getPitch();
        gyroRoll = gyro.getRoll();

        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("Drivetrain");
        frontLeftModule = ModuleBuilder.createModule( //ModuleBuilder.createModule     Mk4SwerveModuleHelper.createNeo
                shuffleboardTab.getLayout("Front Left Module", BuiltInLayouts.kList)
                        .withSize(2, 2)
                        .withPosition(0, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 2, 1, 51, -Math.toRadians(-26.7)); //front left
        frontRightModule = ModuleBuilder.createModule(
                shuffleboardTab.getLayout("Front Right Module", BuiltInLayouts.kList)
                        .withSize(2, 2)
                        .withPosition(2, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 4, 3, 49, -Math.toRadians(99.5 + 306)); //front right
        backLeftModule = ModuleBuilder.createModule(
                shuffleboardTab.getLayout("Back Left Module", BuiltInLayouts.kList)
                        .withSize(2, 2)
                        .withPosition(4, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 8, 7, 50, -Math.toRadians(203 - 180)); //back left
        backRightModule = ModuleBuilder.createModule(
                shuffleboardTab.getLayout("Back Right Module", BuiltInLayouts.kList)
                        .withSize(2, 2)
                        .withPosition(6, 0),
                Mk4SwerveModuleHelper.GearRatio.L1, 6, 5, 52, -Math.toRadians(38.1)); //back right

        //TODO wheel locations
        Translation2d frontLeftWheel = new Translation2d(0.257, 0.2794);
        Translation2d frontRightWheel = new Translation2d(0.257, -0.2794);
        Translation2d backLeftWheel = new Translation2d(-0.257, 0.2794);
        Translation2d backRightWheel = new Translation2d(-0.257, -0.2794);

        fusedHeadingOffset = gyro.getFusedHeading();

        kinematics = new SwerveDriveKinematics(frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel);
        odometry = new SwerveDriveOdometry(kinematics, Rotation2d.fromDegrees(gyro.getFusedHeading() - fusedHeadingOffset));

        shuffleboardTab.addNumber("Gyroscope Angle", () -> gyro.getAngle());
        shuffleboardTab.addNumber("Gyroscope Pitch", () -> getGyroPitch());
        shuffleboardTab.addNumber("Gyroscope Roll", () -> getGyroRoll());
        shuffleboardTab.addNumber("Pose X", () -> odometry.getPoseMeters().getX());
        shuffleboardTab.addNumber("Pose Y", () -> odometry.getPoseMeters().getY());
        shuffleboardTab.addNumber("Fused heading", () -> gyro.getFusedHeading() - fusedHeadingOffset);
        //odometry.getPoseMeters().getRotation().getDegrees()

    }

    public void drive(ChassisSpeeds speeds) {

        SwerveModuleState[] moduleStates;

        moduleStates = kinematics.toSwerveModuleStates(speeds);

        SwerveModuleState frontLeft = moduleStates[0];
        SwerveModuleState frontRight = moduleStates[1];
        SwerveModuleState backLeft = moduleStates[2];
        SwerveModuleState backRight = moduleStates[3];

        odometry.update(Rotation2d.fromDegrees(gyro.getFusedHeading() - fusedHeadingOffset),
                new SwerveModuleState(frontLeftModule.getDriveVelocity(), new Rotation2d(frontLeftModule.getSteerAngle())),
                new SwerveModuleState(frontRightModule.getDriveVelocity(), new Rotation2d(frontRightModule.getSteerAngle())),
                new SwerveModuleState(backLeftModule.getDriveVelocity(), new Rotation2d(backLeftModule.getSteerAngle())),
                new SwerveModuleState(backRightModule.getDriveVelocity(), new Rotation2d(backRightModule.getSteerAngle()))
        );

        //set motor speeds
        frontLeftModule.set(frontLeft.speedMetersPerSecond, frontLeft.angle.getRadians());
        frontRightModule.set(frontRight.speedMetersPerSecond, frontRight.angle.getRadians());
        backLeftModule.set(backLeft.speedMetersPerSecond, backLeft.angle.getRadians());
        backRightModule.set(backRight.speedMetersPerSecond, backRight.angle.getRadians());
    }

    public double getGyroPitch() { return gyro.getPitch() - gyroPitch; }

    public double getGyroRoll() { return gyro.getRoll() - gyroRoll; }

    public void zeroGyro() {
        gyroRoll = gyro.getRoll();
        gyroPitch = gyro.getPitch();
    }

    public Command followTrajectoryCommand(PathPlannerTrajectory path) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> resetOdometry(path) ),
                new PPSwerveControllerCommand(
                        PathPlanner.loadPath("New Path", 1, 1),
                        odometry::getPoseMeters,
                        //TODO this.kinematics, //Add new drive command that takes Module State input.
                        new PIDController(1, 0, 0),
                        new PIDController(1, 0, 0),
                        new PIDController(1, 0, 0),
                        this::drive,
                        true,
                        this)
        );
    }

    public void resetOdometry(PathPlannerTrajectory path) {
        gyro.zeroYaw();
        odometry.resetPosition(path.getInitialHolonomicPose(), gyro.getRotation2d());
    }
    
    @Override
    public void periodic()
    {

    }
    
    
    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}
