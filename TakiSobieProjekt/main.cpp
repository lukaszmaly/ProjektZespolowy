#include <stdio.h>
#include <iostream>
#include "opencv2\imgproc\imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"

#include "opencv2/nonfree/nonfree.hpp"
#include "settings.h"
#include "Card.h"
#include "Player.h"
#include "CardB.h"
#include "aruco.h"
#include <vector>

#include "marker.h"
#include "cvdrawingutils.h"
#include <fstream>
#include "Game.h"
#include "ScriptsManager.h"
#include "MainServer.h"
#include "zbar.h"
using namespace aruco;

using namespace cv;
using namespace zbar;  
using namespace std;


bool check = true;
int c=0;
int threeMax=200;
int threeMin=200;
int maxArea= 19;
int minArea=14;
int moveSize =10;
int drawWindows = 1;
bool oneAttack=false;
int updateTime = 0;
int stackWidth = 224;
int stackHeight = 276;
int stack1x=1130;
int stack1y=94;
int stack2x=1130;
int stack2y=390;

int mouseX,mouseY;
int mouseFlag=false;
bool tryToSaveCard=false;

bool IsInBase(string name)
{

	Http::Request request("/index.php",Http::Request::Post);
	std::ostringstream stream;
	stream << "nick="<<name;
	request.setBody(stream.str());

	Http http;
	http.setHost("pokelife.prv.pl");


	Http::Response response = http.sendRequest(request);

	if (response.getStatus() == sf::Http::Response::Ok)
	{
		// check the contents of the response
		//std::cout << response.getBody() << std::endl;
		//if(response.getBody().compare("OK")==0)
		if(response.getBody()[0]=='O')
		{
					cout<<"OK"<<endl;
			return true;
		}
		//else if(response.getBody().compare("ERROR")==0)
			if(response.getBody()[0]=='E')
		{
			cout<<"Nie ma takiego użytkownika w bazie"<<endl;
			return false;
		}
	}
	else
	{
		std::cout << "request failed" << std::endl;
	}
	
	return false;
}
void ReadQrCodes(Mat &img1, Game &game,MainServer &ms)
{
	ImageScanner scanner;  
	scanner.set_config(ZBAR_NONE, ZBAR_CFG_ENABLE, 1);  
	Mat img ;
	cvtColor(img1,img,CV_BGR2GRAY);
	int width = img.cols;  
	int height = img.rows;  
	uchar *raw = (uchar *)img.data;  
	Image image(width, height, "Y800", raw, width * height);  
	int n = scanner.scan(image);  
	for(Image::SymbolIterator symbol = image.symbol_begin(); symbol != image.symbol_end();++symbol) 
	{  
		vector<Point> vp;  
		Point center;
		int n = symbol->get_location_size();  
		cout << "decoded " << symbol->get_data() <<" with "<<n<< endl;   
		
		for(int i=0;i<n;i++){  
			vp.push_back(Point(symbol->get_location_x(i),symbol->get_location_y(i))); 
		}  
		if(vp.size()>=4) 
		{
			center=Point((vp[0].x+vp[1].x+vp[2].x+vp[3].x)/4,(vp[0].y+vp[1].y+vp[2].y+vp[3].y)/4);
			if(!game.player1.recognized  && IsInBase(symbol->get_data())) 
			{ 
				game.player1.recognized=true;
				game.player1.name=symbol->get_data();
				game.server.AddPlayer(1,game.player1.name.c_str());
				ms.SendPlayer(1,game.player1.name); 
			}
			else if(game.player1.recognized && !game.player2.recognized && IsInBase(symbol->get_data())  && symbol->get_data().compare(game.player1.name)!=0)
			{ 
				game.player2.recognized=true;
				game.player2.name=symbol->get_data();
				game.server.AddPlayer(2,game.player2.name.c_str());
				ms.SendPlayer(2,game.player2.name);
			}
		}
	}  
}

void LoadCardDatabase(vector<CardB> &cards,Game &game)
{
	cout<<"Loading Card`s database"<<endl;
	fstream plik("cards.txt", ios::in );
	int id,type,att,def,rCost,uCost,bCost,wCost,lCost,gCost,enterCount,passiveCount,upkeepCount;
	string name;
	bool hasHexproof, hasDeatchtuch,hasLifelink,hasDefender,hasFlying,hasReach,hasHaste,hasFirstStrike;
	vector<pair<int,int>> enterList,upkeepList,passiveList;
	if(!plik.good()) return;
	while(!plik.eof())
	{
		enterCount=passiveCount=upkeepCount=0;
		enterList.clear();
		passiveList.clear();
		upkeepList.clear();
		plik>>id>>name>>type>>att>>def>>rCost>>wCost>>bCost>>uCost>>gCost>>lCost;
		plik>>hasDeatchtuch>>hasHexproof>>hasFirstStrike>>hasFlying>>hasReach>>hasDefender>>hasLifelink>>hasHaste;


		plik>>enterCount;

		for(unsigned int i=0;i<enterCount;i++)
		{
			int f=0,s=0;
			plik >> f >> s;
			enterList.push_back(make_pair(f,s));
		}

		plik>>upkeepCount;
		for(unsigned int i=0;i<upkeepCount;i++)
		{
			int f,s;
			plik >> f >> s;
			upkeepList.push_back(make_pair(f,s));
		}

		plik>>passiveCount;
		for(unsigned int i=0;i<passiveCount;i++)
		{
			int f,s;
			plik >> f >> s;
			passiveList.push_back(make_pair(f,s));
		}

		string effect;
		plik >> effect;

		Mat a=imread("C:/Users/lukaszmaly/Documents/Visual Studio 2010/Projects/TakiSobieProjekt/TakiSobieProjekt/"+name+".jpg");


		if(!a.data) {cout<<"File "<< name<<".jpg not found"<<endl; continue;}
		if(game.useBlur) GaussianBlur(a,a,Size(15,15),0);
		if(game.IsBgrMode()==false)
			cvtColor(a,a,COLOR_BGR2HSV);
		//tmp.cols=251;
	//	tmp.rows=356;
		
		cards.push_back(CardB(a));
		cards[cards.size()-1].Init(id,name,type,att,def,rCost,wCost,gCost,bCost,uCost,lCost,hasDefender,hasLifelink,hasDeatchtuch,hasHaste,hasFlying,hasReach,hasFirstStrike,hasHexproof,enterList,upkeepList,passiveList,effect);
	}
	cout<<"Loaded "<< cards.size()<<" cards"<<endl;
}

