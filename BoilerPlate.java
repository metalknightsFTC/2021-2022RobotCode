//These are the import statements. They will update automaticaly when using the OnBotJava

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/* this statement here tells the robot that this opmode goes in the TeleOp section
 * this is also where you can disable by adding @Dissable to the code 
 * or change it to Auto by replacing telop with Autonomas (but spelled correctly)
 */
@TeleOp

  /* this is the acctual begining of the program 
   * the name of the code goes where Drive is, the name of the program and the name of the code must match
   * Also your class must extend LinearOpMode or it will not compile
   */
public class BoilerPlate extends LinearOpMode{

  /* this is where you declare all of the variables used to reference the items on the control hub
   * The must be declared as private otherwise things can get messed up
   * It is not neccisary for them to have the same name as labled in the Config file, so long as you know what they are
   */
  
        private Blinker expansion_Hub_1;
        private Blinker expansion_Hub_2;
        private DcMotor fR;
        private DcMotor bR;
        private DcMotor fL;
        private DcMotor bL;
        private BNO055IMU imu1;
        
		private bool test;

        /*this is where the code can be called by the app
         * I'm not entirely sure what the @Override does but it is important
         * All of the code you write to be executed when you hit play are done in the runOpMode() function 
         * it contains all of the code to be ran by the program
         */
         @Override
    public void runOpMode() {
           
        /*the program must start with assigning the items in the config to thier respective variables in the program
         * this is part of the init routine
         */
           
        expansion_Hub_1 = hardwareMap.get(Blinker.class, "Expansion Hub 1");
		//you need to comment out the second expansion hub if you arent using it. if you dont do this it will cause errors at runtime
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        imu1 = hardwareMap.get(BNO055IMU.class, "IMU1");
        fL = hardwareMap.get(DcMotor.class, "fl");
        bL = hardwareMap.get(DcMotor.class, "bl");
        fR = hardwareMap.get(DcMotor.class, "fr");
        bR = hardwareMap.get(DcMotor.class, "br");
        
        
        /*this is where you init all of the motors and servos for your program
         * here i separated them out so I could see which were motors and which were servos
         */
        
        //init all of the motors and set their directions
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
		//The directions the motors are supposed to run it changes depending on the robot
		//this is one thing you HAVE to test on EACH ROBOT FIRST
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        
		//uncomment this to initialize encoders
		/*
		fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
		fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		*/
		
		//servos are initialized in the same way as motors.
        
        /*It is also a good idea to init any other non-global variables used by the program during the init portion
         * 
         * the boolean below is used to as an example of where all global variables need to be initialized*/
        
		test = false;
		
        /*Telemetry is lines of text displayed to the driver station
         * it is quite usefull for monitoring variables and giving the drivers useful information
         * here it is used to tell the drivers that the program has finished it's init routine and play is ready to be pressed
         */
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /*
         * It is vitaly importatn that you include the waitForStart() functin between the init code and the actual opMode
         * without it the code will start before you press play 
         */
        waitForStart();
        /*
         * I found it helpfull to label what what the robot controls should be
         * this helps you see what they should be and it should also help others determine what they should do
         */
        
        /*
        * The controls for the robot should be as follows:
        * 
        * Gamepad1:
        * 
        * left_stick = directional motion
        * right_stick_x = turn
        * 
        * Gamepad2:
        * 
        * 
        */
        
        //init servo start position
        
		
		
        /*
         * Run all of the code that needs to be repeated through a loop
         * for teleOp the code should be contained in a loop that will only run while the opMode is active
         */
        while(opModeIsActive()){
            
        // This is the code that the JABOTs 5223 showed us for dirving with mecanum wheels
       // It has helped the evolution of our robot controls dramatically
          
          /*
           * So basicly this code gets the angle of the joystick and sets the wheels to run at a speed such that the robot will move in that dirction 
           * however one thing I noticed is that the movement speed is less that if you set the motors to a pwr level of 1
           * my thoughts on how to get the full speed is to multiply the final output by some factor (mabey 100 or 10)
           */

       double r = Math.hypot(gamepad1.left_stick_y, gamepad1.left_stick_x);
       double robotAngle = (Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4);
       double rightX = gamepad1.right_stick_x;
	   //if the strafing(left and right movement) is opposite of what it should be replace the cos with sin and vice versa
       final double FR = r * Math.cos(robotAngle) - rightX;
       final double FL = r * Math.sin(robotAngle) + rightX;
       final double BR = r * Math.sin(robotAngle) - rightX;
       final double BL = r * Math.cos(robotAngle) + rightX;


       fr.setPower(FR);
       br.setPower(BR);
       fl.setPower(FL);
       bl.setPower(BL);
       
       //end code from JABOTs 5223
       
       /*
        * when using the built in motor encoders generaly 1120 is the magic # of encoder tics per revolution(TPR) of the output shaft on the gearbox of our neverest motors
        * however depending on any gear ratios afterward the # of tics per revolution may increase or decrease proportionaly the gobilda 5202 series
		* the TPR is 385
        */
       /*
       //display the motor positions & the robot's distance for debugging purposes
       telemetry.addLine(" Drive motors current positions in the form of rotations");
       telemetry.addData("fr", (fr.getCurrentPosition()/ 1120));
       telemetry.addData("br", (br.getCurrentPosition()/ 1120));
       telemetry.addData("fl", (fl.getCurrentPosition()/ 1120));
       telemetry.addData("bl", (bl.getCurrentPosition()/ 1120));
       */
	   
        
        }//end while(opModeIsActive())
    }//end runOpMode()
}//end Drive