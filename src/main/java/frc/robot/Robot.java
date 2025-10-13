// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.SwerveDriveTest;
import swervelib.parser.SwerveParser;

/**
 * The methods in this class are called automatically corresponding to each
 * mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the
 * package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  XboxController mainController = new XboxController(0);

  File directory;
  SwerveParser swerveParser;
  SwerveDrive swerveDrive;
  SwerveController swerveController;

  StructArrayPublisher<SwerveModuleState> currentStatePublisher;
  StructArrayPublisher<SwerveModuleState> desiredStatePublisher;

  public Robot() {
    directory = new File(Filesystem.getDeployDirectory(), "swerve");
    try {
      swerveParser = new SwerveParser(directory);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    swerveDrive = swerveParser.createSwerveDrive(4);

    swerveController = swerveDrive.swerveController;

    currentStatePublisher = NetworkTableInstance.getDefault()
        .getStructArrayTopic("SwerveCurrentStates", SwerveModuleState.struct).publish();
    desiredStatePublisher = NetworkTableInstance.getDefault()
        .getStructArrayTopic("SwerveDesiredStates", SwerveModuleState.struct).publish();

    putDashboard();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  public void putDashboard() {
    SmartDashboard.putNumber("gyro", swerveDrive.getGyroRotation3d().getAngle());
  }

  ChassisSpeeds robotRelativeSpeeds;
  @Override
  public void teleopPeriodic() {
    putDashboard();

    if (mainController.getAButton()) {
      SwerveDriveTest.centerModules(swerveDrive);
    } else if (mainController.getLeftTriggerAxis() > 0.1) {
      SwerveDriveTest.powerDriveMotorsVoltage(
          swerveDrive, mainController.getLeftTriggerAxis() * 5);
    } else {
      ChassisSpeeds fieldRelativeSpeeds = swerveController.getTargetSpeeds(
          mainController.getLeftY(), mainController.getLeftX(),
          mainController.getRightX() * 180, swerveDrive.getGyroRotation3d().getAngle(), 4.0);

      robotRelativeSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
          fieldRelativeSpeeds, swerveDrive.getGyroRotation3d().toRotation2d());

      swerveDrive.driveFieldOriented(fieldRelativeSpeeds);

    }

    if (mainController.getBackButton()) {
      swerveDrive.zeroGyro();
    }

    currentStatePublisher.set(swerveDrive.getStates());
    desiredStatePublisher.set(swerveDrive.toServeModuleStates(robotRelativeSpeeds, true));
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
