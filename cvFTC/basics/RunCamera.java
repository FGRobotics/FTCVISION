package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.opmode.ConceptCV;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.io.IOException;

@Autonomous(name="CVtester",group="Linear OpMode")
public class RunCamera extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        //initializes webcam

        OpenCvWebcam webcam;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        //OpenCV Pipeline

        ConceptCV myPipeline = new ConceptCV();
        webcam.setPipeline(myPipeline);

        //opens an openCv webcam that streams to the openCV pipeline

        OpenCvWebcam finalWebcam = webcam;
        finalWebcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                //sets a camera with width 1920 and height 1080 that is UPRIGHT
                finalWebcam.startStreaming(1920 ,1080, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("Initialization passed ", 1);
                telemetry.update();
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Init Failed ", errorCode);
                telemetry.update();

            }



        });

        int location = 0;

        //red duck side 150,350,680,880,1380,1580,600,900
        // red barrier side 440, 640, 1080, 1280, 1680, 1880, 650, 950

        //configures bounding boxes


        myPipeline.configureRects(150,350,680,880,1380,1580,600,900);


        //ConceptCV.configureRects(440, 640, 1080, 1280, 1680, 1880, 650, 950);









        //location of 0 = left, 1 = middle, 2 is right


        waitForStart();


        while(opModeIsActive()) {

            //returns location of TSE
            location = myPipeline.findTSE();



            telemetry.addData("Location: ",location);

            telemetry.update();



            finalWebcam.stopStreaming();


        }



    }
}
