package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import java.util.ArrayList;
import java.util.List;

@Autonomous

public class AutoFunctions extends LinearOpMode{

    // todo: write your code here
    private DcMotor fr;
    private DcMotor br;
    private DcMotor fl;
    private DcMotor bl;
    private ColorSensor M;
    private ColorSensor lineCSensor;
    private BNO055IMU imu1;

    int startPos = -1;
    int endPos = 0;
    int redLines = 0;
    int lines = 0;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Single";
    private static final String LABEL_SECOND_ELEMENT = "Quad";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final String VUFORIA_KEY =
    "AWdhXNj/////AAABmRSQQCEQY0Z+t33w9GIgzFpsCMHl909n/+kfa54XDdq6fPjSi/8sBVItFQ/J/d5SoF48FrZl4Nz1zeCrwudfhFr4bfWTfh5oiLwKepThOhOYHf8V/GemTPe0+igXEu4VhznKcr3Bm5DiLe2b6zBVzvWFDWEHI/mkhLxRkU+llmwvitwodynP2arFgZ43thde9GJPCBFne/q6tPXeeN8/PoTUOtycTrnTkL6fBuHelMMnvN2RjqnMJ9SBUcaVX8DsWukq1fDr29O8bguAJU5JKxt9E3+XXiexpE/EJ9jxJc7YoMtpxfMro/e0sm9gRNckw4uPtZHnaoDjFhaK9t2D7kQQc3rwgK1OEZlY7FGQyy8g";

@Override
public void runOpMode()
{

    //Expansion_Hub_1 = hardwareMap.get(Blinker.class, "Expansion Hub 1");
    imu1 = hardwareMap.get(BNO055IMU.class, "IMU1");
    fl = hardwareMap.get(DcMotor.class, "fr");
    bl = hardwareMap.get(DcMotor.class, "br");
    fr = hardwareMap.get(DcMotor.class, "fl");
    br = hardwareMap.get(DcMotor.class, "bl");
    M = hardwareMap.get(ColorSensor.class, "M");
    lineCSensor = hardwareMap.get(ColorSensor.class,"lineColor");
    
	//init all of the motors and set their directions
    fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
	
	
    fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    IMUInit();
    initVuforia();
    initTfod();
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    Grabber.setPosition(0.75);
    fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    
    //shooter.setPower(1);
    String label;
    List < Recognition > updatedRecognitions = tfod.getUpdatedRecognitions();
    // Iterate through list and call a function to
    // display info for each recognized object.
    if(tfod == null){
        endPos = -3;
    }
    for (Recognition recognition : updatedRecognitions) {
        // Display info.
        label = recognition.getLabel();
        if (label.equals("Quad")) {
            endPos = 2;
        }
        else if (label.equals("Single")) {
            endPos = 1;
        }else{
            endPos = 0;
        }
    }
    //display that the robot is initilized & ready to begin the round
    telemetry.addData("target updated ", endPos);
    telemetry.update();
    switch (endPos) {
        case 0: {
            break;
        }
        case 1: {
            
            break;
        }
        case 2: {
            
            break;
        }
        case -2:{
            
            break;
        }
    }
    //turnIMU(90,1);
}
  
  public void ColorSensorExample()
{
            fr.setPower(-.8);
            br.setPower(.8);
            fl.setPower(.8);
            bl.setPower(-.8);
            while (lineCSensor.red() < 450 && opModeIsActive()) {
        if (lineCSensor.red() > 450) {
            fr.setPower(0);
            br.setPower(0);
            fl.setPower(0);
            bl.setPower(0);
        }
        fr.setPower(-.8);
        br.setPower(-.8);
        fl.setPower(-.8);
        bl.setPower(-.8);
         while (lineCSensor.red() < 450 && lineCSensor.green() < 450 && lineCSensor.blue() < 450 && opModeIsActive()) {
        if (lineCSensor.red() > 450 && lineCSensor.green() > 450 && lineCSensor.blue()>450) {
            fr.setPower(0);
            br.setPower(0);
            fl.setPower(0);
            bl.setPower(0);
        }
    }
}}

  private void initVuforia() {
    /*
     * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
     */
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

    parameters.vuforiaLicenseKey = VUFORIA_KEY;
    parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

    //  Instantiate the Vuforia engine
    vuforia = ClassFactory.getInstance().createVuforia(parameters);
    
    // Loading trackables is not necessary for the TensorFlow Object Detection engine.
}

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
    int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
        "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
    tfodParameters.minimumConfidence = 0.8;
    tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
    tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
}}
