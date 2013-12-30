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
#include "aruco.h"
#include "marker.h"
#include "cvdrawingutils.h"
#include "SFML\Network.hpp"
#include <fstream>
#include "Game.h"
using namespace cv;
using namespace sf;
using namespace std;
using namespace aruco;
const int OD=50;
int mini=8;
int maxi=30;
int three1=1000;
int three2=1000;
int three=1800;
int kernel;
int biel=1;
UdpSocket soc;
IpAddress client;
int port=54000;




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
		for(unsigned int i=0;i<squares.size();i++)
		{

			for(unsigned int j=i+1;j<squares.size();j++)
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



void Wykryj_karty(Mat &grey_image,Mat &grey_base, int tresh,vector<Card> &karty,vector<CardB> &bkarty,bool first,Game &game)
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
	for(unsigned int i=0;i<contours.size();i++)
	{
		if(contours[i].size()>10) edge_pts.push_back(contours[i]);
	}
	vector<vector<Point> >hull( edge_pts.size() );//tutaj zmiana
	for(unsigned int i = 0; i < edge_pts.size(); i++ )
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
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		if(squares[i][1].y>squares[i][3].y) swap(squares[i][1],squares[i][3]);
		if(Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			squares.erase(squares.begin()+i);
			i=-1;
		}

	}
	RemoveSquare(squares);//Ÿle napisane
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		Card::Prepare(squares[i],grey_image);

		tmp=0;
		for(unsigned int j=0;j< karty.size();j++)
		{
			if(odleglos(karty[j].getCenter(),Card::getCenter(squares[i][0],squares[i][1],squares[i][2],squares[i][3]))>50) tmp++;
			else
			{
				karty[j].Update(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game);
			}
		}
		if(tmp==karty.size())
		{
			bool add=true;
			for(unsigned int k=0;k<karty.size();k++)
			{

				Card ab(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game);
				if(karty[k].Check(ab)==true) {
					add=false;
					break;
				}
			}

			char cad[100];
			sprintf(cad,"Karta: %d %d %d %d P",squares[i][0].x,squares[i][1].x,squares[i][2].x,squares[i][3].x);
			karty.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game));
			if (soc.send(cad,strlen(cad)*sizeof(char) , client, port-10) != sf::Socket::Done)
			{
				cout<<"Blad podczas wysylania danych"<<endl;
			}
			else
			{
				cout<<"Wysylam dane karty"<<endl;
			}
		}
	}



	for(unsigned int i=0;i<karty.size();i++)
	{

		karty[i].Draw(grey_image,bkarty);
	}

	imshow("Podglad", grey_image);
}

int Card::ID=0;
int main( int argc, char** argv )
{
	Game game("lukasz",1,"daniel",2);
	MarkerDetector MDetector;
	vector<Marker> markers;
	vector<CardB> bkarty;

	fstream plik("cards.txt", ios::in );
	string dane;
	while(!plik.eof())
	{
		int id=0,t=0,cost=0;
		char name[10];
		string name1;
		char src[10];
		getline(plik,dane);
		sscanf(dane.c_str(),"%d %s %d %d",&id,name,&t,&cost);
		name1=name;
	Type tt;
	if(t==0) tt=CREATURE;
	else tt=LAND;
		Mat a=imread("C:/umk/"+name1+".jpg");
		if(!a.data) cout<<"ADS"<<endl;
	//	imshow("A",a);
		bkarty.push_back(CardB(a,id,name,Red,tt));
	//cout<<"Karta: "<<id<<" "<<name1<<" "<<t<<" "<<cost<<"|"<<endl;
		

	}
	cout<<"Koniec"<<endl;

	client =IpAddress::getLocalAddress();

	if(soc.bind(port) !=Socket::Done)
	{
		cout<<"Blad podczas tworzenia socketa"<<endl;
		return 1;
	}
	else
	{
		cout<<"Utworzono serwer UDP na porcie "<<port<<endl;
	}




	

	VideoCapture capture(0); 
	Mat frame;
	Mat l_frame;

	vector<Card> karty;

	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 13 );


	capture.read(frame);

	frame.copyTo(l_frame);

	while(1)
	{
		markers.clear();
		capture.read(frame);

		MDetector.detect(frame,markers);
		for(int i=0;i<markers.size();i++) 
			{
				markers[i].draw(frame,Scalar(100,100,100));
				if(game.player1.markerId==markers[i].id) game.aPlayer=markers[i].id;
				if(game.player2.markerId==markers[i].id) game.aPlayer=markers[i].id;
		}

		Wykryj_karty(frame,l_frame,three,karty,bkarty,false,game);
		if(cv::waitKey(30)==97) { cout<<"Druga faza"<<endl; bkarty.clear();  }

		if(cv::waitKey(30)==101) 
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
		if(cv::waitKey(20)==32) 
		{
			frame.copyTo(l_frame);
			cout<<"Zaladowano ponownie klatke bazowa!"<<endl;
		} 
		if(cv::waitKey(30)==27) break;

		//	player1.Draw();
		//	player2.Draw();
	}
	capture.release();
	cv::waitKey(0);
	return 0;
}