bool IsInRectFast(Point p,Point a ,Point c)
{
	if(p.x<a.x || p.x>c.x || p.y<a.y || p.y>c.y) return false;
	return true;
}
void SaveCard(Mat &img,vector<Card>&cards)
{

	for(size_t i = 0; i < cards.size(); i++)
	{
		if(!IsInRectFast(Point(mouseX,mouseY),cards[i].a,cards[i].c)) continue;
		Mat tmp;
		Mat t;
		img.copyTo(t);
		tmp.cols=251;
		tmp.rows=356;
		Point2f c1[4] = {cards[i].a,cards[i].b,cards[i].c,cards[i].d};
		Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
		Mat mmat(3,3,CV_32FC1);
		mmat=getAffineTransform(c1,c2);
		warpAffine(t,tmp,mmat,Size(251,356));


		string name;
		cout<<"||Wpisz nazwe karty:"<<endl;
		cin>>name;
		cout<<"Dodano nowa karte"<<endl;
		imshow("Kartadodana"+name,tmp);
		imwrite( "C:/umk/"+name+".jpg", tmp );
	}
	tryToSaveCard=false;
}



double angle( cv::Point pt1, cv::Point pt2, cv::Point pt0 ) {
	double dx1 = pt1.x - pt0.x;
	double dy1 = pt1.y - pt0.y;
	double dx2 = pt2.x - pt0.x;
	double dy2 = pt2.y - pt0.y;
	return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2));
}

vector<vector<Point>>FindSquaresInImage(Mat &image)
{
	Mat diff;
	vector<vector<Point> > contours;
	vector<vector<Point> > edge_pts;
	vector<Card> tcard;
	vector<vector<Point>> squares;
	vector<Point> approx;
	vector<Vec4i> hierarchy;		
	cvtColor(image,diff,CV_RGB2GRAY);
	Canny(diff,diff,threeMin,threeMax);
	dilate(diff, diff, Mat(), Point(-1,-1));
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
	image=diff;
	return squares;
}

bool IsTheProperlyCard(Card &temp,vector<Card> &cards,int j=-1)
{
	
	//IsTheProperlyCard(tcard[j],cards,i)
	Point2f center = temp.getCenter();
	for(unsigned int i=0;i<cards.size();i++)
	{
		if(Game::Distance(center,cards[i].getCenter())<30) return false;
	}
	if(j==-1) return true;
	int dis;
	int mindist=Game::Distance(center,cards[j].getCenter());
	for(unsigned int i=0;i<cards.size();i++)
	{
		if(i==j || cards[i].fresh==false || cards[i].cardBase.id!=temp.cardBase.id) continue;
		dis=Game::Distance(center,cards[i].getCenter());
		if(dis<mindist) 
		{
			return false;
		}

	}

	return true;
}

void PrepareStack(vector<Card> &stack,Game &game)
{
	for(unsigned int i=0;i<stack.size();i++)
	{
		if(stack[i].cardBase.type==LAND)
		{
			cout<<"Land on the stack !"<<endl;
			stack.erase(stack.begin()+i);
			i=-1;
		}
		else if((stack[i].cardBase.type==ENCHANTMENT || stack[i].cardBase.type==CREATURE || stack[i].cardBase.type==SORCERY) 
			&& (game.GetPhase()==ATAK || game.GetPhase()==OBRONA || game.GetPhase()==WYMIANA || game.GetPhase()==UPKEEP))
		{
			cout<<"Can`t play this card in current phase !"<<endl;
			stack.erase(stack.begin()+i);
			i=-1;
		}

		else if(stack[i].cardBase.id==game.lastId)
		{
			cout<<"Now you can`t play this card, because similiar card was played!"<<endl;
			stack.erase(stack.begin()+i);
			i=-1;
		}
		else if((stack[i].cardBase.type==ENCHANTMENT || stack[i].cardBase.type==CREATURE || stack[i].cardBase.type==SORCERY) 
			&& stack[i].owner!=game.GetCurrentPlayer())
		{
			cout<<"It`s not your turn! You can only play a Instant`s cards"<<endl;
			stack.erase(stack.begin()+i);
			i=-1;
		}
	}
}

bool ChangedPosition(Point a,Point la,Point b,Point lb,Point c,Point lc,Point d,Point ld)
{
	return (Game::Distance(a,la)>moveSize && Game::Distance(b,lb)>moveSize && Game::Distance(c,lc)>moveSize && Game::Distance(d,ld)>moveSize);
}

