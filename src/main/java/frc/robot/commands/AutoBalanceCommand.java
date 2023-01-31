package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PID;

public class AutoBalanceCommand extends CommandBase {

    private AHRS gyro;
    private DriveSubsystem driveSubsystem;
    private PID PIDz = new PID(3,0,0.1, 5, 0.35, 0.3);
    private PID PIDy = new PID(5, 0, 0.1, 5, 0.65, Math.sin(Math.toRadians(1)));
    private PID PIDx = new PID(4, 0, 0.1, 5, 0.65,Math.sin(Math.toRadians(1)));

    public AutoBalanceCommand(DriveSubsystem driveSubsystem) {
        PIDy.setGoal(0);
        PIDz.setGoal(0);
        PIDx.setGoal(0);

        gyro = driveSubsystem.gyro;

        this.driveSubsystem = driveSubsystem;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        double pitch = driveSubsystem.getGyroPitch();
        ChassisSpeeds speeds = new ChassisSpeeds(0, PIDy.calculate(Math.sin(Math.toRadians(pitch))), Math.signum(pitch) * PIDz.calculate(driveSubsystem.getGyroRoll()));
        driveSubsystem.drive(speeds);
    }

}
