<HTML>
<HEAD>

<OBJECT id=TConnector1
	classid=clsid:126C289A-607B-4251-BF31-1555A5951948 VIEWASTEXT>
	<PARAM NAME="IOType" VALUE="3">
	<PARAM NAME="Device" VALUE="COM1">
	<PARAM NAME="Baud" VALUE="9600">
	<PARAM NAME="Data" VALUE="8">
	<PARAM NAME="Stop" VALUE="1">
	<PARAM NAME="Parity" VALUE="N">
	<PARAM NAME="Host" VALUE="">
	<PARAM NAME="Service" VALUE="">
	<PARAM NAME="XOnXOff" VALUE="0">
	<PARAM NAME="RTSCTS" VALUE="0">
	<PARAM NAME="DTRDSR" VALUE="0">
	<PARAM NAME="NoOfBytes" VALUE="100">
	<PARAM NAME="Timeout" VALUE="1000">
	<PARAM NAME="Timeout_Infinite" VALUE="-1">
	<PARAM NAME="Delimiter" VALUE="\x0d\x0a">
	<PARAM NAME="UseDelimiter" VALUE="0">
	<PARAM NAME="IncludeDelimiter" VALUE="0">
	<PARAM NAME="SendKeyStroke" VALUE="0">
	<PARAM NAME="Prefix" VALUE="">
	<PARAM NAME="Postfix" VALUE="">
	<PARAM NAME="IsOpen" VALUE="0">
</OBJECT>

<SCRIPT LANGUAGE=javascript FOR=TConnector1 EVENT=OnData(sData)>

 TConnector1_OnData(sData)

</script>


<script language="javascript">

// GLOBAL VARIABLES

var bTCPresent = true;  //Is TConnector installed (refer to body onload event)


// GLOBAL FUNCTIONS

//////////////////////////////////////
//
//////////////////////////////////////


function TConnector1_OnData(sData)
{
		StatOut( '\nData: ' + sData );
}

function readdata()
{
	TConnector1.Timeout_Infinite = 0;
	try
	{
		StatOut( '\nOpening device ' );
		TConnector1.Open();  // open device, must be called before any operation
		StatOut( '\nStart listening... \n' );
		TConnector1.StartListen()
	}
	catch (error)
	{
		StatOut( '\nCouldn\'t open device...' );
		StatOut( '\nSysErr: ' + TConnector1.TranslateErrorNo(error.number) );
		return;
	}
}


function stop_listen()
{
	try
	{
		StatOut( '\nStop listening... ');
		TConnector1.StopListen();
		StatOut( '\nClosing device ');
		TConnector1.Close();  // Close device, must be called at the end, otherwise the port will be blocked
	}
	catch (error)
	{
		StatOut( '\nCouldn\'t close device...' );
		StatOut( '\n' + TConnector1.TranslateErrorNo(error.number) );
	}

}
//////////////////////////////////////
//
//////////////////////////////////////
function StatOut(strStatLine)
{
var status = window.document.FORM1.STATUS;

	status.value = status.value + strStatLine;
}

//////////////////////////////////////
//
//////////////////////////////////////
function StatClear()
{
var status = window.document.FORM1.STATUS;

	status.value = '';
}

//////////////////////////////////////
//
//////////////////////////////////////
function CheckTC()
{
var TCInstance;
	try
	{
	  // if the object creation isn't successfully an exception is generated
		TCInstance = new ActiveXObject("TConnector2.TConnector2");
	}
	catch (exception)
	{
	    // TConnector2.ProgID was not created
		 alert('Can\'t create TConnector2 ActiveX - make sure it is installed on this system!');
		 
		 document.FORM1.configure.disabled = true;
		 bTCPresent = false;
	}
	TCInstance = null;
}

</script>

</HEAD>

<BODY onload="CheckTC()">

<FORM name="FORM1">
<P>1.&nbsp;&nbsp; <INPUT id=configure
	onclick=TConnector1.PropertyDialog() type=button value="Configure Port"></P>
<P>2.&nbsp;&nbsp; <INPUT id=read style="LEFT: 30px; TOP: 266px"
	onclick="readdata()" type=button value="Open port and start listening"></P>
<P>3.&nbsp;&nbsp; <input type="button"
	value="Stop listening and close port" name="B1" onclick="stop_listen()"></P>
<P>&nbsp;</P>
<P>Actual State:<BR>
<TEXTAREA cols=60 id=STATUS name=STATUS rows=8></TEXTAREA></P>
</FORM>
</BODY>
</HTML>
