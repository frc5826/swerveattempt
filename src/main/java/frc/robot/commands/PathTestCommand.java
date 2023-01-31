package frc.robot.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class PathTestCommand extends CommandBase {

    private final DriveSubsystem driveSubsystem;

    PathPlannerTrajectory testPath;

    public PathTestCommand(DriveSubsystem driveSubsystem, String path) {
        this.driveSubsystem = driveSubsystem;
        addRequirements(driveSubsystem);

        testPath = PathPlanner.loadPath(path, new PathConstraints(1, 1));
    }


}
