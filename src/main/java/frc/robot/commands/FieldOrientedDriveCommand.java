// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;



/** An example command that uses an example subsystem. */
public class FieldOrientedDriveCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final DriveSubsystem driveSubsystem;

    private SwerveModuleState[] moduleStates;

    private int counter = 0;

    /**
     * Creates a new ExampleCommand.
     *
     * @param driveSubsystem The subsystem used by this command.
     */
    public FieldOrientedDriveCommand(DriveSubsystem driveSubsystem)
    {
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

        if(Constants.joystick.getRawButtonPressed(8)) {
            driveSubsystem.gyro.zeroYaw();
        }

        double[] joystickInputs = getJoystickInput();

        ChassisSpeeds speeds =  ChassisSpeeds.fromFieldRelativeSpeeds(joystickInputs[0] * Constants.driveSpeed, joystickInputs[1] * Constants.driveSpeed, joystickInputs[2] * Constants.turnSpeed, driveSubsystem.gyro.getRotation2d());

        driveSubsystem.drive(speeds);
    }

    public double[] getJoystickInput() {
        double joystickY = Constants.joystick.getY() * -1;
        double joystickX = Constants.joystick.getX() * -1;
        double joystickZ = Constants.joystick.getZ() * -1;

        if  (Math.abs(joystickY) <= Constants.joystickMin) {
            joystickY = 0.0; }
        if (Math.abs(joystickX) <= Constants.joystickMin) {
            joystickX = 0.0; }
        if (Math.abs(joystickZ) <= Constants.joystickMin) {
            joystickZ = 0.0; }

        double[] joystickReturn = new double[] {joystickY, joystickX, joystickZ};

        return joystickReturn;
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