void MultiplyerCardPreparation(vector<Card> &cards,vector<Card>&temp,Game &game)
{
	for(unsigned int i=0;i<cards.size();i++)
	{
		if(cards[i].owner==game.playerIdInMultiplayerMode) continue;
		for(unsigned int j=0;j<temp.size();j++)
		{
			Point oldCenter=Point((cards[i].la.x+cards[i].lb.x+cards[i].lc.x+cards[i].ld.x)/4,(cards[i].la.y+cards[i].lb.y+cards[i].lc.y+cards[i].ld.y)/4);
			if(Game::Distance(cards[i].veryOld,temp[j].getCenter())<30 || Game::Distance(temp[j].getCenter(),cards[i].getCenter())<30 || Game::Distance(temp[j].getCenter(),oldCenter)<30 )
			{
				temp.erase(temp.begin()+j);
				--j;
			}
		}
	}
	for(int i=0;i<temp.size();i++)
	{
		if(game.playerIdInMultiplayerMode==1)
		{
			if(IsInRectFast(temp[i].getCenter(),game.player2.stackB,game.player2.stackE)==true) 
			{
				temp.erase(temp.begin()+i);
				--i;
			}
		}
		else if(game.playerIdInMultiplayerMode==2)
		{
			if(IsInRectFast(temp[i].getCenter(),game.player1.stackB,game.player1.stackE)==true) 
			{
				temp.erase(temp.begin()+i);
				--i;
			}
		}
	}
}

