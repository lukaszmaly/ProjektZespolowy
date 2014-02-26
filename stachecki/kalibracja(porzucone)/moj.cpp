#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <stdio.h>
#include <time.h>
#include <vector>

#define VIDEO_WINDOW   "Webcam"
#define CORNER_EIG     "Eigenvalue Corner Detection"
// Disable harris processing
//#define CORNER_HARRIS  "Corner Detection (Harris)"

#define USEC_PER_SEC 1000000L

CvScalar target_color[4] = { // in BGR order 
		{{   0,   0, 255,   0 }},  // red
		{{   0, 255,   0,   0 }},  // green
		{{ 255,   0,   0,   0 }},  // blue
		{{   0, 255, 255,   0 }}   // yellow
};

using namespace std;

// A Simple Camera Capture Framework
int main(int argc, char *argv[]) {
	long harris_time;
	long eig_time;

	CvCapture* capture = 0;
	IplImage* curr_frame = 0; // current video frame
	IplImage* gray_frame = 0; // grayscale version of current frame
	int w, h; // video frame size
	IplImage* eig_image = 0;
	IplImage* temp_image = 0;

	
	// Capture from a webcam
	capture = cvCaptureFromCAM(CV_CAP_ANY);
	//capture = cvCaptureFromCAM(0); // capture from video device #0
	if ( !capture) {
		fprintf(stderr, "ERROR: capture is NULL... Exiting\n");
		//getchar();
		return -1;
	}
	
	// Create a window in which the captured images will be presented
	cvNamedWindow(VIDEO_WINDOW, 0); // allow the window to be resized
	
	cvNamedWindow(CORNER_EIG, 0); // allow the window to be resized
	cvMoveWindow(CORNER_EIG, 330, 0);

	
	// Show the image captured from the camera in the window and repeat
	while (true) {
		
		// Get one frame
		curr_frame = cvQueryFrame(capture);
		if ( !curr_frame) {
			fprintf(stderr, "ERROR: frame is null... Exiting\n");
			//getchar();
			break;
		}
		// Do not release the frame!

		// Get frame size
		w = curr_frame->width;
		h = curr_frame->height;

		// Convert the frame image to grayscale
		if( ! gray_frame ) {
			//fprintf(stderr, "Allocate gray_frame\n");
			int channels = 1;
			gray_frame = cvCreateImage(
					cvGetSize(curr_frame), 
					IPL_DEPTH_8U, channels);
		}
		cvCvtColor(curr_frame, gray_frame, CV_BGR2GRAY);
		
		// ==== Allocate memory for corner arrays ====
		if ( !eig_image) {
			//fprintf(stderr, "Allocate eig_image\n");
			eig_image = cvCreateImage(cvSize(w, h),
					IPL_DEPTH_32F, 1);
		}
		if ( !temp_image) {
			//fprintf(stderr, "Allocate temp_image\n");
			temp_image = cvCreateImage(cvSize(w, h),
					IPL_DEPTH_32F, 1);
		}

		
		const int MAX_CORNERS = 100;
		CvPoint2D32f corners[MAX_CORNERS] = {0};
		int corner_count = MAX_CORNERS;
		double quality_level = 0.1;
		double min_distance = 5;
		int eig_block_size = 3;
		int use_harris = false;
		
		cvGoodFeaturesToTrack(gray_frame,
				eig_image,                    // output
				temp_image,
				corners,
				&corner_count,
				quality_level,
				min_distance,
				NULL,
				eig_block_size,
				use_harris);
		cvScale(eig_image, eig_image, 100, 0.00);
	
		cvShowImage(CORNER_EIG, eig_image);
		

		for( int i = 0; i < corner_count; i++) {
			int radius = h/25;
			cvCircle(curr_frame,
					cvPoint((int)(corners[i].x + 0.5f),(int)(corners[i].y + 0.5f)),
					radius,
					target_color[0]);
		}
		cvShowImage(VIDEO_WINDOW, curr_frame);

		// If ESC key pressed, Key=0x10001B under OpenCV 0.9.7(linux version),
		// remove higher bits using AND operator
		if ( (cvWaitKey(10) & 255) == 27)
			break;
	}

	// Release the capture device housekeeping
	cvReleaseCapture( &capture);
	cvDestroyWindow(VIDEO_WINDOW);
	cvDestroyWindow(CORNER_EIG);
// Disable harris processing
//	cvDestroyWindow(CORNER_HARRIS);
	return 0;
}
