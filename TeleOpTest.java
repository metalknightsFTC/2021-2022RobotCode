/*
Code by Brison
DO NOT TOUCH - MAKE COPY TO EDIT
made 4th quarter 2021
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOpTest", group="")

public class TeleOpTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor duck;
    private DcMotor intake;
    private DcMotor lifter;
    private double angle;
    private double magnitude;
    private double magnitudeA;
    private double magnitudeB;
    private double direction;
    private double posChange;
    private double prePos = 0;
    private double postPos = 0;
    private double frontRightPower = 0;
    private double backLeftPower = 0;
    private double frontLeftPower = 0;
    private double backRightPower = 0;
    private double duckPower = 0;
    private double intakePower = 0;
    private double linearPower = 0;
    private int intakeToggle = 0;
    private double sprint = 1.5;
    
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        intake = hardwareMap.get(DcMotor.class, "intake");
        duck = hardwareMap.get(DcMotor.class, "duckRusher");
        lifter = hardwareMap.get(DcMotor.class, "lifter");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();
        runtime.reset();
        
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        duck.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        duck.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        while (opModeIsActive()) {
             
            magnitudeB = gamepad1.right_stick_x;
            
            magnitude = Math.hypot(Math.abs(gamepad1.left_stick_x), Math.abs(gamepad1.left_stick_y));
            
            if (gamepad1.right_bumper) {
                duckPower = 1;
            } else if (gamepad1.left_bumper) {
                duckPower = -1;
            } else
                duckPower = 0;
            
            if (magnitude > 1) {
                magnitude = 1;
            }
            
            angle = Math.toDegrees(Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y));
            
            if (gamepad1.left_stick_y == 0 && gamepad1.left_stick_x == 0) {
                angle = 0;
            }
            
            if (angle == 0) {
                frontRightPower = magnitude - magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontLeftPower = magnitude + magnitudeB;
                backLeftPower = magnitude + magnitudeB;
            }
            
            if (angle == 180) {
                frontRightPower = -magnitude - magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontLeftPower = -magnitude + magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
            }
            
            if (angle == 90) {
                frontRightPower = -magnitude - magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontLeftPower = magnitude + magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
            }
            
            if (angle == -90) {
                frontRightPower = magnitude - magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontLeftPower = -magnitude + magnitudeB;
                backLeftPower = magnitude + magnitudeB;
            }
            
            if (angle > -90 && angle < 0) {
                frontRightPower = magnitude - magnitudeB;
                backLeftPower = magnitude + magnitudeB;
                frontLeftPower = (0.0222 * angle + 1) * magnitude + magnitudeB;
                backRightPower = (0.0222 * angle + 1) * magnitude - magnitudeB;
            }
            
            if (angle > 0 && angle < 90) {
                frontLeftPower = magnitude + magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontRightPower = (-0.0222 * angle + 1) * magnitude - magnitudeB;
                backLeftPower = (-0.0222 * angle + 1) * magnitude + magnitudeB;
            }
            
            if (angle > 90 && angle < 180) {
                frontRightPower = -magnitude - magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
                frontLeftPower = (-0.0222 * angle + 3) * magnitude + magnitudeB;
                backRightPower = (-0.0222 * angle + 3) * magnitude - magnitudeB;
            }
            
            if (angle > -180 && angle < -90) {
                frontLeftPower = -magnitude + magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontRightPower = (0.0222 * angle + 3) * magnitude - magnitudeB;
                backLeftPower = (0.0222 * angle + 3) * magnitude + magnitudeB;
            }
            
            if (gamepad1.right_stick_button) {
                if (sprint == 1.5) {
                    sprint = 0.8;
                } else if (sprint == 0.8) {
                    sprint = 1.5;
                }
            }
            
            frontRight.setPower(frontRightPower * sprint);
            backRight.setPower(backRightPower * sprint);
            frontLeft.setPower(frontLeftPower * sprint);
            backLeft.setPower(backLeftPower * sprint);

            if (gamepad1.right_bumper == false && gamepad1.left_bumper == false) {
                duckPower = 0;
                duck.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }else if(gamepad1.right_bumper == true && gamepad1.left_bumper == false){
                duckPower = 0.46;
                duck.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }else if(gamepad1.right_bumper == false && gamepad1.left_bumper == true){
                duckPower = -.46;
                duck.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            
            duck.setPower(duckPower);
            
            
            
            if (magnitude == 0 && magnitudeB == 0) {
                frontRight.setPower(0);
                frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backRight.setPower(0);
                backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontLeft.setPower(0);
                frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backLeft.setPower(0);
                backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            
            telemetry.addData("Positions", "frontLeft (%.2f), frontRight (%.2f)", frontLeftPower, frontRightPower);
            telemetry.addData("Positions", "backLeft (%.2f), backRight (%.2f)", backLeftPower, backRightPower);
            telemetry.addData("magnitude", magnitude);
            telemetry.addData("angle", angle);
            telemetry.addData("StickY", gamepad1.left_stick_y);
            telemetry.addData("StickX", gamepad1.left_stick_x);
            telemetry.addData("Sprint", sprint);
            telemetry.update();
        }
    }
}