void CardPrepare(vector<Card> &tcard,vector<Card> &cards,Game &game)
{

	for(int j=0;j<tcard.size();j++)
	{
		if(IsInRectFast(tcard[j].getCenter(),Point(stack1x,stack1y),Point(stack2x+stackWidth,stack2y+stackHeight))) continue;

		for(int i=0;i<cards.size();i++)
		{
			if(!ChangedPosition(cards[i].a,tcard[j].a,cards[i].b,tcard[j].b,cards[i].c,tcard[j].c,cards[i].d,tcard[j].d))
			{
				cards[i].Update(tcard[j].a,tcard[j].b,tcard[j].c,tcard[j].d,game);
				cards[i].fresh=false;
				tcard.erase(tcard.begin()+j);	
				--j;
				break;
			}
		}
	}
}
void MainCardLogic(Mat &frame,vector<Card> &cards,vector<Card>&stack,vector<CardB> &bcards,Game &game, MainServer &mainServer)
{



	ReadQrCodes(frame,game,mainServer);
	Mat diff;
	vector<vector<Point>> squares;
	frame.copyTo(diff);
	squares=FindSquaresInImage(diff);

	vector<Card> tcard;
	//Poprawa rogów kart

	for(unsigned int i=0;i<cards.size();i++)
	{
		cards[i].fresh=true;
	}

	for(unsigned int i=0;i<squares.size();i++)
	{
		Card::Prepare2(squares[i],frame);
		if(IsInRectFast(Point(mouseX,mouseY),squares[i][0],squares[i][2]))
			drawContours(diff,squares,i,Scalar(100,100,100),5);
		else drawContours(diff,squares,i,Scalar(255,255,255),2);
	}


	int erased =0;
	for(unsigned int i=0;i<squares.size();i++)
	{	
		if(game.firstCardChecked==false && IsInRectFast(Card::getCenter(squares[i][0],squares[i][1],squares[i][2],squares[i][3]),game.firsCardPoint,game.firsCardPoint+Point(game.firstCardWidth,game.firstCardHeight))==true)
		{
			continue;
		}
		if(game.CheckCardsProp()==true && Card::Valid(squares[i][0],squares[i][1],squares[i][2],squares[i][3])==false)
		{
			squares.erase(squares.begin()+i);
			//i=-1;
			--i;
			erased++;
		}
	}
	//if(erased!=0) cout<<erased <<" kart nie spelnialo norm"<<endl;

	//tworzenie tymczaswego wektora z wykrytymi kartami na stole
	for (unsigned int i = 0; i<squares.size(); i++ ) 
	{
		tcard.push_back(Card(squares[i][0],squares[i][1],squares[i][2],squares[i][3],frame,bcards,game,true));
	}
	
		if(drawWindows==1) imshow("Kontury",diff);

	if(tcard.size()!=0 && tryToSaveCard)
	{
		SaveCard(frame,tcard);
	}
	//redukcja powtórzeń
	for (unsigned int i = 0; i<tcard.size(); i++) 
	{
		for(unsigned int j=0; j<tcard.size(); j++)
		{
			if(i!=j && Game::Distance(tcard[i].getCenter(),tcard[j].getCenter())<50)
			{
				tcard.erase(tcard.begin()+j);
				//i=-1;
				//j=-1;
				--i;
				--j;
				break;
			}
		}
	}

	if(game.IsMultiplayer())
	{
		MultiplyerCardPreparation(cards,tcard,game);
	}
	CardPrepare(tcard,cards,game);

	for (unsigned int i = 0; i<tcard.size(); i++ ) 
	{
		tcard[i].Update(tcard[i].a,tcard[i].b,tcard[i].c,tcard[i].d,frame,bcards,game,true);
	}

		for (unsigned int i = 0; i<tcard.size(); i++) 
	{
		cout<<tcard[i].cardBase.name<<endl;
	}


	//
	if(game.firstCardChecked==false)
	{
		rectangle(frame,game.firsCardPoint,game.firsCardPoint+Point(game.firstCardWidth,game.firstCardHeight),Scalar(0,0,255),4);
		for(unsigned int i=0;i<tcard.size();i++)
		{
			if(IsInRectFast(tcard[i].getCenter(),game.firsCardPoint,game.firsCardPoint+Point(game.firstCardWidth,game.firstCardHeight))==true)
			{
				int w = (int)Game::Distance(tcard[i].a,tcard[i].b);
				int h = (int)Game::Distance(tcard[i].b,tcard[i].c);
				if(check==true)
				{
					Mat tmp;
					Mat t;
					frame.copyTo(t);
					tmp.cols=251;
					tmp.rows=356;
					Point2f c1[4] = {tcard[i].a,tcard[i].b,tcard[i].c,tcard[i].d};
					Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
					Mat mmat(3,3,CV_32FC1);
					mmat=getAffineTransform(c1,c2);
					warpAffine(t,tmp,mmat,Size(251,356));

					Card::fastImg("STATYSTYKA", Card::Compare2(bcards[0].img,tmp,game));
				}
				if(check==false)
				{
					game.firstCardChecked=true;
					game.server.Markers(w,h,game.IsMultiplayer(),game.playerIdInMultiplayerMode);
					if(game.IsMultiplayer())
					{
						game.SetPlayerPrepared();
						mainServer.SendReady();
					}
				}


				break;
				return;
			}
		}
		return;
	}



	if(game.IsMultiplayer() && !game.ArePlayersReady() && !game.gameStarted)
	{
		return;
	}
	else if(game.IsMultiplayer() && !game.gameStarted)
	{
		game.gameStarted=true;
		game.server.Start();
	}




	///Sprawdzanie czy są cards na stackie
	for (unsigned int i = 0; i<stack.size(); i++ ) 
	{
		int index=-1;
		int minn=90000;
		for(unsigned int j=0;j<tcard.size();j++)
		{
			if(IsInRectFast(tcard[j].getCenter(),game.player1.stackB,game.player1.stackE) || IsInRectFast(tcard[j].getCenter(),game.player2.stackB,game.player2.stackE))//&& stack[i].cardBase.id==tcard[j].cardBase.id
			{
				int dist = Game::Distance(stack[i].getCenter(),tcard[j].getCenter());
				if(dist<minn)
				{
					minn=dist;
					index=j;
				}
			}
		}
		if(index!=-1)
		{
			stack[i].Update(tcard[index].a,tcard[index].b,tcard[index].c,tcard[index].d,frame,bcards,game,true);
			tcard.erase(tcard.begin()+index);
		}
	}

	//dodanie nowych kart do stacku
	for(unsigned int i=0;i<tcard.size();i++)
	{
		if(IsInRectFast(tcard[i].getCenter(),game.player1.stackB,game.player1.stackE)==true) 
		{
			tcard[i].owner=1;
			stack.push_back(tcard[i]);
			tcard.erase(tcard.begin()+i);
			//	i=-1;
			--i;
		}
		else if(IsInRectFast(tcard[i].getCenter(),game.player2.stackB,game.player2.stackE)==true) 
		{
			tcard[i].owner=2;
			stack.push_back(tcard[i]);
			tcard.erase(tcard.begin()+i);
			//i=-1;
			--i;
		}
	}

	PrepareStack(stack,game);

	///redukcja duplikatów --jeśli błąd nie będzie się pojawiał można usunąć
	//for (unsigned int i = 0; i<stack.size(); i++ ) 
	//{
	//	for(unsigned int j=0;j<stack.size();j++)
	//	{
	//		if(i!=j && Game::Distance(stack[i].getCenter(),stack[j].getCenter())<30)
	//		{
	//			cout<<"BŁĄD: Usunieto  duplikat karty na stosie!"<<endl;
	//			game.server.Write("BLAD1");
	//			stack.erase(stack.begin()+max(j,i));
	//			j=-1;
	//			i=-1;
	//			break;
	//		}
	//	}
	//}

	///Tutaj mamy pewność że w bezpiecznej strefie nie znajdują się cards ze stacku

	for (unsigned int i = 0; i<cards.size(); i++ ) 
	{
		if(cards[i].fresh==false) continue;
		int index=-1;
		int minn=90000;
		int tempDistance;
		for(unsigned int j=0;j<tcard.size();j++)
		{

			if(cards[i].cardBase.id==tcard[j].cardBase.id)
			{
				tempDistance=Game::Distance(cards[i].getCenter(),tcard[j].getCenter());
				if(tempDistance<30 || (tempDistance<minn && IsTheProperlyCard(tcard[j],cards,i)==true))
				{
					minn=tempDistance;
					index=j;
				}
			}
		}
		if(index!=-1)
		{
			cards[i].Update(tcard[index].a,tcard[index].b,tcard[index].c,tcard[index].d,game);
			tcard.erase(tcard.begin()+index);	
		}
	}

	for(unsigned int i=0;i<stack.size();i++)
	{
		if(stack[i].ttl<=0) 
		{
			cout<<"Przestarzala karta na stosie"<<endl;
			
		if(stack.size()==1)	
		{
			game.ChangeStackState(stack[i].owner,NEUTRAL);
			if(game.IsMultiplayer()) mainServer.ChangeStackColor(stack[i].owner,NEUTRAL);
		}
		stack.erase(stack.begin()+i);
			//i=-1;
			--i;
		}
	}


	if(game.IsMultiplayer())
	{

		for(size_t i =0;i<stack.size();i++)
		{
			if(stack[i].owner==game.playerIdInMultiplayerMode && mainServer.IsStackClean(game.playerIdInMultiplayerMode,stack[i].cardBase.id))
			{
				mainServer.SendCardOnStack(stack[i].owner,stack[i].cardBase.id);
			}
		}

	}
	//dodanie nowych kart, które mogły się pojawić
	//dodają się tylko landy
	for(unsigned int i=0;i<tcard.size();i++)
	{
		if(tcard[i].cardBase.type==LAND && IsTheProperlyCard(tcard[i],cards)==true )
		{
			if(game.oneLandEachTurn==false || (game.oneLandEachTurn==true && game.playedLand==false))
			{
				game.playedLand=true;
				tcard[i].Unlock();
				tcard[i].la=tcard[i].lb=tcard[i].lc=tcard[i].ld=Point(-1,-1);
				cards.push_back(tcard[i]);


				game.server.SendNewCard(tcard[i].id,tcard[i].cardBase.id,tcard[i].owner,tcard[i].a,tcard[i].b,tcard[i].c,tcard[i].d,tcard[i].taped);
				mainServer.SendNewCard(tcard[i].id,tcard[i].cardBase.id,tcard[i].owner,tcard[i].a,tcard[i].b,tcard[i].c,tcard[i].d,tcard[i].taped);

				//game.GHPlay(tcard[i].owner,tcard[i].id,tcard[i].cardBase.name,-1,-1);
				tcard.erase(tcard.begin()+i);
				//i=-1;
				--i;
			}
		}
	}

	for(unsigned int i=0;i<stack.size();i++)
	{

		for(unsigned int j=0;j<tcard.size();j++)
		{
			///Stara wersja. -podczas zagrania wstawiło karte w miejsce innej karty(nie wpływa to na rozgrywkę(bug wizualny)
			//if(stack[i].cardBase.id==tcard[j].cardBase.id && IsInRectFast(tcard[j].getCenter())==false && game.CanPay(stack[i].owner,stack[i].cardBase.whiteCost,stack[i].cardBase.blueCost,stack[i].cardBase.blackCost,stack[i].cardBase.redCost,stack[i].cardBase.greenCost,stack[i].cardBase.lessCost)) 

			if(stack[i].cardBase.id==tcard[j].cardBase.id && IsTheProperlyCard(tcard[j],cards)==true && IsInRectFast(tcard[j].getCenter(),game.player2.stackB,game.player2.stackE)==false  && IsInRectFast(tcard[j].getCenter(),game.player1.stackB,game.player1.stackE)==false  && game.CanPay(stack[i].owner,stack[i].cardBase.whiteCost,stack[i].cardBase.blueCost,stack[i].cardBase.blackCost,stack[i].cardBase.redCost,stack[i].cardBase.greenCost,stack[i].cardBase.lessCost)) 
			{

				game.Pay(stack[i].owner,stack[i].cardBase.whiteCost,stack[i].cardBase.blueCost,stack[i].cardBase.blackCost,stack[i].cardBase.redCost,stack[i].cardBase.greenCost,stack[i].cardBase.lessCost);
				mainServer.SendPaid(stack[i].owner,stack[i].cardBase.whiteCost,stack[i].cardBase.blueCost,stack[i].cardBase.blackCost,stack[i].cardBase.redCost,stack[i].cardBase.greenCost,stack[i].cardBase.lessCost);
				tcard[j].Unlock();
				stack[i].id=tcard[j].id;

				stack[i].a = tcard[j].a;
				stack[i].b = tcard[j].b;
				stack[i].c = tcard[j].c;
				stack[i].d = tcard[j].d;

				game.server.SendNewCard(tcard[j].id,tcard[j].cardBase.id,tcard[j].owner,stack[i].a,stack[i].b,stack[i].c,stack[i].d,tcard[j].taped);
				game.GHPlay(tcard[j].owner,tcard[j].id,tcard[j].cardBase.name,-1,-1);

				vector<pair<int,int>> tt = tcard[j].cardBase.passiveAbilities;
				for(int z=0;z<tt.size();z++)
				{
					game.stack.push_back(Spell(tcard[j].cardBase.id,tt[z].first,tcard[j].owner,tcard[j].id,tcard[j].owner,tt[z].second,tcard[j].cardBase.effect));
				}
				tt.clear();
				tt=tcard[j].cardBase.enterAbilities;
				for(int z=0;z<tt.size();z++)
				{
					game.stack.push_back(Spell(tcard[j].cardBase.id,tt[z].first,tcard[j].owner,tcard[j].id,tcard[j].owner,tt[z].second,tcard[j].cardBase.effect));
				}

				cards.push_back(stack[i]);

				game.ChangeStackState(stack[i].owner,NEUTRAL);
				if(game.IsMultiplayer())
				{
					mainServer.ChangeStackColor(stack[i].owner,NEUTRAL);
				}
				stack.erase(stack.begin()+i);
				tcard.erase(tcard.begin()+j);
				//i=j=-1;
				--i;
				--j;
				break;
			}
		}
	}

	///redukcja duplikatów -- mozna usunac jesli blad nie bedzie sie pojawial
	//for (unsigned int i = 0; i<cards.size(); i++ ) 
	//{
	//	for(unsigned int j=0;j<cards.size();j++)
	//	{
	//		if(i!=j && Game::Distance(cards[i].getCenter(),cards[j].getCenter())<30 && cards[i].cardBase.id == cards[j].cardBase.id)
	//		{
	//			cout<<"BLAD:USUWAM DUPLIKAT"<<endl;
	//			game.server.Write("BLAD2");
	//			cards.erase(cards.begin()+max(j,i));
	//			j=-1;
	//			i=-1;
	//			break;
	//		}
	//	}
	//}

	//WYSYLANIE Kosztu karty/lepiej zastapic oblsuga stosu(wymaga bazy kart)
	for(unsigned int i=0;i<stack.size();i++)
	{
		if(stack[i].cardBase.type==LAND) continue;
		if(game.CanPay(stack[i].owner,stack[i].cardBase.whiteCost,stack[i].cardBase.blueCost,stack[i].cardBase.blackCost,stack[i].cardBase.redCost,stack[i].cardBase.greenCost,stack[i].cardBase.lessCost)==true)
		{
			game.ChangeStackState(stack[i].owner,OK);
			if(game.IsMultiplayer()) mainServer.ChangeStackColor(stack[i].owner,OK);
		}
		else
		{
			game.ChangeStackState(stack[i].owner,DENY);
			if(game.IsMultiplayer()) mainServer.ChangeStackColor(stack[i].owner,DENY);
		}
	}
	if(game.IsMultiplayer())
	{
		for(int i=0;i<cards.size();i++)
		{
			if(cards[i].owner!=game.playerIdInMultiplayerMode)
				cards[i].ttl=Card::TTL;
		}
	}
}

