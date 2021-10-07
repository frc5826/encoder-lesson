// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot
{

    private WPI_TalonSRX talon;

    private final int TALON_PID = 2;

    private final double GEAR_RATIO = 5;
    private final double PULSE_PER_ROT = 4506;
    private int count = 0;

    private final int PID_IDX = 0;
    private final int TIMEOUT_MS = 1000;

    /**
     * TODO #1
     * We need to find the encode value when the pieces of red tape are aligned.
     * You can find this by slowly rotating the motor, printing out the position value, and noting when they're aligned.
     */

    private final double ZERO_POS = 0;


    private double TEST_ANGLE = .5;



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
    }

    @Override
    public void teleopInit()
    {

    }

    @Override
    public void teleopPeriodic()
    {
        double currentPosition = talon.getSelectedSensorPosition(0);

        if(count++ % 25 == 0) {
            System.out.println("Current Pos: " + currentPosition);
            System.out.println();
        }
        
        /**
         * TODO #2
         * Once you have found the ZERO_POS value, you can start to use the percentToPulse/pulsesToPercent methods. Read the notes below to understand how that works.
         * Make sure your TEST_ANGLE is set to .5
         * Similar to last week, you'll need to write some code that moves the wheel to approach the correct angle.
         * You can either use pulsesToPercent or percentToPulse to find your error.
         */

        // You can either get the desired position and compare that to current position
        double desiredPosition = percentToPulse(TEST_ANGLE);

        // Or you find the current angle and compare that to our TEST_ANGLE
        double currentAngle = pulsesToPercent(currentPosition);


        /**
         * TODO #3
         * There are some naive approach that work, but aren't great. Either they lead to error (angles not being correct)
         * or your wheel oscillates back and forth. What are some ways we can fix this?
         */

        /**
         * TODO #4
         * What if we want to rotate different amounts during the competition? Is there a way to make this more modular?
         * Dig into FRC's command based programming to get some ideas.
         * https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html
         */

    }

    @Override
    public void disabledInit()
    {

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
