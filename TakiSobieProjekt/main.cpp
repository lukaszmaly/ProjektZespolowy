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
#define ACTION 428
using namespace cv;
using namespace sf;
using namespace std;
using namespace aruco;
UdpSocket soc;
int three=1800;
IpAddress client;
int port=54000;

int odleglos(Point a,Point b)
{
	return std::sqrtf(((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)));
}

double angle( cv::Point pt1, cv::Point pt2, cv::Point pt0 ) {
	double dx1 = pt1.x - pt0.x;
	double dy1 = pt1.y - pt0.y;
	double dx2 = pt2.x - pt0.x;
	double dy2 = pt2.y - pt0.y;
	return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2));
}


void Wykryj_karty(Mat &grey_image,Mat &grey_base, int tresh,vector<Card> &karty,vector<CardB> &bkarty,Game &game)
{
	Mat a,b;
	Mat diff;

	vector<vector<Point> > contours;
	vector<vector<Point> > edge_pts;
	vector<Vec4i> hierarchy;
	//grey_image.copyTo(diff);
	cvtColor(grey_image,a,CV_RGB2GRAY);
	cvtColor(grey_base,b,CV_RGB2GRAY);
	absdiff(a,b,diff);
	Canny(diff,diff,160,160);
	findContours( diff, contours,hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );

	for(unsigned int i=0;i<contours.size();i++)
	{
		if(contours[i].size()>5) edge_pts.push_back(contours[i]);
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
			fabs(contourArea(Mat(approx))) > 10 )
		{
			double maxCosine = 0;

			for (int j = 2; j < 5; j++)
			{
				double cosine = fabs(angle(approx[j%4], approx[j-2], approx[j-1]));
				maxCosine = MAX(maxCosine, cosine);
			}

			if(maxCosine<=0.4)
				squares.push_back(approx);
		}
	}

	int tmp=0;
	vector<Card> kartyTemp;
	
	//sprawdzenie czy wykryte krawedzie sa rzeczywiscie kartami
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		if(Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			squares.erase(squares.begin()+i);
			i=-1;
			continue;
		}
		if(squares[i][1].y>squares[i][3].y) swap(squares[i][1],squares[i][3]);
	}

	//tworzenie tymczaswego wektora z wykrytymi kartami na stole
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		kartyTemp.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game,true));
	}

	//aktualizacja kart "ktore powinny znajdowac siê na stole"
	for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		karty[i].fresh=false;
		int index=-1;
		int minn=10000;
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
			if(karty[i].cardBase.id==kartyTemp[j].cardBase.id && kartyTemp[j].fresh==false)
			{
				if(odleglos(karty[i].getCenter(),kartyTemp[j].getCenter())<minn)
				{
					minn=odleglos(karty[i].getCenter(),kartyTemp[j].getCenter());
					index=j;
				}
			}
			if(index!=-1)
			{
				kartyTemp[j].fresh=true;
				karty[i].Update(kartyTemp[index].a,kartyTemp[index].b,kartyTemp[index].c,kartyTemp[index].d,grey_image,bkarty,game,false);
			}
		}
	}

	//dodanie nowych kart, które mog³y siê pojawiæ
	for(unsigned int i=0;i<kartyTemp.size();i++)
	{
		if(kartyTemp[i].fresh==false)
		{
			karty.push_back(kartyTemp[i]);
		}
	}



	if(game.phase==ATAK || game.phase==OBRONA)
	{
		for (unsigned int i = 0; i<squares.size(); i++ ) 
		{
			Card::Prepare(squares[i],grey_image);
			int index=-1;
			int minn=10000;
			for(unsigned int j=0;j< karty.size();j++)
			{
				if(odleglos(karty[j].getCenter(),Card::getCenter(squares[i][0],squares[i][1],squares[i][2],squares[i][3]))<minn)
				{
					minn=odleglos(karty[j].getCenter(),Card::getCenter(squares[i][0],squares[i][1],squares[i][2],squares[i][3]));
					index=j;
				}
			}
			if(index!=-1)
			{
				karty[index].Update(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game,false);
				if(odleglos(karty[index].getCenter(),karty[index].old)>50) {karty[index].attack==true; }
			}
		}

		if(game.phase==OBRONA)
		{
			for (unsigned int i = 0; i<karty.size(); i++ ) 
			{
				if(karty[i].attack==false) continue;
				if(!(karty[i].owner==game.getCurrentPlayer())) continue;
				int index=-1;
				int minn=10000;
				for(unsigned int j=i+1;j< karty.size();j++)
				{
					if(karty[j].attack==false) continue;
					if(karty[j].owner==game.getCurrentPlayer()) continue;
					if(odleglos(karty[j].getCenter(),karty[i].getCenter())<minn)
					{
						minn=odleglos(karty[j].getCenter(),karty[i].getCenter());
						index=j;
					}
				}
				if(index!=-1)
				{
					karty[i].enemy=karty[index].getCenter();
				}

			}
		}


	}

	//wyswietlenie wszytkich kart
	for(unsigned int i=0;i<karty.size();i++)
	{
		karty[i].Draw(grey_image,bkarty,game);
	}

	imshow("Podglad", grey_image);
}

