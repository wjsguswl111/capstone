import socket
import os
from tkinter import*
from tkinter.filedialog import*
from gtts import gTTS
from pygame import*
import datetime
from pydub import AudioSegment
from os import system
import sys
import subprocess

def OnOff(n):
    if n == "ON":
        system("python3 /home/pi/face_mask_detection/detect_mask_picam.py")
    elif n == "OFF":
        #return False
        os.system('taskkill /f /im detect_mask_picam.py')

HOST = "192.168.219.199"
PORT = 5900
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')
s.bind((HOST, PORT))
print('Socket bind complete')
s.listen(1)
print('Socket now listening')

while True:

    #system("python3 /home/pi/face_mask_detection/detect_mask_picam.py")

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

    if res == "ON" or res == "OFF":
        if os.path.isfile("/home/pi/face_mask_detection/stat.txt") == True:
            os.remove('/home/pi/face_mask_detection/stat.txt')
            file = open('stat.txt', 'w')
            file.write(res)
            file.close()
        else:
            file = open('stat.txt', 'w')
            file.write(res)
            file.close()
        OnOff(res)
    
    date, cont = res.split('.txt')
    Tdate = date + '.txt'
    year, month, day = date.split('-')
        
    if len(month)==1:
        month = '0' + month
    if len(day)==1:
        day = '0' + day
            
    date = year+'-'+month+'-'+day
    
    #삭제
    if cont == '':
        os.remove("/home/pi/"+Tdate)
        os.remove("/home/pi/"+date+'.mp3')
        os.remove("/home/pi/"+date+'.wav')
    
    #수정
    elif os.path.isfile("/home/pi/"+Tdate) == True:
        os.remove("/home/pi/"+Tdate)
        os.remove("/home/pi/"+date+'.mp3')
        os.remove("/home/pi/"+date+'.wav')
        myFile = open(Tdate, "w")
        myFile.write(cont)
        myFile.close()

        myFile = open(Tdate, "r")
        sText = myFile.read()
        tts = gTTS(text=sText, lang='ko', slow=False)
        tts.save(date+'.mp3')
        src = date + '.mp3'
        song = AudioSegment.from_mp3(src)
        song.export(date+'.wav', 'wav')


    #생성
    else:
        myFile = open(Tdate, "w")
        Fcont, Tcont = cont.split(' ')
        myFile.write(Tcont)
        myFile.close()

        myFile = open(Tdate, "r")
        sText = myFile.read()
        tts = gTTS(text=sText, lang='ko', slow=False)
        tts.save(date+'.mp3')
        src = date + '.mp3'
        song = AudioSegment.from_mp3(src)
        song.export(date+'.wav', 'wav')

    myFile.close()


    #클라이언트에게 답을 보냄
    conn.sendall(res.encode("utf-8"))
    #연결 닫기
    conn.close()
s.close()
