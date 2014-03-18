/*
Znane błędy:
-jeśli karta jest idealnie równolegle(abs(a.y-d.y)~=0) to wystepuje problem z wyznaczeniem orientacji karty
położenie problemu: metoda Card::Valid();
(dokonałem poprawki, ale nie mam na 100% pewnosci że problem zniknął, dlatego zostawiam ten punkt)

-jeśli są 2 karty tego samego rodzaju, i jeśli 1 z nich jest tapniety to wystepują błędy z duplikacją(POWAŻNE)

*/

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
#include "ScriptsManager.h"

using namespace aruco;
#define SAFEREGIONX 100
#define SAFEREGIONY 100
#define SAFEREGIONWIDTH 300
#define SAFEREGIONHEIGHT 600
using namespace cv;

using namespace std;


int port=54000;

int c=0;
int three=100;
//int maxArea= 21;
int maxArea = 51;
int minArea=20;
//int minArea=10;
int zmienna=21;
int white=2800;
int gracz=0;
int faza=0;
bool oneAttack=false;



bool IsInRectFast(Point p,Point a = Point(SAFEREGIONX,SAFEREGIONY) ,Point c = Point(SAFEREGIONX+SAFEREGIONWIDTH,SAFEREGIONHEIGHT+SAFEREGIONHEIGHT))
{
	if(p.x<a.x || p.x>c.x || p.y<a.y || p.y>c.y) return false;
	return true;

}

void DrawSafeRegion(Mat &img)
{
	rectangle(img,Point(SAFEREGIONX,SAFEREGIONY),Point(SAFEREGIONX+SAFEREGIONWIDTH,SAFEREGIONHEIGHT),Scalar(0,0,255));
}

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


