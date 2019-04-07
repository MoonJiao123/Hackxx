import serial
import time
import pygame

BAUD_RATE = 115200
PORT = "/dev/tty.3-DevB"

TIMEOUT = 0

# Bytes!
FORWARD = b"w"
BACKWARD = b"s"
LEFT = b"a"
RIGHT = b"d"
SERVO_LEFT = b"q"
SERVO_RIGHT = b"e"

def forward():
    bt.write(FORWARD)
    time.sleep(TIMEOUT)

def backwards():
    bt.write(BACKWARD)
    time.sleep(TIMEOUT)

def left():
    bt.write(LEFT)
    time.sleep(TIMEOUT)

def right():
    bt.write(RIGHT)
    time.sleep(TIMEOUT)

def servo_left():
    bt.write(SERVO_LEFT)
    time.sleep(TIMEOUT)

def servo_right():
    bt.write(SERVO_RIGHT)
    time.sleep(TIMEOUT)

# Setup serial port with path to port
bt = serial.Serial(PORT, 115200, timeout=5)
bt.flushInput()
pygame.init()

##while True:
##    cmd = input('Enter your command: ')
##    if cmd == 'w':
##        forward()
##    elif cmd == 'a':
##        left()
##    elif cmd == 's':
##        backwards()
##    elif cmd == 'd':
##        right()
##    elif cmd == 'q':
##        servo_left()
##    elif cmd == 'e':
##        servo_right()
##    else:
##        exit()

cmd = 'n'
while True:
    for event in pygame.event.get():
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_w:
                cmd = 'w'
            elif event.key == pygame.K_a:
                cmd = 'a'
            elif event.key == pygame.K_s:
                cmd = 's'
            elif event.key == pygame.K_d:
                cmd = 'd'
            elif event.key == pygame.K_q:
                cmd = 'q'
            elif event.key == pygame.K_e:
                cmd = 'e'
        elif event.type == pygame.KEYUP:
            cmd = 'n'
        
    if cmd == 'w':
        forward()
    elif cmd == 'a':
        left()
    elif cmd == 's':
        backwards()
    elif cmd == 'd':
        right()
    elif cmd == 'q':
        servo_left()
        time.sleep(0.03)
    elif cmd == 'e':
        servo_right()
        time.sleep(0.03)
        
    time.sleep(0.001)
