package top.vchao.live.j2xx;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import top.vchao.live.R;

public class J2xxActivity extends Activity {
    // j2xx
    public static D2xxManager ftD2xx = null;
    final int UPDATE_TEXT_VIEW_CONTENT = 0;
    final int UPDATE_MODEM_RECEIVE_DATA = 9;
    final int UPDATE_MODEM_RECEIVE_DATA_BYTES = 10;
    final int UPDATE_MODEM_RECEIVE_DONE = 11;
    final int MSG_MODEM_RECEIVE_PACKET_TIMEOUT = 12;
    final int MSG_YMODEM_PARSE_FIRST_PACKET_FAIL = 15;
    final int MSG_FORCE_STOP_SEND_FILE = 16;
    final int UPDATE_ASCII_RECEIVE_DATA_BYTES = 17;
    final int MSG_FORCE_STOP_SAVE_TO_FILE = 19;
    final int UPDATE_ZMODEM_STATE_INFO = 20;
    final int ACT_ZMODEM_AUTO_START_RECEIVE = 21;
    final int MSG_SPECIAL_INFO = 98;
    final int MSG_UNHANDLED_CASE = 99;
    final byte XON = 0x11;    /* Resume transmission */
    final byte XOFF = 0x13;    /* Pause transmission */

    final int MODE_GENERAL_UART = 0;
    final int MODE_X_MODEM_CHECKSUM_RECEIVE = 1;
    final int MODE_X_MODEM_CHECKSUM_SEND = 2;
    final int MODE_X_MODEM_CRC_RECEIVE = 3;
    final int MODE_X_MODEM_CRC_SEND = 4;
    final int MODE_X_MODEM_1K_CRC_RECEIVE = 5;
    final int MODE_X_MODEM_1K_CRC_SEND = 6;
    final int MODE_Y_MODEM_1K_CRC_RECEIVE = 7;

    final int MODE_Z_MODEM_RECEIVE = 9;
    final int MODE_Z_MODEM_SEND = 10;

    final byte ACK = 6;    /* ACKnowlege */
    final byte NAK = 0x15; /* Negative AcKnowlege */

    final byte CHAR_C = 0x43; /* Character 'C' */
    final byte CHAR_G = 0x47; /* Character 'G' */

    final int DATA_SIZE_256 = 256;
    final int MODEM_BUFFER_SIZE = 2048;


    final byte ZPAD = 0x2A; // '*' 052 Padding character begins frames
    final byte ZDLE = 0x18;
    final byte ZDLEE = (byte) (ZDLE ^ 0100);   /* Escaped ZDLE as transmitted */
    final byte ZBIN = 0x41;        // 'A' Binary frame indicator (CRC-16)
    final byte ZHEX = 0x42;        // 'B' HEX frame indicator
    final byte ZBIN32 = 0x43;    // 'C' Binary frame with 32 bit CRC
    final int ZRQINIT = 0;   /* Request receive init */
    final int ZRINIT = 1;   /* Receive init */
    final int ZFILE = 4;     /* File name from sender */
    final int ZFIN = 8;      /* Finish session */
    final int ZRPOS = 9;     /* Resume data trans at this position */
    final int ZDATA = 10;    /* Data packet(s) follow */
    final int ZDATA_HEADER = 21;
    final int ZEOF = 11;     /* End of file */
    final int ZOO = 20;
    final int ZCRCW = 0x6B; // file info end
    final int ZDLE_END_SIZE_4 = 4; // zdle ZCRC? crc1 crc2
    final int ZDLE_END_SIZE_5 = 5; // zdle ZCRC? zdle crc1 crc2 || zdle ZCRC? crc1 zdle crc2
    final int ZDLE_END_SIZE_6 = 6; // zdle ZCRC? zdle crc1 zdle crc2
    final int ZMS_0 = 0;
    final int ZMS_1 = 1; // r
    final int ZMS_2 = 2; // z
    final int ZMS_3 = 3; // \r
    final int ZMS_4 = 4; // ZPAD (ZRQINIT)
    final int ZMS_5 = 5; // ZPAD
    final int ZMS_6 = 6; // ZDLE
    final int ZMS_7 = 7; // ZHEX
    final int ZMS_8 = 8; // 0x30
    final int ZMS_9 = 9; // 0x30
    final int ZMS_10 = 10; // 0x30
    final int ZMS_11 = 11; // 0x30
    final int ZMS_12 = 12; // 0x30
    final int ZMS_13 = 13; // 0x30
    final int ZMS_14 = 14; // 0x30
    final int ZMS_15 = 15; // 0x30
    final int ZMS_16 = 16; // 0x30
    final int ZMS_17 = 17; // 0x30
    final int ZMS_18 = 18; // 0x30
    final int ZMS_19 = 19; // 0x30
    final int ZMS_20 = 20; // 0x30
    final int ZMS_21 = 21; // 0x30 (14th 0x30)
    final int ZMS_22 = 22; // 0x0D
    final int ZMS_23 = 23; // 0x0A
    final int ZMS_24 = 24; // 0x11
    final int TEXT_MAX_LINE = 1000;
    final int UI_READ_BUFFER_SIZE = 10240; // Notes: 115K:1440B/100ms, 230k:2880B/100ms
    final int MAX_NUM_BYTES = 65536;
    FT_Device ftDev;
    int DevCount = -1;
    int currentPortIndex = -1;
    int portIndex = -1;
    boolean INTERNAL_DEBUG_TRACE = false; // Toast message for debug
    String currentProtocol;
    int transferMode = MODE_GENERAL_UART;
    int[] modemReceiveDataBytes;
    byte[] modemDataBuffer;
    byte[] zmDataBuffer;
    byte receivedPacketNumber = 1;
    boolean bModemGetNak = false;
    boolean bModemGetAck = false;
    boolean bModemGetCharC = false;
    boolean bModemGetCharG = false;
    int totalModemReceiveDataBytes = 0;
    boolean bDataReceived = false;
    boolean bReceiveFirstPacket = false;
    boolean bUartModeTaskSet = true;
    boolean bReadDataProcess = true;
    String modemFileName;
    String modemFileSize;
    int zmodemState = 0;
    int zmStartState = 0;
    int totalReceiveDataBytes = 0;
    int totalUpdateDataBytes = 0;
    HandlerThread handlerThread; // update data to UI
    ReadThread readThread; // read data from USB
    TextView uartInfo;
    ScrollView scrollView;
    TextView readText;
    EditText writeText;
    Button writeButton, configButton, formatButton;
    boolean bSendButtonClick = false;
    boolean bLogButtonClick = false;
    boolean bFormatHex = false;
    boolean bSendHexData = false;
    boolean bContentFormatHex = false;
    int timesMessageHexFormatWriteData = 0;
    byte[] writeBuffer;
    byte[] readBuffer;
    char[] readBufferToChar;
    int actualNumBytes;
    int baudRate = 9600; /* baud rate */
    byte stopBit = 1; /* 1:1stop bits, 2:2 stop bits */
    byte dataBit = 8; /* 8:8bit, 7: 7bit */
    byte parity = 0; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
    byte flowControl = 0; /* 0:none, 1: CTS/RTS, 2:DTR/DSR, 3:XOFF/XON */
    boolean uart_configured = false;
    String uartSettings = "";
    BufferedOutputStream buf_save;
    boolean WriteFileThread_start = false;
    String fileNameInfo;
    long start_time, end_time;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT_VIEW_CONTENT:
                    if (actualNumBytes > 0) {
                        totalUpdateDataBytes += actualNumBytes;
                        for (int i = 0; i < actualNumBytes; i++) {
                            readBufferToChar[i] = (char) readBuffer[i];
                        }
                        appendData(String.copyValueOf(readBufferToChar, 0, actualNumBytes));
                    }
                    break;
                case UPDATE_MODEM_RECEIVE_DATA:
                case UPDATE_MODEM_RECEIVE_DATA_BYTES: {
                    String temp = currentProtocol;
                    if (totalModemReceiveDataBytes <= 10240)
                        temp += " Receive " + totalModemReceiveDataBytes + "Bytes";
                    else
                        temp += " Receive " + new DecimalFormat("#.00").format(totalModemReceiveDataBytes / (double) 1024) + "KBytes";

                    updateStatusData(temp);
                }
                break;

