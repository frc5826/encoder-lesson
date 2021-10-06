// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the build.gradle file in the
 * project.<br>
 * <br>
 * This class, via its super class, provides the following methods which are called by the main loop:<br>
 * <br>
 *   - startCompetition(), at the appropriate times:<br>
 * <br>
 *   - robotInit() -- provide for initialization at robot power-on<br>
 * <br>
 * init methods -- each of the following methods is called once when the appropriate mode is entered:<br>
 *     - disabledInit()   -- called each and every time disabled is entered from another mode<br>
 *     - autonomousInit() -- called each and every time autonomous is entered from another mode<br>
 *     - teleopInit()     -- called each and every time teleop is entered from another mode<br>
 *     - testInit()       -- called each and every time test is entered from another mode<br>
 * <br>
 * periodic methods -- each of these methods is called on an interval:<br>
 *   - robotPeriodic()<br>
 *   - disabledPeriodic()<br>
 *   - autonomousPeriodic()<br>
 *   - teleopPeriodic()<br>
 *   - testPeriodic()<br>
 */
public class Robot extends TimedRobot
{

    private WPI_TalonSRX talon;

    private final int TALON_PID = 2;
    private final double GEAR_RATIO = 5;
    //Spec says 4096 - This gave me more accurate results
    private final double PULSE_PER_ROT = 4300;
    private final int PID_IDX = 0;
    private final int TIMEOUT_MS = 1000;
    private final double ZERO_POS = 0;

    private double TEST_ANGLE = 0.0;
    private int count = 0;


    /**
     * This method is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit()
    {
        talon = new WPI_TalonSRX(TALON_PID);
        talon.configFactoryDefault(TIMEOUT_MS);
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, PID_IDX, TIMEOUT_MS);
        talon.setSensorPhase(true);
        talon.configAllowableClosedloopError(0, PID_IDX, TIMEOUT_MS);

        talon.config_kF(PID_IDX, 0.0, TIMEOUT_MS);
        talon.config_kP(PID_IDX, 0.75, TIMEOUT_MS);
        talon.config_kI(PID_IDX, 0.0, TIMEOUT_MS);
        talon.config_kD(PID_IDX, 0.5, TIMEOUT_MS);

        //Going to 100% makes the power supply unhappy
        talon.configNominalOutputForward(0, TIMEOUT_MS);
        talon.configNominalOutputReverse(0, TIMEOUT_MS);
        talon.configPeakOutputForward(0.5, TIMEOUT_MS);
        talon.configPeakOutputReverse(-0.5, TIMEOUT_MS);
    }

    @Override
    public void teleopInit()
    {
        System.out.println("Start Pos: " + talon.getSelectedSensorPosition(0));
    }

    @Override
    public void teleopPeriodic()
    {
        if(count++ % 25 == 0) {
            System.out.println("Angle: " + TEST_ANGLE);
            System.out.println("Current Pos: " + talon.getSelectedSensorPosition(0));
            System.out.println("Current PWM Pos: " + talon.getSensorCollection().getPulseWidthPosition());
            System.out.println("Desired Pos: " + percentToPulse(TEST_ANGLE));
            System.out.println("Current Error: " + talon.getClosedLoopError());
            System.out.println();
        }

        talon.set(ControlMode.Position, percentToPulse(TEST_ANGLE));
    }

    @Override
    public void disabledInit()
    {
        System.out.println("Final Pos: " + talon.getSelectedSensorPosition(0));
    }

    // "Percent" refers to the percent of the total rotation
    // 1.0 -> 360 degrees, 0.5 -> 180 degrees, -0.25 -> -90 degrees
    // Eventually we'll convert to degrees (% * 360) or radians (% * 2π)
    private double pulsesToPercent(double reading){
        double convertRatio = PULSE_PER_ROT * GEAR_RATIO;
        return ((reading - ZERO_POS) % convertRatio) / convertRatio;
    }

    private double percentToPulse(double percent){
        double convertRatio = PULSE_PER_ROT * GEAR_RATIO;
        return (percent * convertRatio) + ZERO_POS;
    }
}
