// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {

  WPI_TalonSRX leftFront;
  WPI_TalonSRX leftBack;
  WPI_TalonSRX rightFront;
  WPI_TalonSRX rightBack;

  MecanumDrive drive;
  XboxController controller;

  @Override
  public void robotInit() {
    leftFront = new WPI_TalonSRX(0);
    leftBack = new WPI_TalonSRX(1);
    rightFront = new WPI_TalonSRX(2);
    rightBack = new WPI_TalonSRX(3);
    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    rightFront.setInverted(true);
    rightBack.setInverted(true);

    drive = new MecanumDrive(leftFront, leftBack, rightFront, rightBack);

    controller = new XboxController(0);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for forward movement, Y axis for lateral
    // movement, and Z axis for rotation.
    drive.driveCartesian(controller.getLeftY(), controller.getRightY(), controller.getRightX());
  }
}
