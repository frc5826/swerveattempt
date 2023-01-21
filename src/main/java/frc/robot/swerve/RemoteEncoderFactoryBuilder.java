package frc.robot.swerve;

import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.swervedrivespecialties.swervelib.AbsoluteEncoder;
import com.swervedrivespecialties.swervelib.AbsoluteEncoderFactory;
import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.swervedrivespecialties.swervelib.ctre.CanCoderFactoryBuilder;
import com.swervedrivespecialties.swervelib.ctre.CtreUtils;

public class RemoteEncoderFactoryBuilder {

    private int periodMilliseconds = 10;

    public RemoteEncoderFactoryBuilder withReadingUpdatePeriod(int periodMilliseconds) {
        this.periodMilliseconds = periodMilliseconds;
        return this;
    }

    public AbsoluteEncoderFactory<CanCoderAbsoluteConfiguration> build() {
        return configuration -> {
            CANCoderConfiguration config = new CANCoderConfiguration();
            config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
            config.magnetOffsetDegrees = Math.toDegrees(configuration.getOffset());
            config.sensorDirection = false;

            RemoteEncoder encoder = new RemoteEncoder(configuration.getId());
            CtreUtils.checkCtreError(encoder.getCanCoder().configAllSettings(config, 250), "Failed to configure CANCoder");

            CtreUtils.checkCtreError(encoder.getCanCoder().setStatusFramePeriod(CANCoderStatusFrame.SensorData, periodMilliseconds, 250), "Failed to configure CANCoder update rate");

            return new RemoteEncoderFactoryBuilder.EncoderImplementation(encoder);
        };
    }

    private static class EncoderImplementation implements AbsoluteEncoder {
        private final RemoteEncoder encoder;

        private EncoderImplementation(RemoteEncoder encoder) {
            this.encoder = encoder;
        }

        @Override
        public double getAbsoluteAngle() {
            double angle = Math.toRadians(encoder.getPosition());
            angle %= 2.0 * Math.PI;
            if (angle < 0.0) {
                angle += 2.0 * Math.PI;
            }

            return angle;
        }
    }
}
