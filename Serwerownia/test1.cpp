// test1.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <SFML\Network.hpp>
#include "GraSieciowa.h"

int main()
{
	GraSieciowa gra;
	//gra.Serwer();
	gra.Klient();

    return 0;
}

