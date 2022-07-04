//you can copy over the entire thing then change it to match this years game
package org.firstinspires.ftc.teamcode.drive.opmode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class contourPipeline extends OpenCvPipeline {
    int midpoint;

    @Override
    public Mat processFrame(Mat input) {
    
    //entire thing has to be in a try catch other wise if a contour is not found it will throw and error and stop running
        try {
            Mat end = input;

            Mat src = input;
            
            //Freight Frenzy CV goal- to detect a custom team shipping element and determine which tape mark it is at- the left, middle, or right


            //this is YCRCB - Scalar scalarLowerYCrCb = new Scalar(0.0, 0.0, 0.0);
            //this is YCRCB - Scalar scalarUpperYCrCb = new Scalar(255.0, 100.0, 170.0);
            
            //those are hard to figure out ^^^^^^^^^^^ but they are in the Y CR CB color space
            
            //forming a color range
            //pick a range of HSV colors that aren't too specific nor general- they should contain the color of object you are trying to find in the range


            Scalar scalarLowerHSV = new Scalar(30.0, 71.4, 40.0);//HSV for now
            Scalar scalarUpperHSV = new Scalar(80.0, 255.0, 255.0);//HSV for now
            
            //Those are the boundaries of the accepted colors in HSV- which can be found be searching up color picker and translating each one to openCV hsv
            
            //open cv- Hue goes to 179, the other two go to 255
            //google - Hue goes to 360, the other two are percentages out of 100%
            //translate google color picker for open cv and put those here, tune if needed
            
            
            //gets the image ready for contour detection
            

            //Converts space to HSV
            Imgproc.cvtColor(src, src,Imgproc.COLOR_BGR2HSV);
            //filters out all colors not in this range
            Core.inRange(src, scalarLowerHSV, scalarUpperHSV, src);
            // Remove Noise
            //choose one or the other or they cancel things out, I AM USING CLOSE
            Imgproc.morphologyEx(src, src, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(src, src, Imgproc.MORPH_CLOSE, new Mat());
            // GaussianBlur
            Imgproc.blur(src, src, new Size(10, 10));
            
            


            //Finding Contours

            ArrayList<MatOfPoint> contours = new ArrayList<>();

            Mat hierarchey = new Mat();
            
            //finds contour
            Imgproc.findContours(src, contours, hierarchey, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            
            //sorts through and finds largest one

            int largestIndex = 0;
            int largest = contours.get(0).toArray().length;


            for (int i = 0; i < contours.size(); i++) {
                int currentSize = contours.get(i).toArray().length;
                if (currentSize > largest) {

                    largest = currentSize;
                    largestIndex = i;
                }

            }


            //Draw rectangle on largest contours

            MatOfPoint2f areaPoints = new MatOfPoint2f(contours.get(largestIndex).toArray());

            Rect rect = Imgproc.boundingRect(areaPoints);

            Imgproc.rectangle(end, rect, new Scalar(255, 0, 0));
            

            midpoint = (rect.x + (rect.x + rect.width)) / 2;


            int middleBound = 600;

            int rightBound = 1300;


            Imgproc.line(end, new Point(middleBound, 1920), new Point(middleBound, 0), new Scalar(255, 0, 0));

            Imgproc.line(end, new Point(rightBound, 1920), new Point(rightBound, 0), new Scalar(255, 0, 0));

            return end;
        }catch(IndexOutOfBoundsException e){
            int one = 1;

        }

        return input;

    }


    public int getMidpoint(){
        return midpoint;
    }


    public int getLocation(){
        //change these values when needed
        if(midpoint>1300){
            return 2;
        }else if(midpoint>600){
            return 1;
        }else{
            return 0;
        }


    }
}
