
//2014 - 2015 Season.
package org.usfirst.frc.team611.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
    XBoxController xBox;
    Talon fL, fR, bL, bR;
    RobotDrive drive;
    Talon lift, grab;
    CameraServer server;
    Timer auto;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	//Initialization of XBoxController
        xBox = new XBoxController(0);
        
        //Drivetrain:
        //FRONT LEFT--------FRONT RIGHT-------
        fL = new Talon(2);  fR = new Talon(0);
        //------------------------------------
        //BACK LEFT---------BACK RIGHT--------
        bL = new Talon(3);  bR = new Talon(1);
       
    ///*
        //Initialization of lift and grab
        grab = new Talon(4);
    //*/
        lift = new Talon(5);
    
        
        //Initialization of drive class
        drive = new RobotDrive(fL, bL, fR, bR);
        
        //This lets the camera work
        server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
        /*
        To find name:
        -Plug into RoboRIO via USB 
        -go to 172.22.11.2 [in a web browser]
        -find out its name in the left hand column
        -put in the above field
    	*/
        
        auto = new Timer();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        while(isOperatorControl() ) {
            while (isOperatorControl() && isEnabled() && !xBox.getButtonStart()) {
            	//Drive Control
            	drive.mecanumDrive_Cartesian ( (xBox.getLeftJoyX() / 2), (xBox.getLeftJoyY() / 2), (xBox.getRightJoyX() / 2), 0 );
            	//drive.mecanumDrive_Cartesian (xBox.getLeftJoyX(), xBox.getLeftJoyY(), xBox.getRightJoyX(), 0);
            	
            	//Lifting Control
            	if (xBox.getButtonY()) {	//up
            		lift.set(-0.5);
            	} else if (xBox.getButtonA()) {	//down
            		lift.set(0.5);
            	} else {
            		lift.set(0);
            	}
            	
            	//Grabber Control 
            	if (xBox.getButtonX()) {
            		grab.set(1);
            	} else if (xBox.getButtonB()) {
            		grab.set(-1);
            	} else {
            		grab.set(0);
            	}
            	
            	
            	/*
            	if (xBox.getButtonStart()) {
            		drive.stopMotor();
            	}
            	*/
            	if (xBox.getButtonLB()) {	// Strafe Left; Left Button above trigger
            		fL.set(-.5);
                    fR.set(.5);
                    bL.set(.4);
                    bR.set(-.5);
            		//drive.mecanumDrive_Cartesian (-.5, 0, 0, 0);
            		SmartDashboard.putString("DB/String 0", "i like left strafe");
            		
            	}
            	if (xBox.getButtonRB()) {	// Strafe Right; Right Button above trigger
            		fL.set(.4);
                    fR.set(-.5);
                    bL.set(-.5);
                    bR.set(.33);
            		//drive.mecanumDrive_Cartesian (1, 0, 0, 0);
            		SmartDashboard.putString("DB/String 5", "i like right strafe");
            	}
            	
                Timer.delay(0.001);
                
            }
            
        }
    }
    
    
    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	
    	pMove(2, 1, "f");
    	pDelay(.25);
    	pMove(2, 0, "r");
    	
 
    	SmartDashboard.putString("DB/String 4", "plz work plox");
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void pDelay(double t) {
    	auto.start();
    	do {
    		SmartDashboard.putString("DB/String 9", "---------");
    		SmartDashboard.putString("DB/String 9", "0000000");
    	} while (auto.get() <= t);
    	auto.stop();
  		auto.reset();
    }
    
    public void pMove(double x, int y, String str) {	//x: time (seconds)		y: type (1: y-axis, 2: x-axis, 0: rotate)		str: (f)orward, (b)ack, (r)ight, (l)eft
    	int dir = 0;
    	double spd = .25;
    	double rot = .25;
    	double sid = .5;
    	if (y == 1) {
    		rot = 0;
    		sid = 0;
    	}
    	else if (y == 0) {
    		spd = 0;
    		sid = 0;
    	}
    	else if (y == 2) {
    		rot = 0;
    		spd = 0;
    	}
    	if (str == "f" || str == "l") {							//f, b, r, l
    		dir = -1;
    	}
    	else if (str == "b" || str == "r") {
    		dir = 1;
    	}
    	else {
    		dir = 0;
    	}
    	auto.start();
    	do {
    		drive.mecanumDrive_Cartesian(sid * dir, spd * dir, rot * dir, 0);
    	} while (auto.get() <= x);
    	auto.stop();
  		auto.reset();
		
		drive.mecanumDrive_Cartesian(0, 0, 0, 0);
    }
    
    public void pLift(double x, String str) {
    	int dir = 0;
    	if (str == "up") {
    		dir = -1;
    	}
    	else if (str == "down") {
    		dir = 1;
    	}
    	else {
    		dir = 0;
    	}
    	auto.start();
    	do {
    		lift.set(.5 * dir);
    	} while (auto.get() <= x);
    	auto.stop();
  		auto.reset();
		
		lift.set(0);
    } 
    
    public void pGrab(double x, String str) {
    	int dir = 0;
    	if (str == "in") {
    		dir = -1;
    	}
    	else if (str == "out") {
    		dir = 1;
    	}
    	else {
    		dir = 0;
    	}
    	auto.start();
    	do {
    		grab.set(.5 * dir);
    	} while (auto.get() <= x);
    	auto.stop();
  		auto.reset();
		
		grab.set(0);
    } 
    
}
