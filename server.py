import socket
import os
from tkinter import*
from tkinter.filedialog import*
from gtts import gTTS
from pygame import*
import datetime

HOST = "192.168.219.199"
PORT = 5900
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')
s.bind((HOST, PORT))
print('Socket bind complete')
s.listen(1)
print('Socket now listening')

while True:
    #접속 승인
    conn, addr = s.accept()
    print("Connected by ", addr)

    #데이터 수신
    data = conn.recv(1024)
    data = data.decode("utf8").strip()
    if not data: break
    print("Received: " + data)

    #수신한 데이터로 파이를 컨트롤
    res = data
    print("파이 동작 : " + res)

    #데이터 txt로 저장
    date, cont = res.split('.txt')
    te = res.split('.txt')
    print(te)
    Tdate = date + '.txt'
    
    #삭제
    if cont == '':
        os.remove("/home/pi/"+Tdate)
        os.remove("/home/pi/"+date+'.mp3')
    
    #수정
    elif os.path.isfile("/home/pi/"+Tdate) == True:
        os.remove("/home/pi/"+Tdate)
        os.remove("/home/pi/"+date+'.mp3')
        myFile = open(Tdate, "w")
        myFile.write(cont)
        myFile.close()

        myFile = open(Tdate, "r")
        sText = myFile.read()
        tts = gTTS(text=sText, lang='ko', slow=False)
        #sSaveFile = date + '.mp3'
        tts.save(date+'.mp3')


    #생성
    else:
        myFile = open(Tdate, "w")
        Fcont, Tcont = cont.split(' ')
        print(Tcont)
        myFile.write(Tcont)
        myFile.close()

        myFile = open(Tdate, "r")
        sText = myFile.read()
        print(sText)
        tts = gTTS(text=sText, lang='ko', slow=False)
        #sSaveFile = date + '.mp3'
        tts.save(date+'.mp3')

    myFile.close()


    #클라이언트에게 답을 보냄
    conn.sendall(res.encode("utf-8"))
    #연결 닫기
    conn.close()
s.close()