void Wykryj_karty(Mat &grey_image, int tresh,vector<Card> &karty,vector<Card>&stos,vector<CardB> &bkarty,Game &game)
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

		if (approx.size() == 4 && fabs(contourArea(Mat(approx)))>minArea*1000 && fabs(contourArea(Mat(approx)))<maxArea*1000)
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

	//Poprawa rogów kart
	for(unsigned int i=0;i<squares.size();i++)
	{
		drawContours(diff,squares,i,Scalar(200,200,200),2);
		Card::Prepare(squares[i],grey_image);
	}

	imshow("Kontury",diff);
	for(unsigned int i=0;i<squares.size();i++)
	{
		if(game.CheckCardsProp()==true && Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			squares.erase(squares.begin()+i);
			--i;
		}
	}

	//tworzenie tymczaswego wektora z wykrytymi kartami na stole
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		kartyTemp.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3],grey_image,bkarty,game,true));
	}

	//redukcja powtórzeń
	for (unsigned int i = 0; i<kartyTemp.size(); i++) 
	{
		for(unsigned int j=0; j<kartyTemp.size(); j++)
		{
			if(i!=j && odleglos(kartyTemp[i].getCenter(),kartyTemp[j].getCenter())<40)
			{
				kartyTemp.erase(kartyTemp.begin()+j);
				//i=-1;
				--i;
				break;
			}
		}
	}

	///Sprawdzanie czy są karty na stosie
	///BLAD NAPRAW GO .BLAD WAZNY
	///mozna naprawić poprzez poprawienie warunku. nie szukaj najblizszego sasiada, tylko porównaj odległości między środkami
	for (unsigned int i = 0; i<stos.size(); i++ ) 
	{
		int index=-1;
		int minn=90000;
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
			if(IsInRectFast(kartyTemp[j].getCenter())==true)//&& stos[i].cardBase.id==kartyTemp[j].cardBase.id
			{
				if(odleglos(stos[i].getCenter(),kartyTemp[j].getCenter())<minn)
				{
					minn=odleglos(stos[i].getCenter(),kartyTemp[j].getCenter());
					index=j;
				}
			}
		}
		if(index!=-1)
		{
			stos[i].Update(kartyTemp[index].a,kartyTemp[index].b,kartyTemp[index].c,kartyTemp[index].d,grey_image,bkarty,game,false);
	kartyTemp.erase(kartyTemp.begin()+index);
		}
	}


	char cad5[100];
	sprintf(cad5,"Kart tymczasowych: %d",kartyTemp.size());


	//dodanie nowych kart do stosu
	for(unsigned int i=0;i<kartyTemp.size();i++)
	{
		if(IsInRectFast(kartyTemp[i].getCenter())==true) 
		{
			kartyTemp[i].owner=game.getCurrentPlayer();
			stos.push_back(kartyTemp[i]);
			kartyTemp.erase(kartyTemp.begin()+i);
			//i=-1
			--i;
		}
	}


	///redukcja duplikatów
	for (unsigned int i = 0; i<stos.size(); i++ ) 
	{
		for(unsigned int j=0;j<stos.size();j++)
		{
			if(i!=j && odleglos(stos[i].getCenter(),stos[j].getCenter())<30)
			{
				stos.erase(stos.begin()+max(j,i));
				j=-1;
				//i=-1;
				--i;
				break;
			}
		}
	}

	


	///Tutaj mamy pewność że w bezpiecznej strefie nie znajdują się karty ze stosu

	for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		int index=-1;
		int minn=90000;
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
			if(karty[i].cardBase.id==kartyTemp[j].cardBase.id)
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
			karty[i].Update(kartyTemp[index].a,kartyTemp[index].b,kartyTemp[index].c,kartyTemp[index].d,grey_image,bkarty,game,false);

			kartyTemp.erase(kartyTemp.begin()+index);	
		}
	}



	//dodanie nowych kart, które mogły się pojawić
	//dodają się tylko landy
	for(unsigned int i=0;i<kartyTemp.size();i++)
	{
		if(kartyTemp[i].cardBase.type==LAND)
		{
			kartyTemp[i].Unlock();
			karty.push_back(kartyTemp[i]);
			game.server.SendNewCard(kartyTemp[i].id,kartyTemp[i].cardBase.id,game.GetPlayer(kartyTemp[i].owner),kartyTemp[i].a,kartyTemp[i].b,kartyTemp[i].c,kartyTemp[i].d,kartyTemp[i].taped);
			kartyTemp.erase(kartyTemp.begin()+i);
			//i=-1;
			--i;
		}
	}

	for(unsigned int i=0;i<stos.size();i++)
	{
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
			
			//if(stos[i].cardBase.id==kartyTemp[j].cardBase.id && kartyTemp[j].getCenter().x<SAFEREGION) 
			if(stos[i].owner.mana>=stos[i].cardBase.koszt && stos[i].cardBase.id==kartyTemp[j].cardBase.id && IsInRectFast(kartyTemp[j].getCenter())==false) 
			{
				stos[i].owner.mana-=stos[i].cardBase.koszt;
				game.server.SubMana(game.GetPlayer(stos[i].owner),stos[i].cardBase.koszt);
				kartyTemp[j].Unlock();
				stos[i].id=kartyTemp[j].id;
				game.server.SendNewCard(kartyTemp[j].id,kartyTemp[j].cardBase.id,game.GetPlayer(kartyTemp[j].owner),kartyTemp[j].a,kartyTemp[j].b,kartyTemp[j].c,kartyTemp[j].d,kartyTemp[j].taped);
				karty.push_back(stos[i]);
				stos.erase(stos.begin()+i);
				kartyTemp.erase(kartyTemp.begin()+j);
				//i=j=-1;
				--i;
				j=-1;
				break;


			}
		}
	}

	///redukcja duplikatów
	for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		for(unsigned int j=0;j<karty.size();j++)
		{
			if(i!=j && odleglos(karty[i].getCenter(),karty[j].getCenter())<30 && karty[i].cardBase.id == karty[j].cardBase.id)
			{
				cout<<"USUWAM DUPLIKAT"<<endl;
				karty.erase(karty.begin()+max(j,i));
				j=-1;
				//i=-1;
				--i;
				break;
			}
		}
	}

	for(unsigned int i=0;i<karty.size();i++)
	{
		if(!karty[i].TrySend(game)) continue;
		if(karty[i].attack==true)
		{
				game.server.Attack(karty[i].id,karty[i].cardBase.id,game.GetPlayer(karty[i].owner),karty[i].a,karty[i].b,karty[i].c,karty[i].d,karty[i].taped);
		
		}
		else if(karty[i].block == true)
		{
			game.server.Block(karty[i].id,karty[i].cardBase.id,game.GetPlayer(karty[i].owner),karty[i].a,karty[i].b,karty[i].c,karty[i].d,karty[i].taped,karty[i].blocking);
		
		}
		else
		{
			game.server.UpdateCard(karty[i].id,karty[i].cardBase.id,game.GetPlayer(karty[i].owner),karty[i].a,karty[i].b,karty[i].c,karty[i].d,karty[i].taped);
		
		}

	}
		for(unsigned int i=0;i<stos.size();i++)
	{
		if(!stos[i].TrySend(game) || stos[i].cardBase.type==LAND) continue;
			game.server.Cost(game.GetPlayer(stos[i].owner),stos[i].cardBase.koszt);

	}


	if(game.GetPhase()==ATAK)
	{
		for (unsigned int i = 0; i<karty.size(); i++ ) 
		{
			karty[i].attack=false; 
			if(karty[i].owner==game.getCurrentPlayer() && odleglos(karty[i].getCenter(),karty[i].old)>50 && karty[i].taped==true) 
			{
				karty[i].attack=true; 
			}
		}
	}

	if(game.GetPhase()==OBRONA)
	{
		for (unsigned int i = 0; i<karty.size(); i++ ) 
		{
			karty[i].block=false; 
			if(!(karty[i].owner==game.getCurrentPlayer()) && odleglos(karty[i].getCenter(),karty[i].old)>50) 
			{
				karty[i].block=true; 
			}

		}
		for (unsigned int i = 0; i<karty.size(); i++ ) //karty broniace
		{	
			int index=-1;
			int minn=10000;
			if(karty[i].block==false) continue;

			for(unsigned int j=0;j<karty.size();j++)//karty atakujace
			{
				if(karty[j].attack==false || i==j) continue;

				if(odleglos(karty[j].getCenter(),karty[i].getCenter())<minn)
				{
					minn=odleglos(karty[j].getCenter(),karty[i].getCenter());
					index=j;
				}
			}
			if(index!=-1)
			{
				karty[i].enemy=karty[index].getCenter();
				karty[i].blocking=karty[index].id;
			}
		}
	}
	if(game.GetPhase()==WYMIANA && game.oneAttack==false)
	{
		game.oneAttack=true;
		for(int i=0;i<karty.size();i++)
		{
			if(karty[i].block==false) continue;
			for(int j=0;j<karty.size();j++)
			{
				if(karty[j].attack==false || i==j) continue;
				karty[i].Fight(karty[j]);
				cout<<"WALKA"<<endl;
			}
		}

		for(int i=0;i<karty.size();i++)
		{
			if(karty[i].attack==true)
			{
				if(karty[i].owner==game.player1)
				{
					game.player2.hp-=karty[i].att;
			
			
					game.server.SubLife(2,karty[i].att);
				}
				else
				{
						game.player1.hp-=karty[i].att;
					game.server.SubLife(1,karty[i].att);
				}
			}
			karty[i].Clear();
		}
		game.nextPhase();
		setTrackbarPos("Faza gry ","Ustawienia",4);
	}

	if(game.GetPhase()==UPKEEP)
	{
		for(int i=0;i<karty.size();i++)
		{
			karty[i].NewRound();
		}
		setTrackbarPos("Faza gry ","Ustawienia",0);
	}

	for(int i=0;i<karty.size();i++)
	{
		if(karty[i].dead==true && karty[i].ttl<=0) {game.server.Dead(karty[i].id); karty.erase(karty.begin() +i); i=-1; }
	}

	//wyswietlenie wszytkich kart
	for(unsigned int i=0;i<karty.size();i++)
	{
		karty[i].Draw(grey_image,bkarty,game);
	}

	for(unsigned int i=0;i<stos.size();i++)
	{
		stos[i].Draw(grey_image,bkarty,game);
	}

	DrawSafeRegion(grey_image);
	char cad[100];
	char cad1[100];
	char cad2[100];
	char cad3[100];
	char cad4[100];

	sprintf(cad,"Kart niedozwolonych: %d",kartyTemp.size());
	sprintf(cad1,"Kart na polu bitwy: %d",karty.size());
	sprintf(cad2,"Kart na stosie: %d",stos.size());
	sprintf(cad3,"Aktualny gracz: %s",game.getCurrentPlayer().name.c_str());
	sprintf(cad4,"Aktualna faza: %s",game.getCurrentPhase().c_str());

	putText(grey_image,cad, Point(10,10),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(grey_image,cad1, Point(10,25),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(grey_image,cad2, Point(10,40),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(grey_image,cad3, Point(10,55),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(grey_image,cad4, Point(10,70),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(grey_image,cad5, Point(10,85),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);


	if(game.t==false)
	{
		stos.clear();
		karty.clear();
	}


}

void draw_s(vector<Marker> markers,Mat &img,Game &game)
{
	
	Mat dst;
	dst.cols=game.GetGameWidth();
	dst.rows=game.GetGameHeight();
	int t=0;
	Point a,b,c,d;
	if(game.t==false)
	{
	for(int i=0;i<markers.size();i++)
	{
		circle(img,markers[i][0],4,Scalar(0,0,200),2);
		//markers[i].draw(img,Scalar(200,0,0),3);
		if(markers[i].id==341) 
		{
			float constans = 212.13f; // stała oznaczająca długość przekątnej w markerze wyświetlanym przez Olka

			float distance = Card::Distance(markers[i][0],markers[i][2]);
			float rel = distance/constans;
			float xx = markers[i][0].x - markers[i][2].x;
			float yy = markers[i][0].y - markers[i][2].y;
			float xxx = xx/(float)150;
			float yyy = yy/(float)150;
			Point2f movement(xxx*25,yyy*25);
			
			t++; 
			game.a=markers[i][0] + movement;
				circle(img,game.a,4,Scalar(0,0,200),3);
	
		}
		if(markers[i].id==1005) 
		{
						float constans = 212.13f; // stała oznaczająca długość przekątnej w markerze wyświetlanym przez Olka

			float distance = Card::Distance(markers[i][0],markers[i][2]);
			float rel = distance/constans;
			float xx = markers[i][0].x - markers[i][2].x;
			float yy = markers[i][0].y - markers[i][2].y;
			float xxx = xx/(float)150;
			float yyy = yy/(float)150;
			Point2f movement(xxx*25,yyy*25);
			
			t++; 
			game.b=markers[i][0] +movement;
			circle(img,game.b,4,Scalar(0,0,200),3);
		}
		if(markers[i].id==791) {
						float constans = 212.13f; // stała oznaczająca długość przekątnej w markerze wyświetlanym przez Olka

			float distance = Card::Distance(markers[i][0],markers[i][2]);
			float rel = distance/constans;
			float xx = markers[i][0].x - markers[i][2].x;
			float yy = markers[i][0].y - markers[i][2].y;
			float xxx = xx/(float)150;
			float yyy = yy/(float)150;
			Point2f movement(xxx*25,yyy*25);
			
			t++;
			game.c=markers[i][0]+movement;
			circle(img,game.c,4,Scalar(0,0,200),3);
		}
		if(markers[i].id==977) {
						float constans = 212.13f; // stała oznaczająca długość przekątnej w markerze wyświetlanym przez Olka

			float distance = Card::Distance(markers[i][0],markers[i][2]);
			float rel = distance/constans;
			float xx = markers[i][0].x - markers[i][2].x;
			float yy = markers[i][0].y - markers[i][2].y;
			float xxx = xx/(float)150;
			float yyy = yy/(float)150;
			Point2f movement(xxx*25,yyy*25);
			
			t++; 
			game.d=markers[i][0]+movement;
			circle(img,game.d,4,Scalar(0,0,200),3);
		}
	}
	if(t==4)
	{
		game.t=true;
		game.server.Markers();
	}
	}
	if(game.t==true)
	{
		Point2f c1[4] = {game.a,game.b,game.c,game.d};
		Point2f c2[4] = {Point2f(0,0), Point2f(game.GetGameWidth(),0), Point2f(game.GetGameWidth(),game.GetGameHeight()),Point2f(0,game.GetGameHeight())};
		Mat mmat(3,3,CV_32FC1);
		mmat=getPerspectiveTransform(c1,c2);
		warpPerspective(img,dst,mmat,Size(game.GetGameWidth(),game.GetGameHeight()));
	}

	imshow("Frame",img);
	img=dst;
}

int Card::ID=0;
int main( int argc, char** argv )
{
	//cout<<Card::Distance(Point(724,533),Point(828,529))<<endl;
	//cout<<Card::Distance(Point(828,664),Point(828,529))<<endl;

	//return 0;
	Point action(-1,-1);

	namedWindow("Ustawienia",CV_WINDOW_NORMAL);
	createTrackbar("Threeshold","Ustawienia",&three,200);
	createTrackbar("Zmienna ","Ustawienia",&zmienna,100);
	createTrackbar("Balans bieli ","Ustawienia",&white,10000);
	createTrackbar("Min area ","Ustawienia",&minArea,100);
	createTrackbar("Max arrea ","Ustawienia",&maxArea,500);
	createTrackbar("Gracz ","Ustawienia",&gracz,1);
	createTrackbar("Faza gry ","Ustawienia",&faza,4);


	
	Game game("lukasz",1,"daniel",2,"10.10.0.1",6121,1366,768,8,true);
	ScriptsManager scriptManager;
	game.server.AddPlayer(1,"lukasz");
	game.server.AddPlayer(2,"daniel");
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
		Mat a=imread("C:/Users/lukaszmaly/Documents/Visual Studio 2010/Projects/TakiSobieProjekt/TakiSobieProjekt/"+name1+".jpg");
		if(!a.data) {cout<<"Nie znaleziono karty "<< name<<".jpg"<<endl; continue;}
		bkarty.push_back(CardB(a,id,name,Red,tt,att,def,cost));

	}
	cout<<"Wczytane karty: "<< bkarty.size()<<endl;



	VideoCapture capture(0); 
	Mat frame;
	vector<Card> karty;
	vector<Card> stos;


	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 12 );

	capture.read(frame);
	while(1)
	{
			
		game.Update();
		game.setPlayer(gracz);
		game.setFaza(faza);
		capture.read(frame);
		//	Card::Draw(frame);

		game.CheckMarkers(frame);
		draw_s(game.markers,frame,game);
		if(frame.data)
		{
			Wykryj_karty(frame,three,karty,stos,bkarty,game);
			scriptManager.Update(frame,game,karty,stos);
			
			imshow("Podglad", frame);
		}
			game.Draw();
		if(cv::waitKey(20)==27) break;

		if(cv::waitKey(10)==97) game.nextPhase();
	}
	capture.release();
	cv::waitKey(0);
	return 0;
}