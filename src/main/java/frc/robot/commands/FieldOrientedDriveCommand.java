// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PID;


/** An example command that uses an example subsystem. */
public class FieldOrientedDriveCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final DriveSubsystem driveSubsystem;

    private SwerveModuleState[] moduleStates;

    private int counter = 0;

    private PID zeroPID = new PID(1.0, 0, 0.1, 5, 0.2, Math.PI / 180);

    /**
     * Creates a new ExampleCommand.
     *
     * @param driveSubsystem The subsystem used by this command.
     */
    public FieldOrientedDriveCommand(DriveSubsystem driveSubsystem)
    {
        zeroPID.setGoal(0);

        this.driveSubsystem = driveSubsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveSubsystem);
    }
    
    
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}
    
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (Constants.joystick.getRawButtonPressed(8)) {
            driveSubsystem.gyro.zeroYaw();
        }

        if (Constants.joystick.getRawButtonPressed(3)) {
            turnToZero();
        }

        double[] input = getJoystickInput();

        ChassisSpeeds speeds =  ChassisSpeeds.fromFieldRelativeSpeeds(input[0] * Constants.driveSpeed, input[1] * Constants.driveSpeed, input[2] * Constants.turnSpeed, driveSubsystem.gyro.getRotation2d());

        driveSubsystem.drive(speeds);
    }

    public void turnToZero() {
        ChassisSpeeds speeds = new ChassisSpeeds(0, 0, zeroPID.calculate(driveSubsystem.gyro.getAngle()));
        driveSubsystem.drive(speeds);
    }

    public double[] getJoystickInput() {
        double joystickY = Constants.joystick.getY() * Math.abs(Constants.joystick.getY()) * -1;
        double joystickX = Constants.joystick.getX() * Math.abs(Constants.joystick.getX()) * -1;
        double joystickZ = Constants.joystick.getZ() * Math.abs(Constants.joystick.getZ()) * -1;

        if  (Math.abs(joystickY) <= Constants.joystickMin) {
            joystickY = 0.0; }
        if (Math.abs(joystickX) <= Constants.joystickMin) {
            joystickX = 0.0; }
        if (Math.abs(joystickZ) <= Constants.joystickMin) {
            joystickZ = 0.0; }

        double[] joystickReturn = new double[] {joystickY, joystickX, joystickZ};

        return joystickReturn;
    }

    public double[] getXboxInput() {
        double xboxLeftY = Constants.xbox.getLeftY() * Math.abs(Constants.xbox.getLeftY()) * -1;
        double xboxLeftX = Constants.xbox.getLeftX() * Math.abs(Constants.xbox.getLeftX()) * -1;
        double xboxRightX = Constants.xbox.getRightX() * Math.abs(Constants.xbox.getRightX()) * -1;

        if  (Math.abs(xboxLeftY) <= Constants.xboxMin) {
            xboxLeftY = 0.0; }
        if (Math.abs(xboxLeftX) <= Constants.xboxMin) {
            xboxLeftX = 0.0; }
        if (Math.abs(xboxRightX) <= Constants.xboxMin) {
            xboxRightX = 0.0; }

        double[] xboxReturn = new double[] {xboxLeftY, xboxLeftX, xboxRightX};

        return xboxReturn;
    }
    
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
    
    
    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
