// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Timer;

/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {

  private final WPI_TalonSRX leftFront = new WPI_TalonSRX(3);
  private final WPI_TalonSRX leftBack = new WPI_TalonSRX(4);
  private final WPI_TalonSRX rightFront = new WPI_TalonSRX(1);
  private final WPI_TalonSRX rightBack = new WPI_TalonSRX(2);

  private final Spark armExtend = new Spark(1);
  private final Spark armTilt = new Spark(0);
  private final Spark intakeOuter = new Spark(3);
  private final Spark intakeInner = new Spark(2);

  private final MecanumDrive drive = new MecanumDrive(leftFront, leftBack, rightFront, rightBack);
  private final XboxController controller = new XboxController(0);
  private final Timer timer = new Timer();

  private double[][] instructions1 = {
    // {IntakeOuter, LeftFront, LeftBack, RightFront, RightBack, Seconds}
    {0.5, 0.0, 0.0, 0.0, 0.0, 3.0}, // run intake for 3 secs
    {0.0, -0.5, -0.5, -0.5, -0.5, 6.0} // drive backward at half speed for 6 secs
  };

  private double[][] instructions2 = {
    {0.5, 0.0, 0.0, 0.0, 0.0, 3.0}, // run intake for 3 secs
    {0.0, -0.5, -0.5, -0.5, -0.5, 4.0}, // drive backward at half speed for 4 secs
    {0.0, 0.0, 0.0, 0.0, 0.0, 1.0} // stop
  };
  
  private int currentInstruction;

  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    rightFront.setInverted(true);
    rightBack.setInverted(true);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
  }

  @Override
  public void autonomousPeriodic() {

    double[][] instructionSet = instructions1;

    double instructionIntakeOuter = 0.0;
    double instructionLeftFront = 0.0;
    double instructionLeftBack = 0.0;
    double instructionRightFront = 0.0;
    double instructionRightBack = 0.0;
    double instructionSeconds = 1.0;
    if (currentInstruction < instructionSet.length) {
     instructionIntakeOuter = instructionSet[currentInstruction][0]; // intake outer
     instructionLeftFront = instructionSet[currentInstruction][1]; // leftfront
     instructionLeftBack = instructionSet[currentInstruction][2]; // leftfront
     instructionRightFront = instructionSet[currentInstruction][3]; // leftfront
     instructionRightBack = instructionSet[currentInstruction][4]; // leftfront
     instructionSeconds = instructionSet[currentInstruction][5]; // seconds
    }

   if (timer.get() < instructionSeconds) {
     intakeOuter.set(instructionIntakeOuter);
     leftFront.set(instructionLeftFront);
     leftBack.set(instructionLeftBack);
     rightFront.set(instructionRightFront);
     rightBack.set(instructionRightBack);
   } else {
     timer.reset();
     currentInstruction++;
   }

 }

  @Override
  public void teleopInit() {
    timer.reset();
    timer.stop();
  }

  @Override
  public void teleopPeriodic() {
    
    double RT_IntakeCone = controller.getRightTriggerAxis(); //right trigger (held) (?)
    double LT_IntakeCube = controller.getLeftTriggerAxis(); //left trigger (held) (?)
    boolean RB_ReleaseCone = controller.getRightBumper(); //right bumper (held)
    boolean LB_ReleaseCube = controller.getLeftBumper(); //left bumper (held)
    boolean A_ArmExtend = controller.getAButton(); // a button (held)
    boolean B_ArmExtendReverse = controller.getBButton(); // b button (held)
    boolean X_ArmTilt = controller.getXButton(); // x button (held)
    boolean Y_ArmTiltReverse = controller.getYButton(); // y button (held)

    boolean backButton = controller.getBackButton();
    boolean startButton = controller.getStartButtonPressed(); // start button
    boolean startHeld = false; // start held variable
    
    //mecha drive
    drive.driveCartesian(controller.getLeftY()*0.5, -controller.getLeftX()*0.5, controller.getRightX()*0.5);

    //intake
    if (RT_IntakeCone > 0.1) {
      intakeOuter.set(0.5);
      intakeInner.set(0.5);
    } else if (RB_ReleaseCone) {
      intakeOuter.set(-0.5);
      intakeInner.set(-0.5);
    } else if (LT_IntakeCube > 0.1) {
      intakeOuter.set(-0.5);
      intakeInner.set(-0.5);
    } else if (LB_ReleaseCube) {
      intakeOuter.set(0.5);
      intakeInner.set(0.5);
    } else {
      intakeOuter.set(0.0);
      intakeInner.set(0.0);
    }
  
    //arm
    if (A_ArmExtend) {
      armExtend.set(0.7); 
    } else if (B_ArmExtendReverse) {
      armExtend.set(-0.7);
    } else {
      armExtend.set(0.0);
    }

    if (X_ArmTilt) {
      armTilt.set(-0.65);
    } else if (Y_ArmTiltReverse) {
      armTilt.set(0.35);
    } else {
      armTilt.set(0.0);
    }

    if (backButton){
      intakeInner.set(-0.7);
      intakeOuter.set(1.0);
    }

    if (startButton) {
      startHeld = !startHeld;
    }

    while(startHeld == true) {
      armTilt.set(-0.2);
      startHeld = true;
      if (startButton) {
        startHeld = !startHeld;
      }
    }

  }
}
