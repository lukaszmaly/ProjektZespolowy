#include <afxwin.h>      //MFC core and standard components
#include "resource.h"    //main symbols
#include "Connection.h"

//-----------------------------------------------------------------------------------------

//Globals

CEdit *pInput;
CEdit *pOutput;
CButton *pConnect;
NetworkGame polaczenie;

class GAME_FORM : public CDialog
{
    public:
    GAME_FORM(CWnd* pParent = NULL): CDialog(GAME_FORM::IDD, pParent)
    {    }

    // Dialog Data, name of dialog form
    enum{IDD = Interface};

    protected:
    virtual void DoDataExchange(CDataExchange* pDX) { CDialog::DoDataExchange(pDX); }

    //Called right after constructor. Initialize things here.
    virtual BOOL OnInitDialog() 
    { 
            CDialog::OnInitDialog();
			pInput = (CEdit *) GetDlgItem(C_Input);
			pOutput = (CEdit *) GetDlgItem(C_Output);
			pConnect = (CButton *) GetDlgItem(C_Connect);
			pInput->SetWindowTextW(L"192.168.0.100");
			//MessageBoxW(L"widac?");
            return true; 
    }
public:
DECLARE_MESSAGE_MAP()

	afx_msg void conn()
	{
		CString cos_tmp;
		pInput->GetWindowTextW(cos_tmp);
	    CT2CA pszConvertedAnsiString (cos_tmp); //konwersja cstring do string
		std::string cos (pszConvertedAnsiString);
		polaczenie.Init(cos);

		polaczenie.ConnectGame();
		pOutput->SetWindowTextW(L"polaczony");
	}

	afx_msg void host()
	{
		polaczenie.Init("192.168.0.100");
		//polaczenie.HostGame();
		polaczenie.Start_Game();
	//	polaczenie.Nic();
		pOutput->SetWindowTextW(L"Po³¹czono");
	}

	afx_msg void sending()
	{
		polaczenie.Send("wyslalem wiadomosc!");
		pOutput->SetWindowTextW(L"wys³ano");
	}
};

//-----------------------------------------------------------------------------------------

class TheGame : public CWinApp
{
public:
TheGame() {  }

public:
virtual BOOL InitInstance()
    {
   CWinApp::InitInstance();
   GAME_FORM dlg;
   m_pMainWnd = &dlg;
   INT_PTR nResponse = dlg.DoModal();
   return FALSE;

} //close function

};

//-----------------------------------------------------------------------------------------
//Need a Message Map Macro for both CDialog and CWinApp

BEGIN_MESSAGE_MAP(GAME_FORM, CDialog)
	ON_COMMAND(C_Connect,conn)
	ON_COMMAND(C_Host,host)
	ON_COMMAND(C_Send,sending)
END_MESSAGE_MAP()

//-----------------------------------------------------------------------------------------

TheGame theApp;  //Starts the Application