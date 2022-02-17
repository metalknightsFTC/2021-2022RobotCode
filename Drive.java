package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class Drive extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontRight = null;
    private DcMotor frontLeft = null;
    private DcMotor backRight = null;
    private DcMotor backLeft = null;
    private DcMotor intake;
    private DcMotor duck;
    private DcMotor lift;
    private Servo grabber;
    private boolean automatics = false;
    private int targetRotations = 0;
    private double duckPower = .47;
    double totalLiftPercentage;

    @Override
    public void runOpMode() {
        
        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        intake = hardwareMap.get(DcMotor.class, "intake");
        duck = hardwareMap.get(DcMotor.class, "duck");
        lift = hardwareMap.get(DcMotor.class, "lifter");
        grabber = hardwareMap.get(Servo.class, "grabber");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        
        waitForStart();
        
        while (opModeIsActive()) {
            
            if(gamepad1.back){
                automatics = true;
            }
            if(gamepad1.start){
                automatics = false;
                targetRotations = 0;
            }
            if(gamepad1.left_bumper){
                grabber.setPosition(1);
            }else{
                grabber.setPosition(0);
            }
            
            if(gamepad1.a){
                targetRotations = 58;
            }
            if(gamepad1.x){
                targetRotations = 144;
            }
            if(gamepad1.y){
                targetRotations = 288;
            }
            if(gamepad1.b){
                targetRotations = 0;
            }
            
            telemetry.addData("AP = ", automatics);
            
            double forward = gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;
            
            frontLeft.setPower((forward)-(turn)-(strafe));
            backLeft.setPower((forward)-(turn)+(strafe));
            
            frontRight.setPower((forward)+(turn)+(strafe));
            backRight.setPower((forward)+(turn)-(strafe));
            
            if(automatics){
                int liftDelta = (targetRotations-lift.getCurrentPosition())*.2;
				totalLiftPercentage = liftDelta/targetRotations;
				lift.setPower(liftDelta);
				
            }else{
                if(gamepad1.dpad_up)
                    lift.setPower(0.5);
                else if(gamepad1.dpad_down)
                    lift.setPower(-0.5);
                else
                    lift.setPower(0);
            }
            
            
            
            intake.setPower(gamepad1.right_trigger);
            
            if(gamepad1.dpad_left){
                duckPower = .47 + (gamepad1.left_trigger*2);
            }else if(gamepad1.dpad_right){
                duckPower = -.47 - (gamepad1.left_trigger*2);
            }else{
                duckPower = 0;
            }
            duck.setPower(duckPower);
            
            
            
            telemetry.addData("Lift Target: ", (double)((double)targetRotations));
            telemetry.addData("Current Lift Position: ", lift.getCurrentPosition());
			telemetry.addData("Lift Percentage: ", totalLiftPercentage*100,"%");
            telemetry.update();
        }
    }
}
