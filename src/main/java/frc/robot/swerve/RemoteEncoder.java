package frc.robot.swerve;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.swervedrivespecialties.swervelib.AbsoluteEncoder;

public class RemoteEncoder implements RelativeEncoder, AbsoluteEncoder {
    private final CANCoder canCoder;

    @Override
    public double getPosition() {
        return canCoder.getAbsolutePosition();
    }

    @Override
    public double getVelocity() {
        return canCoder.getVelocity();
    }

    @Override
    public REVLibError setPosition(double newPosition) {
        canCoder.setPosition(newPosition);
        return null;
    }

    public RemoteEncoder(int deviceNumber) {
        canCoder = new CANCoder(deviceNumber);
    }

    @Override
    public REVLibError setPositionConversionFactor(double factor) {
        return null;
    }

    @Override
    public REVLibError setVelocityConversionFactor(double factor) {
        return null;
    }

    @Override
    public double getPositionConversionFactor() {
        return 0;
    }

    @Override
    public double getVelocityConversionFactor() {
        return 0;
    }

    @Override
    public REVLibError setAverageDepth(int depth) {
        return null;
    }

    @Override
    public int getAverageDepth() {
        return 0;
    }

    @Override
    public REVLibError setMeasurementPeriod(int period_ms) {
        return null;
    }

    @Override
    public int getMeasurementPeriod() {
        return 0;
    }

    @Override
    public int getCountsPerRevolution() {
        return 0;
    }

    @Override
    public REVLibError setInverted(boolean inverted) {
        return null;
    }

    @Override
    public boolean getInverted() {
        return false;
    }

    @Override
    public double getAbsoluteAngle() {
        return canCoder.getAbsolutePosition();
    }

    public CANCoder getCanCoder() {
        return canCoder;
    }
}
