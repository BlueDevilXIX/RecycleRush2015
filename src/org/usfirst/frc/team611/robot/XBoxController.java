package org.usfirst.frc.team611.robot;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Use an XBox controller like a regular Joystick class
 *
 * @author Jon Morton
 * Source: https://github.com/team1306/BadgerbotsLib/blob/master/src/org/badgerbots/lib/XBoxController.java
 */
public class XBoxController {

    private DriverStation _ds;
    private final int _port;

    public XBoxController(int port) {
        _ds = DriverStation.getInstance();
        _port = port;
    }

    public double getRawAxis(final int axis) {
        return _ds.getStickAxis(_port, axis);
    }

    public boolean getRawButton(final int button) {
        return ((0x1 << (button - 1)) & _ds.getStickButtons(_port)) != 0;
    }

   
    public double getRightTrigger() {
    	return deadZone(3);
    }

    public double getLeftTrigger() {
    	return deadZone(2);
    }

    public double getRightJoyX() {
    	return deadZone(4);
    }

    public double getRightJoyY() {
    	return deadZone(5);
    }

    public double getLeftJoyX() {
    	return deadZone(0);
    }

    public double getLeftJoyY() {
    	return deadZone(1);
    }
    public double deadZone(int axis) {
    	if (Math.abs(getRawAxis(axis)) >= 0.1) {
    		return getRawAxis(axis);
    	} else {
    		return 0;
    	}
    }

    public boolean getButtonA() {
        return getRawButton(1);
    }

    public boolean getButtonB() {
        return getRawButton(2);
    }

    public boolean getButtonX() {
        return getRawButton(3);
    }

    public boolean getButtonY() {
        return getRawButton(4);
    }

    public boolean getButtonBack() {
        return getRawButton(7);
    }

    public boolean getButtonStart() {
        return getRawButton(8);
    }

    public boolean getButtonRB() {
        return getRawButton(6);
    }

    public boolean getButtonLB() {
        return getRawButton(5);
    }

    public boolean getButtonLS() {
        return getRawButton(9);
    }

    public boolean getButtonRS() {
        return getRawButton(10);
    }
    
    //d-Pad
    //     [12]
    // [14]    [15]
    //     [13]
    
    public boolean getPadUp() {
    	return getRawButton(12);
    }
    
    public boolean getPadDown() {
    	return getRawButton(13);
    }
    
    public boolean getPadLeft() {
    	return getRawButton(14);
    }
    
    public boolean getPadRight() {
    	return getRawButton(15);
    }
}