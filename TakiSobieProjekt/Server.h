#pragma once

#include <iostream>
#include <vector>
#include <string>
#include "opencv2/core/core.hpp"
#include "SFML\Network.hpp"
using namespace std;
using namespace sf;
using namespace cv;
class Server
{
public:
	UdpSocket soc;
		Mat macierz;
	int port;
	void Init(string ip,int port);
	IpAddress client;
Server()
{
	macierz =  Mat(3,3,CV_32FC1);
}

	void sendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
	{
			Point2f aa,bb,cc,dd;
		
		char data[100];
		if(macierz.data)
		{
				macierz.convertTo(macierz,CV_32FC1,1,0);
		


		vector<Point3f> vec;
		vec.push_back(Point3f(a.x,a.y,1));
	
		Mat srcMat = Mat(vec).reshape(1).t();
		Mat dstMat = macierz*srcMat;
		aa=Point2f(dstMat.at<float>(0,0),dstMat.at<float>(1,0));

		
		vector<Point3f> vec1;
		vec1.push_back(Point3f(b.x,b.y,1));
	
		Mat srcMat1 = Mat(vec1).reshape(1).t();
		Mat dstMat1 = macierz*srcMat1;
		bb=Point2f(dstMat1.at<float>(0,0),dstMat1.at<float>(1,0));


		
		vector<Point3f> vec2;
		vec2.push_back(Point3f(c.x,c.y,1));

		Mat srcMat2 = Mat(vec2).reshape(1).t();
		Mat dstMat2 = macierz*srcMat2;
		cc=Point2f(dstMat2.at<float>(0,0),dstMat2.at<float>(1,0));


		
		vector<Point3f> vec3;
		vec3.push_back(Point3f(a.x,a.y,1));

		Mat srcMat3 = Mat(vec3).reshape(1).t();
		Mat dstMat3 = macierz*srcMat3;
		dd=Point2f(dstMat3.at<float>(0,0),dstMat3.at<float>(1,0));



		//cout<<"KARTA"<<aa.x<<" "<<aa.y<<endl;
		//cout<<"KARTA"<<aa.x<<" "<<aa.y<<endl;
		//cout<<"KARTA"<<aa.x<<" "<<aa.y<<endl;
		//cout<<"KARTA"<<aa.x<<" "<<aa.y<<endl;
		

		//		cout<<"f"<<endl;
		//a=tab2[0];
		//	cout<<"f"<<endl;
		//b=tab2[1];
		//c=tab2[2];
		//d=tab2[3];
		int n = sprintf(data,"| ADD %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,aa.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
		if (soc.send(data, n, client, port) != sf::Socket::Done)
		{
			cout<<"Nie wyslano"<<endl;
		}
		else
		{
			cout<<"Wyslano wiadomosc:"<<endl;
			cout<<data<<endl;
			cout<<"Koniec wiadomosci"<<endl;
		}

				}
		else
		{
			cout<<"Blad macierzy"<<endl;
		}

	}
		void UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped)
	{
		char data[100];
		
		int n = sprintf(data,"| UPDATE %d %d %d %d %d %d %d %d %d %d %d %d |",id,idb,gracz,taped,a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);	
		if (soc.send(data, n, client, port) != sf::Socket::Done)
		{
			cout<<"Nie wyslano"<<endl;
		}
				else
		{
		/*	cout<<"Wyslano wiadomosc:"<<endl;
			cout<<data<<endl;
			cout<<"Koniec wiadomosci"<<endl;*/
		}
	}

				void addPlayer(int id,const char name[])
	{
		char data[100];
		int n = sprintf(data,"| PLAYER %d %s |",id,name);	
		if (soc.send(data, n, client, port) != sf::Socket::Done)
		{
			cout<<"Nie wyslano"<<endl;
		}
				else
		{
			cout<<"Wyslano wiadomosc:"<<endl;
			cout<<data<<endl;
			cout<<"Koniec wiadomosci"<<endl;
		}
	}



	~Server(void);
};