                case UPDATE_MODEM_RECEIVE_DONE: {
//                    saveFileActionDone();

                    String temp = currentProtocol;
                    if (totalModemReceiveDataBytes <= 10240)
                        temp += " Receive " + totalModemReceiveDataBytes + "Bytes";
                    else
                        temp += " Receive " + new DecimalFormat("#.00").format(totalModemReceiveDataBytes / (double) 1024) + "KBytes";

                    Double diffime = (double) (end_time - start_time) / 1000;
                    temp += " in " + diffime.toString() + " seconds";

                    updateStatusData(temp);
                }
                break;

                case MSG_MODEM_RECEIVE_PACKET_TIMEOUT: {

                    String temp = currentProtocol;
                    if (totalModemReceiveDataBytes <= 10240)
                        temp += " Receive " + totalModemReceiveDataBytes + "Bytes";
                    else
                        temp += " Receive " + new DecimalFormat("#.00").format(totalModemReceiveDataBytes / (double) 1024) + "KBytes";

                    updateStatusData(temp);
//                    saveFileActionDone();
                }
                break;

                case MSG_YMODEM_PARSE_FIRST_PACKET_FAIL:

                    resetLogButton();
                    break;

                case MSG_FORCE_STOP_SEND_FILE:

                    break;

                case UPDATE_ASCII_RECEIVE_DATA_BYTES: {
                    String temp = currentProtocol;
                    if (totalReceiveDataBytes <= 10240)
                        temp += " Receive " + totalReceiveDataBytes + "Bytes";
                    else
                        temp += " Receive " + new DecimalFormat("#.00").format(totalReceiveDataBytes / (double) 1024) + "KBytes";

                    long tempTime = System.currentTimeMillis();
                    Double diffime = (double) (tempTime - start_time) / 1000;
                    temp += " in " + diffime.toString() + " seconds";

                    updateStatusData(temp);
                }
                break;

                case MSG_FORCE_STOP_SAVE_TO_FILE:

                    break;

                case UPDATE_ZMODEM_STATE_INFO:
                    updateStatusData("zmodemState:" + zmodemState);

                    if (ZOO == zmodemState) {

                    }
                    break;

                case ACT_ZMODEM_AUTO_START_RECEIVE:
                    bUartModeTaskSet = false;
                    transferMode = MODE_Z_MODEM_RECEIVE;
                    currentProtocol = "ZModem";

                    receivedPacketNumber = 1;
                    modemReceiveDataBytes[0] = 0;
                    totalModemReceiveDataBytes = 0;
                    bDataReceived = false;
                    bReceiveFirstPacket = false;
                    fileNameInfo = null;

                    setLogButton();

