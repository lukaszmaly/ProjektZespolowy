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

#include <fstream>
#include "Game.h"
#define ACTION 428
using namespace aruco;
using namespace cv;

using namespace std;


int port=54000;


int three=80;

int zmienna=40;
int white=2800;

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


void Wykryj_karty(Mat &grey_image, int tresh,vector<Card> &karty,vector<CardB> &bkarty,Game &game)
{
	Mat diff;
	vector<vector<Point> > contours;
	vector<vector<Point> > edge_pts;
		vector<Card> kartyTemp;
	vector<Vec4i> hierarchy;
	cvtColor(grey_image,diff,CV_RGB2GRAY);
	Canny(diff,diff,tresh,tresh);
	findContours( diff, contours,hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );

	for(unsigned int i=0;i<contours.size();i++)
	{
		if(contours[i].size()>5) edge_pts.push_back(contours[i]);
	}

	vector<vector<Point> >hull( edge_pts.size() );

	for(unsigned int i=0; i<edge_pts.size();i++)
	{  
		convexHull(Mat(edge_pts[i]),hull[i],true); 
	}

	vector<vector<Point>> squares;
	vector<Point> approx;
	for (size_t i = 0; i < edge_pts.size(); i++)
	{
		approxPolyDP(Mat(edge_pts[i]), approx, arcLength(Mat(edge_pts[i]), true)*0.02, true);

		if (approx.size() == 4 && fabs(contourArea(Mat(approx)))>15500)
		{
			double maxCosine = 0;
			for (int j=2; j<5; j++)
			{
				double cosine = fabs(angle(approx[j%4], approx[j-2], approx[j-1]));
				maxCosine = MAX(maxCosine, cosine);
			}
			//if(maxCosine>=zmienna/(double)10)
			squares.push_back(approx);
		}
	}




	//sprawdzenie czy wykryte krawedzie sa rzeczywiscie kartami


	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		if(squares[i][1].y>squares[i][3].y) swap(squares[i][1],squares[i][3]);
		if(Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			//squares.erase(squares.begin()+i);
			//i=-1;
			//continue;
		}

	}

	for(unsigned int i=0;i<squares.size();i++)
	{
		drawContours(diff,squares,i,Scalar(200,200,200),2);
	Card::Prepare(squares[i],grey_image);

	}
	imshow("Kon",diff);


	//tworzenie tymczaswego wektora z wykrytymi kartami na stole
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		kartyTemp.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game,true));
	}

	//	cout<<"Ilosc kart tymczasowych: "<<kartyTemp.size()<<endl;

	//aktualizacja kart "ktore powinny znajdowac siê na stole"


	for (unsigned int i = 0; i<kartyTemp.size(); i++) 
	{
		for(unsigned int j=0; j<kartyTemp.size(); j++)
		{
			if(i!=j && odleglos(kartyTemp[i].getCenter(),kartyTemp[j].getCenter())<30)
			{
				kartyTemp.erase(kartyTemp.begin()+j);
				j=-1;
				i=-1;
				break;
			}
		}
	}

	for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		int index=-1;
		int minn=90000;
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
		}
			if(index!=-1)
			{
				kartyTemp[index].fresh=true;
				karty[i].Update(kartyTemp[index].a,kartyTemp[index].b,kartyTemp[index].c,kartyTemp[index].d,grey_image,bkarty,game,false);
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

	///poprawka
			for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		for(unsigned int j=0;j<karty.size();j++)
		{
			if(i!=j && odleglos(karty[i].getCenter(),karty[j].getCenter())<30)
			{
				karty.erase(karty.begin()+j);
				j=-1;
				i=-1;
				break;
			}
		}
	}


			cout<<"Ilosc kart:"<<karty.size()<<endl;


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
	MarkerDetector MDetector;
	vector<Marker> markers;
	Point action(-1,-1);

	namedWindow("Ustawienia",CV_WINDOW_NORMAL);
	createTrackbar("Threeshold","Ustawienia",&three,200);
	createTrackbar("Zmienna ","Ustawienia",&zmienna,100);
	createTrackbar("Balans bieli ","Ustawienia",&white,10000);



	Game game("lukasz",985,"daniel",838,"25.172.199.151",6121);

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

	VideoCapture capture(0); 
	Mat frame;
	vector<Card> karty;
	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 13 );


	capture.read(frame);

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

		Wykryj_karty(frame,three,karty,bkarty,game);

		game.Draw();
		if(cv::waitKey(30)==27) break;
	}
	capture.release();
	cv::waitKey(0);
	return 0;
}