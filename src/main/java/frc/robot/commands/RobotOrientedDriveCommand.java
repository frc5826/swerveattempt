package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;



/** An example command that uses an example subsystem. */
public class RobotOrientedDriveCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final DriveSubsystem driveSubsystem;


    private int counter = 0;

    private double driveSpeed = 2;
    private double turnSpeed = 3.14;


    /**
     * Creates a new ExampleCommand.
     *
     * @param driveSubsystem The subsystem used by this command.
     */
    public RobotOrientedDriveCommand(DriveSubsystem driveSubsystem)
    {
        this.driveSubsystem = driveSubsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveSubsystem);
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }


    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        double[] joystickInputs = getJoystickInput();

        //take joystick input
        ChassisSpeeds speeds = new ChassisSpeeds(joystickInputs[0] * driveSpeed, joystickInputs[1] * driveSpeed, joystickInputs[2] * turnSpeed);

        driveSubsystem.drive(speeds);
    }

    public double[] getJoystickInput() {
        double joystickY = Constants.joystick.getY();
        double joystickX = Constants.joystick.getX();
        double joystickZ = Constants.joystick.getZ();

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

