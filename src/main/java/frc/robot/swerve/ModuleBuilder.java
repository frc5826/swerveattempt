package frc.robot.swerve;

import com.swervedrivespecialties.swervelib.*;
import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.swervedrivespecialties.swervelib.ctre.CanCoderFactoryBuilder;
import com.swervedrivespecialties.swervelib.rev.NeoDriveControllerFactoryBuilder;
import com.swervedrivespecialties.swervelib.rev.NeoSteerConfiguration;
import com.swervedrivespecialties.swervelib.rev.NeoSteerControllerFactoryBuilder;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class ModuleBuilder {
    private static DriveControllerFactory<?, Integer> getNeoDriveFactory(Mk4ModuleConfiguration configuration) {
        return new NeoDriveControllerFactoryBuilder()
                .withVoltageCompensation(configuration.getNominalVoltage())
                .withCurrentLimit(configuration.getDriveCurrentLimit())
                .build();
    }

    private static SteerControllerFactory<?, NeoSteerConfiguration<CanCoderAbsoluteConfiguration>> getNeoSteerFactory(Mk4ModuleConfiguration configuration) {
        return new SteerControllerFactoryBuilder()
                .withVoltageCompensation(configuration.getNominalVoltage())
                .withPidConstants(1.0, 0.0, 0.1)
                .withCurrentLimit(configuration.getSteerCurrentLimit())
                .build(new CanCoderFactoryBuilder()
                        .withReadingUpdatePeriod(100)
                        .build());
    }

    public static SwerveModule createModule(ShuffleboardLayout container,
                                            Mk4ModuleConfiguration configuration,
                                            Mk4SwerveModuleHelper.GearRatio gearRatio,
                                            int driveMotorPort,
                                            int steerMotorPort,
                                            int steerEncoderPort,
                                            double steerOffset) {
        return new SwerveModuleFactory<>(gearRatio.getConfiguration(), getNeoDriveFactory(configuration), getNeoSteerFactory(configuration)).create(
                container,
                driveMotorPort,
                new NeoSteerConfiguration<>(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                )
        );
    }

    public static SwerveModule createModule(ShuffleboardLayout container,
                                            Mk4SwerveModuleHelper.GearRatio gearRatio,
                                            int driveMotorPort,
                                            int steerMotorPort,
                                            int steerEncoderPort,
                                            double steerOffset) {
        return createModule(container, new Mk4ModuleConfiguration(), gearRatio, driveMotorPort, steerMotorPort, steerEncoderPort, steerOffset);
    }
}