                    zmodemState = ZRINIT;
                    start_time = System.currentTimeMillis();
                    ZModemReadDataThread zmReadThread = new ZModemReadDataThread(handler);
                    zmReadThread.start();
                    break;

                case MSG_SPECIAL_INFO:

                    break;

                case MSG_UNHANDLED_CASE:
                    if (msg.obj != null) {

                    } else {

                    }
                    break;
                default:
                    //Toast.makeText(J2xxActivity.this, ".", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    long cal_time_1, cal_time_2;

    byte[] readDataBuffer; /* circular buffer */
    int iTotalBytes;
    int iReadIndex;
    boolean bReadTheadEnable = false;

    public static int hexToInt(char ch) {
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j2xx);
        getPreIntent();

    }

    public void getPreIntent() {
        try {
            ftD2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }


        // init modem variables
        modemReceiveDataBytes = new int[1];
        modemReceiveDataBytes[0] = 0;
        modemDataBuffer = new byte[MODEM_BUFFER_SIZE];
        zmDataBuffer = new byte[MODEM_BUFFER_SIZE];

        uartInfo = (TextView) findViewById(R.id.UartInfo);

        scrollView = (ScrollView) findViewById(R.id.ReadField);
        readText = (TextView) findViewById(R.id.ReadValues);
        writeText = (EditText) findViewById(R.id.WriteValues);

        configButton = (Button) findViewById(R.id.ConfigButton);
        writeButton = (Button) findViewById(R.id.WriteButton);
        formatButton = (Button) findViewById(R.id.FormatButton);

		/* allocate buffer */
        writeBuffer = new byte[512];
        readBuffer = new byte[UI_READ_BUFFER_SIZE];
        readBufferToChar = new char[UI_READ_BUFFER_SIZE];
        readDataBuffer = new byte[MAX_NUM_BYTES];
        actualNumBytes = 0;

        // start main text area read thread
        handlerThread = new HandlerThread(handler);
        handlerThread.start();

		/* port */
        portIndex = 0;

        configButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDeviceList();
                if (DevCount > 0) {
                    connectFunction();
                }

                if (DeviceStatus.DEV_NOT_CONNECT == checkDevice()) {
                    return;
                }

                setConfig(baudRate);

                uart_configured = true;
            }
        });

        formatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bFormatHex == false) {
                    formatButton.setText("HEX");
                    bFormatHex = true;
                } else {
                    formatButton.setText("CHAR");
                    bFormatHex = false;
                }
            }
        });

