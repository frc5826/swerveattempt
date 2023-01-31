package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PID;

public class AutoDriveCommand extends CommandBase {

    private final DriveSubsystem driveSubsystem;

    private AHRS gyro;

    private PID pidTurn = new PID(0.5, 0, 0.0, 10, 0.2, 1);
    private double targetAngle;

    private boolean finished = false;

    public AutoDriveCommand(DriveSubsystem driveSubsystem, double goal) {
        gyro = driveSubsystem.gyro;

        pidTurn.setGoal(0);

        targetAngle = goal;

        this.driveSubsystem = driveSubsystem;
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        finished = false;
    }

    @Override
    public void execute() {
        double gyroAngle = gyro.getAngle() % 360;
        if (gyroAngle < 0) {
            gyroAngle += 360;
        }
        double angleDifference = targetAngle - gyroAngle;

        if (Math.abs(angleDifference) <= 1) {
            finished = true;
        }

        if (angleDifference > 180) {
            angleDifference -= 360;
        } else if (angleDifference < -180) {
            angleDifference += 360;
        }

        ChassisSpeeds speeds = new ChassisSpeeds(0, 0, pidTurn.calculate(angleDifference));
        driveSubsystem.drive(speeds);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
