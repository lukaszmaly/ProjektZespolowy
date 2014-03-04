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
#define SAFEREGION 550
using namespace cv;

using namespace std;


int port=54000;

int c=0;
int three=80;
int maxArea= 32;
int minArea=20;
int zmienna=40;
int white=2800;
int gracz=0;
int faza=0;




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
	c++;
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
		//	cout<<"Rozmiar"<<fabs(contourArea(Mat(approx)))<<endl;
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

	//aktualizacja kart "ktore powinny znajdowac się na stole"


	//redukcja powtórzeń
	for (unsigned int i = 0; i<kartyTemp.size(); i++) 
	{
		for(unsigned int j=0; j<kartyTemp.size(); j++)
		{
			if(i!=j && odleglos(kartyTemp[i].getCenter(),kartyTemp[j].getCenter())<40)
			{
				kartyTemp.erase(kartyTemp.begin()+j);
				i=-1;
				break;
			}
		}
	}
	
	///Sprawdzanie czy są karty na stosie
	for (unsigned int i = 0; i<stos.size(); i++ ) 
	{
		int index=-1;
		int minn=90000;
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
		//cout<<"ZNALEZIONO"<<kartyTemp[j].cardBase.id << stos[i].cardBase.id<<" "<< kartyTemp[j].getCenter().x<<endl;
			if(kartyTemp[j].getCenter().x>SAFEREGION )//&& stos[i].cardBase.id==kartyTemp[j].cardBase.id
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
						int owner = 1;
			if(kartyTemp[i].owner==game.player2) owner=2;
			kartyTemp.erase(kartyTemp.begin()+index);
				//if(c>5)
			//	game.server.UpdateCard(karty[i].id,karty[i].cardBase.id,owner,karty[i].a,karty[i].b,karty[i].c,karty[i].d,karty[i].taped);
			}
	}

	///powyzej jest BLAD NAPRAW GO .BLAD WAZNY
	///mozna naprawić poprzez poprawienie warunku. nie szukaj najblizszego sasiada, tylko porównaj odległości między środkami
			char cad5[100];
		sprintf(cad5,"Kart tymczasowych: %d",kartyTemp.size());


	//dodanie nowych kart do stosu
	for(unsigned int i=0;i<kartyTemp.size();i++)
	{
		if(kartyTemp[i].getCenter().x>SAFEREGION) 
		{
			stos.push_back(kartyTemp[i]);
			kartyTemp.erase(kartyTemp.begin()+i);
			i=-1;
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
				i=-1;
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
						int owner = 1;
			if(kartyTemp[i].owner==game.player2) owner=2;
			kartyTemp.erase(kartyTemp.begin()+index);	
			
			if(c>5)
				game.server.UpdateCard(karty[i].id,karty[i].cardBase.id,owner,karty[i].a,karty[i].b,karty[i].c,karty[i].d,karty[i].taped);
			}
	}
	


	//dodanie nowych kart, które mogły się pojawić
	//dodają się tylko landy
	for(unsigned int i=0;i<kartyTemp.size();i++)
	{
		if(kartyTemp[i].cardBase.type==LAND)
		{
			karty.push_back(kartyTemp[i]);
			int owner = 1;
			if(kartyTemp[i].owner==game.player2) owner=2;
			game.server.sendNewCard(kartyTemp[i].id,kartyTemp[i].cardBase.id,owner,kartyTemp[i].a,kartyTemp[i].b,kartyTemp[i].c,kartyTemp[i].d,kartyTemp[i].taped);
		}
	}
	if(c>5)
		c=0;
	
	for(unsigned int i=0;i<stos.size();i++)
	{
		for(unsigned int j=0;j<kartyTemp.size();j++)
		{
			if(stos[i].cardBase.id==kartyTemp[j].cardBase.id && kartyTemp[j].getCenter().x<SAFEREGION) 
			{
				cout<<"DODANO NOWA KARTE"<<endl;
				karty.push_back(stos[i]);
				stos.erase(stos.begin()+i);
				kartyTemp.erase(kartyTemp.begin()+j);
				i=j=-1;
			}

		}

	}


	///redukcja duplikatów
			for (unsigned int i = 0; i<karty.size(); i++ ) 
	{
		for(unsigned int j=0;j<karty.size();j++)
		{
			if(i!=j && odleglos(karty[i].getCenter(),karty[j].getCenter())<30)
			{
				cout<<"USUNIETO ZDUPLIKOWANA KARTE"<<endl;
				karty.erase(karty.begin()+max(j,i));
				j=-1;
				i=-1;
				break;
			}
		}
	}


		//	cout<<"Ilosc kart:"<<karty.size()<<endl;






	if(game.phase==ATAK)
	{
		for (unsigned int i = 0; i<karty.size(); i++ ) 
		{
			karty[i].attack=false; 
			if(karty[i].owner==game.getCurrentPlayer() && odleglos(karty[i].getCenter(),karty[i].old)>50) 
				{
					karty[i].attack=true; 
				}
		}
	}


		if(game.phase==OBRONA)
		{
			for (unsigned int i = 0; i<karty.size(); i++ ) 
			{
				karty[i].block=false; 
				if(!(karty[i].owner==game.getCurrentPlayer()) && odleglos(karty[i].getCenter(),karty[i].old)>50) 
				{
						karty[i].block=true; 
				}
				
			}
			/*int at,br,tw;
			at=br=tw=0;
			for(int i=0;i<karty.size();i++)
			{
				if(karty[i].attack==true) at++;
				if(karty[i].block==true) br++;
					if(karty[i].block==true && karty[i].attack==true) tw++;

			}
			if(at!=0) cout<<"AT: "<<at<<endl;
			if(br!=0) cout<<"BR: "<<br<<endl;
			if(tw!=0) cout<<"TW: "<<tw<<endl;*/
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
					//cout<<"DYSTANS"<<Card::Distance(karty[i].getCenter(),karty[i].enemy)<<endl;
					//cout<<"WYBRANO BRONIACEGO "<<karty[index].cardBase.name<<endl;
				}
			}
		}
		if(game.phase==WYMIANA)
	{

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
			karty[i].Clear();
		}


			setTrackbarPos("Faza gry ","Ustawienia",4);
	}
			for(int i=0;i<karty.size();i++)
		{
			if(karty[i].dead==true && karty[i].ttl<=0) {karty.erase(karty.begin() +i); i=-1; }
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
	line(grey_image,Point(550,0),Point(550,600),Scalar(0,0,200),3);
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
	imshow("Podglad", grey_image);
}
int bb=0;
void draw_s(vector<Marker> markers,Mat &img,Game &game)
{
	Mat dst;
	dst.cols=800;
	dst.rows=600;
	int t=0;
	Point a,b,c,d;
	for(int i=0;i<markers.size();i++)
	{
		markers[i].draw(img,Scalar(200,0,0),3);
		if(markers[i].id==341) {t++; a=markers[i].getCenter();}
		if(markers[i].id==1005) {t++; b=markers[i].getCenter();}
		if(markers[i].id==791) {t++; c=markers[i].getCenter();}
		if(markers[i].id==977) {t++; d=markers[i].getCenter();}
		if(markers[i].id==177) {game.cardScaner=markers[i].getCenter();}
	}
	if(t==4)
	{
	/*	line(img,a,b,Scalar(0,0,200),1);
			line(img,b,c,Scalar(0,0,200),1);
				line(img,c,d,Scalar(0,0,200),1);
					line(img,a,d,Scalar(0,0,200),1);
*/

						Point2f c1[4] = {a,b,c,d};
	Point2f c2[4] = {Point2f(0,0), Point2f(800,0), Point2f(800,600),Point2f(0,600)};
		Mat mmat(3,3,CV_32FC1);
	mmat=getPerspectiveTransform(c1,c2);
	warpPerspective(img,dst,mmat,Size(800,600));

	//imshow("Druga",dst);



	//if(bb==0){
	//	
	//	cout<<mmat<<endl; bb=1;
	//game.server.macierz=mmat;
	//
	//}
		int width=dst.cols;
	int channels=dst.channels();
	int height=dst.rows;
	int r=200;
//	for(int x=0;x<dst.cols;x++)
//for(int y=0;y<dst.rows;y++)
//{
//	if(dst.data[channels*(width*y + x)]>50 &&
//			dst.data[channels*(width*y + x) +1]>50 &&
//			dst.data[channels*(width*y + x) +2]>50)
//	{
//		dst.data[channels*(width*y + x)]=255;
//			dst.data[channels*(width*y + x) +1]=255;
//			dst.data[channels*(width*y + x) +2]=255;
//	}
//}

	//dst=dst/200;
	//dst=dst*200;


	}
			imshow("Frame",img);
			
			img=dst;

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
	createTrackbar("Min area ","Ustawienia",&minArea,100);
			createTrackbar("Max arrea ","Ustawienia",&maxArea,500);
			createTrackbar("Gracz ","Ustawienia",&gracz,1);
			createTrackbar("Faza gry ","Ustawienia",&faza,4);


			
	Game game("lukasz",985,"daniel",838,"25.172.199.151",6121,1280,720);
//	game.server.addPlayer(1,"lukasz");
	//game.server.addPlayer(2,"daniel");
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
	cout<<"Wczytane karty: "<< bkarty.size()<<endl;

	VideoCapture capture(0); 
	Mat frame;
	vector<Card> karty;
	vector<Card> stos;


	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 13 );


	capture.read(frame);
	while(1)
	{
		game.setPlayer(gracz);
		game.setFaza(faza);
		capture.read(frame);
		markers.clear();
		MDetector.detect(frame,markers);
		draw_s(markers,frame,game);
		if(frame.data)
		Wykryj_karty(frame,three,karty,stos,bkarty,game);
			if(cv::waitKey(30)==27) break;
	}
		capture.release();
	cv::waitKey(0);
	return 0;
	while(1)
	{
		capture.read(frame);

		markers.clear();

		
		MDetector.detect(frame,markers);
		for(int i=0;i<markers.size();i++) 
		{
			markers[i].draw(frame,Scalar(0,0,255),3);
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
	
		draw_s(markers,frame,game);
		
		}

		Wykryj_karty(frame,three,karty,stos,bkarty,game);

		game.Draw();
		if(cv::waitKey(30)==27) break;
	}
	capture.release();
	cv::waitKey(0);
	return 0;
}