SET DIRECTORY_NAME="C:\Java\mail-service\target"
TAKEOWN /f %DIRECTORY_NAME% /r /d y
ICACLS %DIRECTORY_NAME% /grant administrators:F /t
PAUSE
192.168.99.100