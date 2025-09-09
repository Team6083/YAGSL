// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;
import java.io.IOException;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
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

  File directory = new File(
      "src/main/java/frc/robot/YAGSLConfig/swerve/swervedrive.json");
  SwerveParser swerveParser;
  SwerveDrive swerveDrive;
  SwerveController swerveController;

  public Robot() {
    try {
      swerveParser = new SwerveParser(directory);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    swerveDrive = swerveParser.createSwerveDrive(4);
    swerveController = swerveDrive.swerveController;
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

  @Override
  public void teleopPeriodic() {
    if (mainController.getAButton()) {
      SwerveDriveTest.centerModules(swerveDrive);
    } else if (mainController.getLeftTriggerAxis() > 0.1) {
      SwerveDriveTest.runAngleMotorsCharacterizationOnSimModules(
          swerveDrive, mainController.getLeftTriggerAxis() * 12);
    } else if (mainController.getRightTriggerAxis() > 0.1) {
      SwerveDriveTest.runDriveMotorsCharacterizationOnSimModules(
          swerveDrive, mainController.getRightTriggerAxis() * 12, true);
    }

    if (mainController.getBackButton()) {
      swerveDrive.zeroGyro();
    }
  }

  private void swerveDriveControl() {
    swerveDrive.driveFieldOriented(
        swerveController.getTargetSpeeds(
            mainController.getLeftY(), mainController.getLeftX(),
            mainController.getRightX() * 180, swerveDrive.getGyroRotation3d().getAngle(), 4.0));
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