int Card::ID=0;
int main( int argc, char** argv )
{
	Point action(-1,-1);
	Game game("lukasz",985,"daniel",838);
	MarkerDetector MDetector;
	vector<Marker> markers;
	vector<CardB> bkarty;
	cout<<"Wczytywanie kart"<<endl;
	fstream plik("cards.txt", ios::in );
	string dane;
	while(!plik.eof())
	{
		int id=0,t=0,cost=0,att,def;
		char name[10];
		string name1;
		char src[10];
		getline(plik,dane);
		sscanf(dane.c_str(),"%d %s %d %d %d %d",&id,name,&t,&cost,&att,&def);
		name1=name;
		Type tt;
		if(t==0) tt=CREATURE;
		else tt=LAND;
		Mat a=imread("C:/umk/"+name1+".jpg");
		if(!a.data) {cout<<"Nie znaleziono karty "<< name<<".jpg"<<endl; continue;}
		bkarty.push_back(CardB(a,id,name,Red,tt,att,def,cost));
	}
	cout<<"Wczytalem karty"<<endl;

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
			if(markers[i].id==ACTION)
			{
				if(action.x==-1) 
				{ 
					action=markers[i].getCenter();
				}
				else
				{
					if(odleglos(action,markers[i].getCenter())>=100)
					{
						action=markers[i].getCenter();
						game.nextPhase();
						if(game.phase==ATAK)
						{
							for(int j=0;j<karty.size();j++)
							{
								if(karty[j].owner==game.getCurrentPlayer())
								{
									karty[j].prepareToAttack();
								}
							}
						}
						else if(game.phase==OBRONA)
						{
							for(int j=0;j<karty.size();j++)
							{
								if(!(karty[j].owner==game.getCurrentPlayer()))
								{
									karty[j].prepareToBlock();
								}
							}
						}
						else if(game.phase==WYMIANA)
						{
							for(int j=0;j<karty.size();j++)
							{
								karty[j].Clear();
							}
						}
					}
				}
			}

			if(game.player1.markerId==markers[i].id && game.aPlayer!=game.player1.markerId) {game.aPlayer=markers[i].id; cout<<"Zmiana gracza"<<endl; break;}
			if(game.player2.markerId==markers[i].id && game.aPlayer!=game.player2.markerId) {game.aPlayer=markers[i].id; cout<<"Zmiana gracza"<<endl;break;}
		}


		Wykryj_karty(frame,l_frame,three,karty,bkarty,game);


		game.Draw();


		if(cv::waitKey(20)==32) //spacja
		{
			frame.copyTo(l_frame);
			cout<<"Zaladowano ponownie klatke bazowa!"<<endl;
		} 

		if(cv::waitKey(30)==27) break;

	}
	capture.release();
	cv::waitKey(0);
	return 0;
}