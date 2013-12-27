#include <stdio.h>
#include <iostream>
#include "opencv2\imgproc\imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/nonfree/nonfree.hpp"
#include "Card.h"
#include "Player.h"
#include "CardB.h"
using namespace cv;
using namespace std;
const int OD=50;
int mini=8;
int maxi=30;
int three1=1000;
int three2=1000;
int three=1800;
int kernel;
int biel=1;





void Compare(Mat &img1,Mat &img2)
{
	int width=img1.cols;
	int channels=img1.channels();
	int height=img1.rows;
	long int red=0,green=0,blue=0;
	long int red2=0,green2=0,blue2=0;
	float fred=0,fgreen=0,fblue=0;
	int n=width*height;
	for(int y=0;y<height;y++)
	{
		for(int x=0;x<width;x++)
		{
			blue=img1.data[channels*(width*y + x)]-img2.data[channels*(width*y + x)];
			blue2+=blue*blue;
			green=img1.data[channels*(width*y + x) +1]-img2.data[channels*(width*y + x) +1];
			green2+=green*green;
			red=img1.data[channels*(width*y + x) +2]-img2.data[channels*(width*y + x) +2];
			red2+=red*red;
		}
	}
	fred=red2/(float)n;
	fgreen=green2/(float)n;
	fblue=blue2/(float)n;
	cout<<"Fred = "<<fred<<endl;
	cout<<"Fgreen = "<<fgreen<<endl;
	cout<<"Gblue = "<<fblue<<endl;
}



int findObject(Mat img,Mat base, int minHessian)
{
	//vector of keypoints	
	vector<cv::KeyPoint> keypointsO;
	vector<cv::KeyPoint> keypointsS;


	SurfFeatureDetector surf(minHessian);
	surf.detect(img,keypointsO);
	surf.detect(base,keypointsS);


	//-- Step 2: Calculate descriptors (feature vectors)
	SurfDescriptorExtractor extractor;

	Mat descriptors_object, descriptors_scene;

	extractor.compute( img, keypointsO, descriptors_object );
	extractor.compute( base, keypointsS, descriptors_scene );



	//-- Step 3: Matching descriptor vectors using FLANN matcher
	FlannBasedMatcher matcher;  
	//BFMatcher matcher(NORM_L1);
	std::vector< DMatch > matches;
	matcher.match( descriptors_object, descriptors_scene, matches );

	double max_dist = 0; double min_dist = 150;
	double dist;

	//Quick calculation of min and max distances between keypoints
	for(int i=0; i<descriptors_object.rows; i++)
	{
		dist = matches[i].distance;
		if( dist < min_dist ) min_dist = dist;
		if( dist > max_dist ) max_dist = dist;
	}

	/* cout << "-- Max Dist: " << max_dist << endl;
	cout << "-- Min Dist: " << min_dist << endl;*/

	vector< DMatch > good_matches;

	for(int i = 0; i < descriptors_object.rows; i++)
	{
		if( matches[i].distance < 3*min_dist) 
			good_matches.push_back( matches[i] );
	}



	return (int)good_matches.size();
}

Mat Obraz(Mat src,vector<Point> a)
{

	int maxX= std::max(a[0].x,std::max(a[1].x,std::max(a[2].x,a[3].x)));
	int maxY= std::max(a[0].y,std::max(a[1].y,std::max(a[2].y,a[3].y)));
	Mat temp;
	return temp;
}





int odleglos(Point a,Point b)
{
	return std::sqrtf(((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)));
}
void RemoveSquare(std::vector<std::vector<cv::Point>> &squares)
{
	bool flaga=false;
	while(flaga==false)
	{
		flaga=true;
		for(int i=0;i<squares.size();i++)
		{

			for(int j=i+1;j<squares.size();j++)
			{
				if((squares[i][0].x>=squares[j][0].x && squares[i][0].y>=squares[j][0].y) ||
					(squares[i][1].x>=squares[j][1].x && squares[i][1].y<=squares[j][1].y) ||
					(squares[i][2].x<=squares[j][2].x && squares[i][2].y<=squares[j][2].y) ||
					(squares[i][3].x<=squares[j][3].x && squares[i][3].y>=squares[j][3].y))
				{
					squares.erase(squares.begin()+i);
					flaga=false;
					break;
				}

			}
			if(flaga==false) break;
		}
	}
}
double angle( cv::Point pt1, cv::Point pt2, cv::Point pt0 ) {
	double dx1 = pt1.x - pt0.x;
	double dy1 = pt1.y - pt0.y;
	double dx2 = pt2.x - pt0.x;
	double dy2 = pt2.y - pt0.y;
	return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2));
}



