@echo off
set verno=
set /p verno=������汾��:
call mvn clean package
pause

mkdir .\target\Ethinkface
xcopy .\buildline\runtime .\target\Ethinkface /E /F /Y
xcopy .\*.dll .\target\Ethinkface /F /Y

xcopy .\target\etface.jar .\target\Ethinkface /Y

echo ��Ӱ汾��Ϣ%verno%��.\target\Ethinkface\version
echo %verno% > .\target\Ethinkface\version.ini

echo ������

pause