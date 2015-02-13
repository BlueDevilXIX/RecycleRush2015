
//2014 - 2015 Season.
package org.usfirst.frc.team611.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
    XBoxController xBox;
    Talon fL, fR, bL, bR;
    RobotDrive drive;
    Talon lift;
    TalonSRX grab;
    CameraServer server;
    Relay grip;
    Timer auto;
    DigitalInput limitSwitch;
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
        grab = new TalonSRX(4);
    //*/
        lift = new Talon(5);
    
        grip = new Relay(0);
        
        //Initialization of drive class
        drive = new RobotDrive(fL, bL, fR, bR);
        
        //Initializes the limit switch
        limitSwitch = new DigitalInput(0);
        
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
            	if (xBox.getButtonY()) {		//up
           			lift.set(-1.0);
            	} else if (xBox.getButtonA()) {	//down
            		lift.set(.25);			
            	} else if (xBox.getRightTrigger() > 0) {
            		lift.set(-1 * xBox.getRightTrigger());
            	} else if (xBox.getLeftTrigger() > 0) {
            		lift.set(1 * xBox.getLeftTrigger());
            	} else {						//off
            		lift.set(0);
            	}
            	
            	//Grabber Control 
            	if (xBox.getButtonB()) {
            		grab.set(1);
            		//grip.set(Relay.Value.kForward);	//out
            	} else if (xBox.getButtonX()) {
            		grab.set(-1);
            		//grip.set(Relay.Value.kReverse);	//in
            	} else {
            		grab.set(0);
            		//grip.set(Relay.Value.kOff);;	//off
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
    
    public void autonomousPeriodic() {		//auto run period
    	/*
    	pMove(1, 1, "f");
    	pDelay(.1); */
    	pLift(.5, "down");
    	pDelay(.1);
    	pGrip(.3, "in");
    	pDelay(.1); /*
    	pLift(.5, "up");
    	pDelay(.1);
    	pMove(.5, 1, "b");
    	pDelay(.1);
    	pMove(.5, 2, "r");
    	pDelay(.1);
    	pMove(.5, 1, "f");
    	pDelay(.1);
    	pLift(.5, "down");
    	pDelay(.1);
    	pGrip(.3, "out");
    	pDelay(.1); */
    	
    	SmartDashboard.putString("DB/String 4", "plz work plox");
    	
    	pDelay(5);
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void pDelay(double t) {	//t: time
    	auto.start();
    	do {
    		SmartDashboard.putString("DB/String 9", "---------");
    		SmartDashboard.putString("DB/String 9", "0000000");
    	} while (auto.get() <= t);
    	auto.stop();
  		auto.reset();
    }
    
    public void pMove(double x, int y, String str) {	//x: time (seconds)	y: type (1: y-axis, 2: x-axis, 0: rotate) str: (f)orward, (b)ack, (r)ight, (l)eft
    	int dir = 0;		//forward, left = -1, back, right = 1
    	double spd = .25;	//forward and back speed
    	double rot = .25;	//rotation speed
    	double sid = .5;	//strafing speed
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
    
    public void pLift(double x, String str) {		//x: time, str: up or down
    	int dir = 0;		//up = -1, down = 1
    	double spd = 0;
    	if (str == "up") {
    		dir = -1;
    		spd = .5;
    	}
    	else if (str == "down") {
    		dir = 1;
    		spd = .25;
    	}
    	else {
    		dir = 0;
    		spd = 0;
    	}
    	auto.start();
    	do {
    		lift.set(spd * dir);
    	} while (auto.get() <= x);
    	auto.stop();
  		auto.reset();
		
		lift.set(0);
    } 
    
    public void pGrab(double x, String str) {		//x: time, str: in and out			******talonSRX only************
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
    
    public void pGrip(double x, String str) {		//x: time, str: in and out			********relay only******
    	if (str == "out") {
    		auto.start();
        	do {
        		grip.set(Relay.Value.kReverse);
        	} while (auto.get() <= x);
    	}
    	else if (str == "if") {
    		auto.start();
        	do {
        		grip.set(Relay.Value.kForward);
        	} while (auto.get() <= x);
    	}
    	else {
    		auto.start();
        	do {
        		grip.set(Relay.Value.kOff);
        	} while (auto.get() <= x);
    	}
    	auto.stop();
  		auto.reset();
		
  		grip.set(Relay.Value.kOff);
    } 
    
}