// write button +
        writeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DeviceStatus.DEV_CONFIG != checkDevice()) {
                    return;
                }

                // check whether there is some data
                if (writeText.length() != 0x00) {
                    // check format
                    if (false == bFormatHex) {// character format


                        String temp = writeText.getText() + "\n";
                        String tmp = temp.replace("\\n", "\n");
                        appendData(tmp);

                        int numBytes = writeText.length();

                        for (int i = 0; i < numBytes; i++) {
                            writeBuffer[i] = (byte) (writeText.getText().charAt(i));
                        }

                        sendData(numBytes, writeBuffer);
                        writeText.setText("");
                    } else { // hexadecimal format

                        if (writeText.length() % 2 != 0) {

                            return;
                        }

                        String temp = writeText.getText().toString();
                        try {
                            String atemp = hexToAscii(temp);


                            byte numBytes = (byte) atemp.length();
                            for (int i = 0; i < numBytes; i++) {
                                writeBuffer[i] = (byte) atemp.charAt(i);
                            }

                            sendData(numBytes, writeBuffer);
                        } catch (IllegalArgumentException e) {


                            return;
                        }
                        temp += "(hex)\n";
                        String tmp = temp.replace("\\n", "\n");
                        bSendHexData = true;
                        appendData(tmp);

                        writeText.setText("");
                    }
                }
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    void setLogButton() {
        bLogButtonClick = true;
    }

    void resetLogButton() {
        bLogButtonClick = false;
    }

    public String hexToAscii(String s) throws IllegalArgumentException {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);
        for (int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);
            sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
        }
        return sb.toString();
    }

    void appendData(String data) {
        if (true == bContentFormatHex) {
            if (timesMessageHexFormatWriteData < 3) {
                timesMessageHexFormatWriteData++;

            }
            return;
        }

        if (true == bSendHexData) {
            SpannableString text = new SpannableString(data);
            text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, data.length(), 0);
            readText.append(text);
            bSendHexData = false;
        } else {
            readText.append(data);
        }

        int overLine = readText.getLineCount() - TEXT_MAX_LINE;

        if (overLine > 0) {
            int IndexEndOfLine = 0;
            CharSequence charSequence = readText.getText();

            for (int i = 0; i < overLine; i++) {
                do {
                    IndexEndOfLine++;
                }
                while (IndexEndOfLine < charSequence.length() && charSequence.charAt(IndexEndOfLine) != '\n');
            }

            if (IndexEndOfLine < charSequence.length()) {
                readText.getEditableText().delete(0, IndexEndOfLine + 1);
            } else {
                readText.setText("");
            }
        }

        scrollView.smoothScrollTo(0, readText.getHeight() + 30);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createDeviceList();
        if (DevCount > 0) {
            connectFunction();
            setUARTInfoString();

            setConfig(baudRate);
        }
    }

    protected void onResume() {
        super.onResume();
        if (null == ftDev || !ftDev.isOpen()) {
            createDeviceList();
            if (DevCount > 0) {
                connectFunction();
                setUARTInfoString();

                setConfig(baudRate);
            }
        }
    }

    protected void onDestroy() {
        disconnectFunction();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }


    public void createDeviceList() {
        int tempDevCount = ftD2xx.createDeviceInfoList(this);

        if (tempDevCount > 0) {
            if (DevCount != tempDevCount) {
                DevCount = tempDevCount;
            }
        } else {
            DevCount = -1;
            currentPortIndex = -1;
        }
    }

    public void disconnectFunction() {
        DevCount = -1;
        currentPortIndex = -1;
        bReadTheadEnable = false;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ftDev != null) {
            if (ftDev.isOpen()) {
                ftDev.close();
            }
        }
    }

    public void connectFunction() {
        if (portIndex + 1 > DevCount) {
            portIndex = 0;
        }

        if (currentPortIndex == portIndex
                && ftDev != null
                && ftDev.isOpen()) {

            return;
        }

        if (bReadTheadEnable) {
            bReadTheadEnable = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (null == ftDev) {
            ftDev = ftD2xx.openByIndex(J2xxActivity.this, portIndex);
        } else {
            ftDev = ftD2xx.openByIndex(J2xxActivity.this, portIndex);
        }
        uart_configured = false;

        if (ftDev == null) {
            return;
        }

        if (ftDev.isOpen()) {
            currentPortIndex = portIndex;
            if (!bReadTheadEnable) {
                readThread = new ReadThread(handler);
                readThread.start();
            }
        } else {

        }
    }

    DeviceStatus checkDevice() {
        if (ftDev == null || false == ftDev.isOpen()) {
            return DeviceStatus.DEV_NOT_CONNECT;
        } else if (false == uart_configured) {
            return DeviceStatus.DEV_NOT_CONFIG;
        }
        return DeviceStatus.DEV_CONFIG;
    }

    void setUARTInfoString() {
        String parityString, flowString;

        switch (parity) {
            case 0:
                parityString = new String("None");
                break;
            case 1:
                parityString = new String("Odd");
                break;
            case 2:
                parityString = new String("Even");
                break;
            case 3:
                parityString = new String("Mark");
                break;
            case 4:
                parityString = new String("Space");
                break;
            default:
                parityString = new String("None");
                break;
        }

        switch (flowControl) {
            case 0:
                flowString = new String("None");
                break;
            case 1:
                flowString = new String("CTS/RTS");
                break;
            case 2:
                flowString = new String("DTR/DSR");
                break;
            case 3:
                flowString = new String("XOFF/XON");
                break;
            default:
                flowString = new String("None");
                break;
        }

        uartSettings = "Port " + portIndex + "; UART Setting  -  Baudrate:" + baudRate + "  StopBit:" + stopBit
                + "  DataBit:" + dataBit + "  Parity:" + parityString
                + "  FlowControl:" + flowString;
        Toast.makeText(this, "" + uartSettings, Toast.LENGTH_SHORT).show();
        resetStatusData();
    }

    /**
     * 设置波特率
     *
     * @param baud
     */
    void setConfig(int baud) {
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
        ftDev.setBaudRate(baud);

        byte dataBits1 = D2xxManager.FT_DATA_BITS_8;
        byte stopBits1 = D2xxManager.FT_STOP_BITS_1;

        byte parity1 = D2xxManager.FT_PARITY_NONE;
        ftDev.setDataCharacteristics(dataBits1, stopBits1, parity1);

        short flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
        ftDev.setFlowControl(flowCtrlSetting, XON, XOFF);

        setUARTInfoString();
        uart_configured = true;
    }

    void sendData(int numBytes, byte[] buffer) {
        if (ftDev.isOpen() == false) {
            Toast.makeText(J2xxActivity.this, "Device not open!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numBytes > 0) {
            ftDev.write(buffer, numBytes);
        }
    }

    byte readData(int numBytes, int offset, byte[] buffer) {
        byte intstatus = 0x00; /* success by default */

        if ((numBytes < 1) || (0 == iTotalBytes)) {
            actualNumBytes = 0;
            intstatus = 0x01;
            return intstatus;
        }

        if (numBytes > iTotalBytes) {
            numBytes = iTotalBytes;
        }

		/* update the number of bytes available */
        iTotalBytes -= numBytes;
        actualNumBytes = numBytes;

		/* copy to the user buffer */
        for (int count = offset; count < numBytes + offset; count++) {
            buffer[count] = readDataBuffer[iReadIndex];
            iReadIndex++;
            iReadIndex %= MAX_NUM_BYTES;
        }

        return intstatus;
    }

    byte readData(int numBytes, byte[] buffer) {
        byte intstatus = 0x00; /* success by default */

		/* should be at least one byte to read */
        if ((numBytes < 1) || (0 == iTotalBytes)) {
            actualNumBytes = 0;
            intstatus = 0x01;
            return intstatus;
        }

        if (numBytes > iTotalBytes) {
            numBytes = iTotalBytes;
        }

		/* update the number of bytes available */
        iTotalBytes -= numBytes;
        actualNumBytes = numBytes;

		/* copy to the user buffer */
        for (int count = 0; count < numBytes; count++) {
            buffer[count] = readDataBuffer[iReadIndex];
            iReadIndex++;
            iReadIndex %= MAX_NUM_BYTES;
        }

        return intstatus;
    }

    void resetStatusData() {
        String tempStr = "Format - " + (bContentFormatHex ? "Hexadecimal" : "Character") + "\n" + uartSettings;
        String tmp = tempStr.replace("\\n", "\n");
        uartInfo.setText(tmp);
    }

    void updateStatusData(String str) {
        String temp;
        if (null == fileNameInfo)
            temp = "\n" + str;
        else
            temp = fileNameInfo + "\n" + str;

        String tmp = temp.replace("\\n", "\n");
        uartInfo.setText(tmp);
    }

    boolean zmParseFileInfo(int dataNum) {
        boolean parseOK = true;
        char filename[] = new char[128];
        char filesize[] = new char[12];


        int i = 0;
        while (modemDataBuffer[i] != 0x00) {
            filename[i] = (char) modemDataBuffer[i];
            i++;
        }

        i++;
        int j = 0;
        // file size: xp hyperterm  end with 0x20, pcomm end with 0x00
        while ((modemDataBuffer[j + i] != 0x00 && modemDataBuffer[j + i] != 0x20) && ((j + i) < (dataNum - 1))) {
            filesize[j] = (char) modemDataBuffer[j + i];
            j++;
        }


        modemFileName = String.copyValueOf(filename, 0, i - 1);
        modemFileSize = String.copyValueOf(filesize, 0, j);


        return parseOK;
    }


    // for Z modem read data
    int zmReadAllData(int waitTime) {
        long time_1, time_2;
        time_1 = System.currentTimeMillis();

        do {
            if (modemReceiveDataBytes[0] > 0) {
                int dataNum = modemReceiveDataBytes[0];
                if (dataNum > DATA_SIZE_256)        // read 256 bytes data each time
                {
                    dataNum = DATA_SIZE_256;
                }

                readData(dataNum, modemDataBuffer);
                modemReceiveDataBytes[0] -= dataNum;
                return dataNum;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false == bLogButtonClick && false == bSendButtonClick) {
                return 0;
            }

            time_2 = System.currentTimeMillis();
        }
        while ((time_2 - time_1) < waitTime);
        return 0;
    }

    int zmCheckZEOF(int[] dataNum) {
        for (int i = 0; i < dataNum[0]; i++) {
            if (ZPAD == modemDataBuffer[i]) {

                if (dataNum[0] < i + 3 + 1) // check whether there is enough data for parsing: zeof
                {
                    zmWaitReadData((i + 3 + 1 - dataNum[0]), dataNum[0], 1000);
                    dataNum[0] += (i + 3 + 1 - dataNum[0]);
                }

                if ((i + 3 + 1) <= dataNum[0]) {
                    if (ZDLE == modemDataBuffer[i + 1] &&
                            ZBIN == modemDataBuffer[i + 2] &&
                            0x0b == modemDataBuffer[i + 3]) // ZEOF
                    {
                        if (dataNum[0] < i + 9 + 1) // ZEOF, get remain data
                        {
                            zmWaitReadData((i + 9 + 1 - dataNum[0]), dataNum[0], 2000);
                            dataNum[0] += (i + 9 + 1 - dataNum[0]);
                        }


                        return i;
                    }
                } else {
                    if (true == INTERNAL_DEBUG_TRACE) {
                        Message msg = handler.obtainMessage(MSG_UNHANDLED_CASE, String.valueOf("zmCheckZEOF unhandle case"));
                        handler.sendMessage(msg);
                    }

                }
            }
        }
        return -1;
    }

    boolean zmWaitReadData(int numByte, int offset, int waitTime) // for Z modem read data
    {
        long time_1, time_2;
        time_1 = System.currentTimeMillis();

        if (offset != 0) {

        }

        do {
            if (modemReceiveDataBytes[0] >= numByte) {
                readData(numByte, offset, modemDataBuffer);
                modemReceiveDataBytes[0] -= numByte;
                return true;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false == bLogButtonClick && false == bSendButtonClick) {
                return false;
            }

            time_2 = System.currentTimeMillis();
        }
        while ((time_2 - time_1) < waitTime);


        return false;
    }

    void zmWaitFileInfoData(int[] dataNum) {


        int i = 0;
        while (true) {

            if (dataNum[0] < i + 1 + 1) {
                zmWaitReadData(1, dataNum[0], 1000); // // check whether there is enough data for parsing: zlde zcrcw
                dataNum[0] += 1;
            }

            if (ZDLE == modemDataBuffer[i])  // find zdle
            {
                if (ZCRCW == modemDataBuffer[i + 1]) {

                    return;
                }
            }

            i++;

            if (i > 256)
                break;
        }
    }

    int zmGetHeaderType() {
        byte headerType = 0;

        String logStr = new String("Data: ");
        for (int i = 0; i < 21; i++) {
            logStr += Integer.toHexString(modemDataBuffer[i]) + " ";
        }


        if (modemDataBuffer[0] != ZPAD) {
            return -1;
        }

        if (ZPAD == modemDataBuffer[1]) {
            if (modemDataBuffer[2] != ZDLE) {

                return -1;
            }

            if (modemDataBuffer[3] != ZHEX) {

                return -1;
            } else {
                headerType = ZHEX;
            }
        } else if (ZDLE == modemDataBuffer[1]) {

            if (ZBIN == modemDataBuffer[2]) {
                headerType = ZBIN;
            } else if (ZBIN32 == modemDataBuffer[2]) {
                headerType = ZBIN32;
            } else {

                return -1;
            }
        } else {
            return -1;
        }

        switch (headerType) {
            case ZHEX:
                break;
            case ZBIN:

                break;
            case ZBIN32:

                break;
            default:
                break;
        }

        return (headerType);
    }

    int zmParseDataPacket(int[] dataNum, int zeofPos) {
        int j = 0;
        int frameendByte = ZDLE_END_SIZE_4;

        if (zeofPos >= 0) {

            dataNum[0] = zeofPos;
        }

        for (int i = 0; i < dataNum[0]; i++) {
            if (ZDLE == modemDataBuffer[i])  // find zdle
            {
                if (0 == i)


                    // check whether there is enough data for parsing: zlde frameend crc1 crc2
                    if (dataNum[0] < i + 3 + 1) {


                        zmWaitReadData((i + 3 + 1 - dataNum[0]), dataNum[0], 1000);
                        dataNum[0] += (i + 3 + 1 - dataNum[0]);


                    }


                // check next char is special word or not


                if (((modemDataBuffer[i + 1] & 0x40) != 0) && ((modemDataBuffer[i + 1] & 0x20) == 0)) {
                    modemDataBuffer[i + 1] &= 0xbf;
                    zmDataBuffer[j++] = modemDataBuffer[i + 1];
                    i++; // i+1 is converted, skip this one

                    // not zdle frame, keep scan next char
                } else {


                    // check two zdle case
                    if (ZDLE == modemDataBuffer[i + 2] || ZDLE == modemDataBuffer[i + 3]) {

                        zmWaitReadData(1, dataNum[0], 1000);
                        dataNum[0] += 1;
                        frameendByte = ZDLE_END_SIZE_5;

                        if (ZDLE == modemDataBuffer[i + 4])  // check three zlde case
                        {

                            zmWaitReadData(1, dataNum[0], 1000);
                            dataNum[0] += 1;
                            frameendByte = ZDLE_END_SIZE_6;
                        }
                    }

                    // skip frame data
                    i += frameendByte - 1;
                }
            } else {
                zmDataBuffer[j++] = modemDataBuffer[i];
            }
        }

        return j; // return j: data number for saving
    }

    void checkZMStartingZRQINIT() {
        Message msg;
        for (int i = 0; i < actualNumBytes; i++) {
            switch (zmStartState) {
                case ZMS_0:
                    if (0x72 == readBuffer[i]) zmStartState = ZMS_1;
                    break;
                case ZMS_1:
                    if (0x7A == readBuffer[i]) zmStartState = ZMS_2;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_2:
                    if (0x0D == readBuffer[i]) zmStartState = ZMS_3;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_3:
                    if (ZPAD == readBuffer[i]) zmStartState = ZMS_4;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_4:
                    if (ZPAD == readBuffer[i]) zmStartState = ZMS_5;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_5:
                    if (ZDLE == readBuffer[i]) zmStartState = ZMS_6;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_6:
                    if (ZHEX == readBuffer[i]) zmStartState = ZMS_7;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_7:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_8;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_8:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_9;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_9:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_10;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_10:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_11;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_11:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_12;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_12:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_13;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_13:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_14;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_14:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_15;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_15:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_16;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_16:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_17;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_17:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_18;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_18:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_19;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_19:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_20;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_20:
                    if (0x30 == readBuffer[i]) zmStartState = ZMS_21;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_21:
                    if (0x0D == readBuffer[i]) zmStartState = ZMS_22;
                    else zmStartState = ZMS_0;
                    break;
                case ZMS_22:
                    if (0x0A == readBuffer[i] || (byte) 0x8A == readBuffer[i])
                        zmStartState = ZMS_23;
                    else {

                        zmStartState = ZMS_0;
                    }
                    break;
                case ZMS_23:
                    if (0x11 == readBuffer[i]) zmStartState = ZMS_24;
                    else zmStartState = ZMS_0;
                    break;
                default:
                    break;
            }

            if (zmStartState >= ZMS_1) {

            }

            if (ZMS_24 == zmStartState) {

                zmStartState = ZMS_0;
                msg = handler.obtainMessage(ACT_ZMODEM_AUTO_START_RECEIVE);
                handler.sendMessage(msg);
            }
        }
    }

    public enum DeviceStatus {
        DEV_NOT_CONNECT,
        DEV_NOT_CONFIG,
        DEV_CONFIG
    }

    // Update UI content
    class HandlerThread extends Thread {
        Handler mHandler;

        HandlerThread(Handler h) {
            mHandler = h;
        }

        public void run() {
            byte status;
            Message msg;

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (true == bContentFormatHex) // consume input data at hex content format
                {
                    status = readData(UI_READ_BUFFER_SIZE, readBuffer);
                } else if (MODE_GENERAL_UART == transferMode) {
                    status = readData(UI_READ_BUFFER_SIZE, readBuffer);

                    if (0x00 == status) {
                        if (false == WriteFileThread_start) {
                            checkZMStartingZRQINIT();
                        }

                        // save data to file
                        if (true == WriteFileThread_start && buf_save != null) {
                            try {
                                buf_save.write(readBuffer, 0, actualNumBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        msg = mHandler.obtainMessage(UPDATE_TEXT_VIEW_CONTENT);
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }
    }

    class ReadThread extends Thread {
        final int USB_DATA_BUFFER = 8192;

        Handler mHandler;

        ReadThread(Handler h) {
            mHandler = h;
            this.setPriority(MAX_PRIORITY);
        }

        public void run() {
            byte[] usbdata = new byte[USB_DATA_BUFFER];
            int readcount = 0;
            int iWriteIndex = 0;
            bReadTheadEnable = true;

            while (true == bReadTheadEnable) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                while (iTotalBytes > (MAX_NUM_BYTES - (USB_DATA_BUFFER + 1))) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                readcount = ftDev.getQueueStatus();
                //Log.e(">>@@","iavailable:" + iavailable);
                if (readcount > 0) {
                    if (readcount > USB_DATA_BUFFER) {
                        readcount = USB_DATA_BUFFER;
                    }
                    ftDev.read(usbdata, readcount);

                    if ((MODE_X_MODEM_CHECKSUM_SEND == transferMode)
                            || (MODE_X_MODEM_CRC_SEND == transferMode)
                            || (MODE_X_MODEM_1K_CRC_SEND == transferMode)) {
                        for (int i = 0; i < readcount; i++) {
                            modemDataBuffer[i] = usbdata[i];

                        }

                        if (NAK == modemDataBuffer[0]) {

                            bModemGetNak = true;
                        } else if (ACK == modemDataBuffer[0]) {

                            bModemGetAck = true;
                        } else if (CHAR_C == modemDataBuffer[0]) {

                            bModemGetCharC = true;
                        }
                        if (CHAR_G == modemDataBuffer[0]) {

                            bModemGetCharG = true;
                        }
                    } else {
                        totalReceiveDataBytes += readcount;
                        //
                        //
                        for (int count = 0; count < readcount; count++) {
                            readDataBuffer[iWriteIndex] = usbdata[count];
                            iWriteIndex++;
                            iWriteIndex %= MAX_NUM_BYTES;
                        }

                        if (iWriteIndex >= iReadIndex) {
                            iTotalBytes = iWriteIndex - iReadIndex;
                        } else {
                            iTotalBytes = (MAX_NUM_BYTES - iReadIndex) + iWriteIndex;
                        }

                        //
                        if ((MODE_X_MODEM_CHECKSUM_RECEIVE == transferMode)
                                || (MODE_X_MODEM_CRC_RECEIVE == transferMode)
                                || (MODE_X_MODEM_1K_CRC_RECEIVE == transferMode)
                                || (MODE_Y_MODEM_1K_CRC_RECEIVE == transferMode)
                                || (MODE_Z_MODEM_RECEIVE == transferMode)
                                || (MODE_Z_MODEM_SEND == transferMode)) {
                            modemReceiveDataBytes[0] += readcount;

                        }
                    }
                }
            }


        }
    }

    class ZModemReadDataThread extends Thread {
        Handler mHandler;

        ZModemReadDataThread(Handler h) {
            mHandler = h;
            this.setPriority(MAX_PRIORITY);
        }

        public void run() {

            Message msg;
            boolean bFileReciveDone = false;
            int[] getDataNum = new int[1];
            byte[] tempBuffer = new byte[24];

            bReadDataProcess = true;
            totalModemReceiveDataBytes = 0;

            cal_time_1 = System.currentTimeMillis();

            while (bReadDataProcess) {

                if (false == bLogButtonClick) {
                    msg = mHandler.obtainMessage(MSG_FORCE_STOP_SAVE_TO_FILE);
                    mHandler.sendMessage(msg);
                    continue;
                }

                if (zmodemState >= ZDATA) {
                    cal_time_2 = System.currentTimeMillis();
                    if ((cal_time_2 - cal_time_1) > 200) // update status every 200ms
                    {
                        msg = mHandler.obtainMessage(UPDATE_MODEM_RECEIVE_DATA_BYTES);
                        mHandler.sendMessage(msg);
                        cal_time_1 = cal_time_2;
                    }
                }

                switch (zmodemState) {
                    case ZRQINIT:

                        if (true == zmWaitReadData(3, 0, 20000)) {
                            if (modemDataBuffer[0] == 0x72 &&  // r
                                    modemDataBuffer[1] == 0x7A &&  // z
                                    modemDataBuffer[2] == 0x0D)      // \r
                            {
                                start_time = System.currentTimeMillis();


                                if (true == zmWaitReadData(21, 0, 10000)) {
                                    if (zmGetHeaderType() > 0) {


                                        zmodemState = ZRINIT;

                                        msg = mHandler.obtainMessage(UPDATE_MODEM_RECEIVE_DATA);
                                        mHandler.sendMessage(msg);
                                    } else {

                                    }
                                }
                            } else {

                            }
                        } else {

                        }

                        break;

                    case ZEOF:
                    case ZRINIT:
                        // TODO: how to set ZRINIT packet

                        tempBuffer[0] = 0x2A;
                        tempBuffer[1] = 0x2A;
                        tempBuffer[2] = 0x18;
                        tempBuffer[3] = 0x42;
                        tempBuffer[4] = 0x30;
                        tempBuffer[5] = 0x31;
                        tempBuffer[6] = 0x30;
                        tempBuffer[7] = 0x30;
                        tempBuffer[8] = 0x30;
                        tempBuffer[9] = 0x30;
                        tempBuffer[10] = 0x30;
                        tempBuffer[11] = 0x30;
                        tempBuffer[12] = 0x30;
                        tempBuffer[13] = 0x33;
                        tempBuffer[14] = 0x39;
                        tempBuffer[15] = 0x61;
                        tempBuffer[16] = 0x33;
                        tempBuffer[17] = 0x32;
                        tempBuffer[18] = 0x0D;
                        tempBuffer[19] = 0x0A;
                        tempBuffer[20] = 0x11;
                        sendData(21, tempBuffer);

                        if (ZRINIT == zmodemState) {
                            zmodemState = ZFILE;
                        } else if (ZEOF == zmodemState) {
                            zmodemState = ZFIN;
                        }
                        break;

                    case ZFILE:

                        if (true == zmWaitReadData(10, 0, 10000)) {
                            if (modemDataBuffer[0] == ZPAD &&
                                    modemDataBuffer[1] == ZDLE &&
                                    modemDataBuffer[2] == ZBIN &&
                                    modemDataBuffer[3] == ZFILE) {

                                // file info packet size:  fname + 1 + fsize + 1 + 5 -> 9
                                if (true == zmWaitReadData(9, 0, 1000)) {
                                    getDataNum[0] = 9;
                                } else {
                                    getDataNum[0] = 0;

                                }


                                zmWaitFileInfoData(getDataNum);


                                zmParseFileInfo(getDataNum[0]);


                            } else {

                            }
                        }


                        zmodemState = ZRPOS;

                        try {                    // clear remain data after zdle zcrcw
                            Thread.sleep(300);
                        } catch (Exception e) {
                        }
                        zmReadAllData(1000);

                        msg = mHandler.obtainMessage(UPDATE_ZMODEM_STATE_INFO);
                        mHandler.sendMessage(msg);
                        break;

                    case ZRPOS:
                        // TODO: how to set ZRPOS packet

                        tempBuffer[0] = 0x2A;
                        tempBuffer[1] = 0x2A;
                        tempBuffer[2] = 0x18;
                        tempBuffer[3] = 0x42;
                        tempBuffer[4] = 0x30;
                        tempBuffer[5] = 0x39;
                        tempBuffer[6] = 0x30;
                        tempBuffer[7] = 0x30;
                        tempBuffer[8] = 0x30;
                        tempBuffer[9] = 0x30;
                        tempBuffer[10] = 0x30;
                        tempBuffer[11] = 0x30;
                        tempBuffer[12] = 0x30;
                        tempBuffer[13] = 0x30;
                        tempBuffer[14] = 0x61;
                        tempBuffer[15] = 0x38;
                        tempBuffer[16] = 0x37;
                        tempBuffer[17] = 0x63;
                        tempBuffer[18] = 0x0D;
                        tempBuffer[19] = 0x0A;
                        tempBuffer[20] = 0x11;
                        sendData(21, tempBuffer);


                        zmodemState = ZDATA_HEADER;

                        break;

                    case ZDATA_HEADER:
                        if (true == zmWaitReadData(10, 0, 10000)) {

                            for (int i = 0; i < 10; i++)


                                if (modemDataBuffer[0] == ZPAD &&
                                        modemDataBuffer[1] == ZDLE &&
                                        modemDataBuffer[2] == ZBIN &&
                                        modemDataBuffer[3] == 0x0A) // ZDATA
                                {

                                    zmodemState = ZDATA;
                                } else {

                                    bReadDataProcess = false;
                                }
                        } else {

                            bReadDataProcess = false;
                        }

                        break;

                    case ZDATA: {
                        getDataNum[0] = zmReadAllData(1000);

                        int zeofPos = zmCheckZEOF(getDataNum);
                        int numSaveDataBytes = zmParseDataPacket(getDataNum, zeofPos);

                        try {
                            buf_save.write(zmDataBuffer, 0, numSaveDataBytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        totalModemReceiveDataBytes += numSaveDataBytes;

                        if (zeofPos != -1) {

                            zmodemState = ZEOF;
                            bFileReciveDone = true;
                        }
                    }
                    break;

                    case ZFIN: {
                        if (true == zmWaitReadData(20, 0, 10000)) // get ZFIN
                        {
                            tempBuffer[0] = 0x2A; // ZPAD
                            tempBuffer[1] = 0x2A; // ZPAD
                            tempBuffer[2] = 0x18; // ZDLE
                            tempBuffer[3] = 0x42; // ZHEX
                            tempBuffer[4] = 0x30;
                            tempBuffer[5] = 0x38;
                            tempBuffer[6] = 0x30;
                            tempBuffer[7] = 0x30;
                            tempBuffer[8] = 0x30;
                            tempBuffer[9] = 0x30;
                            tempBuffer[10] = 0x30;
                            tempBuffer[11] = 0x30;
                            tempBuffer[12] = 0x30;
                            tempBuffer[13] = 0x30;
                            tempBuffer[14] = 0x30;
                            tempBuffer[15] = 0x32;
                            tempBuffer[16] = 0x32;
                            tempBuffer[17] = 0x64;
                            tempBuffer[18] = 0x0d;
                            tempBuffer[19] = (byte) 0x8a;
                            sendData(20, tempBuffer); // send ZFIN

                            zmodemState = ZOO;
                        } else {

                            bReadDataProcess = false;
                        }
                    }
                    break;

                    case ZOO:
                        if (true == zmWaitReadData(2, 0, 10000)) // get ZFIN
                        {
                            if (0x4F == modemDataBuffer[0] && 0x4F == modemDataBuffer[1]) {

                                bReadDataProcess = false;
                            } else {

                                bReadDataProcess = false;


                                int num = zmReadAllData(10);

                                if (num > 0) {

                                    for (int i = 0; i < num; i++) {
                                    }

                                }
                            }
                        } else {
                            bReadDataProcess = false;
                        }
                        break;
                    default:
                        break;
                }
            }

            transferMode = MODE_GENERAL_UART;
            bUartModeTaskSet = true;
            end_time = System.currentTimeMillis();
            if (true == bFileReciveDone) {

                msg = mHandler.obtainMessage(UPDATE_MODEM_RECEIVE_DONE);
                mHandler.sendMessage(msg);
            } else if (true == bLogButtonClick) {

                msg = mHandler.obtainMessage(MSG_MODEM_RECEIVE_PACKET_TIMEOUT);
                mHandler.sendMessage(msg);
            }
        }
    }

}