void NewRound(Game &game,vector<Card> &cards,vector<Card>&stack,ScriptsManager &sm)
{
	stack.clear();
	game.Clear();
	game.nextPhase();

	if(game.GetCurrentPlayer()==1)  game.setPlayer(1);
	else game.setPlayer(0);
	game.gh.NewRound(game.GetCurrentPlayer());
	game.server.ActivePlayer(game.GetCurrentPlayer());
	game.server.DrawCard(game.GetCurrentPlayer(),1);

	sm.Upkeep(game,cards,game.GetCurrentPlayer());
	for(unsigned int i=0;i<cards.size();i++)
	{
		cards[i].NewRound(game.GetCurrentPlayer());
	}
}

void MainGameLogic(Mat &frame,vector<Card> &cards,vector<Card>&stack,vector<CardB> &bcards,Game &game,ScriptsManager &sm, MainServer &mainServer)
{

	MainCardLogic(frame,cards,stack,bcards,game,mainServer);

	if(game.GetPhase()==ATAK)
	{
		for (unsigned int i = 0; i<cards.size(); i++ ) 
		{
			cards[i].attack=false; 
			if(cards[i].owner==game.GetCurrentPlayer() && cards[i].CanAttack() && Game::Distance(cards[i].getCenter(),cards[i].old)>50 && cards[i].taped==true) 
			{
				cards[i].attack=true; 
				cout<<"Atakuje karta "<<cards[i].id<<endl;
			}
		}
	}
	else if(game.GetPhase()==OBRONA)
	{
		for (unsigned int i = 0; i<cards.size(); i++ ) 
		{
			cards[i].block=false; 
			if(!(cards[i].owner==game.GetCurrentPlayer()) && Game::Distance(cards[i].getCenter(),cards[i].old)>50) 
			{
				cards[i].block=true; 
				cout<<"KArta blokuje" <<cards[i].id<<endl;
			}

		}
		for (unsigned int i = 0; i<cards.size(); i++ ) //cards broniace
		{	
			int index=-1;
			int minn=10000;
			int tempDistance;
			if(cards[i].block==false) continue;

			for(unsigned int j=0;j<cards.size();j++)//cards atakujace
			{
				if(cards[j].attack==false || i==j) continue;
				tempDistance=Game::Distance(cards[j].getCenter(),cards[i].getCenter());
				if(tempDistance<minn && cards[i].CanBlock(cards[j])==true)
				{
					minn=tempDistance;
					index=j;
				}
			}
			if(index!=-1)
			{
				cards[i].enemy=cards[index].getCenter();
				cards[i].blocking=cards[index].id;
			}
			else
			{
				cards[i].block=false;
			}
		}
	}
	else if(game.GetPhase()==WYMIANA && game.oneAttack==false)
	{
		cout<<"Wymiana obrazen"<<endl;
		game.oneAttack=true;
		for(int i=0;i<cards.size();i++)
		{
			if(cards[i].block==false) continue;
			for(int j=0;j<cards.size();j++)
			{
				if(cards[i].blocking==cards[j].id)
				{
					int life1=0;
					int life2=0;
					game.GHAtack(cards[j].id);
					game.GHDefense(cards[j].id,cards[i].id);
					cards[j].Fight(cards[i],game,life1,life2);
					//to raczej nie potrzebne
					/*		if(game.IsMultiplayer())
					{
					if(life1!=0) mainServer.SendAddLife(cards[j].owner,life1);
					if(life2!=0) mainServer.SendAddLife(cards[i].owner,life2);
					}*/
					cards[j].attack=false;
					cards[j].canUntap=false;
				}
			}
		}

		for(int i=0;i<cards.size();i++)
		{
			if(cards[i].attack==true)
			{
				game.GHAtack(cards[i].id);
				cards[i].canUntap=false;
				if(cards[i].owner==1)
				{
					game.SubLife(2,cards[i].GetAttack());
					if(cards[i].HasLifelink())
					{
						game.AddLife(1,cards[i].GetAttack());
					}
				}
				else if(cards[i].owner==2)
				{
					game.SubLife(1,cards[i].GetAttack());
					if(cards[i].HasLifelink())
					{
						game.AddLife(2,cards[i].GetAttack());
					}
				}
			}
			cards[i].Clear();
		}
		game.nextPhase();
	}

	else if(game.GetPhase()==UPKEEP)
	{
		NewRound(game,cards,stack,sm);
	}

	for(int i=0;i<cards.size();i++)
	{
		if(cards[i].dead==true && cards[i].deadSended==false) { cards[i].deadSended=true; game.server.Dead(cards[i].id);game.GHDie(cards[i].id); if(cards[i].owner==game.playerIdInMultiplayerMode) mainServer.SendDead(cards[i].id); }
		if(cards[i].dead && cards[i].ttl<=0) { cards.erase(cards.begin() +i); i=-1; }
	}

	//wyswietlenie wszytkich kart
	for(unsigned int i=0;i<cards.size();i++)
	{
		cards[i].Draw(frame,game);
		if(cards[i].GetDefense()<0 && cards[i].cardBase.type==CREATURE) cards[i].die();
	}
	for(unsigned int i=0;i<cards.size();i++)
	{
		if(cards[i].dead==true) continue;
		if(ChangedPosition(cards[i].a,cards[i].la,cards[i].b,cards[i].lb,cards[i].c,cards[i].lc,cards[i].d,cards[i].ld))
		{
			cards[i].la=cards[i].a;
			cards[i].lb=cards[i].b;
			cards[i].lc=cards[i].c;
			cards[i].ld=cards[i].d;
		}
		else
		{
			continue;
		}

		if(cards[i].attack==true)
		{
			game.server.Attack(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].GetAttack(),cards[i].GetDefense());
			if(game.IsMultiplayer() && cards[i].owner==game.playerIdInMultiplayerMode) mainServer.SendAttackingCard(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].GetAttack(),cards[i].GetDefense());

		}
		else if(cards[i].block == true)
		{
			game.server.Block(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].blocking,cards[i].GetAttack(),cards[i].GetDefense());
			if(game.IsMultiplayer() && cards[i].owner==game.playerIdInMultiplayerMode) mainServer.SendBlockingCard(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].GetAttack(),cards[i].GetDefense(),cards[i].blocking);

		}
		else
		{
			game.server.UpdateCard(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].GetAttack(),cards[i].GetDefense());
		if(game.IsMultiplayer() && cards[i].owner==game.playerIdInMultiplayerMode) 	mainServer.SendUpdatedCard(cards[i].id,cards[i].cardBase.id,cards[i].owner,cards[i].a,cards[i].b,cards[i].c,cards[i].d,cards[i].taped,cards[i].GetAttack(),cards[i].GetDefense());

		}
	}

	for(unsigned int i=0;i<stack.size();i++)
	{
		stack[i].Draw(frame,game);
	}



	char cad1[100];
	char cad2[100];
	char cad3[100];
	char cad4[100];

	sprintf(cad1,"Kart na polu bitwy: %d",cards.size());
	sprintf(cad2,"Kart na stackie: %d",stack.size());
	sprintf(cad3,"Aktualny gracz: %s",game.getCurrentPlayer().name.c_str());
	sprintf(cad4,"Aktualna faza: %s",game.getCurrentPhase().c_str());


	putText(frame,cad1, Point(10,25),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(frame,cad2, Point(10,40),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(frame,cad3, Point(10,55),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
	putText(frame,cad4, Point(10,70),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);

	if(game.t==false)
	{
		stack.clear();
		cards.clear();
	}
}


void FirstCalibration(vector<Marker> markers,Mat &img,Game &game)
{
	int t=0;
	Mat dst;
	dst.cols=game.GetGameWidth();
	dst.rows=game.GetGameHeight();

	if(game.t==false)
	{
		Point a,b,c,d;
		float constans = 212.13f; //Magiczna liczba oznaczająca długość przekątnej markera o rozmiarach 150 x 150
		float distance,rel,xx,yy,xxx,yyy;
		Point2f movement;
		for(int i=0;i<markers.size();i++)
		{

			distance = Card::Distance(markers[i][0],markers[i][2]);
			rel = distance/constans;
			xx = markers[i][0].x - markers[i][2].x;
			yy = markers[i][0].y - markers[i][2].y;
			xxx = xx/(float)150;
			yyy = yy/(float)150;
			movement = Point(xxx*25,yyy*25);
			//circle(img,markers[i][0],4,Scalar(0,0,200),2);
			//markers[i].draw(img,Scalar(200,0,0),3);
			if(markers[i].id==341) 
			{
				t++; 
				game.a=markers[i][0] + movement;
				//circle(img,game.a,4,Scalar(0,0,200),3);
			}
			else if(markers[i].id==1005) 
			{
				t++; 
				game.b=markers[i][0] +movement;
				//circle(img,game.b,4,Scalar(0,0,200),3);
			}
			else if(markers[i].id==791) {
				t++;
				game.c=markers[i][0]+movement;
				//circle(img,game.c,4,Scalar(0,0,200),3);
			}
			else if(markers[i].id==977) {
				t++; 
				game.d=markers[i][0]+movement;
				//circle(img,game.d,4,Scalar(0,0,200),3);
			}
		}
		if(t==4) game.t=true;
	}
	else if(game.t==true)
	{
		Point2f c1[4] = {game.a,game.b,game.c,game.d};
		Point2f c2[4] = {Point2f(0,0), Point2f(game.GetGameWidth(),0), Point2f(game.GetGameWidth(),game.GetGameHeight()),Point2f(0,game.GetGameHeight())};
		Mat mmat(3,3,CV_32FC1);
		mmat=getPerspectiveTransform(c1,c2);
		warpPerspective(img,dst,mmat,Size(game.GetGameWidth(),game.GetGameHeight()));
	}
	if(drawWindows==1)	imshow("Frame",img);
	img=dst;
}

int Card::ID=0;
int Card::width=-1;
int Card::height=-1;


void CheckMarkers(Mat &frame, Game &game,MainServer &mainServer)
{
	MarkerDetector MDetector;
	MDetector.setMinMaxSize(0.01f);
	game.markers.clear();
	MDetector.detect(frame,game.markers);

	for(int i = 0; i < game.markers.size(); i++) 
	{
		if(game.markers[i].id==game.player1.markerId || game.markers[i].id==game.player1.secondMarkerId )
		{
			if(game.IsMultiplayer() && game.playerIdInMultiplayerMode==2) continue;


			if(game.player1.ChangedMarkersId(game.markers[i].id)==true)
			{
				game.player1.agree=true;
				game.server.Agree(1);
				if(game.IsMultiplayer()) mainServer.SendNextPhase();
			}
		}
		else if(game.markers[i].id==game.player2.markerId || game.markers[i].id==game.player2.secondMarkerId )
		{
			if(game.IsMultiplayer() && game.playerIdInMultiplayerMode==1) continue;

			if(game.player2.ChangedMarkersId(game.markers[i].id)==true)
			{
				game.player2.agree=true;
				game.server.Agree(2);
				if(game.IsMultiplayer()) mainServer.SendNextPhase();
			}

		}
		else if(game.markers[i].id == TARGETMARKER)
		{
			continue;
		}
		game.markers[i].draw(frame,Scalar(0,0,255));
	}
}




void MouseCallbackFunc(int event, int x, int y, int flags, void* userdata)
{
	mouseX=x;
	mouseY=y;
	if(event == EVENT_LBUTTONDOWN && mouseFlag==false && tryToSaveCard==false)
	{
		cout<<"Clicked"<<endl;
		mouseFlag=true;
		tryToSaveCard=true;
	}
	else
	{
		mouseFlag=false;
	}
}

void Demo()
{

	Http::Request request("/index.php",Http::Request::Post);
	std::ostringstream stream;
	stream << "imie=dsfddsfdsfsdfsdfsddsf dsf\ndsfsdfdsfsdfsdfsdfsddsf\ndsfsdfsdfsdfsddsfsdfs\nsdfsdfsdfsdfsdfsdsfsdfsd\nsdfsdfsdfadfafafsafcdvfdgrsgsd\nsdfsdfefsdfsgwegfafadfasfrgtrh\n koniec";
	request.setBody(stream.str());

	Http http;
	http.setHost("pokelife.prv.pl");


	Http::Response response = http.sendRequest(request);

	if (response.getStatus() == sf::Http::Response::Ok)
	{
		// check the contents of the response
		std::cout << response.getBody() << std::endl;
	}
	else
	{
		std::cout << "request failed" << std::endl;
	}

}
int main( int argc, char** argv )
{
	int id=1;
	bool multi=false;
	string clientIp="192.168.128.22";
	string serverIp;
	if(argc<=1)
	{
	
		clientIp="192.168.128.22";
		string serverIp="192.168.0.100";
	}
	else
	{
		if(argv[1][0]=='M')
		{
			multi=true;
			cout<<"MULTI"<<endl;
			if(argv[2][0]=='1')
			{
				cout<<"Pierwsze"<<endl;
			}
			else
			{
					cout<<"Drugi"<<endl;
					id=2;
			}
				clientIp=argv[3];
				serverIp=argv[4];

		}
		else
		{
			cout<<"SINGLE"<<endl;
			clientIp=argv[2];
		}
	}
	//Demo();
	//return 0;

	check=false;
	bool presed = true;

	//Game game("lukasz",233,"daniel",760,"192.168.0.100",600,800,600,8,true);

	Game game("NONE",684,461,"NONE",908,985,clientIp,600,1366,768,8,true);


	ScriptsManager scriptManager;
	vector<CardB> dataBaseCards;
	vector<Card> cardsInPlay;
	vector<Card> cardsOnStack;
	Mat frame;	
	VideoCapture capture(0);


	LoadCardDatabase(dataBaseCards,game);



	MainServer mainServer = MainServer();


	//mainServer.Read(cardsInPlay,dataBaseCards,game);
	//mainServer.Read(cardsInPlay,dataBaseCards,game);
	//mainServer.Read(cardsInPlay,dataBaseCards,game);
	//mainServer.Read(cardsInPlay,dataBaseCards,game);


	namedWindow("Settings",CV_GUI_EXPANDED);
	createTrackbar("Threeshold Min","Settings",&threeMin,300);
	createTrackbar("Threeshold Max","Settings",&threeMax,300);
	createTrackbar("Min area ","Settings",&minArea,100);
	createTrackbar("Max arrea ","Settings",&maxArea,500);
	createTrackbar("Draw Windows ","Settings",&drawWindows,1);
	createTrackbar("UpdateTime ","Settings",&updateTime,10);
	createTrackbar("Movement Size  ","Settings",&moveSize,60);
	createTrackbar("StackWidth ","Settings",&stackWidth,500);
	createTrackbar("StackHeight ","Settings",&stackHeight,500);
	createTrackbar("Stack 1 x ","Settings",&stack1x,1200);
	createTrackbar("Stack 1 y ","Settings",&stack1y,1200);
	createTrackbar("Stack 2 x ","Settings",&stack2x,1200);
	createTrackbar("Stack 2 y ","Settings",&stack2y,1200);


	namedWindow("Frame",CV_WINDOW_NORMAL);
	namedWindow("Preview",CV_WINDOW_NORMAL);
	namedWindow("Kontury",CV_WINDOW_NORMAL);
	setMouseCallback("Preview", MouseCallbackFunc, NULL);
	capture.set(CV_CAP_PROP_FRAME_WIDTH, 1280 );
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 720 );
	capture.set(CV_CAP_PROP_FOCUS, 12 );
	capture.read(frame);
	game.StackSettings(stackWidth,stackHeight,stack1x,stack1y,stack2x,stack2y);
	game.multiplayerMode=multi;
	game.playerIdInMultiplayerMode=id;
	if(game.IsMultiplayer())
	{

		mainServer.Init(serverIp);
		if(game.playerIdInMultiplayerMode==1)
		mainServer.HostGame();
		else
			mainServer.ConnectGame();
	}

	while(!game.gameEnded)
	{


		game.Update();

		game.server.SetInterval(updateTime);
		capture.read(frame);

		//ReadQrCodes(frame,game);
		//imshow("Podglad",frame);
		//if(cv::waitKey(30)==27) break;
		//continue;

		CheckMarkers(frame,game,mainServer);


		FirstCalibration(game.markers,frame,game);
		if(game.IsMultiplayer())
		{
			mainServer.Recv();
			mainServer.Read(cardsInPlay,dataBaseCards,game);
		}

		if(frame.data)
		{
			MainGameLogic(frame,cardsInPlay,cardsOnStack,dataBaseCards,game,scriptManager,mainServer);
			scriptManager.Update(frame,game,cardsInPlay,cardsOnStack,mainServer);
			//game.Draw(frame);
			char cad6[20];
			sprintf(cad6,"Can: %d",!presed);
			putText(frame,cad6, Point(10,130),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);

			imshow("Preview", frame);

		}


		if(cv::waitKey(5)==27) game.gameEnded=true;
		if(cv::waitKey(5)==113) presed = false;
		if(cv::waitKey(5)==97 && presed==false) { game.nextPhase(); presed=true; }
		if(cv::waitKey(5)==119 && presed==false) {game.setFaza(4); presed=true; }

	}
	capture.release();
	if(!game.IsMultiplayer() || (game.IsMultiplayer() && game.playerIdInMultiplayerMode==1))
	{
		game.gh.GenerateLog(game.player1.name,game.player2.name);
	}
	cv::waitKey(0);
	//getchar();
	return 0;
}