void Wykryj_karty(Mat &grey_image,Mat &grey_base, int tresh,vector<Card> &karty,vector<CardB> &bkarty,bool first)
{
	Mat a,b;
	Mat diff;
	Mat dst;
	grey_base.copyTo(dst);
	vector<vector<Point> > contours;
	vector<vector<Point> > edge_pts;
	vector<Vec4i> hierarchy;
	grey_image.copyTo(diff);
	cvtColor(grey_image,a,CV_RGB2GRAY);
	cvtColor(grey_base,b,CV_RGB2GRAY);


	absdiff(a,b,diff);
	Canny(diff,diff,160,160);

	findContours( diff, contours,hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );
	// goodFeaturesToTrack(diff,
	//                      contours,
	//                      1000,//max corner
	//                      0.01,//quality
	//                      10,//minDistance,
	//                      Mat(),
	//                     3,// blockSize,
	//                     false,// useHarrisDetector,
	//                      0.04 );

	//TermCriteria criteria = TermCriteria( CV_TERMCRIT_EPS + CV_TERMCRIT_ITER, 40, 0.001 );
	//cornerSubPix(diff,contours,Size(5,5),Size(-1,-1),criteria);
	for(int i=0;i<contours.size();i++)
	{
		if(contours[i].size()>10) edge_pts.push_back(contours[i]);
		// if(contours[i].size()==0) break; //spróbuj bez tego
	}
	vector<vector<Point> >hull( edge_pts.size() );//tutaj zmiana
	for( int i = 0; i < edge_pts.size(); i++ )
	{  convexHull( Mat(edge_pts[i]), hull[i],true); }


	vector<vector<Point> > squares;
	vector<Point> approx;
	for (size_t i = 0; i < edge_pts.size(); i++)
	{
		approxPolyDP(Mat(edge_pts[i]), approx, arcLength(Mat(edge_pts[i]), true)*0.02, true);
		if (approx.size() == 4 &&
			fabs(contourArea(Mat(approx))) > 1000 &&
			isContourConvex(Mat(approx)))
		{
			double maxCosine = 0;

			for (int j = 2; j < 5; j++)
			{
				double cosine = fabs(angle(approx[j%4], approx[j-2], approx[j-1]));
				maxCosine = MAX(maxCosine, cosine);
			}

			if (maxCosine < 0.5)
				squares.push_back(approx);
		}
	}




	int tmp=0;
	for ( int i = 0; i<squares.size(); i++ ) 
	{
		if(squares[i][1].y>squares[i][3].y) swap(squares[i][1],squares[i][3]);
		if(Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			squares.erase(squares.begin()+i);
			i=-1;
		}

	}
	RemoveSquare(squares);
	for ( int i = 0; i<squares.size(); i++ ) 
	{

		//   cv::drawContours(dst, squares, i, cv::Scalar(255,0,0), 1, 8, std::vector<cv::Vec4i>(), 0, cv::Point());

		tmp=0;
		for(int j=0;j< karty.size();j++)
		{
			if(odleglos(squares[i][0],karty[j].a)>35 
				&& odleglos(squares[i][1],karty[j].b)>35
				&& odleglos(squares[i][2],karty[j].c)>35
				&& odleglos(squares[i][3],karty[j].d)>35) tmp++;
			else
			{
				karty[j].Update(squares[i][0],squares[i][1],squares[i][2],squares[i][3]);
			}
		}
		if(tmp==karty.size())
		{
			bool add=true;
			for(int k=0;k<karty.size();k++)
			{

				Card ab(squares[i][0],squares[i][1],squares[i][2],squares[i][3],true);
				if(karty[k].Check(ab)==true) {
					add=false;
					break;
				}
			}

			//if(add==true)

			karty.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3]));

		}
	}



	for(int i=0;i<karty.size();i++)
	{

		karty[i].Draw(grey_image,bkarty,first);


	}

	namedWindow("Hull demo", CV_WINDOW_AUTOSIZE );
	imshow( "Hull demo", grey_image);




}



int Card::ID=0;
int main( int argc, char** argv )
{
	Player player1("lukasz");
	Player player2("daniel");
	Mat img1=imread("Roting.jpg");
	int tab1[3]={0,0,0};
	int tab2[3]={0,0,0};

	int width=img1.cols;
	int channels=img1.channels();
	int height=img1.rows;
	int red1=0,blue1=0,green1=0;
	int red2=0,blue2=0,green2=0;
	for(int y=10;y<height/2-10;y++)
	{
		for(int x=0;x<width;x++)
		{
			blue1+=img1.data[channels*(width*y + x)];

			green1+=img1.data[channels*(width*y + x) +1];

			red1+=img1.data[channels*(width*y + x) +2];

		}
	}

	for(int y=height/2+10;y<height;y++)
	{
		for(int x=0;x<width;x++)
		{
			blue2+=img1.data[channels*(width*y + x)];

			green2+=img1.data[channels*(width*y + x) +1];

			red2+=img1.data[channels*(width*y + x) +2];

		}
	}

	cout<<"R="<<red2-red1<<endl;
cout<<"G="<<green2-green1<<endl;
cout<<"B="<<blue2-blue1<<endl;










	return 1;
	vector<CardB> bkarty;


	VideoCapture capture(0); 
	Mat frame;
	Mat l_frame;

	vector<Card> karty;

	char data[256];
	//namedWindow("Ustawienia",CV_WINDOW_AUTOSIZE);
	//createTrackbar("Threeshold1","Ustawienia",&three1,3000);
	//createTrackbar("Threeshold","Ustawienia",&three,3000);
	//createTrackbar("Min","Ustawienia",&mini,20);
	//createTrackbar("Maxi","Ustawienia",&maxi,50);

	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 13 );


	capture.read(frame);

	frame.copyTo(l_frame);

	while(1)
	{
		capture.read(frame);


		Wykryj_karty(frame,l_frame,three,karty,bkarty,false);


		if(waitKey(30)==97) { cout<<"Druga faza"<<endl; bkarty.clear();  }

		if(waitKey(30)==101) 
		{ 
			cout<<"Usuwam ostatnio dodana karte"<<endl;
			if(bkarty.size()!=0) 
			{
				bkarty.pop_back(); 	
				cout<<"Karta usunieta"<<endl;
			}
			else
				cout<<"Brak kart"<<endl;
		}
		if(waitKey(20)==32) 
		{
			frame.copyTo(l_frame);
			cout<<"Zaladowano ponownie klatke bazowa!"<<endl;
		} 
		if(waitKey(30)==27) break;

		player1.Draw();
		player2.Draw();
	}
	capture.release();
	waitKey(0);
	return 0;
}